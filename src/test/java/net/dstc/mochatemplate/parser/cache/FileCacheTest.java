package net.dstc.mochatemplate.parser.cache;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Patrizio Bruno <desertconsulting@gmail.com>
 */
public class FileCacheTest {
    
    private URI uri1;
    
    public FileCacheTest() {
    }
    
    @Before
    public void setUp() throws URISyntaxException {
        try {
            this.uri1 = File.createTempFile("mochatpl", ".tmp").toURI();
        } catch (IOException ex) {
            Logger.getLogger(FileCacheTest.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of get and put methods, of class FileCache.
     */
    @Test
    public void testPutAndGet() {
        System.out.println("get");
        CacheFile key = new CacheFile(uri1);
        FileCache<String> instance = new FileCache<>();
        String expResult = "test";
        instance.put(key, expResult);
        String result = instance.get(key);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testGetChangedEntry() {
        System.out.println("get");
        CacheFile key = new CacheFile(uri1);
        FileCache<String> instance = new FileCache<>();
        instance.put(key, "test");
        URI uri = URI.create(uri1.toString());
        new File(uri).setLastModified(new Date().getTime() + 1100);
        key = new CacheFile(uri);
        String result = instance.get(key);
        assertNull(result);
    }

    /**
     * Test of containsKey method, of class FileCache.
     */
    @Test
    public void testContainsKey() {
        System.out.println("containsKey");
        CacheFile key = new CacheFile(uri1);
        CacheFile test = new CacheFile(uri1);
        test.getFile().setLastModified(new Date().getTime());
        FileCache instance = new FileCache();
        boolean expResult = false;
        boolean result = instance.containsKey(key);
        assertEquals(expResult, result);
    }
}
