package com.sg.flooringmastery;

import com.sg.flooringmastery.controller.FlooringController;
import com.sg.flooringmastery.ui.FlooringView;
import com.sg.flooringmastery.ui.UserIO;
import com.sg.flooringmastery.ui.UserIOConsoleImpl;

public class App {
    public static void main(String[] args) {


        UserIO io = new UserIOConsoleImpl();
        FlooringView view = new FlooringView(io);
        FlooringController controller = new FlooringController(view);
        controller.run();

    }
}