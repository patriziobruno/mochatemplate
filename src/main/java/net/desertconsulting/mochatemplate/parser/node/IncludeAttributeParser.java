/* 
 * Copyright 2016 Patrizio Bruno <desertconsulting@gmail.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.desertconsulting.mochatemplate.parser.node;

import net.desertconsulting.mochatemplate.parser.Parser;
import net.desertconsulting.mochatemplate.parser.TemplateEngine;
import net.desertconsulting.mochatemplate.parser.cache.CacheFile;
import net.desertconsulting.mochatemplate.parser.cache.FileCache;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import javax.servlet.ServletContext;
import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

/**
 * This attribute parser supports data-include attribute. It implements template
 * inclusion. It looks for a &lt;template data-type="server/template"&gt; either
 * into the DOM or in an external file and copy all the children of the template
 * into the current node. It gets node-selector or file-reference:node-selector
 * as value: eg.
 * <ul>
 * <li>data-include="#customer-details"</li>
 * <li>data-include="@/templates/details.html:#customer"</li>
 * </ul>
 *
 * @author Patrizio Bruno <desertconsulting@gmail.com>
 */
public class IncludeAttributeParser extends TemplateAttributeParser {

    private static final Pattern TEMPLATE_SRC_RX = Pattern.compile(
            "^@([^:]+)(:(.+))?");

    private final static String SUPPORTED_FIELD = "include";
    private final static String TEMPLATE_SELECTOR = "template[data-type=\"server/template\"]";

    private final ServletContext context;

    public IncludeAttributeParser(ScriptEngine engine, ServletContext context) {
        super(engine);
        this.context = context;
    }

    private static final FileCache<Document> EXT_TPL_CACHE
            = new FileCache<>();

    @Override
    public boolean eval(TemplateNode dataNode, AttributeParserArguments args,
            Parser parser) throws ScriptException, UnsupportedEncodingException {

        boolean external = false;
        Elements templateElements;
        Bindings bindings = dataNode.getBindings();
        Node node = dataNode.getNode();
        String templateSelector = parser.parseString(args.getAttribute().
                getValue(), bindings);

        if (templateSelector.startsWith("@")) {
            templateElements = loadExternalTemplate(templateSelector);
            external = true;
        } else {
            templateElements = dataNode.getDocument().select(TEMPLATE_SELECTOR).select(
                    templateSelector);
        }

        for (Element templateElement : templateElements) {
            boolean foundScripts = external && runScripts(templateElement, bindings);
            for (Node child : templateElement.childNodes()) {
                Node nChild = child.clone();
                ((Element) node).appendChild(nChild);
                
                // cannot use nChild.select while it hasn't a parent
                if (foundScripts && nChild instanceof Element) {
                    ((Element) nChild).select(TemplateEngine.SCRIPT_SELECTOR).remove();
                }
            }
        }

        node.removeAttr(args.getAttributeName());

        return true;
    }

    @Override
    public String supportedAttr() {
        return SUPPORTED_FIELD;
    }

    private Elements loadExternalTemplate(String templateSelector) throws
            ScriptException {

        Elements rv = null;
        Matcher matcher = TEMPLATE_SRC_RX.matcher(templateSelector);

        if (matcher.find()) {
            Document dom = null;
            String fileName = matcher.group(1);

            CacheFile file = null;
            try {
                file = new CacheFile(context.getResource(fileName).toURI());
            } catch (MalformedURLException | URISyntaxException ex) {
                Logger.getLogger(IncludeAttributeParser.class.getName()).
                        log(Level.SEVERE, null, ex);
                throw new ScriptException(
                        new Exception("template loading error", ex));
            }
            String selector = matcher.groupCount() == 3 ? matcher.group(3)
                    : null;

            if (EXT_TPL_CACHE.containsKey(file)) {
                dom = EXT_TPL_CACHE.get(file);
            } else {
                try (InputStream stream = new FileInputStream(file.getFile())) {
                    dom = Jsoup.parse(stream, null, "");
                    EXT_TPL_CACHE.put(file, dom);
                } catch (IOException ex) {
                    Logger.getLogger(IncludeAttributeParser.class.getName()).
                            log(Level.SEVERE, null, ex);
                    throw new ScriptException(new Exception(
                            "template loading error", ex));
                }
            }

            if (dom != null) {
                if (!StringUtil.isBlank(selector)) {
                    rv = dom.select(TEMPLATE_SELECTOR).
                            select(selector);
                } else {
                    rv = dom.select(TEMPLATE_SELECTOR);
                }
            }
        } else {
            throw new ScriptException("bad template reference syntax: "
                    + templateSelector);
        }
        return rv;
    }

    private boolean runScripts(Element templateElement, Bindings bindings) throws ScriptException {
        Elements scripts = templateElement.select(TemplateEngine.SCRIPT_SELECTOR);
        if (!scripts.isEmpty()) {
            Element[] scriptElements = scripts.toArray(new Element[0]);
            for (Element element : scriptElements) {
                engine.eval(element.data(), bindings);
            }
            return true;
        }
        return false;
    }
}
