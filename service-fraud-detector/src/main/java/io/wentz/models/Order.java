package io.wentz.models;

import java.math.BigDecimal;

public class Order {
    private final String userId;
    private final String orderId;
    private final BigDecimal amount;

    public Order(String userId, String orderId, BigDecimal ammount) {
        this.userId = userId;
        this.orderId = orderId;
        this.amount = ammount;
    }

    public boolean isFraud() {
        return amount.compareTo(new BigDecimal("4500")) >= 0;
    }

    public String getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "Order{" +
                "userId='" + userId + '\'' +
                ", orderId='" + orderId + '\'' +
                ", ammount=" + amount +
                '}';
    }
}
