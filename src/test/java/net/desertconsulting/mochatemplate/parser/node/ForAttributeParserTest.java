package net.desertconsulting.mochatemplate.parser.node;

import net.desertconsulting.mochatemplate.parser.Parser;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
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
public class ForAttributeParserTest {

    public ForAttributeParserTest() {
    }

    @Mocked
    Node node;

    @Mocked
    Bindings bindings;

    @Mocked
    ScriptEngine engine;

    @Mocked
    Document document;

    Attribute attribute = new Attribute("data-for-x", "xs");

    /**
     * Test of eval method, of class ForAttributeParser.
     * @throws java.lang.Exception
     */
    @Test
    public void testEvalList() throws Exception {
        System.out.println("eval");
        final List<String> ls = new ArrayList<>();
        ls.add("test1");
        ls.add("test2");
        new Expectations() {
            {
                engine.eval("xs", bindings);
                returns(ls);
            }
        };
        TemplateNode dataNode = new TemplateNode(node, bindings, engine,
                document);
        AttributeParserArguments args = new AttributeParserArguments(attribute);
        ForAttributeParser instance = new ForAttributeParser(engine);
        boolean expResult = false;
        ParserImpl parser = new ParserImpl();
        boolean result = instance.eval(dataNode, args, parser);
        assertEquals(expResult, result);
        assertEquals(2, parser.count);
    }

    /**
     * Test of eval method, of class ForAttributeParser.
     * @throws java.lang.Exception
     */
    @Test
    public void testEvalString() throws Exception {
        System.out.println("eval");
        new Expectations() {
            {
                engine.eval("xs", bindings);
                returns("test");
            }
        };
        TemplateNode dataNode = new TemplateNode(node, bindings, engine,
                document);
        AttributeParserArguments args = new AttributeParserArguments(attribute);
        ForAttributeParser instance = new ForAttributeParser(engine);
        boolean expResult = false;
        ParserImpl parser = new ParserImpl();
        boolean result = instance.eval(dataNode, args, parser);
        assertEquals(expResult, result);
        assertEquals(4, parser.count);
    }

    /**
     * Test of eval method, of class ForAttributeParser.
     * @throws java.lang.Exception
     */
    @Test
    public void testEvalRange() throws Exception {
        System.out.println("eval");
        TemplateNode dataNode = new TemplateNode(node, bindings, engine,
                document);

        Attribute attr = new Attribute("data-for-x", "1...4");

        AttributeParserArguments args = new AttributeParserArguments(attr);
        ForAttributeParser instance = new ForAttributeParser(engine);
        boolean expResult = false;
        ParserImpl parser = new ParserImpl();
        boolean result = instance.eval(dataNode, args, parser);
        assertEquals(expResult, result);
        assertEquals(4, parser.count);
    }

    /**
     * Test of eval method, of class ForAttributeParser.
     * @throws java.lang.Exception
     */
    @Test
    public void testEvalSingle() throws Exception {
        System.out.println("eval");
        new Expectations() {
            {
                engine.eval("xs", bindings);
                returns((double) 1.0);
            }
        };
        TemplateNode dataNode = new TemplateNode(node, bindings, engine,
                document);
        AttributeParserArguments args = new AttributeParserArguments(attribute);
        ForAttributeParser instance = new ForAttributeParser(engine);
        boolean expResult = false;
        ParserImpl parser = new ParserImpl();
        boolean result = instance.eval(dataNode, args, parser);
        assertEquals(expResult, result);
        assertEquals(1, parser.count);
    }

    /**
     * Test of eval method, of class ForAttributeParser.
     * @throws java.lang.Exception
     */
    @Test
    public void testEvalObject() throws Exception {
        System.out.println("eval");
        new Expectations() {
            {
                engine.eval("xs", bindings);
                returns(new Object() {
                    public final String test = "test";
                    public final boolean isTest = true;

                    public int getTest() {
                        return 0;
                    }
                });
            }
        };
        TemplateNode dataNode = new TemplateNode(node, bindings, engine,
                document);
        AttributeParserArguments args = new AttributeParserArguments(attribute);
        ForAttributeParser instance = new ForAttributeParser(engine);
        boolean expResult = false;
        ParserImpl parser = new ParserImpl();
        boolean result = instance.eval(dataNode, args, parser);
        assertEquals(expResult, result);
        assertEquals(3, parser.count);
    }

    /**
     * Test of eval method, of class ForAttributeParser.
     * @throws java.lang.Exception
     */
    @Test
    public void testEvalMap() throws Exception {
        System.out.println("eval");
        final Map<String, Object> map = new HashMap<>();
        map.put("test", "test");
        new Expectations() {
            {
                engine.eval("xs", bindings);
                returns(map);
            }
        };
        TemplateNode dataNode = new TemplateNode(node, bindings, engine,
                document);
        AttributeParserArguments args = new AttributeParserArguments(attribute);
        ForAttributeParser instance = new ForAttributeParser(engine);
        boolean expResult = false;
        ParserImpl parser = new ParserImpl();
        boolean result = instance.eval(dataNode, args, parser);
        assertEquals(expResult, result);
        assertEquals(1, parser.count);
    }

    /**
     * Test of eval method, of class ForAttributeParser.
     * @throws java.lang.Exception
     */
    @Test
    public void testEvalWithNullBindings() throws Exception {
        System.out.println("eval");
        final Map<String, Object> map = new HashMap<>();
        map.put("test", "test");
        TemplateNode dataNode = new TemplateNode(node, null, engine,
                document);
        AttributeParserArguments args = new AttributeParserArguments(attribute);
        ForAttributeParser instance = new ForAttributeParser(engine);
        boolean expResult = false;
        ParserImpl parser = new ParserImpl();
        boolean result = instance.eval(dataNode, args, parser);
        assertEquals(expResult, result);
        assertEquals(0, parser.count);
    }

    private class ParserImpl implements Parser {

        public Attribute parsedAttr;
        public int count = 0;

        @Override
        public void parseNode(Node node, Bindings bindings) throws
                ScriptException, UnsupportedEncodingException {
            count++;
        }

        @Override
        public Object parseDataAttr(Attribute attr, Bindings bindings)
                throws ScriptException {
            parsedAttr = attr;
            return null;
        }

        @Override
        public String parseString(String text, Bindings bindings) throws
                ScriptException {
            return null;
        }
    }

    /**
     * Test of supportedAttr method, of class ForAttributeParser.
     */
    @Test
    public void testSupportedAttr() {
        System.out.println("supportedAttr");
        ForAttributeParser instance = new ForAttributeParser(null);
        String expResult = "for";
        String result = instance.supportedAttr();
        assertEquals(expResult, result);
    }

}
