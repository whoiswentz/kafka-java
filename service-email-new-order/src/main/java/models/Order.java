package models;

import java.math.BigDecimal;

public class Order {
    private final String email;
    private final String orderId;
    private final BigDecimal amount;

    public Order(String orderId, BigDecimal ammount, String email) {
        this.orderId = orderId;
        this.amount = ammount;
        this.email = email;
    }

    public boolean isFraud() {
        return amount.compareTo(new BigDecimal("4500")) >= 0;
    }

    @Override
    public String toString() {
        return "Order{" +
                ", orderId='" + orderId + '\'' +
                ", ammount=" + amount +
                ", email=" + email +
                '}';
    }

    public String getEmail() {
        return email;
    }
}
