package com.sg.flooringmastery.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Order {

    private String orderNumber;
    private LocalDate orderDate;
    private String customertName;
    private BigDecimal area;

    private StateTax stateTax;
    private Product product;
}
