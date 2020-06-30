package io.wentz.handlers;

import io.wentz.CorrelationId;
import io.wentz.KafkaDispatcher;
import io.wentz.models.User;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class GenerateReadingReport extends HttpServlet {
    final KafkaDispatcher<String> batchDispatcher = new KafkaDispatcher<>();

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    @Override
    public void destroy() {
        super.destroy();
        batchDispatcher.close();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        System.out.println("doing the get");
        try {
            String className = GenerateReadingReport.class.getSimpleName();

            batchDispatcher.send(
                    "ECOMMERCE_SEND_MESSAGE_TO_ALL_USERS",
                    "ECOMMERCE_USER_GENERATE_READING_REPORT",
                    new CorrelationId(className),
                    "ECOMMERCE_USER_GENERATE_READING_REPORT");

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().println("New order Sent");

            System.out.println("get done");
        } catch (IOException | InterruptedException | ExecutionException e) {
            throw new ServletException(e);
        }
    }
}
