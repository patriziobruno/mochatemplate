/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.desertconsulting.mochatemplate.parser.node;

import net.desertconsulting.mochatemplate.parser.Parser;
import java.net.MalformedURLException;
import java.net.URL;
import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import javax.servlet.ServletContext;
import mockit.Expectations;
import mockit.Mocked;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Patrizio Bruno <desertconsulting@gmail.com>
 */
public class IncludeAttributeParserTest {

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

    @Mocked
    private ServletContext context;

    public IncludeAttributeParserTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of eval method, of class IncludeAttributeParser.
     * @throws java.lang.Exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void testEvalIllegalArgumentException() throws Exception {
        System.out.println("eval");
        Attribute attribute = new Attribute("data-include", "@..");
        TemplateNode dataNode = new TemplateNode(node, bindings, engine, document);
        AttributeParserArguments args = new AttributeParserArguments(attribute);
        new Expectations() {
            {
                parser.parseString("@..", bindings);
                returns("@...");
            }
        };
        IncludeAttributeParser instance = new IncludeAttributeParser(engine, context);
        boolean expResult = true;
        boolean result = instance.eval(dataNode, args, parser);
        assertEquals(expResult, result);
    }

    /**
     * Test of eval method, of class IncludeAttributeParser.
     * @throws java.lang.Exception
     */
    @Test(expected = ScriptException.class)
    public void testEvalBadRefSyntax() throws Exception {
        System.out.println("eval");
        Attribute attribute = new Attribute("data-include", "@/tmp/tmpfile");
        TemplateNode dataNode = new TemplateNode(node, bindings, engine, document);
        AttributeParserArguments args = new AttributeParserArguments(attribute);
        new Expectations() {
            {
                context.getResource("/tmp/tmpfile");
                returns(new URL("file:///tmp/tmpfile"));

                parser.parseString("@/tmp/tmpfile", bindings);
                returns("@/tmp/tmpfile");
            }
        };
        IncludeAttributeParser instance = new IncludeAttributeParser(engine, context);
        boolean expResult = true;
        boolean result = instance.eval(dataNode, args, parser);
        assertEquals(expResult, result);
    }

    @Mocked
    private Elements templateNodes;
    
    /**
     * Test of eval method, of class IncludeAttributeParser.
     * @throws java.lang.Exception
     */
    @Test
    public void testEval() throws Exception {
        System.out.println("eval");
        Attribute attribute = new Attribute("data-include", "#test");
        TemplateNode dataNode = new TemplateNode(node, bindings, engine, document);
        AttributeParserArguments args = new AttributeParserArguments(attribute);
        new Expectations() {
            {
                parser.parseString("#test", bindings);
                returns("#test");

                document.select("template[data-type=\"server/template\"]");
                returns(templateNodes);
                
                templateNodes.select("#test");
                returns(templateNodes);
            }
        };
        IncludeAttributeParser instance = new IncludeAttributeParser(engine, context);
        boolean expResult = true;
        boolean result = instance.eval(dataNode, args, parser);
        assertEquals(expResult, result);
    }

    /**
     * Test of supportedAttr method, of class IncludeAttributeParser.
     */
    @Test
    public void testSupportedAttr() {
        System.out.println("supportedAttr");
        IncludeAttributeParser instance = new IncludeAttributeParser(engine, context);
        String expResult = "include";
        String result = instance.supportedAttr();
        assertEquals(expResult, result);
    }

}
