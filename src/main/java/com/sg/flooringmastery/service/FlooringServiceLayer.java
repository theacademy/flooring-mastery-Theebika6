package com.sg.flooringmastery.service;

import com.sg.flooringmastery.dao.FlooringDataPersistenceException;
import com.sg.flooringmastery.dto.Order;
import com.sg.flooringmastery.dto.Product;
import com.sg.flooringmastery.dto.StateTax;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

public interface FlooringServiceLayer {

    Product getProduct(String productType)
            throws OrderDataValidationException;  // or remove if no validation is needed

    List<Product> getProducts();

    StateTax getStateTax(String stateAbbreviation)
            throws OrderDataValidationException;  // e.g. if the state doesn't exist

    List<StateTax> getStateTaxes();

    List<Order> getAllOrders(LocalDate orderDate) throws FlooringDataPersistenceException;

    // validates and adds a new order
    Order addOrder(LocalDate orderDate, int orderNumber);

    Order editOrder(Order order) throws OrderDataValidationException;

    Order removeOrder(LocalDate orderDate, int orderNumber);

    int getTotalOrderCount();
}
