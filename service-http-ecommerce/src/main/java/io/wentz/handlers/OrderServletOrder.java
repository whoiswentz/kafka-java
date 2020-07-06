package io.wentz.handlers;

import io.wentz.CorrelationId;
import io.wentz.dispatcher.KafkaDispatcher;
import io.wentz.models.Order;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class OrderServletOrder extends HttpServlet {
    private static final String NEW_ORDER_TOPIC = "ECOMMERCE_NEW_ORDER";
    final KafkaDispatcher<Order> orderDispatcher = new KafkaDispatcher<>();

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    @Override
    public void destroy() {
        super.destroy();
        orderDispatcher.close();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final var userEmail = req.getParameter("email");
        final var amount = new BigDecimal(req.getParameter("amount"));

        final var orderId = req.getParameter("uuid");
        final var order = new Order(orderId, amount, userEmail);

        try {
            try (var orderDatabase = new OrderDatabase("orders_database")) {
                if (orderDatabase.saveOrder(order)) {
                    var className = OrderServletOrder.class.getSimpleName();
                    orderDispatcher.send(NEW_ORDER_TOPIC, userEmail, new CorrelationId(className), order);
                    System.out.println(order.toString());
                    resp.setStatus(HttpServletResponse.SC_OK);
                    resp.getWriter().println("New order Sent");
                } else {
                    System.out.println(order.toString());
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getWriter().println("Old order");
                }
            }
        } catch (ExecutionException | InterruptedException | SQLException e) {
            throw new ServletException(e);
        }
    }
}
