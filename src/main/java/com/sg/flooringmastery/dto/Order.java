package com.sg.flooringmastery.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Order {

    private String orderNumber;
    private LocalDate orderDate;
    private String customertName;
    private BigDecimal area;

    private StateTax stateTax;
    private Order order;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public String getCustomertName() {
        return customertName;
    }

    public void setCustomertName(String customertName) {
        this.customertName = customertName;
    }

    public BigDecimal getArea() {
        return area;
    }

    public void setArea(BigDecimal area) {
        this.area = area;
    }

    public StateTax getStateTax() {
        return stateTax;
    }

    public void setStateTax(StateTax stateTax) {
        this.stateTax = stateTax;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    private Product product;
}
