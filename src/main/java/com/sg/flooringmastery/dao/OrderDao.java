package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dto.Order;

import java.time.LocalDate;
import java.util.List;

public interface OrderDao {
    List<Order> getAllOrders() throws FlooringDataPersistenceException;

    List<Order> getAllOrders( LocalDate orderDate) throws FlooringDataPersistenceException;

    Order addOrder(Order order) throws FlooringDataPersistenceException;

    Order getOrder(LocalDate orderDate, int orderNumber) throws FlooringDataPersistenceException;

    Order editOrder(Order order) throws FlooringDataPersistenceException;

    Order removeOrder(LocalDate orderDate, int orderNumber) throws FlooringDataPersistenceException;

}
