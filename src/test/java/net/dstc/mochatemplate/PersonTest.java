package net.dstc.mochatemplate;

import java.util.Collections;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Patrizio Bruno <desertconsulting@gmail.com>
 */
public class PersonTest {

    public PersonTest() {
    }

    /**
     * Test of lookup method, of class Person.
     */
    @Test
    public void testLookupByNull() {
        System.out.println("lookup");
        String id = null;
        Person expResult = new Person(0, "Empty Name", "Empty spouse", false, 0);
        Person result = Person.lookup(id);
        assertNotNull(result);
        assertEquals(expResult.getId(), result.getId());
        assertEquals(expResult.getName(), result.getName());
        assertEquals(expResult.getSpouse(), result.getSpouse());
        assertEquals(expResult.isMarried(), result.isMarried());
        assertEquals(expResult.getChildren(), result.getChildren());
    }

    /**
     * Test of lookup method, of class Person.
     */
    @Test
    public void testLookupById() {
        System.out.println("lookup");
        String id = "1";
        Person expResult = new Person(0, "Mario Rossi", "", false, 1);
        Person result = Person.lookup(id);
        assertNotNull(result);
        assertEquals(expResult.getId(), result.getId());
        assertEquals(expResult.getName(), result.getName());
        assertEquals(expResult.getSpouse(), result.getSpouse());
        assertEquals(expResult.isMarried(), result.isMarried());
        assertEquals(expResult.getChildren(), result.getChildren());
    }

    /**
     * Test of lookup method, of class Person.
     */
    @Test
    public void testLookupByNonExistantId() {
        System.out.println("lookup");
        String id = "4";
        Person expResult = new Person(0, "Empty Name", "Empty spouse", false, 0);
        Person result = Person.lookup(id);
        assertNotNull(result);
        assertEquals(expResult.getId(), result.getId());
        assertEquals(expResult.getName(), result.getName());
        assertEquals(expResult.getSpouse(), result.getSpouse());
        assertEquals(expResult.isMarried(), result.isMarried());
        assertEquals(expResult.getChildren(), result.getChildren());
    }

    /**
     * Test of search method, of class Person.
     */
    @Test
    public void testSearchNoResults() {
        System.out.println("search");
        String name = "not_an_available_name";
        List<Person> expResult = Collections.emptyList();
        List<Person> result = Person.search(name);
        assertEquals(expResult, result);
    }

    /**
     * Test of search method, of class Person.
     */
    @Test
    public void testSearchSingleResult() {
        System.out.println("search");
        String name = "Mario";
        int expResult = 1;
        List<Person> result = Person.search(name);
        assertEquals(expResult, result.size());
    }

    /**
     * Test of search method, of class Person.
     */
    @Test
    public void testSearchNullQuery() {
        System.out.println("search");
        String name = null;
        int expResult = 3;
        List<Person> result = Person.search(name);
        assertEquals(expResult, result.size());
    }

    /**
     * Test of getId method, of class Person.
     */
    @Test
    public void testGetId() {
        System.out.println("getId");
        Person instance = new Person();
        int expResult = 0;
        int result = instance.getId();
        assertEquals(expResult, result);
    }

    /**
     * Test of setId method, of class Person.
     */
    @Test
    public void testSetId() {
        System.out.println("setId");
        int id = 0;
        Person instance = new Person();
        instance.setId(id);
        assertEquals(id, instance.getId());
    }

    /**
     * Test of getName method, of class Person.
     */
    @Test
    public void testGetName() {
        System.out.println("getName");
        Person instance = new Person();
        String expResult = "";
        String result = instance.getName();
        assertEquals(expResult, result);
    }

    /**
     * Test of setName method, of class Person.
     */
    @Test
    public void testSetName() {
        System.out.println("setName");
        String name = "test";
        Person instance = new Person();
        instance.setName(name);
        assertEquals(name, instance.getName());
    }

    /**
     * Test of getSpouse method, of class Person.
     */
    @Test
    public void testGetSpouse() {
        System.out.println("getSpouse");
        Person instance = new Person();
        String expResult = "";
        String result = instance.getSpouse();
        assertEquals(expResult, result);
    }

    /**
     * Test of setSpouse method, of class Person.
     */
    @Test
    public void testSetSpouse() {
        System.out.println("setSpouse");
        String spouse = "test";
        Person instance = new Person();
        instance.setSpouse(spouse);
        assertEquals(spouse, instance.getSpouse());
    }

    /**
     * Test of isMarried method, of class Person.
     */
    @Test
    public void testIsMarried() {
        System.out.println("isMarried");
        Person instance = new Person();
        boolean expResult = false;
        boolean result = instance.isMarried();
        assertEquals(expResult, result);
    }

    /**
     * Test of setMarried method, of class Person.
     */
    @Test
    public void testSetMarried() {
        System.out.println("setMarried");
        boolean married = false;
        Person instance = new Person();
        instance.setMarried(married);
        assertEquals(married, instance.isMarried());
    }

    /**
     * Test of getChildren method, of class Person.
     */
    @Test
    public void testGetChildren() {
        System.out.println("getChildren");
        Person instance = new Person();
        List<String> expResult = Collections.emptyList();
        List<String> result = instance.getChildren();
        assertEquals(expResult, result);
    }

    /**
     * Test of setChildren method, of class Person.
     */
    @Test
    public void testSetChildren() {
        System.out.println("setChildren");
        List<String> children = Collections.emptyList();
        Person instance = new Person();
        instance.setChildren(children);
        assertEquals(children, instance.getChildren());
    }

    /**
     * Test of toString method, of class Person.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        Person instance = new Person();
        String expResult = "Person [name=, married=false, spouse=, children="
                + instance.getChildren() + "]";
        String result = instance.toString();
        assertEquals(expResult, result);
    }

}
