package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dto.Order;

import java.time.LocalDate;
import java.util.List;

public interface OrderDao {

    //flooringdataexception is used to see if theres any problems to get the order
    //order not found exception is to see for orderdate and orderNumber

    List<Order> getAllOrders(LocalDate date) throws FlooringDataPersistenceException,OrderNotFoundException;

    Order addOrder(Order order) throws FlooringDataPersistenceException,OrderNotFoundException;

    Order getOrder(LocalDate orderDate, int orderNumber) throws FlooringDataPersistenceException,OrderNotFoundException;

    // edit order the file must exist
    Order editOrder(Order order) throws FlooringDataPersistenceException,OrderNotFoundException;

    // remove order
    Order removeOrder(LocalDate orderDate, int orderNumber) throws FlooringDataPersistenceException,OrderNotFoundException;

}
