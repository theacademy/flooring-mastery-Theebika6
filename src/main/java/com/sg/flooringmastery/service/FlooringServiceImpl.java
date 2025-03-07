package com.sg.flooringmastery.service;

import com.sg.flooringmastery.dao.*;
import com.sg.flooringmastery.dto.Order;
import com.sg.flooringmastery.dto.Product;
import com.sg.flooringmastery.dto.StateTax;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.regex.Pattern;

public class FlooringServiceImpl implements FlooringServiceLayer{

    private final OrderDao orderDao;
    private final ProductDao productDao;
    private final StateTaxDao stateTaxDao;

    public FlooringServiceImpl(OrderDao orderDao, ProductDao productDao, StateTaxDao stateTaxDao){
        this.orderDao = orderDao;
        this.productDao = productDao;
        this.stateTaxDao = stateTaxDao;
    }

    @Override
    public Order getOrder(LocalDate orderDate, int orderNumber) throws FlooringDataPersistenceException {
        return orderDao.getOrder(orderDate, orderNumber);
    }

    @Override
    public Product getProduct(String productType) throws OrderDataValidationException {
        return null;
    }

    @Override
    public List<Product> getProducts() {
        return productDao.getAllProducts();
    }

    @Override
    public StateTax getStateTax(String stateAbbreviation) throws OrderDataValidationException {
        try {
            // Get all state taxes from the DAO
            List<StateTax> stateTaxes = stateTaxDao.getAllStateTaxes();

            // Find the matching state
            for (StateTax tax : stateTaxes) {
                if (tax.getStateAbbreviation().equalsIgnoreCase(stateAbbreviation)) {
                    return tax; // Return the matching StateTax
                }
            }

            // If no matching state is found, throw an exception
            throw new OrderDataValidationException("Error: State not found in the tax file.");

        } catch (FlooringDataPersistenceException e) {
            throw new OrderDataValidationException("Error: Unable to load state tax data.", e);
        }
    }

    @Override
    public List<StateTax> getStateTaxes() throws FlooringDataPersistenceException {
        return stateTaxDao.getAllStateTaxes();
    }

    @Override
    public List<Order> getAllOrders(LocalDate orderDate) throws FlooringDataPersistenceException {
        return orderDao.getAllOrders(orderDate);
    }


    @Override
    public Order addOrder(Order order) throws FlooringDataPersistenceException, OrderDataValidationException {
        //  Validate Order Date, CustomerName, State, ProductType,Area, OrderNumber
        validateOrderDate(order.getOrderDate());
        validateCustomerName(order.getCustomerName());
        validateState(order.getStateTax().getStateAbbreviation());
        validateProduct(order.getProduct().getProductType());
        validateArea(order.getArea());
        List<Order> ordersForDate = orderDao.getAllOrders(order.getOrderDate());
        int maxOrderNumber = ordersForDate.stream().mapToInt(Order::getOrderNumber).max().orElse(0);

        order.setOrderNumber(maxOrderNumber + 1);


        for (Order o : ordersForDate) {
            if (o.getOrderNumber() > maxOrderNumber) {
                maxOrderNumber = o.getOrderNumber();
            }
        }
        //  Assign the next available order number
        order.setOrderNumber(maxOrderNumber + 1);
        // Save Order
        return orderDao.addOrder(order);
    }
    private void validateOrderDate(LocalDate orderDate) throws OrderDataValidationException {
        if (orderDate.isBefore(LocalDate.now())) {
            throw new OrderDataValidationException("Error: Order date must be in the future.");
        }
    }
    private void validateCustomerName(String name) throws OrderDataValidationException {
        if (name == null || name.trim().isEmpty()) {
            throw new OrderDataValidationException("Error: Customer name cannot be empty.");
        }
        String regex = "^[a-zA-Z0-9., ]+$";
        if (!Pattern.matches(regex, name)) {
            throw new OrderDataValidationException("Error: Customer name contains invalid characters.");
        }
    }
    private void validateState(String state) throws OrderDataValidationException, FlooringDataPersistenceException {
        if (stateTaxDao.getStateTax(state) == null) {
            throw new OrderDataValidationException("Error: We do not sell in this state.");
        }
    }
    private void validateProduct(String productType) throws OrderDataValidationException {
        if (productDao.getProduct(productType) == null) {
            throw new OrderDataValidationException("Error: Invalid product type.");
        }
    }
    private void validateArea(BigDecimal area) throws OrderDataValidationException {
        if (area.compareTo(new BigDecimal("100")) < 0) {
            throw new OrderDataValidationException("Error: Minimum order size is 100 sq ft.");
        }
    }

    @Override
    public int getNextOrderNumber(LocalDate orderDate) throws FlooringDataPersistenceException, OrderNotFoundException {
        List<Order> ordersForDate = orderDao.getAllOrders(orderDate); // Get orders for the date
        return ordersForDate.stream().mapToInt(Order::getOrderNumber).max().orElse(0) + 1;
    }



    @Override
    public Order editOrder(Order order) throws FlooringDataPersistenceException, OrderDataValidationException {
        validateCustomerName(order.getCustomerName());
        validateState(order.getStateTax().getStateAbbreviation());
        validateProduct(order.getProduct().getProductType());
        validateArea(order.getArea());

        return orderDao.editOrder(order); // Pass `order` as an argument
    }

    @Override
    public Order removeOrder(LocalDate orderDate, int orderNumber) throws FlooringDataPersistenceException, OrderNotFoundException{
        return orderDao.removeOrder(orderDate, orderNumber);
    }

}