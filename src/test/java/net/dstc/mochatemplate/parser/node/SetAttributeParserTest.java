/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.dstc.mochatemplate.parser.node;

import net.dstc.mochatemplate.parser.Parser;
import java.util.HashMap;
import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import mockit.Expectations;
import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Patrizio Bruno <desertconsulting@gmail.com>
 */
public class SetAttributeParserTest {

    @Mocked
    private ScriptEngine engine;

    @Mocked
    private Document document;

    @Mocked
    private Parser parser;

    public SetAttributeParserTest() {
    }

    /**
     * Test of eval method, of class SetAttributeParser.
     */
    @Test
    public void testEval() throws Exception {
        System.out.println("eval");
        MockUp<Node> nodeMock = new MockUp<Node>() {
            private Attributes _attrs;

            @Mock
            public Attributes attributes() {
                if (_attrs == null) {
                    _attrs = new Attributes();
                }
                return _attrs;
            }

            @Mock
            public Node attr(String key, String value) {
                attributes().put(new Attribute(key, value));
                return this.getMockInstance();
            }

            @Mock
            public String attr(String key) {
                return attributes().get(key);
            }
            
            @Mock
            public Node removeAttr(String key) {
                attributes().remove(key);
                return this.getMockInstance();
            }
        };
        Node node = nodeMock.getMockInstance();
        String varName = "x";
        final String testValue = "test";
        final Attribute attribute = new Attribute("data-set-" + varName, "'" + testValue + "'");
        node.attr("data-set-" + varName, "'" + testValue + "'");
        document.appendChild(node);
        final Bindings bindings = new BindingsTestImpl();
        new Expectations() {
            {
                engine.createBindings();
                returns(bindings);

                parser.parseDataAttr(attribute, bindings);
                returns(testValue);
            }
        };
        TemplateNode dataNode = new TemplateNode(node, bindings, engine, document);
        AttributeParserArguments args = new AttributeParserArguments(attribute);
        SetAttributeParser instance = new SetAttributeParser(engine);
        boolean expResult = true;
        boolean result = instance.eval(dataNode, args, parser);
        assertEquals(expResult, result);
        assertTrue(bindings.containsKey(varName));
        assertEquals(testValue, bindings.get(varName));
    }

    /**
     * Test of eval method, of class SetAttributeParser.
     */
    @Test(expected = ScriptException.class)
    public void testEvalTooManyArgs() throws Exception {
        System.out.println("eval");
        MockUp<Node> nodeMock = new MockUp<Node>() {
        };
        Node node = nodeMock.getMockInstance();
        String varName = "x";
        final String testValue = "test";
        final Attribute attribute = new Attribute("data-set-" + varName + "-y-z", "'" + testValue + "'");
        final Bindings bindings = new BindingsTestImpl();
        new Expectations() {
            {
                engine.createBindings();
                returns(bindings);
            }
        };
        TemplateNode dataNode = new TemplateNode(node, bindings, engine, document);
        AttributeParserArguments args = new AttributeParserArguments(attribute);
        SetAttributeParser instance = new SetAttributeParser(engine);
        boolean expResult = true;
        boolean result = instance.eval(dataNode, args, parser);
        assertEquals(expResult, result);
        assertTrue(bindings.containsKey(varName));
        assertEquals(testValue, bindings.get(varName));
    }

    /**
     * Test of supportedAttr method, of class SetAttributeParser.
     */
    @Test
    public void testSupportedAttr() {
        System.out.println("supportedAttr");
        SetAttributeParser instance = new SetAttributeParser(engine);
        String expResult = "set";
        String result = instance.supportedAttr();
        assertEquals(expResult, result);
    }

    private class BindingsTestImpl extends HashMap<String, Object> implements Bindings {
    }
}
