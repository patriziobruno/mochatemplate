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
package net.desertconsulting.mochatemplate.parser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import net.desertconsulting.mochatemplate.parser.cache.CacheFile;
import net.desertconsulting.mochatemplate.parser.cache.FileCache;
import net.desertconsulting.mochatemplate.parser.node.AttributeParserArguments;
import net.desertconsulting.mochatemplate.parser.node.TemplateNode;
import net.desertconsulting.mochatemplate.parser.node.TemplateAttributeParser;
import com.google.common.io.CharStreams;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.servlet.ServletContext;
import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.safety.Whitelist;
import org.reflections.Reflections;

/**
 * This class handles all the template parsing.
 *
 * @author Patrizio Bruno {@literal <desertconsulting@gmail.com>}
 */
public class MochaTemplateEngine implements TemplateEngine {

    /**
     * Javascript engine, to handle the template logic.
     */
    private final ScriptEngine jse = new ScriptEngineManager().getEngineByName(
            "js");
    /**
     * Template document object model.
     */
    private Document dom;
    /**
     * Node parser
     */
    private TemplateParser parser;
    /**
     * Attribute parsers
     */
    private static Map<String, TemplateAttributeParser> attrParsers;
    /**
     * This regular expression find javascript expressions in a string and
     * replaces ${} by their expression result
     */
    private final static Pattern INLINE_EXP_PATTERN = Pattern.compile(
            "\\$\\{([^\\}]+)\\}",
            0);

    private final static Object LOBJ = new Object();
    /**
     * The {@link ServletContext} is needed to load external resources, such as
     * external scripts and partial templates
     */
    private final ServletContext servletContext;

    /**
     * Initialize a new instance of {@link MochaTemplateEngine} that will be
     * used to access external scripts and template partials from the
     * {@link ServletContext} path. No templates are loaded yet.
     *
     * @param servletContext used to access external files, located into the
     * {@link ServletContext} path
     * @throws Exception
     */
    public MochaTemplateEngine(ServletContext servletContext) throws Exception {
        this.servletContext = servletContext;

        synchronized (LOBJ) {
            if (attrParsers == null) {
                // let's load attribute parsers

                attrParsers = new HashMap<>();

                // loading all available node-parsers in the classpath
                Reflections reflections = new Reflections(getClass().
                        getPackage().
                        getName(),
                        getClass().getClassLoader());

                Iterator<Class<? extends TemplateAttributeParser>> iterator
                        = reflections.
                                getSubTypesOf(TemplateAttributeParser.class).
                                iterator();

                while (iterator.hasNext()) {
                    Class<? extends TemplateAttributeParser> impl = iterator.
                            next();
                    TemplateAttributeParser p;
                    try {
                        p = impl.getConstructor(ScriptEngine.class).newInstance(
                                jse);
                    } catch (NoSuchMethodException ex) {
                        p = impl.getConstructor(ScriptEngine.class,
                                ServletContext.class).newInstance(jse,
                                        servletContext);
                    }

                    attrParsers.put(p.supportedAttr(), p);
                }
            }
        }

        parser = new TemplateParser(jse);
    }

    /**
     * Initialize a new instance of {@link MochaTemplateEngine} loading the
     * template from a HTML string {@code html} and setting
     * {@code servletContext}, that will be used to access external scripts and
     * template partials from the {@link ServletContext} path.
     *
     * @param servletContext used to access external files, located into the
     * {@link ServletContext} path
     * @param html string containing the template
     * @throws Exception
     */
    public MochaTemplateEngine(ServletContext servletContext, String html)
            throws Exception {
        this(servletContext);
        try (InputStream s = new ByteArrayInputStream(html.getBytes())) {
            loadTemplate(s);
        }
    }

    /**
     * Initialize a new instance of {@link MochaTemplateEngine} reading the
     * template from {@code file} and setting {@code servletContext}, that will
     * be used to access external scripts and template partials from the
     * {@link ServletContext} path.
     *
     * @param servletContext used to access external files, located into the
     * {@link ServletContext} path
     * @param file template file
     * @throws Exception
     */
    public MochaTemplateEngine(ServletContext servletContext, File file)
            throws Exception {
        this(servletContext);
        try (FileInputStream fis = new FileInputStream(file)) {
            loadTemplate(fis);
        }
    }

    /**
     * Initialize a new instance of {@link MochaTemplateEngine} reading the
     * template from {@code templateStream} and setting {@code servletContext},
     * that will be used to access external scripts and template partials from
     * the {@link ServletContext} path.
     *
     * @param servletContext used to access external files, located into the
     * {@link ServletContext} path
     * @param templateStream stream to read the template from
     * @throws Exception
     */
    public MochaTemplateEngine(ServletContext servletContext,
            InputStream templateStream) throws Exception {
        this(servletContext);
        loadTemplate(templateStream);
    }

    /**
     * Load a template into the engine.
     *
     * @param stream stream to read the template from
     * @throws IOException error parsing the stream
     */
    public final void loadTemplate(InputStream stream) throws IOException {
        dom = Jsoup.parse(stream, null, "");
    }

    @Override
    public String parse(Document.OutputSettings outputSettings) throws
            ScriptException, UnsupportedEncodingException,
            IOException {

        doParse(outputSettings);

        return dom.html();
    }

    @Override
    public void parse(Appendable output, Document.OutputSettings outputSettings) throws
            ScriptException, UnsupportedEncodingException,
            IOException {

        doParse(outputSettings);

        dom.html(output);
    }

    @Override
    public void exec(Appendable output, ApiOutputFormat outputFormat) throws ScriptException, IOException {
        Object val = exec();
        ObjectMapper mapper;
        switch(outputFormat) {
            case JSON:
                mapper = new ObjectMapper();
                break;
            case XML:
                mapper = new XmlMapper();
                break;
            default:
                throw new ApiOutputFormatException(String.format("'%s' is not a supported output format", outputFormat.toString()));
        }
        mapper.writeValue((Writer) output, val);
    }

    @Override
    public Object exec() throws ScriptException, IOException {
        return parseGlobals();
    }

    private void doParse(Document.OutputSettings outputSettings) throws ScriptException, UnsupportedEncodingException, IOException {

        parseGlobals();

        Node[] nodes = dom.childNodes().toArray(new Node[0]);
        for (Node node : nodes) {
            parser.parseNode(node, jse.getBindings(ScriptContext.ENGINE_SCOPE));
        }

        // remove server-side templates from the DOM
        dom.select("template[data-type=\"server/template\"]").remove();

        if (outputSettings == null) {
            outputSettings = new Document.OutputSettings() {
                {
                    syntax(Document.OutputSettings.Syntax.xml);
                    escapeMode(Entities.EscapeMode.extended);
                }
            };
        }
        dom.outputSettings(outputSettings);
    }

    @Override
    public void put(String key, Object value) {
        jse.put(key, value);
    }

    @Override
    public void putJson(String key, String value) throws ScriptException {
        jse.put(key, value);
        jse.put(key, jse.eval(String.format("JSON.parse(%s)", key)));
    }

    /**
     * This method retrieves every <script type="server/javascript"> in the
     * template, loads external script files and evaluates all the found scripts
     * through. {@link ScriptEngine.eval(String,Bindings)} to initialize the
     * current {@link ScriptContext}.
     *
     * @throws ScriptException syntax error evaluating a script
     * @throws IOException error trying to load an external script
     */
    private Object parseGlobals() throws ScriptException, IOException {
        Element[] globals = dom.select(SCRIPT_SELECTOR).
                toArray(new Element[0]);

        String jvmVersion = System.getProperty("java.version");
        jse.put("jvmVersion", jvmVersion.substring(2, 3));

        String script = "";
        for (Element em : globals) {
            if (em.hasAttr("src")) {
                script += loadExternalScript(em.attr("src"));
            }
            script += em.data();
            script += "\n";

            // remove evaluated server-side javascript from the DOM
            em.remove();
        }

        if (!StringUtil.isBlank(script)) {
            return jse.eval(script);
        }
        return null;
    }

    private final static FileCache<String> SCRIPT_CACHE = new FileCache<>();

    /**
     * Load external server-side javascripts, referenced by HTML tag
     * <script type="server/javascript" src="externalScript.js">
     *
     * @param path path to the external script. It is relative to the
     * {@link ServletContext} path.
     * @return the content of the referenced file
     * @throws IOException in case of error accessing the referenced file
     */
    private String loadExternalScript(String path) throws IOException {
        String rv;
        CacheFile file;
        try {
            URL resource = servletContext.getResource(path);
            if (resource != null) {
                file = new CacheFile(resource.toURI());
            } else {
                throw new IOException(String.format("file not found: %s", path));
            }
        } catch (URISyntaxException ex) {
            Logger.getLogger(MochaTemplateEngine.class.getName()).
                    log(Level.SEVERE, null, ex);
            throw new IOException(new Exception(String.format(
                    "bad script path: %s", path), ex));
        }

        if (SCRIPT_CACHE.containsKey(file)) {
            rv = SCRIPT_CACHE.get(file);
        } else {
            try (FileInputStream input = new FileInputStream(file.getFile())) {
                rv = CharStreams.toString(new InputStreamReader(input));
                SCRIPT_CACHE.put(file, rv);
            }
        }
        return rv;
    }

    /**
     * Class to parse Mocha templates, using Javascript as scripting language.
     */
    private class TemplateParser implements Parser {

        private final ScriptEngine jse;

        /**
         * Initialize a new {@link TemplateParser} instance, setting jse as
         * {@link ScriptEngine} to evaluate Javascript expressions in the
         * template.
         *
         * @param jse Javascript engine to evaluate expressions in the template
         */
        public TemplateParser(ScriptEngine jse) {
            this.jse = jse;
        }

        @Override
        public void parseNode(Node node, Bindings bindings) throws
                ScriptException,
                UnsupportedEncodingException {

            if (!("template".equals(node.nodeName())
                    && node.hasAttr("data-type")
                    && "server/template".equals(node.attr("data-type")))) {

                TemplateNode dNode = new TemplateNode(node, bindings, jse, dom);
                Bindings nodeBindings = dNode.getBindings();

                if (node instanceof Element) {
                    boolean nodeNeedsFurtherProcessing = true;

                    Attribute[] attrs = node.attributes().asList().toArray(
                            new Attribute[0]);
                    List<String> processedAttrs = new ArrayList<>();

                    for (Attribute attr : attrs) {
                        EvalResult res = evalDataAttr(attr, dNode);

                        nodeNeedsFurtherProcessing = res.
                                needsFurtherProcessing();
                        if (!nodeNeedsFurtherProcessing) {
                            break;
                        }

                        // evaluated attributes will not be evaluated again
                        if (res.isEvaluated()) {
                            processedAttrs.add(attr.getKey());
                        }
                    }

                    // non data-attributes and child nodes get processed only if the node
                    // needs further processing
                    if (nodeNeedsFurtherProcessing) {
                        for (Attribute attr : attrs) {
                            if (!processedAttrs.contains(attr.getKey())) {
                                this.parseAttr(attr, nodeBindings);
                            }
                        }

                        Node[] nodes = node.childNodes().toArray(new Node[0]);
                        for (Node cnode : nodes) {
                            this.parseNode(cnode, nodeBindings);
                        }
                    }
                } else if (node instanceof TextNode) {
                    parseTextNode((TextNode) node, nodeBindings);
                } else if ("#comment".equals(node.nodeName())
                        && node.attr("comment").trim().startsWith(
                                "server-comment ")) {
                    node.remove();
                }
            }
        }

        @Override
        public Object parseDataAttr(Attribute attr, Bindings bindings) throws
                ScriptException {
            Object rv;
            if (bindings == null) {
                rv = jse.eval(attr.getValue());
            } else {
                rv = jse.eval(attr.getValue(), bindings);
            }
            return rv;
        }

        /**
         * This method parses an attribute value for a not data-* attribute
         *
         * @param attr attribute to be parsed
         * @param bindings local available variable javascript bindings for the
         * attribute's parent node
         * @throws ScriptException syntax error in ${} expressions
         */
        private void parseAttr(Attribute attr, Bindings bindings) throws
                ScriptException {
            String val = parseString(attr.getValue(), bindings);
            if (val != null) {
                attr.setValue(val);
            }
        }

        /**
         * This method parses a text node, executing expressions in ${} blocks
         * and replacing the blocks with expressions results.
         *
         * @param node text node to be parsed
         * @param bindings local available variable javascript bindings for the
         * node
         * @throws ScriptException syntax error in ${} expressions
         */
        private void parseTextNode(TextNode node, Bindings bindings) throws
                ScriptException {
            String val = parseString(node.text(), bindings);
            if (val != null) {
                node.text(Jsoup.clean(val, Whitelist.none()));
            }
        }

        @Override
        public String parseString(String text, Bindings bindings) throws
                ScriptException {
            String rv = null;

            if (!StringUtil.isBlank(text)) {
                StringBuffer sb = new StringBuffer();

                // unfortunately neither Rhino nor Nashorn do support ES6's multiline template literals
                // thus the best chance to support ${expression} syntax, without switching to an external JS engine, 
                // is to find it in text through a regular expression
                Matcher matcher = INLINE_EXP_PATTERN.matcher(text);

                boolean found = false;

                while (matcher.find()) {
                    found = true;
                    String group = matcher.group(1);
                    Object evaluated = jse.eval(group, bindings);
                    if (evaluated != null) {
                        if (evaluated instanceof Double) {
                            // Even if both Rhino and Nashorn turn every number to a Double
                            // integers must be printed as integers, without decimal expansion.
                            // And... DecimalFormat will format floating point numbers
                            // accordingly to the current locale settings.

                            DecimalFormat df = new DecimalFormat("0",
                                    DecimalFormatSymbols.getInstance());
                            // 340 is the maximum number of fraction digits supported by DigitalFormat
                            // this will prevent to print a large double in scientific notation
                            df.setMaximumFractionDigits(340);

                            evaluated = (df.format((double) evaluated));
                        }
                        // we are parsing strings and we have to always give back strings...
                        matcher.appendReplacement(sb, evaluated.toString());
                    } else {
                        matcher.appendReplacement(sb, "");
                    }
                }

                // if the ${[^}]+} pattern is never found in the input string,
                // null will be returned
                if (found) {
                    matcher.appendTail(sb);
                    rv = sb.toString();
                }
            }
            return rv;
        }

        /**
         * This method parses and evaluates a data-* attribute
         *
         * @param attr attribute to be evaluated
         * @param node the attribute's parent node
         * @return an evaluation result with
         * {@link EvalResult.needsFurtherProcessing} = true if the node hasn't
         * been removed from the dom, and {@link EvalResult.isEvaluated} = true
         * if a parser for the attribute has been found.
         * @throws ScriptException syntax error in the attribute's value
         * @throws UnsupportedEncodingException
         */
        private EvalResult evalDataAttr(Attribute attr, TemplateNode node)
                throws ScriptException, UnsupportedEncodingException {
            EvalResult rv = new EvalResult();
            // if nothing changes the node will have to be further processed for sure
            rv.setNeedsFurtherProcessing(true);

            AttributeParserArguments args = new AttributeParserArguments(attr);
            String instruction = args.getInstruction();

            // a blank instruction is expected in case the attribute name hasn't a 
            // valid syntax
            if (!StringUtil.isBlank(instruction)) {
                if (attrParsers.containsKey(instruction)) {

                    TemplateAttributeParser parser = attrParsers.get(instruction);
                    // the parser is expected to inform whether the processing of the current
                    // node has to continue
                    rv.setNeedsFurtherProcessing(parser.eval(node, args, this));

                    // this attribute has to be excluded from further evalutations
                    rv.setEvaluated(true);
                }
            }
            return rv;
        }

        /**
         * This class contains the result of a attribute parsing
         */
        private class EvalResult {

            private boolean needsFurtherProcessing;
            private boolean evaluated;

            /**
             * Check if the attribute's parent node needs further parsing
             *
             * @return true if the node requires further parsing
             */
            public boolean needsFurtherProcessing() {
                return needsFurtherProcessing;
            }

            private void setNeedsFurtherProcessing(
                    boolean needsFurtherProcessing) {
                this.needsFurtherProcessing = needsFurtherProcessing;
            }

            /**
             * Check if the attribute has been evaluated or skipped
             *
             * @return true is the attribute has been evaluated
             */
            public boolean isEvaluated() {
                return evaluated;
            }

            private void setEvaluated(boolean evaluated) {
                this.evaluated = evaluated;
            }
        }
    }
}
