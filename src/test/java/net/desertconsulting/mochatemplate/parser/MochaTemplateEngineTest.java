package net.desertconsulting.mochatemplate.parser;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.servlet.ServletContext;
import mockit.Mocked;
import org.jsoup.nodes.Document;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Patrizio Bruno <desertconsulting@gmail.com>
 */
public class MochaTemplateEngineTest {

    public final static String TEST_HTML
            = "<html><head><script type=\"server/javascript\">var a = 'test';var d=['test', 1, 2, 3];</script></head><body><script type=\"server/javascript\">function test(){return a;} test();</script><div data-if=\"test() === 'test'\" id=\"foo\">${a}</div><div data-for-x=\"d\" data-if=\"(x % 2) === 0\" width=\"${x}0\">${x}</div></body></html";
    public final static String BAD_HTML
            = "<html><head><script type=\"server/javascript\">var a = 'test'var d=['test', 1, 2, 3];</script></head><body><script type=\"server/javascript\">function test(){return a;} test();</script><div data-if=\"test() === 'test'\" id=\"foo\">${a}</div><div data-for-x=\"d\" data-if=\"(x % 2) === 0\" width=\"${x}0\">${x}</div></body></html";
    private final String expectedHtml
            = "<html><head></head><body><div id=\"foo\">test</div><div width=\"20\">2</div></body></html>";

    @Mocked
    private ServletContext servletContext;
    private final Document.OutputSettings outputSettings
            = new Document.OutputSettings() {
        {
            this.syntax(Document.OutputSettings.Syntax.xml);
            this.prettyPrint(false);
        }
    };

    public MochaTemplateEngineTest() {
    }

    @Test
    public void testMochaTemplateEngineFile() throws Exception {
        System.out.println("MochaTemplateEngine(ServletContext, File)");
        try (InputStream stream = new ByteArrayInputStream(TEST_HTML.getBytes())) {
            File file = stream2File(stream);
            MochaTemplateEngine instance = new MochaTemplateEngine(
                    servletContext, file);
            String result = instance.parse(outputSettings);
            assertEquals(expectedHtml, result);
        }
    }

    @Test
    public void testlMochaTemplateEngineStream() throws Exception {
        System.out.println("MochaTemplateEngine(ServletContext, InputStream)");
        try (InputStream stream = new ByteArrayInputStream(TEST_HTML.getBytes())) {
            MochaTemplateEngine instance = new MochaTemplateEngine(
                    servletContext, stream);
            String result = instance.parse(outputSettings);
            assertEquals(expectedHtml, result);
        }
    }

    /**
     * Test of loadTemplate method, of class MochaTemplateEngine.
     * @throws java.lang.Exception
     */
    @Test
    public void testLoadTemplate() throws Exception {
        System.out.println("loadTemplate");
        String result;
        try (InputStream stream = new ByteArrayInputStream(TEST_HTML.getBytes())) {
            MochaTemplateEngine instance = new MochaTemplateEngine(
                    servletContext);
            instance.loadTemplate(stream);
            result = instance.parse(outputSettings);
        }
        assertEquals(expectedHtml, result);
    }

    /**
     * Test of loadTemplate method, of class MochaTemplateEngine.
     * @throws java.lang.Exception
     */
    @Test(expected = NullPointerException.class)
    public void testLoadTemplateNullStream() throws Exception {
        System.out.println("loadTemplate");
        InputStream stream = null;
        MochaTemplateEngine instance = new MochaTemplateEngine(
                servletContext);
        instance.loadTemplate(stream);
    }

    /**
     * Test of parse method, of class MochaTemplateEngine.
     * @throws java.lang.Exception
     */
    @Test
    public void testParse() throws Exception {
        System.out.println("parse");
        MochaTemplateEngine instance = new MochaTemplateEngine(
                servletContext, TEST_HTML);
        String expResult = expectedHtml;
        String result = instance.parse(outputSettings);
        assertEquals(expResult, result);
    }

    /**
     * Test of put method, of class MochaTemplateEngine.
     * @throws java.lang.Exception
     */
    @Test
    public void testPut() throws Exception {
        System.out.println("put");
        String key = "js_var";
        String value = "whatever";
        String test
                = "<html><head></head><body><div>${js_var}</div></body></html>";
        MochaTemplateEngine instance = new MochaTemplateEngine(
                servletContext, test);
        instance.put(key, value);
        String result = instance.parse(outputSettings);

        assertEquals(test.replace("${js_var}", value), result);
    }

    private File stream2File(InputStream in) throws IOException {
        final File tempFile = File.createTempFile("mochatpl", "tmp");
        tempFile.deleteOnExit();
        try (FileOutputStream out = new FileOutputStream(tempFile)) {
            int n;
            while ((n = in.available()) > 0) {
                byte[] buffer = new byte[n];
                in.read(buffer);
                out.write(buffer);
            }
        }
        return tempFile;
    }

}
