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
        String fileName = ORDER_FOLDER + orderDate.format(DateTimeFormatter.ofPattern("MMddyyyy")) + ".txt";
        //create an arraylist
        List<Order> orderList = new ArrayList<>();
        File orderFile = new File(fileName);

        if(!orderFile.exists()){
            return orderList;
        }
        try{
            Scanner sc = new Scanner(new BufferedReader(new FileReader(fileName)));
            if (sc.hasNextLine()) {
                sc.nextLine();
            }
            while(sc.hasNextLine()){
                //Order order = new Order();
                String line = sc.nextLine();
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
        LocalDate orderDate = order.getOrderDate();
        String fileName = ORDER_FOLDER + orderDate.format(DateTimeFormatter.ofPattern("MMddyyyy")) + ".txt";

        File orderFile = new File(fileName);

        if (!orderFile.exists()) {
            // Ask for user confirmation before creating the file
            System.out.println("File " + orderFile.getName() + " does not exist. Do you want to create it? (yes/no)");
            Scanner scanner = new Scanner(System.in);
            String response = scanner.nextLine().trim().toLowerCase();

            if (!response.equals("yes")) {
                throw new FlooringDataPersistenceException("Order was not saved because the file was not created.");
            }
        }

        // Retrieve existing orders for the given date
        List<Order> ordersForDate;
        try {
            ordersForDate = getAllOrders(orderDate);
        } catch (FlooringDataPersistenceException e) {
            // If the file does not exist or cannot be read, assume it's empty
            ordersForDate = new ArrayList<>();
        }

        // Assign the next order number
        int nextOrderNumber = ordersForDate.stream().mapToInt(Order::getOrderNumber).max().orElse(0) + 1;
        order.setOrderNumber(nextOrderNumber);

        // Add the new order to the list
        ordersForDate.add(order);


        // Save orders to the file
        writeOrdersToFile(orderDate, ordersForDate);

        return order;
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

    //write order to file
    private void writeOrdersToFile(LocalDate date, List<Order> orderList) throws FlooringDataPersistenceException {
        String fileName = ORDER_FOLDER + date.format(DateTimeFormatter.ofPattern("MMddyyyy")) + ".txt";
        try (PrintWriter out = new PrintWriter(new FileWriter(fileName))) {
            out.println("OrderNumber,CustomerName,State,TaxRate,ProductType,Area,CostPerSquareFoot,"
                    + "LaborCostPerSquareFoot,MaterialCost,LaborCost,Tax,Total");

            for (Order o : orderList) {
                out.println(marshallOrder(o));
            }

        } catch (IOException e) {
            throw new FlooringDataPersistenceException("Could not write to file: " + fileName, e);
        }
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
