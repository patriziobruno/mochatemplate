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
import javax.script.ScriptEngine;
import javax.script.ScriptException;

/**
 * This serves as a base class to create template node's attributes parsers.
 * 
 * @author Patrizio Bruno {@literal <desertconsulting@gmail.com>}
 */
public abstract class TemplateAttributeParser {

    /**
     * Javascript engine that will evaluate every javascript expression in the template
     * expressions
     */
    protected final ScriptEngine engine;

    /**
     * Initializes a new instance of {@link TemplateAttributeParser}, setting
     * {@code engine} as Javascript engine.
     * @param engine Javascript engine that will evaluate every javascript expression in the template
     */
    public TemplateAttributeParser(ScriptEngine engine) {
        this.engine = engine;
    }

    /**
     * Attribute parsers must override this method to parse a template data- attribute
     * and evaluate the template portion represented by {@code dataNode}.
     * 
     * @param dataNode template node to be parsed
     * @param args attribute and arguments to the parser
     * @param parser template parser, it will eventually be used to parse all the {@code dataNode} children
     * @return true if the {@code dataNode} will need further processing; false if no further processing is needed,
     * for example when the node has been removed from the document
     * 
     * @throws ScriptException syntax error in a Javascript expression
     * @throws UnsupportedEncodingException encoding not supported in a Javascript expression
     */
    public abstract boolean eval(TemplateNode dataNode, AttributeParserArguments args, Parser parser)
            throws ScriptException, UnsupportedEncodingException;

    /**
     * Attribute parsers must override this method to indicate which instruction they support:
     * eg.
     * "for" if they support data-for attributes
     * 
     * @return a string containing the supported instruction
     */
    public abstract String supportedAttr();
}
