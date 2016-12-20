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
package net.dstc.mochatemplate.parser.node;

import net.dstc.mochatemplate.parser.Parser;
import java.io.UnsupportedEncodingException;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import org.jsoup.helper.StringUtil;

/**
 * This attribute parser supports data-if attribute. It implements a condition
 * check instruction (if). It gets no parameters. It evaluates Javascript
 * expression and if the result is false (=== false || === 0 || === ''), the
 * parent node will be removed from the DOM.
 *
 * @author Patrizio Bruno <desertconsulting@gmail.com>
 */
public class IfAttributeParser extends TemplateAttributeParser {

    private static final String SUPPORTED_FIELD = "if";

    /**
     * .ctor
     *
     * @param engine javascript engine
     */
    public IfAttributeParser(ScriptEngine engine) {
        super(engine);
    }

    @Override
    public boolean eval(TemplateNode node, AttributeParserArguments args,
            Parser parser) throws ScriptException, UnsupportedEncodingException {
        boolean rv = false;
        Object obj = parser.parseDataAttr(args.getAttribute(), node.
                getBindings());
        if (obj != null) {
            if (obj instanceof Boolean) {
                rv = ((boolean) obj);
            } else if (obj instanceof Double) {
                rv = ((Double) obj) != 0.0;
            } else if (obj instanceof String) {
                rv = !StringUtil.isBlank((String) obj);
            } else {
                rv = true;
            }
        }

        if (rv) {
            node.getNode().removeAttr(args.getAttributeName());
        } else {
            node.getNode().remove();
        }

        // if the condition is false, the node has been removed and the node
        // wont need further evaluation
        return rv;
    }

    @Override
    public String supportedAttr() {
        return SUPPORTED_FIELD;
    }
}
