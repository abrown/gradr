/*
 * @copyright Copyright 2013 Andrew Brown. All rights reserved.
 * @license GNU/GPL.
 */
package lib;

import java.util.HashMap;
import java.util.Scanner;

/**
 *
 * @author andrew
 */
public class Console {

    /**
     * Prints a message to stdout
     *
     * @param message
     */
    public static void message(String message) {
        System.out.print("[MSG] ");
        System.out.println(message);
    }

    /**
     * Displays a choice menu for a group of objects
     *
     * @param items
     * @return
     */
    public static Object menu(Object[] items) {
        // print list
        System.out.println("Menu:");
        System.out.println(" [0] Cancel");
        for (int i = 0; i < items.length; i++) {
            int index = i + 1;
            System.out.println(" [" + index + "] " + items[i].toString());
        }
        // get choice
        int choice = 0;
        do {
            System.out.print("Enter a number: ");
            Scanner sc = new Scanner(System.in);
            choice = sc.nextInt();
        } while (choice < 0 || choice > items.length);
        // return
        System.out.println();
        if (choice == 0) {
            return null;
        } else {
            return items[choice - 1];
        }
    }
    /**
     * Stores application-wide properties
     */
    static HashMap<String, String> properties = new HashMap<>();

    /**
     * Sets a property for the whole application
     *
     * @param name
     * @return
     */
    public static String property(String name) {
        if (properties.containsKey(name)) {
            return properties.get(name);
        } else {
            System.out.print("Set value for property '" + name + "': ");
            Scanner sc = new Scanner(System.in);
            String value = sc.nextLine();
            properties.put(name, value);
            System.out.println();
            return value;
        }
    }
    
    public static void addEndingSpace(String query){
        if( query.charAt(query.length() - 1) != ' ' ){ System.out.print(" "); }
    }
    
    /**
     * Ask a simple yes/no question; biased towards 'yes' answer
     * @param query
     * @return 
     */
    public static boolean confirm(String query){
        System.out.print(query);
        Console.addEndingSpace(query);
        System.out.print("(Press ENTER for YES, 'no' for NO) ");
        // read input
        Scanner sc = new Scanner(System.in);
        String value = sc.nextLine();
        // return
        if( value.length() == 0 || value.toLowerCase().startsWith("y") ){
            return true;
        }
        else if(value.toLowerCase().startsWith("n")){
            return false;
        }
        else{
            System.out.println("Unknown response.");
            return Console.confirm(query);
        }
    }

    /**
     * Request a text answer in the console
     * @param query
     * @return
     */
    public static String input(String query) {
        // print query, add a space if necessary
        System.out.print(query);
        Console.addEndingSpace(query);
        // on the same line, get the typed line
        Scanner sc = new Scanner(System.in);
        String value = sc.nextLine();
        // add a line
        System.out.println();
        // return
        return value;
    }
    
    /**
     * Request an integer in the console
     * @param query
     * @return 
     */
    public static int inputInteger(String query) {
       String value = "";
       while( !Console.isNumeric(value) ){
           value = Console.input(query);
       }
       return new Integer(value);
    }

    /**
     * From http://stackoverflow.com/questions/1102891/how-to-check-a-string-is-a-numeric-type-in-java
     * @param str
     * @return 
     */
    public static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
    }

    /**
     * Handles exceptions
     *
     * @param e
     */
    public static void error(Exception e) {
        e.printStackTrace();
    }
}
