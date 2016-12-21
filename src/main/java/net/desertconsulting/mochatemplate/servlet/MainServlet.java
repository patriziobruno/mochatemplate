package net.desertconsulting.mochatemplate.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import java.io.BufferedReader;
import net.desertconsulting.mochatemplate.parser.MochaTemplateEngine;
import net.desertconsulting.mochatemplate.parser.TemplateEngine;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.script.ScriptException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.desertconsulting.mochatemplate.parser.ApiOutputFormat;
import net.desertconsulting.mochatemplate.parser.ApiOutputFormatException;
import org.jsoup.helper.StringUtil;

/**
 * @author Patrizio Bruno {@literal <desertconsulting@gmail.com>}
 */
public class MainServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        try (PrintWriter out = response.getWriter()) {
            String pathInfo = request.getPathInfo();

            if (StringUtil.isBlank(pathInfo) || "/".equals(pathInfo)) {
                pathInfo = "/index.html";
            }
            ServletContext servletContext = getServletContext();
            try (InputStream templateStream = servletContext.
                    getResourceAsStream(pathInfo)) {
                if (templateStream != null) {
                    TemplateEngine template = new MochaTemplateEngine(servletContext,
                            templateStream);

                    template.put("request", request);

                    if (pathInfo.endsWith(".api")) {
                        processREST(template, request, response, out);
                    } else {
                        response.setContentType("text/html;charset=UTF-8");
                        template.parse(out, null);
                    }
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                }
            } catch (Exception ex) {
                response.setContentType("text/html;charset=UTF-8");
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                out.println("<html><h1>500 Error</h1><pre>");
                ex.printStackTrace(out);
                out.println("</pre></html>");
            }
        }
    }

    private void processREST(TemplateEngine engine, HttpServletRequest request,
            HttpServletResponse response, PrintWriter out) throws IOException {

        ApiOutputFormat outputFormat = ApiOutputFormat.JSON;

        try {
            response.setContentType(request.getHeader("accept"));
            outputFormat = ApiOutputFormat.parse(request.getHeader("accept"));

            try (BufferedReader input = new BufferedReader(new InputStreamReader(request.getInputStream()))) {
                StringBuilder sb = new StringBuilder(request.getContentLength());
                String line;
                while ((line = input.readLine()) != null) {
                    sb.append(line);
                }
                engine.putJson("mochaRequestObject", sb.toString());
            }

            switch(request.getMethod()) {
                case "POST":
                    response.setStatus(HttpServletResponse.SC_CREATED);
            }
            engine.exec(out, outputFormat);
        } catch (ApiOutputFormatException ex) {
            response.setContentType("text/html;charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            out.println("<html><h1>400 Bad request</h1><pre>");
            ex.printStackTrace(out);
            out.println("</pre></html>");
        } catch (IOException | ScriptException ex) {
            response.setContentType(request.getHeader("accept"));
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            ObjectMapper mapper;
            switch (outputFormat) {
                case XML:
                    mapper = new XmlMapper();
                    break;
                case JSON:
                default:
                    mapper = new ObjectMapper();
                    break;
            }
            mapper.writeValue(out, ex);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPut(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
