package com.market.order;

public class OrderItemView {

    private String bookId;
    private String bookName;
    private int quantity;
    private int unitPrice;

    public OrderItemView(String bookId, String bookName,
                         int quantity, int unitPrice) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public String getBookId() {
        return bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getUnitPrice() {
        return unitPrice;
    }

    public int getSubTotal() {
        return unitPrice * quantity;
    }
}
