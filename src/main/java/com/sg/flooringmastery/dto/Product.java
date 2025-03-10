package com.sg.flooringmastery.dto;

import java.math.BigDecimal;

public class Product {
    private String productType;
    private BigDecimal costPerSqft;
    private BigDecimal laborCostPerSqft;

    // constructor
    public Product(String productType, BigDecimal costPerSqft, BigDecimal laborCostPerSqft){
        this.productType = productType;
        this.costPerSqft = costPerSqft;
        this.laborCostPerSqft = laborCostPerSqft;
    }

    // getter for product type
    public String getProductType(){
        return productType;
    }

    //getter for cost per square foot
    public BigDecimal getCostPerSqft() {
        return costPerSqft;
    }

    // getter for labor cost per square foot
    public BigDecimal getLaborCostPerSqft(){
        return laborCostPerSqft;
    }

    //to String method wasnt implemented
    //equal method


}
