package com.sg.flooringmastery;

import com.sg.flooringmastery.controller.FlooringController;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
    public static void main(String[] args) {


        ApplicationContext appContext
                = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");

        //get bean method  look inside the application context and get bean Controller
        // it will look for beans who's id is controller
        FlooringController controller = appContext.getBean("controller", FlooringController.class);
        controller.run();
    }

}