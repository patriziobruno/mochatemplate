package net.desertconsulting.mochatemplate.parser.cache;

import java.io.File;
import java.net.URI;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Patrizio Bruno <desertconsulting@gmail.com>
 */
public class CacheFileTest {
    
    URI uri1 = new File(".").toURI();
    URI uri2 = new File("/tmp").toURI();
    
    public CacheFileTest() {
    }
    /**
     * Test of getUri method, of class CacheFile.
     */
    @Test
    public void testGetUri() {
        System.out.println("getUri");
        URI expResult = uri1;
        CacheFile instance = new CacheFile(expResult);
        URI result = instance.getUri();
        assertEquals(expResult, result);
    }

    /**
     * Test of getFile method, of class CacheFile.
     */
    @Test
    public void testGetFile() {
        System.out.println("getFile");
        URI expResult = uri1;
        CacheFile instance = new CacheFile(expResult);
        File result = instance.getFile();
        assertEquals(expResult, result.toURI());
    }

    /**
     * Test of hashCode method, of class CacheFile.
     */
    @Test
    public void testHashCode() {
        System.out.println("hashCode");
        CacheFile instance1 = new CacheFile(uri1);
        CacheFile instance2 = new CacheFile(uri2);
        assertNotEquals(instance1.hashCode(), instance2.hashCode());
    }

    /**
     * Test of equals method, of class CacheFile.
     */
    @Test
    public void testEquals() {
        System.out.println("equals");
        CacheFile instance1 = new CacheFile(uri1);
        CacheFile instance2 = new CacheFile(uri2);
        CacheFile instance3 = new CacheFile(uri2);
        assertEquals(instance2, instance3);
        assertNotEquals(instance1, instance2);
        assertNotEquals(instance1, null);
        assertNotEquals(instance1, new Object());
    }   
}
