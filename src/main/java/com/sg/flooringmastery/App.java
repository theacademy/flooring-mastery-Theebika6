package com.sg.flooringmastery;

import com.sg.flooringmastery.controller.FlooringController;
import com.sg.flooringmastery.dao.OrderDao;
import com.sg.flooringmastery.dao.OrderDaoFileImpl;
import com.sg.flooringmastery.service.FlooringServiceImpl;
import com.sg.flooringmastery.service.FlooringServiceLayer;
import com.sg.flooringmastery.ui.FlooringView;
import com.sg.flooringmastery.ui.UserIO;
import com.sg.flooringmastery.ui.UserIOConsoleImpl;

public class App {
    public static void main(String[] args) {


        UserIO io = new UserIOConsoleImpl();
        FlooringView view = new FlooringView(io);
        OrderDao dao = new OrderDaoFileImpl();
        FlooringServiceLayer service = new FlooringServiceImpl(dao);
        FlooringController controller = new FlooringController(view, service);
        controller.run();

    }
}