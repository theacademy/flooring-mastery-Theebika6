package com.sg.flooringmastery.controller;

import com.sg.flooringmastery.dao.FlooringDataPersistenceException;
import com.sg.flooringmastery.dto.Order;
import com.sg.flooringmastery.service.FlooringServiceLayer;
import com.sg.flooringmastery.ui.FlooringView;
import com.sg.flooringmastery.ui.MenuSelection;
import com.sg.flooringmastery.ui.UserIO;
import com.sg.flooringmastery.ui.UserIOConsoleImpl;

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
            // 1) Get user choice
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
                  //  exportAllData();
                    break;
                case EXIT:
                    keepGoing = false;
                    break;
                default:
                    System.out.println("Unknown command.");
            }
        }
        System.out.println("Exiting Flooring Program. Goodbye!");
    }


    private MenuSelection promptMenuSelection() {
        return view.printMenuAndGet();
    }

    private void displayOrders() {
        System.out.println("DISPLAY ORDERS ...");
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
        System.out.println("ADD ORDER ...");
        view.displayPressEnterToContinue();
    }

    private void editOrder() {
        System.out.println("EDIT ORDER ...");
        view.displayPressEnterToContinue();
    }

    private void removeOrder() {
        System.out.println("REMOVE ORDER (stub)...");
        view.displayPressEnterToContinue();
    }

}
