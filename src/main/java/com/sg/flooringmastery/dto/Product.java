package com.sg.flooringmastery.dto;

import java.math.BigDecimal;

public class Product {
    private String productType;
    private BigDecimal costPerSqft;
    private BigDecimal laborCostPerSqft;

    public Product(String productType, BigDecimal costPerSqft, BigDecimal laborCostPerSqft){
        this.productType = productType;
        this.costPerSqft = costPerSqft;
        this.laborCostPerSqft = laborCostPerSqft;
    }
    public String getProductType(){
        return productType;
    }

    public BigDecimal getCostPerSqft() {
        return costPerSqft;
    }

    public BigDecimal getLaborCostPerSqft(){
        return laborCostPerSqft;
    }

    //to String
    @Override
    public String toString(){
        return "Product: " +
                " productType =" + productType +
                " costPerSqft = " + costPerSqft +
                " laborCostPerSqft = " + laborCostPerSqft ;
    }


}
