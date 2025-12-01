package com.market.order;

import java.sql.Timestamp;

public class Order {
    private int orderId;
    private int memberId;
    private String username;
    private int totalPrice;
    private Timestamp orderDate;
    private String status;

    public Order(int orderId, int memberId, String username,
                 int totalPrice, Timestamp orderDate, String status) {
        this.orderId   = orderId;
        this.memberId  = memberId;
        this.username  = username;
        this.totalPrice = totalPrice;
        this.orderDate = orderDate;
        this.status    = status;
    }

    public int getOrderId() {
        return orderId;
    }

    public int getMemberId() {
        return memberId;
    }

    public String getUsername() {
        return username;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public Timestamp getOrderDate() {
        return orderDate;
    }

    public String getStatus() {
        return status;
    }
}
