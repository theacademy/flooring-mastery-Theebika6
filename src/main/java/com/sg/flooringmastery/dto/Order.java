package com.sg.flooringmastery.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

public class Order {

    private int orderNumber;
    private LocalDate orderDate;
    private String customerName;
    private BigDecimal area;
    private StateTax stateTax;
    private Product product;

    public Order(){
        orderNumber = -1;
    }

    // constructor
    public Order(int orderNumber, LocalDate orderDate, String customerName, BigDecimal area, StateTax stateTax, Product product){
        this.orderNumber = orderNumber;
        this.orderDate = orderDate;
        this.customerName = customerName;
        this.area = area;
        this.stateTax = stateTax;
        this.product = product;
    }

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

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
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

    public BigDecimal getMaterialCost() {
        if (product == null || area == null) {
            return BigDecimal.ZERO;
        }
        return area.multiply(product.getCostPerSqft()).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getLaborCost() {
        if (product == null || area == null) {
            return BigDecimal.ZERO;
        }
        return area.multiply(product.getLaborCostPerSqft()).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getTax() {
        if (stateTax == null) {
            return BigDecimal.ZERO;
        }
        // (MaterialCost + LaborCost) * (TaxRate / 100)
        BigDecimal subTotal = getMaterialCost().add(getLaborCost());
        BigDecimal taxRateDecimal = stateTax.getTaxRate().divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        return subTotal.multiply(taxRateDecimal).setScale(2, RoundingMode.HALF_UP);
    }
    public BigDecimal getTotal() {
        return getMaterialCost().add(getLaborCost()).add(getTax()).setScale(2, RoundingMode.HALF_UP);
    }

    // to String to format the data of how I want
    @Override
    public String toString() {
        return "Order:" +
                "orderNumber = " + orderNumber +
                ", orderDate = " + orderDate +
                ", customerName = " + customerName + '\'' +
                ", area = " + area +
                ", stateTax = " + (stateTax != null ? stateTax.getStateAbbreviation() : "null") +
                ", product=" + (product != null ? product.getProductType() : "null") +
                ", materialCost = " + getMaterialCost() +
                ", laborCost = " + getLaborCost() +
                ", tax = " + getTax() +
                ", total = " + getTotal() ;
    }

    // equals code removed since it wasn't implemented

    //clone order
    public Order cloneOrder(){
      Order cloneOrder = new Order();
      cloneOrder.setOrderNumber(this.orderNumber);
      cloneOrder.setOrderDate(this.orderDate);
      cloneOrder.setCustomerName(this.customerName);
      cloneOrder.setArea(this.area);
      cloneOrder.setStateTax(this.stateTax);
      cloneOrder.setProduct(this.product);
      return cloneOrder;
    }

}
