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
        return null;
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
            io.print(String.format("Order #%d: %s, %s, %s, etc.",
                    o.getOrderNumber()));
                   // o.getCustomerName(),
                    //o.getProductType(),
                    //o.getTotal()));
        }
    }



}
