package com.sg.flooringmastery.service;

import com.sg.flooringmastery.dao.FlooringDataPersistenceException;
import com.sg.flooringmastery.dao.OrderDao;
import com.sg.flooringmastery.dto.Order;
import com.sg.flooringmastery.dto.Product;
import com.sg.flooringmastery.dto.StateTax;

import java.time.LocalDate;
import java.util.List;

public class FlooringServiceImpl implements FlooringServiceLayer{

    private final OrderDao orderDao;

    public FlooringServiceImpl(OrderDao orderDao){
        this.orderDao = orderDao;
    }

    @Override
    public Product getProduct(String productType) throws OrderDataValidationException {
        return null;
    }

    @Override
    public List<Product> getProducts() {
        return List.of();
    }

    @Override
    public StateTax getStateTax(String stateAbbreviation) throws OrderDataValidationException {
        return null;
    }

    @Override
    public List<StateTax> getStateTaxes() {
        return List.of();
    }

    @Override
    public List<Order> getAllOrders(LocalDate orderDate) throws FlooringDataPersistenceException {
        return orderDao.getAllOrders(orderDate);
    }


    @Override
    public Order addOrder(LocalDate orderDate, int orderNumber) {
        return null;
    }

    @Override
    public Order editOrder(Order order) throws OrderDataValidationException {
        return null;
    }

    @Override
    public Order removeOrder(LocalDate orderDate, int orderNumber) {
        return null;
    }

    @Override
    public int getTotalOrderCount() {
        return 0;
    }
}
