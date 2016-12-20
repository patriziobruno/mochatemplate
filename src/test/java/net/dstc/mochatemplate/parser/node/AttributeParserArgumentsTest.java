package net.dstc.mochatemplate.parser.node;

import org.jsoup.nodes.Attribute;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Patrizio Bruno <desertconsulting@gmail.com>
 */
public class AttributeParserArgumentsTest {
    
    public AttributeParserArgumentsTest() {
    }

    /**
     * Test of getArgs method, of class AttributeParserArguments.
     */
    @Test
    public void testGetArgs() {
        System.out.println("getArgs");
        Attribute attribute = new Attribute(
                "data-instruction-argument1-argument2-argument3", "");
        AttributeParserArguments instance = new AttributeParserArguments(
                attribute);
        String[] expResult = new String[]{"argument1", "argument2", "argument3"};
        String[] result = instance.getArgs();
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of getExpression method, of class AttributeParserArguments.
     */
    @Test
    public void testGetExpression() {
        System.out.println("getExpression");
        String expResult = "parseInt('0')";
        Attribute attribute = new Attribute(
                "data-instruction-argument1-argument2-argument3", expResult);
        AttributeParserArguments instance = new AttributeParserArguments(
                attribute);
        String result = instance.getExpression();
        assertEquals(expResult, result);
    }

    /**
     * Test of getAttributeName method, of class AttributeParserArguments.
     */
    @Test
    public void testGetAttributeName() {
        System.out.println("getAttributeName");
        String expResult = "data-instruction";
        Attribute attribute = new Attribute(expResult, expResult);
        AttributeParserArguments instance = new AttributeParserArguments(
                attribute);
        String result = instance.getAttributeName();
        assertEquals(expResult, result);
    }

    /**
     * Test of getInstruction method, of class AttributeParserArguments.
     */
    @Test
    public void testGetInstruction() {
        System.out.println("getInstruction");
        String expResult = "instruction";
        Attribute attribute = new Attribute("data-" + expResult, expResult);
        AttributeParserArguments instance = new AttributeParserArguments(
                attribute);
        String result = instance.getInstruction();
        assertEquals(expResult, result);
    }

    /**
     * Test of getAttribute method, of class AttributeParserArguments.
     */
    @Test
    public void testGetAttribute() {
        System.out.println("getAttribute");
        Attribute attribute = new Attribute("data-test", "");
        AttributeParserArguments instance = new AttributeParserArguments(
                attribute);
        Attribute expResult = attribute;
        Attribute result = instance.getAttribute();
        assertEquals(expResult, result);
    }

    /**
     * Test of length method, of class AttributeParserArguments.
     */
    @Test
    public void testLength() {
        System.out.println("length");
        Attribute attribute = new Attribute("data-instruction-arg1-arg2", "");
        AttributeParserArguments instance = new AttributeParserArguments(
                attribute);
        int expResult = 2;
        int result = instance.length();
        assertEquals(expResult, result);
    }
    
}
