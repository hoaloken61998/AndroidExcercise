package com.example.models;

public class OrderDetails {
    private int id;
    private int orderId;
    private int productId;
    private double price;
    private double quantity;
    private double vat;
    private double discount;
    private double money;

    // Constructor
    public OrderDetails(int id, int orderId, int productId, double price,
                       double quantity, double vat, double discount, double money) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.price = price;
        this.quantity = quantity;
        this.vat = vat;
        this.discount = discount;
        this.money = money;
    }

    // Default constructor
    public OrderDetails() {
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public double getQuantity() { return quantity; }
    public void setQuantity(double quantity) { this.quantity = quantity; }

    public double getVat() { return vat; }
    public void setVat(double vat) { this.vat = vat; }

    public double getDiscount() { return discount; }
    public void setDiscount(double discount) { this.discount = discount; }

    public double getMoney() { return money; }
    public void setMoney(double money) { this.money = money; }

    public double getTotal() {
        double TotalValue = (price * quantity - discount/100*quantity*price)*(1+vat/100);
        return TotalValue;
    }
}