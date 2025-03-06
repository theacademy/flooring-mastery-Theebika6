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
        String fileName = "Orders_" + fileDate + ".txt";
        //create an arraylist
        List<Order> orderList = new ArrayList<>();

        try{
            Scanner sc = new Scanner(new BufferedReader(new FileReader(fileName)));
            while(sc.hasNextLine()){
                Order order = new Order();
                String line = sc.nextLine();
                String[] split = line.split(DELIMITER);
                order.setOrderNumber(Integer.parseInt(split[0]));
                order.setOrderDate(orderDate);
                order.setCustomertName(split[1]);
                StateTax stateTax = new StateTax("", split[2], new BigDecimal(split[3]));
                order.setStateTax(stateTax);
                Product product = new Product(split[4], new BigDecimal(split[6]), new BigDecimal(split[7]));
                order.setProduct(product);
                order.setArea(new BigDecimal(split[5]));

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
}
