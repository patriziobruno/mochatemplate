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

import javax.script.Bindings;
import javax.script.ScriptEngine;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;

/**
 * Container for a {@link Node} to be parsed.
 *
 * @author Patrizio Bruno <desertconsulting@gmail.com>
 */
public class TemplateNode {

    private final Node node;
    private final Document document;
    private final Bindings bindings;

    /**
     * Initialize a new instance of {@link TemplateNode}, setting all the
     * instance's properties with the passed paramters.
     *
     * @param node node to be parsed
     * @param bindings Javascript variable bindings
     * @param engine Javascript engine
     * @param document node's parent document
     */
    public TemplateNode(Node node, Bindings bindings, ScriptEngine engine,
            Document document) {
        this.node = node;
        this.document = document;
        if (bindings != null) {
            this.bindings = engine.createBindings();
            this.bindings.putAll(bindings);
        } else {
            this.bindings = null;
        }
    }

    /**
     * Gets the node to be parsed
     *
     * @return {@code node} passed as parameter to the constructor
     */
    public Node getNode() {
        return node;
    }

    /**
     * Gets the Javacript variable bindings to be passed to the Javascript
     * engine
     *
     * @return {@code bindings} passed as parameter to the constructor
     */
    public Bindings getBindings() {
        return bindings;
    }

    /**
     * Gets the document
     *
     * @return {@code document} passed as parameter to the constructor
     */
    public Document getDocument() {
        return document;
    }
}
