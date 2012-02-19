package app;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author andrew
 */
public class Console {

    /**
     * Prints a message to stdout
     * @param message 
     */
    public static void message(String message) {
        System.out.print("[MSG] ");
        System.out.println(message);
    }

    /**
     * Displays a choice menu for a group of objects
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
        if( choice == 0 ) return null;
        else return items[choice - 1];
    }
    
    /**
     * Displays a choice menu for a group of strings, returning the choice entered
     * @param items
     * @return 
     */
//    public static int menu(String[] items) {
//        // print list
//        System.out.println("Menu:");
//        System.out.println(" [0] Cancel");
//        for (int i = 0; i < items.length; i++) {
//            int index = i + 1;
//            System.out.println(" [" + index + "] " + items[i].toString());
//        }
//        // get choice
//        int choice = 0;
//        do {
//            System.out.print("Enter a number: ");
//            Scanner sc = new Scanner(System.in);
//            choice = sc.nextInt();
//        } while (choice < 0 || choice > items.length);
//        // return
//        System.out.println();
//        return choice - 1;
//    }
    
    /**
     * Displays a choice menu for an arraylist of objects, returning the choice entered
     * @param items
     * @return 
     */
//    public static int menu(List items) {
//        // print list
//        System.out.println("Menu:");
//        System.out.println(" [0] Cancel");
//        for (Object item : items) {
//            int index = items.indexOf(item);
//            System.out.println(" [" + index + "] " + item.toString());
//        }
//        // get choice
//        int choice = 0;
//        do {
//            System.out.print("Enter a number: ");
//            Scanner sc = new Scanner(System.in);
//            choice = sc.nextInt();
//        } while (choice < 0 || choice > items.size());
//        // return
//        System.out.println();
//        return choice;
//    }
    
    /**
     * Stores application-wide properties
     */
    static HashMap<String, String> properties = new HashMap<>();

    /**
     * Sets a property for the whole application
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

    /**
     * Displays a simple text input
     * @param query
     * @return 
     */
    public static String input(String query) {
        System.out.print(query);
        Scanner sc = new Scanner(System.in);
        String value = sc.nextLine();
        return value;
    }
    
    /**
     * Handles exceptions
     * @param e 
     */
    public static void error(Exception e){
        e.printStackTrace();
    }
}
