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

    public String getUserId() {
        return userId;
    }

    public String getOrderId() {
        return orderId;
    }

    public BigDecimal getAmmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "io.wentz.models.Order{" +
                "userId='" + userId + '\'' +
                ", orderId='" + orderId + '\'' +
                ", ammount=" + amount +
                '}';
    }
}
