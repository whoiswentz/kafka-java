package io.wentz;

import io.wentz.handlers.GenerateReadingReport;
import io.wentz.handlers.OrderServletOrder;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class HttpEcommerceService {
    public static void main(String[] args) throws Exception {
        var server = new Server(8080);

        var context = new ServletContextHandler();
        context.setContextPath("/");
        context.addServlet(new ServletHolder(new OrderServletOrder()), "/new");
        context.addServlet(new ServletHolder(new GenerateReadingReport()), "/admim/generate-reports");

        server.setHandler(context);

        server.start();
        System.out.println("Serving");

        server.join();
    }
}
