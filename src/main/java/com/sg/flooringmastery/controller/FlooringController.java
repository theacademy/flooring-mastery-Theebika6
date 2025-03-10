package com.sg.flooringmastery.controller;

import com.sg.flooringmastery.dao.FlooringDataPersistenceException;
import com.sg.flooringmastery.dto.Order;
import com.sg.flooringmastery.dto.Product;
import com.sg.flooringmastery.dto.StateTax;
import com.sg.flooringmastery.service.FlooringServiceLayer;
import com.sg.flooringmastery.service.OrderDataValidationException;
import com.sg.flooringmastery.ui.FlooringView;
import com.sg.flooringmastery.ui.MenuSelection;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;

public class FlooringController {

    private final FlooringView view;
    private final FlooringServiceLayer service;

    public FlooringController(FlooringView view, FlooringServiceLayer service) {
        this.view = view;
        this.service= service;
    }

    public void run() {
        boolean keepGoing = true;
        while (keepGoing) {
            // Get user choice
            MenuSelection selection = promptMenuSelection();
            switch (selection) {
                case DISPLAY_ORDER:
                    displayOrders();
                    break;
                case ADD_ORDER:
                    addOrder();
                    break;
                case EDIT_ORDER:
                    editOrder();
                    break;
                case REMOVE_ORDER:
                    removeOrder();
                    break;
                case EXPORT_ALL_DATA:
                    exportAllData();
                    break;
                case EXIT:
                    // ask confirmation to exit
                    boolean confirmToExit = view.readBoolean("Are you sure you want to exit? (yes/no) ");
                    if (confirmToExit){
                        keepGoing = false;
                    }

                    break;
                default:
                    System.out.println("Unknown command.");
            }
        }
        System.out.println("Exiting Flooring Program. Goodbye!");
        view.displayPressEnterToContinue();
    }


    private MenuSelection promptMenuSelection() {
        return view.printMenuAndGet();
    }

    private void displayOrders() {
        System.out.println("DISPLAY ORDERS");
        //ask view for a date
        LocalDate date= view.promptOrderDate();

        try{
            List<Order> orders = service.getAllOrders(date);
            view.displayOrders(orders);
        }catch (FlooringDataPersistenceException e){
            view.displayHeader("Error " + e.getMessage());
        }

        view.displayPressEnterToContinue();

    }

    private void addOrder() {
        System.out.println("ADD ORDER");

        List<Product> products = service.getProducts();
        List<StateTax> stateTaxes;
        try {
            stateTaxes = service.getStateTaxes(); // Handle exception when retrieving states
        } catch (FlooringDataPersistenceException e) {
            view.displayHeader("Error loading state tax data: " + e.getMessage());
            return;
        }
        Order newOrder = view.displayAddOrder(products, stateTaxes);

        try {
            // Get next order number
            int orderNumber = service.getNextOrderNumber(newOrder.getOrderDate());
            newOrder.setOrderNumber(orderNumber);

            // order summary before confirmation
            view.displayHeader("Order Summary: ");
            System.out.println(newOrder);
            //ask user to confirm
            boolean confirm = view.readBoolean(" Do you want to place this order? (yes/no) ");
            if(!confirm){
                view.displayHeader("Order not added");
                return;
            }
            //Add order
            Order addedOrder = service.addOrder(newOrder);
            view.displayHeader("Order added successfully!");
            System.out.println(addedOrder);
        } catch (FlooringDataPersistenceException | OrderDataValidationException e) {
            view.displayHeader("Error: " + e.getMessage());
        }

        view.displayPressEnterToContinue();
    }


    private void editOrder() {
        System.out.println("EDIT ORDER");

        // get order details
        LocalDate orderDate = view.promptOrderDate();
        try {
            // Check if orders exist for this date
            List<Order> ordersForDate = service.getAllOrders(orderDate);
            if (ordersForDate.isEmpty()) {
                view.displayHeader("Error: No orders found for " + orderDate);
                return;
            }

            // asks user for order number
            int orderNumber = view.promptOrderNumber();

            // gets existing order from the service layer
            Order existingOrder = service.getOrder(orderDate, orderNumber);
            if (existingOrder == null) {
                view.displayHeader("Error: Order not found.");
                return;
            }
            // user edits the order
            Order updatedOrder = view.displayEditOrder(service.getProducts(), service.getStateTaxes(), existingOrder);
            if(updatedOrder == null){
                return ;
            }
            // validate and saves the order
            service.editOrder(updatedOrder);
            view.displayHeader("Order updated successfully!");

        } catch (FlooringDataPersistenceException | OrderDataValidationException e) {
            view.displayHeader("Error: " + e.getMessage());
        }

        view.displayPressEnterToContinue();
    }

    private void removeOrder() {
        view.displayRemoveOrderBanner();

        // Prompt for order date
        LocalDate orderDate = view.promptOrderDate();
        // check if order date exists in correct format
        int orderNumber = view.promptOrderNumber();

        try {
            // Fetch the existing order from the service layer
            Order existingOrder = service.getOrder(orderDate, orderNumber);
            if (existingOrder == null) {
                view.displayHeader("Error: Order not found for " + orderDate + " and order number " + orderNumber);
                return;
            }

            // Display order details before removal
            view.displayOrders(List.of(existingOrder));

            // Ask for confirmation
            boolean confirm = view.readBoolean("Are you sure you want to remove this order? (yes/no)");
            if (!confirm) {
                view.displayHeader("Order removal canceled.");
                return;
            }

            // Remove the order
            Order removedOrder = service.removeOrder(orderDate, orderNumber);
            view.displayRemoveResult(removedOrder);

        } catch (FlooringDataPersistenceException | OrderDataValidationException e) {
            view.displayHeader("Error: " + e.getMessage());
        }

        view.displayPressEnterToContinue();
    }

    public void exportAllData() {
        String exportFilePath = "Orders/ExportOrders.txt";

        try (PrintWriter writer = new PrintWriter(new FileWriter(exportFilePath))) {
            writer.println("OrderNumber,CustomerName,State,TaxRate,ProductType,Area,CostPerSquareFoot,LaborCostPerSquareFoot,MaterialCost,LaborCost,Tax,Total");

            int orderCount = 0;

            // Get all order dates first
            for (LocalDate date : service.getAllOrderDates()) {
                List<Order> orders = service.getAllOrders(date);
                for (Order o : orders) {
                    writer.println(date + "-" + o.getOrderNumber() + "," + o.getCustomerName() + "," +
                            o.getStateTax().getStateAbbreviation() + "," + o.getStateTax().getTaxRate() + "," +
                            o.getProduct().getProductType() + "," + o.getArea() + "," +
                            o.getProduct().getCostPerSqft() + "," + o.getProduct().getLaborCostPerSqft() + "," +
                            o.getMaterialCost() + "," + o.getLaborCost() + "," +
                            o.getTax() + "," + o.getTotal());
                    orderCount++;
                }
            }

            //  Display confirmation message
            view.displayExportAllData(orderCount, exportFilePath);

        } catch (Exception e) {
            view.displayHeader("Error exporting data: " + e.getMessage());
        }
        view.displayPressEnterToContinue();
    }

}
