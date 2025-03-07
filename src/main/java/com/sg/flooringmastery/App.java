package com.sg.flooringmastery;

import com.sg.flooringmastery.controller.FlooringController;
import com.sg.flooringmastery.dao.*;
import com.sg.flooringmastery.service.FlooringServiceImpl;
import com.sg.flooringmastery.service.FlooringServiceLayer;
import com.sg.flooringmastery.ui.FlooringView;
import com.sg.flooringmastery.ui.UserIO;
import com.sg.flooringmastery.ui.UserIOConsoleImpl;

public class App {
    public static void main(String[] args) {
        try {
            UserIO io = new UserIOConsoleImpl();
            FlooringView view = new FlooringView(io);

            // Instantiate DAOs
            OrderDao orderDao = new OrderDaoFileImpl();
            ProductDao productDao = new ProductDaoFileImpl();  // May throw exception
            StateTaxDao stateTaxDao = new StateTaxFileImpl(); // May throw exception

            // Instantiate service and controller
            FlooringServiceLayer service = new FlooringServiceImpl(orderDao, productDao, stateTaxDao);
            FlooringController controller = new FlooringController(view, service);
            controller.run();

        } catch (FlooringDataPersistenceException e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }
}