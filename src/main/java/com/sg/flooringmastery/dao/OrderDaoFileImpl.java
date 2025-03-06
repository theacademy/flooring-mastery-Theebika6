package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dto.Order;
import com.sg.flooringmastery.dto.Product;
import com.sg.flooringmastery.dto.StateTax;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class OrderDaoFileImpl implements OrderDao {

    public static final String DELIMITER = ",";

    @Override
    public List<Order> getAllOrders(LocalDate orderDate) throws FlooringDataPersistenceException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMddyyyy");
        String fileDate = orderDate.format(formatter);
        String fileName = "Orders/Orders_" + fileDate + ".txt";
        //create an arraylist
        List<Order> orderList = new ArrayList<>();

        try{
            Scanner sc = new Scanner(new BufferedReader(new FileReader(fileName)));
            if (sc.hasNextLine()) {
                sc.nextLine();
            }
            while(sc.hasNextLine()){
                //Order order = new Order();
                String line = sc.nextLine();
                String[] split = line.split(DELIMITER);
                Order order= unmarshalOrder(line, orderDate);
                orderList.add(order);
            }
        }
        catch(IOException e){
            throw new FlooringDataPersistenceException("Error, cannot read file: " + fileName);
        }
        return orderList;
    }


    @Override
    public Order addOrder(Order order) throws FlooringDataPersistenceException {
        return null;
    }

    @Override
    public Order getOrder(LocalDate orderDate, int orderNumber) throws FlooringDataPersistenceException {
        return null;
    }

    @Override
    public Order editOrder(Order order) throws FlooringDataPersistenceException {
        return null;
    }

    @Override
    public Order removeOrder(LocalDate orderDate, int orderNumber) throws FlooringDataPersistenceException {
        return null;
    }

    //unmarshall
    private Order unmarshalOrder(String line, LocalDate orderDate) {
        String[] split = line.split(DELIMITER);
        Order order = new Order();
        order.setOrderNumber(Integer.parseInt(split[0]));
        order.setOrderDate(orderDate);
        order.setCustomerName(split[1]);
        // Create the StateTax object.
        StateTax stateTax = new StateTax("", split[2], new BigDecimal(split[3]));
        order.setStateTax(stateTax);
        // Create the Product object.
        Product product = new Product(split[4], new BigDecimal(split[6]), new BigDecimal(split[7]));
        order.setProduct(product);
        // Set the area.
        order.setArea(new BigDecimal(split[5]));
        return order;
    }

    //marshal
    public String marshallOrder(Order order) {
        String orderAsText = order.getOrderNumber() + DELIMITER;
        orderAsText += order.getCustomerName() + DELIMITER;
        orderAsText += order.getStateTax().getStateAbbreviation() + DELIMITER;
        orderAsText += order.getStateTax().getTaxRate() + DELIMITER;
        orderAsText += order.getProduct().getProductType() + DELIMITER;
        orderAsText += order.getArea() + DELIMITER;
        orderAsText += order.getProduct().getCostPerSqft() + DELIMITER;
        orderAsText += order.getProduct().getLaborCostPerSqft() + DELIMITER;
        orderAsText += order.getMaterialCost() + DELIMITER;
        orderAsText += order.getLaborCost() + DELIMITER;
        orderAsText += order.getTax() + DELIMITER;
        orderAsText += order.getTotal();
        return orderAsText;
    }

}
