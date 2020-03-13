package net.guides.view.web.client;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

public class ClientServlet extends HttpServlet {
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) {
        try (Writer output = response.getWriter()) {
            output.write("<html><head><title>Servlet page</title></head><body><p>Congratulations for making it to the servlet page</p></body></html");
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }
}
