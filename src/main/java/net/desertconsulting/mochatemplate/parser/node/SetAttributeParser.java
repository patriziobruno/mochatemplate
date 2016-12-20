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
import java.io.UnsupportedEncodingException;
import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

/**
 * This attribute parser supports data-set attribute. It permits to set a
 * variable in the current Javascript context, available only to javascript expressions
 * nested into the parent node.
 *
 * @author Patrizio Bruno <desertconsulting@gmail.com>
 */
public class SetAttributeParser extends TemplateAttributeParser {

    private static final String SUPPORTED_FIELD = "set";

    /**
     * .ctor
     *
     * @param engine Javascript engine to evaluate Javascript expressions
     */
    public SetAttributeParser(ScriptEngine engine) {
        super(engine);
    }

    /**
     * This method evaluates data-set attributes. args must hold a single arg,
     * with the name of the variable to be set: eg. "data-set-name" will set a
     * new variable named "name" in the current Javascript context.
     *
     * @return always true
     *
     * @throws ScriptException syntax error in a javascript expression or bad
     * data-set
     * @throws UnsupportedEncodingException
     */
    @Override
    public boolean eval(TemplateNode dataNode, AttributeParserArguments args,
            Parser parser) throws ScriptException, UnsupportedEncodingException {
        String[] arguments = args.getArgs();
        if (arguments != null && arguments.length == 1) {
            Bindings bindings = dataNode.getBindings();
            bindings.put(arguments[0], parser.parseDataAttr(args.getAttribute(),
                    bindings));
        } else {
            throw new ScriptException(
                    "template error: wrong number of arguments to data-set");
        }
        dataNode.getNode().removeAttr(args.getAttributeName());

        return true;
    }

    /**
     * This method returns "set".
     *
     * @return "set"
     */
    @Override
    public String supportedAttr() {
        return SUPPORTED_FIELD;
    }

}
