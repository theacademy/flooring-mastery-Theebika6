package com.sg.flooringmastery.dto;

import java.math.BigDecimal;

public class StateTax {

    private String stateName;
    private String stateAbbreviation;
    private BigDecimal taxRate;

    public StateTax(String stateName, String stateAbbreviation, BigDecimal taxRate){
        this.stateName = stateName;
        this.stateAbbreviation = stateAbbreviation;
        this.taxRate = taxRate;
    }
    public String getStateName(){
    return stateName;
    }

    public String getStateAbbreviation(){
        return stateAbbreviation;
    }

    public void setStateAbbreviation(String stateAbbreviation) {
        this.stateAbbreviation = stateAbbreviation;
    }


    public BigDecimal getTaxRate(){
        return taxRate;
    }

   //to string removed, didnt implement




}
