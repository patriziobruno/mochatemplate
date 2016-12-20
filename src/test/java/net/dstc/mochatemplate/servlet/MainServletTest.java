package net.dstc.mochatemplate.servlet;

import net.dstc.mochatemplate.parser.MochaTemplateEngineTest;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mockit.Expectations;
import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Patrizio Bruno <desertconsulting@gmail.com>
 */
public class MainServletTest {

    public MainServletTest() {
    }

    @Mocked
    private HttpServletRequest request;

    @Mocked
    private ServletConfig config;

    @Mocked
    private ServletContext context;

    class HttpServletResponseResultHolder extends MockUp<HttpServletResponse> {

        public int result;
        public ByteArrayOutputStream output = new ByteArrayOutputStream();

        @Mock
        public void sendError(int error) {
            result = error;
        }
        
        @Mock
        public void setStatus(int sc) {
            result = sc;
        }

        @Mock
        public PrintWriter getWriter() throws IOException {
            return new PrintWriter(output);
        }
    }

    /**
     * Test of processRequest method, of class MainServlet.
     * @throws java.lang.Exception
     */
    @Test
    public void testProcessRequest404() throws Exception {
        System.out.println("processRequest");
        final MainServlet instance = new MainServlet();
        instance.init(config);
        new Expectations() {
            {
                config.getServletContext();
                returns(context);

                request.getPathInfo();
                returns("/");

                context.getResourceAsStream("/index.html");
                returns(null);
            }
        };
        HttpServletResponseResultHolder mockedRes = new HttpServletResponseResultHolder();
        HttpServletResponse res = mockedRes.getMockInstance();
        int expResult = 404;
        instance.processRequest(request, res);
        assertEquals(expResult, mockedRes.result);
    }

    /**
     * Test of processRequest method, of class MainServlet.
     * @throws java.lang.Exception
     */
    @Test
    public void testProcessRequest() throws Exception {
        System.out.println("processRequest");
        final MainServlet instance = new MainServlet();
        final ByteArrayInputStream stream = new ByteArrayInputStream(MochaTemplateEngineTest.TEST_HTML.getBytes());
        instance.init(config);
        new Expectations() {
            {
                config.getServletContext();
                returns(context);

                request.getPathInfo();
                returns("/");

                context.getResourceAsStream("/index.html");
                returns(stream);
            }
        };
        HttpServletResponseResultHolder mockedRes = new HttpServletResponseResultHolder();
        HttpServletResponse res = mockedRes.getMockInstance();
        int expResult = 0;
        instance.processRequest(request, res);
        assertEquals(expResult, mockedRes.result);
    }

    /**
     * Test of processRequest method, of class MainServlet.
     * @throws java.lang.Exception
     */
    @Test
    public void testProcessRequest500() throws Exception {
        System.out.println("processRequest");
        final MainServlet instance = new MainServlet();
        final ByteArrayInputStream stream = new ByteArrayInputStream(MochaTemplateEngineTest.BAD_HTML.getBytes());
        instance.init(config);
        new Expectations() {
            {
                config.getServletContext();
                returns(context);

                request.getPathInfo();
                returns("/");

                context.getResourceAsStream("/index.html");
                returns(stream);
            }
        };
        HttpServletResponseResultHolder mockedRes = new HttpServletResponseResultHolder();
        HttpServletResponse res = mockedRes.getMockInstance();
        int expResult = 500;
        instance.processRequest(request, res);
        assertEquals(expResult, mockedRes.result);
    }

    /**
     * Test of doGet method, of class MainServlet.
     * @throws java.lang.Exception
     */
    @Test
    public void testdoGet404() throws Exception {
        System.out.println("doGet");
        final MainServlet instance = new MainServlet();
        instance.init(config);
        new Expectations() {
            {
                config.getServletContext();
                returns(context);

                request.getPathInfo();
                returns("/");

                context.getResourceAsStream("/index.html");
                returns(null);
            }
        };
        HttpServletResponseResultHolder mockedRes = new HttpServletResponseResultHolder();
        HttpServletResponse res = mockedRes.getMockInstance();
        int expResult = 404;
        instance.doGet(request, res);
        assertEquals(expResult, mockedRes.result);
    }

    /**
     * Test of doGet method, of class MainServlet.
     * @throws java.lang.Exception
     */
    @Test
    public void testdoGet() throws Exception {
        System.out.println("doGet");
        final MainServlet instance = new MainServlet();
        final ByteArrayInputStream stream = new ByteArrayInputStream(MochaTemplateEngineTest.TEST_HTML.getBytes());
        instance.init(config);
        new Expectations() {
            {
                config.getServletContext();
                returns(context);

                request.getPathInfo();
                returns("/");

                context.getResourceAsStream("/index.html");
                returns(stream);
            }
        };
        HttpServletResponseResultHolder mockedRes = new HttpServletResponseResultHolder();
        HttpServletResponse res = mockedRes.getMockInstance();
        int expResult = 0;
        instance.doGet(request, res);
        assertEquals(expResult, mockedRes.result);
    }

    /**
     * Test of doGet method, of class MainServlet.
     * @throws java.lang.Exception
     */
    @Test
    public void testdoGet500() throws Exception {
        System.out.println("doGet");
        final MainServlet instance = new MainServlet();
        final ByteArrayInputStream stream = new ByteArrayInputStream(MochaTemplateEngineTest.BAD_HTML.getBytes());
        instance.init(config);
        new Expectations() {
            {
                config.getServletContext();
                returns(context);

                request.getPathInfo();
                returns("/");

                context.getResourceAsStream("/index.html");
                returns(stream);
            }
        };
        HttpServletResponseResultHolder mockedRes = new HttpServletResponseResultHolder();
        HttpServletResponse res = mockedRes.getMockInstance();
        int expResult = 500;
        instance.doGet(request, res);
        assertEquals(expResult, mockedRes.result);
    }

    /**
     * Test of doPost method, of class MainServlet.
     * @throws java.lang.Exception
     */
    @Test
    public void testdoPost404() throws Exception {
        System.out.println("doPost");
        final MainServlet instance = new MainServlet();
        instance.init(config);
        new Expectations() {
            {
                config.getServletContext();
                returns(context);

                request.getPathInfo();
                returns("/");

                context.getResourceAsStream("/index.html");
                returns(null);
            }
        };
        HttpServletResponseResultHolder mockedRes = new HttpServletResponseResultHolder();
        HttpServletResponse res = mockedRes.getMockInstance();
        int expResult = 404;
        instance.doPost(request, res);
        assertEquals(expResult, mockedRes.result);
    }

    /**
     * Test of doPost method, of class MainServlet.
     * @throws java.lang.Exception
     */
    @Test
    public void testdoPost() throws Exception {
        System.out.println("doPost");
        final MainServlet instance = new MainServlet();
        final ByteArrayInputStream stream = new ByteArrayInputStream(MochaTemplateEngineTest.TEST_HTML.getBytes());
        instance.init(config);
        new Expectations() {
            {
                config.getServletContext();
                returns(context);

                request.getPathInfo();
                returns("/");

                context.getResourceAsStream("/index.html");
                returns(stream);
            }
        };
        HttpServletResponseResultHolder mockedRes = new HttpServletResponseResultHolder();
        HttpServletResponse res = mockedRes.getMockInstance();
        int expResult = 0;
        instance.doPost(request, res);
        assertEquals(expResult, mockedRes.result);
    }

    /**
     * Test of doPost method, of class MainServlet.
     * @throws java.lang.Exception
     */
    @Test
    public void testdoPost500() throws Exception {
        System.out.println("doPost");
        final MainServlet instance = new MainServlet();
        final ByteArrayInputStream stream = new ByteArrayInputStream(MochaTemplateEngineTest.BAD_HTML.getBytes());
        instance.init(config);
        new Expectations() {
            {
                config.getServletContext();
                returns(context);

                request.getPathInfo();
                returns("/");

                context.getResourceAsStream("/index.html");
                returns(stream);
            }
        };
        HttpServletResponseResultHolder mockedRes = new HttpServletResponseResultHolder();
        HttpServletResponse res = mockedRes.getMockInstance();
        int expResult = 500;
        instance.doPost(request, res);
        assertEquals(expResult, mockedRes.result);
    }

    /**
     * Test of getServletInfo method, of class MainServlet.
     */
    @Test
    public void testGetServletInfo() {
        System.out.println("getServletInfo");
        MainServlet instance = new MainServlet();
        String expResult = "Short description";
        String result = instance.getServletInfo();
        assertEquals(expResult, result);
    }

}
