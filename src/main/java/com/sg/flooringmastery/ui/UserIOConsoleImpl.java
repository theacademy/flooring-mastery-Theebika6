package com.sg.flooringmastery.ui;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Scanner;

public class UserIOConsoleImpl implements UserIO{
    Scanner sc = new Scanner(System.in);

    @Override
    public void print(String msg) {
        System.out.println(msg);
    }

    @Override
    public boolean readBoolean(String prompt) {
        print(prompt + "(true/false : ");
        String input = sc.nextLine().trim().toLowerCase();
        return input.equals("true") || input.equals("yes") || input.equals("y");
    }


    @Override
    public int readInt(String prompt) {
        while(true){
            print(prompt);
            String input = sc.nextLine();
            try{
                return Integer.parseInt(input);
            }catch (NumberFormatException e){
                print("Invalid integer, please try again");
            }
        }
    }

    @Override
    public int readInt(String prompt, int min, int max) {
        System.out.println(prompt);
        return Integer.parseInt(sc.nextLine());
    }


    @Override
    public String readString(String prompt) {
        System.out.println(prompt);
        return sc.nextLine();
    }

    @Override
    public String readString(String prompt, int minChars, int maxChars) {
        return "";
    }

    @Override
    public LocalDate readLocalDate(String prompt) {
        return null;
    }

    @Override
    public LocalDate readLocalDate(String prompt, LocalDate minDate) {
        return null;
    }

    @Override
    public BigDecimal readBigDecimal(String prompt) {
        while(true){
            print(prompt);
            String input = sc.nextLine();
            try{
                return new BigDecimal(input);
            }catch (NumberFormatException e){
                print("Invalid decimal, please try again");
            }
        }
    }

    @Override
    public BigDecimal readBigDecimal(String prompt, int scale, BigDecimal min) {
        return null;
    }
}
