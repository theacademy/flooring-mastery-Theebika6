package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dto.Order;
import com.sg.flooringmastery.dto.Product;
import com.sg.flooringmastery.dto.StateTax;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class OrderDaoFileImpl implements OrderDao {

    private static final String ORDER_FOLDER = "Orders/Orders_";
    public static final String DELIMITER = ",";
    private Map<LocalDate, List<Order>> orders = new HashMap<>();


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
    public Order addOrder(Order order) throws FlooringDataPersistenceException, OrderNotFoundException {
        order.setOrderNumber(order.getOrderNumber());

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

    /*write order to file
    private void writeOrdersToFile(LocalDate date, List<Order> orderList) throws FlooringDataPersistenceException {
        String fileName = ORDER_FOLDER + "Orders_"
                + date.format(DateTimeFormatter.ofPattern("MMddyyyy")) + FILE_EXT;
        try (PrintWriter out = new PrintWriter(new FileWriter(fileName))) {
            out.println("OrderNumber,CustomerName,State,TaxRate,ProductType,Area,CostPerSquareFoot,"
                    + "LaborCostPerSquareFoot,MaterialCost,LaborCost,Tax,Total");

            for (Order o : orderList) {
                out.println(marshallOrder(o));
            }
        } catch (IOException e) {
            throw new FlooringDataPersistenceException("Could not write to file: " + fileName, e);
        }
    }*/

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
