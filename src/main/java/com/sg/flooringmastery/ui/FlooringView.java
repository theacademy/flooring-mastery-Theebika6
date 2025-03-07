package com.sg.flooringmastery.ui;

import com.sg.flooringmastery.dto.Order;
import com.sg.flooringmastery.dto.Product;
import com.sg.flooringmastery.dto.StateTax;

import java.math.BigDecimal;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class FlooringView {

    private final UserIO io;
    private final DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");

    public FlooringView(UserIO io) {
        this.io = io;
    }

    // displayHeader
    public void displayHeader(String title){
        io.print("===" + title + "===");
    }

    //display press enter
    public void displayPressEnterToContinue(){
        io.print("Press Enter to continue.");
    }

    // ask for order date
    public LocalDate promptOrderDate(){
        while (true) {
            String date = io.readString("Please enter the order date MMddyyyy");

            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMddyyyy");
                return LocalDate.parse(date, formatter);
            } catch (DateTimeException e) {
                io.print("Invalid date format. Please try again");
            }
        }
    }

    //ask for order number
    public int promptOrderNumber(){
        return io.readInt("Enter your order number");
    }

    // prompt decimal value
    public BigDecimal promptBigDecimal() {
        return io.readBigDecimal("Enter a decimal value: ");
    }

    //display add order
    public Order displayAddOrder(List<Product> products, List<StateTax> stateTaxes){
        displayHeader("Add Order");
        //prompt order date --> future
        LocalDate orderDate;
        do {
            orderDate = promptOrderDate();
            if (orderDate.isBefore(LocalDate.now())) {
                io.print("Order date must be in the future. Try again.");
            }
        } while (orderDate.isBefore(LocalDate.now()));

        //prompt for customerName
        String customerName;
        while (true) {
            customerName = io.readString("Enter customer name (letters, numbers, commas, periods allowed): ").trim();
            if (!customerName.isBlank() && customerName.matches("^[a-zA-Z0-9., ]+$")) {
                break;
            }
            io.print("Invalid name format. Please try again.");
        }
        //States --> it will loop and if user enters in lowercase it will change to uppercase to match
        io.print("Available states:");
        for (StateTax tax : stateTaxes) {
            io.print(tax.getStateAbbreviation() + " - TaxRate: " + tax.getTaxRate());
        }

        StateTax selectedState = null;
        while (selectedState == null) {
            String stateAbbreviation = io.readString("Enter state: ").toUpperCase();
            System.out.println("User input state: " + stateAbbreviation); // Debugging
            for (StateTax tax : stateTaxes) {
                if (tax.getStateAbbreviation().trim().equalsIgnoreCase(stateAbbreviation) ||
                        tax.getStateName().trim().equalsIgnoreCase(stateAbbreviation)) {
                    selectedState = tax;
                    break;
                }
            }
            if (selectedState == null) {
                io.print("Invalid state. Please enter a valid state from the list.");
            }
        }
        //products
        io.print("Available products:");
        for (Product product : products) {
            io.print(product.getProductType() + " - Cost per sq ft: " + product.getCostPerSqft() +
                    ", Labor cost per sq ft: " + product.getLaborCostPerSqft());
        }

        Product selectedProduct = null;
        while (selectedProduct == null) {
            String productType = io.readString("Enter product type: ");
            for (Product product : products) {
                if (product.getProductType().equalsIgnoreCase(productType)) {
                    selectedProduct = product;
                    break;
                }
            }
            if (selectedProduct == null) {
                io.print("Invalid product type. Please select from the list.");
                io.print("Available products again:");
                for (Product product : products) {
                    io.print(product.getProductType());
                }
            }
        }
        //prompt area must be bigger than 100 sqft
        BigDecimal area;
        while (true) {
            try {
                area = io.readBigDecimal("Enter area (min 100 sq ft): ");
                if (area.compareTo(BigDecimal.valueOf(100)) >= 0) {
                    break;
                }
                io.print("Invalid area. Must be at least 100 sq ft.");
            } catch (NumberFormatException e) {
                io.print("Invalid input. Please enter a numeric value.");
            }
        }

        return new Order(0, orderDate, customerName, area, selectedState, selectedProduct);
    }

    // edit Order
    public Order displayEditOrder(List<Product> products, List<StateTax> stateTaxes, Order existingOrder){
        displayHeader("Edit Order");

        //editing name
        String newCustomerName = io.readString("Enter new customer name (" + existingOrder.getCustomerName() + ")");
        if(!newCustomerName.trim().isEmpty()){
            existingOrder.setCustomerName(newCustomerName);
        }
        //editing state
        // Allow editing of state
        io.print("Available states:");
        for (StateTax tax : stateTaxes) {
            io.print(tax.getStateAbbreviation() + " - TaxRate: " + tax.getTaxRate());
        }
        String newState = io.readString("Enter new state (" + existingOrder.getStateTax().getStateAbbreviation() + ") or press Enter to keep: ");
        if (!newState.trim().isEmpty()) {
            for(StateTax tax : stateTaxes){
                if(tax.getStateAbbreviation().equalsIgnoreCase(newState) || tax.getStateName().equalsIgnoreCase(newState)){
                    existingOrder.setStateTax(tax);
                    break;
                }
            }
        }

        // editing product type
        io.print("Available products:");
        for (Product product : products) {
            io.print(product.getProductType() + " - Cost per sq ft: " + product.getCostPerSqft() +
                    ", Labor cost per sq ft: " + product.getLaborCostPerSqft());
        }
        String newProductType = io.readString("Enter new product type (" + existingOrder.getProduct().getProductType() + ") or press Enter to keep: ");
        if (!newProductType.trim().isEmpty()) {
            for (Product product : products) {
                if (product.getProductType().equalsIgnoreCase(newProductType)) {
                    existingOrder.setProduct(product);
                    break;
                }
            }
        }

        // editing area
        String newAreaInput = io.readString("Enter new area (" + existingOrder.getArea() + ") or press Enter to keep: ");
        if (!newAreaInput.trim().isEmpty()) {
            BigDecimal newArea = new BigDecimal(newAreaInput);
            if (newArea.compareTo(BigDecimal.valueOf(100)) >= 0) {
                existingOrder.setArea(newArea);
            } else {
                io.print("Invalid area. Must be at least 100 sq ft.");
            }
        }

        // confirm to save
        boolean saveChanges = io.readBoolean(("Do you want to save changes? (yes/no)"));
        if(!saveChanges){
            io.print("Editing failed.");
            return null;
        }

        return existingOrder;
    }
   // remove order
   public void displayRemoveOrderBanner() {
       io.print("=== Remove Order ===");
   }

    public void displayRemoveResult(Order removedOrder) {
        if (removedOrder != null) {
            io.print("Order successfully removed.");
        } else {
            io.print("No such order found.");
        }
        io.readString("Please hit enter to continue.");
    }


    // display Menu
    public MenuSelection printMenuAndGet() {
        io.print("***************************************");
        io.print("* <<Flooring Program>>");
        io.print("* 1. Display Orders");
        io.print("* 2. Add an Order");
        io.print("* 3. Edit an Order");
        io.print("* 4. Remove an Order");
        io.print("* 5. Export All Data");
        io.print("* 6. Exit");
        io.print("***************************************");


        int choice= io.readInt("Please select from the above choices: ", 1, 6);
        return MenuSelection.fromInt(choice);
    }

    public String displayExportAllData(){
        displayHeader("Export All Data");
        // ask user to confirm
        boolean confirm = io.readBoolean("Are you sure you want to export all the data?");
        if(confirm){
            return "All data are exported sucessfully";
        }else{
            return "Export failed";
        }
    }

    public void displayOrders(List<Order> orders) {
        for (Order o : orders) {

            System.out.println(o);
        }
    }



}