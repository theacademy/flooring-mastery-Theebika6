package com.sg.flooringmastery.ui;

import com.sg.flooringmastery.dto.Order;
import com.sg.flooringmastery.dto.Product;
import com.sg.flooringmastery.dto.StateTax;

import java.math.BigDecimal;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class FlooringView {

    private final UserIO io;
    private final DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");

    public FlooringView(UserIO io) {
        this.io = io;
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

    // displayHeader
    public void displayHeader(String title){
        io.print("===" + title + "===");
    }

    //display press enter
    public void displayPressEnterToContinue(){
        io.print("Press Enter to continue.");
        io.readString(" ");

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

    public void displayOrders(List<Order> orders) {
        for (Order o : orders) {

            System.out.println(o);
        }
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

        // clone order before modifying
        Order editOrder = existingOrder.cloneOrder();

        // edit customer name
        String newCustomerName = io.readString("Enter new customer name (" + editOrder.getCustomerName() + ")");
        if(!newCustomerName.trim().isEmpty() && newCustomerName.matches("^[a-zA-Z0-9., ]+$")){
            editOrder.setCustomerName(newCustomerName);
        }
        //editing state
        StateTax selectedState= editOrder.getStateTax();
        while(true){
            String newState = io.readString("Enter new State (" + selectedState.getStateAbbreviation() + ") or press to Enter to keep: " ).trim();

            if (newState.isEmpty()) break;

            boolean isValidState = false;
            for (StateTax tax : stateTaxes) {
                if (tax.getStateAbbreviation().equalsIgnoreCase(newState)) {
                    selectedState = tax;
                    isValidState = true;
                    break;
                }
            }
            if (isValidState) {
                editOrder.setStateTax(selectedState);
                break;
            } else {
                io.print("Error: Invalid state. Please enter a valid state.");
            }
        }

        // Editing product type
        io.print("Available products:");
        for (Product product : products) {
            io.print(product.getProductType() + " - Cost per sq ft: " + product.getCostPerSqft() +
                    ", Labor cost per sq ft: " + product.getLaborCostPerSqft());
        }

        Product selectedProduct = editOrder.getProduct();
        while (true) {
            String newProductType = io.readString("Enter new product type (" + selectedProduct.getProductType() + ") or press Enter to keep: ").trim();
            if (newProductType.isEmpty()) break;

            boolean isValidProduct = false;
            for (Product product : products) {
                if (product.getProductType().equalsIgnoreCase(newProductType)) {
                    selectedProduct = product;
                    isValidProduct = true;
                    break;
                }
            }
            if (isValidProduct) {
                editOrder.setProduct(selectedProduct);
                break;
            } else {
                io.print("Error: Invalid product type. Please select from the list.");
            }
        }

        // editing area
        while (true) {
            String newAreaInput = io.readString("Enter new area (" + editOrder.getArea() + ") or press Enter to keep: ").trim();
            if (newAreaInput.isEmpty()) break;
            try {
                BigDecimal newArea = new BigDecimal(newAreaInput);
                if (newArea.compareTo(BigDecimal.valueOf(100)) >= 0) {
                    editOrder.setArea(newArea);
                    break;
                } else {
                    io.print("Error: Area must be at least 100 sq ft.");
                }
            } catch (NumberFormatException e) {
                io.print("Error: Invalid input.");
            }
        }

        // confirm to save
        boolean saveChanges = io.readBoolean(("Do you want to save changes? (yes/no)"));
        if(!saveChanges){
            io.print("Editing failed.");
            return null;
        }
        return editOrder;
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

    public void displayExportAllData(int numOfOrders, String filePath) {
        io.print("=== Export All Data ===");
        io.print(numOfOrders + " orders exported successfully to: " + filePath);
    }

    public boolean readBoolean(String prompt) {
        return io.readBoolean(prompt);
    }


}