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
 * This attribute parser supports data-ignore attribute, any other template related
 * stuff in the parent node will be ignored when this attribute is encountered.
 *
 * @author Patrizio Bruno <desertconsulting@gmail.com>
 */
public class IgnoreAttributeParser extends TemplateAttributeParser {

    private static final String SUPPORTED_FIELD = "ignore";

    public IgnoreAttributeParser(ScriptEngine engine) {
        super(engine);
    }

    @Override
    public boolean eval(TemplateNode dataNode, AttributeParserArguments args, Parser parser) throws ScriptException, UnsupportedEncodingException {
        dataNode.getNode().removeAttr(args.getAttributeName());
        return false;
    }

    @Override
    public String supportedAttr() {
        return SUPPORTED_FIELD;
    }
}
