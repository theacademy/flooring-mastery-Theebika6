package com.sg.flooringmastery.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Order {

    private int orderNumber;
    private LocalDate orderDate;
    private String customertName;
    private BigDecimal area;

    private StateTax stateTax;
    private Product product;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
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

}
