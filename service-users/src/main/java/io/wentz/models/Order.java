package io.wentz.models;

import java.math.BigDecimal;

public class Order {
    private final String orderId;
    private final BigDecimal amount;
    private final String email;

    public Order(String orderId, BigDecimal ammount, String email) {
        this.orderId = orderId;
        this.amount = ammount;
        this.email = email;
    }

    public String getUserEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId='" + orderId + '\'' +
                ", ammount=" + amount +
                ", email=" + email +
                '}';
    }
}
