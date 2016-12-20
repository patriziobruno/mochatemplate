/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.desertconsulting.mochatemplate.parser.node;

import net.desertconsulting.mochatemplate.parser.Parser;
import javax.script.Bindings;
import javax.script.ScriptEngine;
import mockit.Expectations;
import mockit.Mocked;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Patrizio Bruno {@literal <desertconsulting@gmail.com>}
 */
public class IfAttributeParserTest {

    public IfAttributeParserTest() {
    }

    @Mocked
    Node node;

    @Mocked
    Bindings bindings;

    @Mocked
    ScriptEngine engine;

    @Mocked
    Document document;

    @Mocked
    Parser parser;

    /**
     * Test of eval method, of class IfAttributeParser.
     * @throws java.lang.Exception
     */
    @Test
    public void testEvalFalse() throws Exception {
        System.out.println("eval");
        TemplateNode dataNode = new TemplateNode(node, bindings, engine,
                document);
        final Attribute attribute = new Attribute("data-if", "false");
        AttributeParserArguments args = new AttributeParserArguments(attribute);
        IfAttributeParser instance = new IfAttributeParser(engine);
        final boolean expResult = false;

        new Expectations() {
            {
                parser.parseDataAttr(attribute, bindings);
                returns(expResult);
            }
        };
        boolean result = instance.eval(dataNode, args, parser);
        assertEquals(expResult, result);
    }

    /**
     * Test of eval method, of class IfAttributeParser.
     * @throws java.lang.Exception
     */
    @Test
    public void testEvalZero() throws Exception {
        System.out.println("eval");
        TemplateNode dataNode = new TemplateNode(node, bindings, engine,
                document);
        final Attribute attribute = new Attribute("data-if", "0");
        AttributeParserArguments args = new AttributeParserArguments(attribute);
        IfAttributeParser instance = new IfAttributeParser(engine);
        final boolean expResult = false;

        new Expectations() {
            {
                parser.parseDataAttr(attribute, bindings);
                returns((double)0.0);
            }
        };
        boolean result = instance.eval(dataNode, args, parser);
        assertEquals(expResult, result);
    }

    /**
     * Test of eval method, of class IfAttributeParser.
     * @throws java.lang.Exception
     */
    @Test
    public void testEvalNull() throws Exception {
        System.out.println("eval");
        TemplateNode dataNode = new TemplateNode(node, bindings, engine,
                document);
        final Attribute attribute = new Attribute("data-if", "null");
        AttributeParserArguments args = new AttributeParserArguments(attribute);
        IfAttributeParser instance = new IfAttributeParser(engine);
        final boolean expResult = false;

        new Expectations() {
            {
                parser.parseDataAttr(attribute, bindings);
                returns(null);
            }
        };
        boolean result = instance.eval(dataNode, args, parser);
        assertEquals(expResult, result);
    }

    /**
     * Test of eval method, of class IfAttributeParser.
     * @throws java.lang.Exception
     */
    @Test
    public void testEvalObject() throws Exception {
        System.out.println("eval");
        TemplateNode dataNode = new TemplateNode(node, bindings, engine,
                document);
        final Attribute attribute = new Attribute("data-if", "null");
        AttributeParserArguments args = new AttributeParserArguments(attribute);
        IfAttributeParser instance = new IfAttributeParser(engine);
        final boolean expResult = true;

        new Expectations() {
            {
                parser.parseDataAttr(attribute, bindings);
                returns(new Object());
            }
        };
        boolean result = instance.eval(dataNode, args, parser);
        assertEquals(expResult, result);
    }

    /**
     * Test of eval method, of class IfAttributeParser.
     * @throws java.lang.Exception
     */
    @Test
    public void testEvalEmptyString() throws Exception {
        System.out.println("eval");
        TemplateNode dataNode = new TemplateNode(node, bindings, engine,
                document);
        final Attribute attribute = new Attribute("data-if", "''");
        AttributeParserArguments args = new AttributeParserArguments(attribute);
        IfAttributeParser instance = new IfAttributeParser(engine);
        final boolean expResult = false;

        new Expectations() {
            {
                parser.parseDataAttr(attribute, bindings);
                returns("");
            }
        };
        boolean result = instance.eval(dataNode, args, parser);
        assertEquals(expResult, result);
    }
    /**
     * Test of eval method, of class IfAttributeParser.
     * @throws java.lang.Exception
     */
    @Test
    public void testEvalTrue() throws Exception {
        System.out.println("eval");
        TemplateNode dataNode = new TemplateNode(node, bindings, engine,
                document);
        final Attribute attribute = new Attribute("data-if", "true");
        AttributeParserArguments args = new AttributeParserArguments(attribute);
        IfAttributeParser instance = new IfAttributeParser(engine);
        final boolean expResult = true;

        new Expectations() {
            {
                parser.parseDataAttr(attribute, bindings);
                returns(expResult);
            }
        };
        boolean result = instance.eval(dataNode, args, parser);
        assertEquals(expResult, result);
    }

    /**
     * Test of supportedAttr method, of class IfAttributeParser.
     */
    @Test
    public void testSupportedAttr() {
        System.out.println("supportedAttr");
        IfAttributeParser instance = new IfAttributeParser(engine);
        String expResult = "if";
        String result = instance.supportedAttr();
        assertEquals(expResult, result);
    }
}
