package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dto.Order;
import com.sg.flooringmastery.dto.Product;
import com.sg.flooringmastery.dto.StateTax;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class OrderDaoFileImpl implements OrderDao {

    private static final String ORDER_FOLDER = "Orders/Orders_";
    public static final String DELIMITER = ",";
    private Map<LocalDate, List<Order>> orders = new HashMap<>();


    @Override
    public List<Order> getAllOrders(LocalDate orderDate) throws FlooringDataPersistenceException {
        read(orderDate);
        return orders.getOrDefault(orderDate, new ArrayList<>());
    }


    @Override
    public Order addOrder(Order order) throws FlooringDataPersistenceException, OrderNotFoundException {
        LocalDate orderDate = order.getOrderDate();
        read(orderDate); // Load existing orders

        List<Order> ordersForDate = orders.getOrDefault(orderDate, new ArrayList<>());

        int nextOrderNumber = ordersForDate.stream().mapToInt(Order::getOrderNumber).max().orElse(0) + 1;
        order.setOrderNumber(nextOrderNumber);

        ordersForDate.add(order);
        orders.put(orderDate, ordersForDate); // Store updated list

        writeOrdersToFile(orderDate, orders.get(orderDate)); // Save changes
        return order;
    }

    @Override
    public Order getOrder(LocalDate orderDate, int orderNumber) throws FlooringDataPersistenceException {
        read(orderDate);
        List<Order> ordersForDate = orders.get(orderDate);

        if (ordersForDate != null) {
            for (Order order : ordersForDate) {
                if (order.getOrderNumber() == orderNumber) {
                    return order;
                }
            }
        }
        return null;
    }

    @Override
    public Order editOrder(Order order) throws FlooringDataPersistenceException, OrderNotFoundException {
        read(order.getOrderDate()); // Load orders for date

        List<Order> ordersForDate = orders.get(order.getOrderDate());
        if (ordersForDate == null || ordersForDate.isEmpty()) {
            throw new FlooringDataPersistenceException("Error: No orders exist for this date.");
        }

        boolean found = false;
        for (int i = 0; i < ordersForDate.size(); i++) {
            if (ordersForDate.get(i).getOrderNumber() == order.getOrderNumber()) {
                ordersForDate.set(i, order);
                found = true;
                break;
            }
        }

        if (!found) {
            throw new FlooringDataPersistenceException("Error: Order number " + order.getOrderNumber() + " not found.");
        }

        writeOrdersToFile(order.getOrderDate(), ordersForDate);
        return order;
    }


    @Override
    public Order removeOrder(LocalDate orderDate, int orderNumber) throws FlooringDataPersistenceException, OrderNotFoundException {
        read(orderDate); // Load orders from file

        List<Order> ordersForDate = orders.get(orderDate);
        if (ordersForDate == null || ordersForDate.isEmpty()) {
            throw new OrderNotFoundException("Error: No orders exist for this date.");
        }

        Order removedOrder = null;
        Iterator<Order> iterator = ordersForDate.iterator();
        while (iterator.hasNext()) {
            Order order = iterator.next();
            if (order.getOrderNumber() == orderNumber) {
                removedOrder = order;
                iterator.remove(); // Remove order from list
                break;
            }
        }

        if (removedOrder == null) {
            throw new OrderNotFoundException("Error: Order number " + orderNumber + " not found.");
        }

        // Update file: delete if no orders left, else rewrite
        if (ordersForDate.isEmpty()) {
            String fileName = ORDER_FOLDER + orderDate.format(DateTimeFormatter.ofPattern("MMddyyyy")) + ".txt";
            new File(fileName).delete();
        } else {
            writeOrdersToFile(orderDate, ordersForDate);
        }

        return removedOrder;
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

    //implement read
    private void read(LocalDate orderDate) throws FlooringDataPersistenceException {
        String fileName = ORDER_FOLDER + orderDate.format(DateTimeFormatter.ofPattern("MMddyyyy")) + ".txt";
        File orderFile = new File(fileName);

        if (!orderFile.exists()) {
            orders.put(orderDate, new ArrayList<>()); // Initialize empty list if file doesn't exist
            return;
        }

        try (Scanner sc = new Scanner(new BufferedReader(new FileReader(fileName)))) {
            if (sc.hasNextLine()) {
                sc.nextLine(); // Skip the header line
            }

            List<Order> orderList = new ArrayList<>();
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                Order order = unmarshalOrder(line, orderDate);
                orderList.add(order);
            }

            orders.put(orderDate, orderList); // Store in HashMap
        } catch (IOException e) {
            throw new FlooringDataPersistenceException("Error reading file: " + fileName);
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

    @Override
    public Set<LocalDate> getAllOrderDates() {
        return orders.keySet();
    }

}
