package com.sg.flooringmastery.service;

import com.sg.flooringmastery.dao.FlooringDataPersistenceException;
import com.sg.flooringmastery.dao.OrderNotFoundException;
import com.sg.flooringmastery.dto.Order;
import com.sg.flooringmastery.dto.Product;
import com.sg.flooringmastery.dto.StateTax;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface FlooringServiceLayer {

    Product getProduct(String productType)
            throws OrderDataValidationException;  // or remove if no validation is needed

    List<Product> getProducts();

    StateTax getStateTax(String stateAbbreviation)
            throws OrderDataValidationException;  // if the state doesn't exist

    Order getOrder(LocalDate orderDate, int orderNumber) throws FlooringDataPersistenceException;

    List<StateTax> getStateTaxes() throws FlooringDataPersistenceException;

    List<Order> getAllOrders(LocalDate orderDate) throws FlooringDataPersistenceException;

    // validates and adds a new order
    Order addOrder(Order order) throws FlooringDataPersistenceException, OrderDataValidationException;

    Order editOrder(Order order) throws FlooringDataPersistenceException, OrderDataValidationException;

    Order removeOrder(LocalDate orderDate, int orderNumber) throws FlooringDataPersistenceException, OrderNotFoundException;

    int getNextOrderNumber(LocalDate orderDate) throws FlooringDataPersistenceException, OrderNotFoundException;

    Set<LocalDate> getAllOrderDates()  throws FlooringDataPersistenceException;

}
