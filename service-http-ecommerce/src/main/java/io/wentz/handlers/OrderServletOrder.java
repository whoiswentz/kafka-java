package io.wentz.handlers;

import io.wentz.KafkaDispatcher;
import io.wentz.models.Email;
import io.wentz.models.Order;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class OrderServletOrder extends HttpServlet {
    final KafkaDispatcher<Order> orderDispatcher = new KafkaDispatcher<>();
    final KafkaDispatcher<Email> emailDispatcher = new KafkaDispatcher<>();

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    @Override
    public void destroy() {
        super.destroy();
        orderDispatcher.close();
        emailDispatcher.close();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final var userEmail = req.getParameter("email");
        final var amount = new BigDecimal(req.getParameter("amount"));

        final var orderId = UUID.randomUUID().toString();

        final var order = new Order(orderId, amount, userEmail);
        final var email = new Email("New Order", "Thank");

        try {
            orderDispatcher.send("NEW_ORDER_TOPIC", userEmail, order);
            emailDispatcher.send("EMAIL_ORDER_TOPIC", userEmail, email);
        } catch (ExecutionException | InterruptedException e) {
            throw new ServletException(e);
        }

        System.out.println(order.toString());
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().println("New order Sent");

    }
}
