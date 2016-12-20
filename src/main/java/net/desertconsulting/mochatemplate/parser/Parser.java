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

import java.io.UnsupportedEncodingException;
import javax.script.Bindings;
import javax.script.ScriptException;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Node;

/**
 * This interface defines basic methods for a template parser.
 * 
 * @author Patrizio Bruno {@literal <desertconsulting@gmail.com>}
 */
public interface Parser {

    /**
     * Parses a template node
     * 
     * @param node node to be parsed
     * @param bindings javascript variable bindings for the current node
     * @throws ScriptException javascript syntax error
     * @throws UnsupportedEncodingException 
     */
    void parseNode(Node node, Bindings bindings) throws ScriptException,
            UnsupportedEncodingException;

    /**
     * Parses a node's data-* attribute
     * 
     * @param attr the attribute to be parsed
     * @param bindings javascript variable bindings for the current node
     * @return the result of the javascript expression contained in the attribute's value
     * @throws ScriptException javascript syntax error
     */
    Object parseDataAttr(Attribute attr, Bindings bindings) throws
            ScriptException;

    /**
     * Parses the text in a text-node or in an attribute value. The text may contain
     * javascript expressions in ${} blocks.
     * 
     * @param text text to be par
     * @param bindings
     * @return the parsed string or null if no ${} blocks have been found
     * @throws ScriptException javascript syntax error
     */
    String parseString(String text, Bindings bindings) throws
            ScriptException;
}
