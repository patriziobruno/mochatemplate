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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import javax.script.ScriptException;
import org.jsoup.nodes.Document;

/**
 * This interface defines template engines
 *
 * @author Patrizio Bruno {@literal <desertconsulting@gmail.com>}
 */
public interface TemplateEngine {

    public final static String SCRIPT_SELECTOR
            = "script[type=\"server/javascript\"]";

    /**
     * Runs the parsing of a template and returns the output as a String.
     *
     * @param outputSettings HTML generation settings
     * @return HTML document
     * @throws ScriptException javascript syntax error
     * @throws UnsupportedEncodingException
     * @throws IOException error loading the template or one of its external files
     * resource
     */
    String parse(Document.OutputSettings outputSettings) throws ScriptException,
            UnsupportedEncodingException, IOException;

    /**
     * Runs the parsing of a template and writes the output on parameter
     * {@code output}.
     *
     * @param output output stream
     * @param outputSettings HTML generation settings
     * @throws ScriptException javascript syntax error
     * @throws UnsupportedEncodingException
     * @throws IOException error loading the template or one of its external files
     * resource
     */
    void parse(Appendable output, Document.OutputSettings outputSettings) throws
            ScriptException, UnsupportedEncodingException,
            IOException;

    /**
     * Runs the scripts in the loaded template and writes the scripts' output on
     * the stream {@code output}.
     *
     * @param output output stream
     * @param outputFormat outputFormat (JSON/XML)
     * @throws ScriptException javascript syntax error
     * @throws IOException error loading the template or one of its external files
     */
    void exec(Appendable output, ApiOutputFormat outputFormat) throws ScriptException, IOException;

    /**
     * Runs the scripts in the loaded template.
     *
     * @throws ScriptException javascript syntax error
     * @throws IOException error loading the template or one of its external files
     * @return result of the executed script.
     */
    Object exec() throws ScriptException, IOException;

    /**
     * Adds a variable to be made available to the Javascript engine.
     *
     * @param key name of the variable
     * @param value value of the variable
     */
    void put(String key, Object value);

    /**
     * Adds a variable to be made available to the Javascript engine after
     * parsing it as JSON.
     *
     * @param key name of the variable
     * @param value value of the variable
     * @throws ScriptException error parsing the value
     */
    void putJson(String key, String value) throws ScriptException;
}
