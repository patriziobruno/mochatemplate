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

import java.util.Arrays;
import org.jsoup.nodes.Attribute;

/**
 * This class is a container for an attribute to be passed to a
 * {@link TemplateAttributeParser}. It parses and validates attribute data.
 *
 * @author Patrizio Bruno <desertconsulting@gmail.com>
 */
public class AttributeParserArguments {

    private final String[] args;
    private final String expression;
    private final String attributeName;
    private final String instruction;
    private final Attribute attribute;

    /**
     * Initialize a new instance of {@link AttributeParserArguments} validating
     * and parsing the {@code attribute} passed as parameter. It parses the
     * attribute's name and value and initialize the instance based on the
     * following rules:
     *
     * - The attribute name must start with "data-". - The first word after
     * "data-" is the name of the instruction and thus the name of the template
     * parser to be used (eg. "for", "if", ...). - Every other word, separated
     * by a dash "-", will be added to the property {@code arguments}. Each
     * argument will be lower case. - The value of the attribute will be
     * assigned to the property {@code expression}.
     *
     * @param attribute attribute to be parsed
     */
    public AttributeParserArguments(Attribute attribute) {

        this.attributeName = attribute.getKey();

        if (attributeName.startsWith("data-")) {

            this.attribute = attribute;
            this.expression = attribute.getValue();

            String[] arguments = attributeName.split("-");
            this.instruction = arguments[1];

            if (arguments.length >= 3) {
                this.args = Arrays.copyOfRange(arguments, 2, arguments.length);
            } else {
                this.args = new String[0];
            }
        } else {
            this.expression = null;
            this.instruction = null;
            this.args = null;
            this.attribute = null;
        }
    }

    /**
     * Gets arguments passed within the attribute name: eg. - "data-for-x":
     * {@code x} is the first element of the array - "data-for-x-idx": {@code x}
     * and {@code idx} are respectively first and second element of the array
     *
     * @return arguments
     */
    public String[] getArgs() {
        return args;
    }

    /**
     * Gets the {@link Attribute} value.
     *
     * @return {@link Attribute} value
     */
    public String getExpression() {
        return expression;
    }

    /**
     * Gets the full attribute's name: eg. data-for-x
     *
     * @return the full attribute's name
     */
    public String getAttributeName() {
        return attributeName;
    }

    /**
     * Gets the instruction portion of the attribute name: eg. for "data-if" it
     * returns "if"
     *
     * @return the instruction portion of the attribute name
     */
    public String getInstruction() {
        return instruction;
    }

    /**
     * Gets the parsed {@code attribute}, passed as a parameter to the
     * constructor.
     *
     * @return {@code attribute}
     */
    public Attribute getAttribute() {
        return attribute;
    }

    /**
     * Gets the length of the {@code args} property.
     *
     * @return length of the {@code args} property
     */
    public int length() {
        return args != null ? args.length : 0;
    }
}
