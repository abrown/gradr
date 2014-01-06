package app;

import java.util.Arrays;

/*
 * @copyright Copyright 2013 Andrew Brown. All rights reserved.
 * @license GNU/GPL.
 */
/**
 *
 * @author andrew
 */
public class main {
    
    public static String[] arguments;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (args.length < 1 || args[0].equals("?")) {
            main.printUsage();
            System.exit(0);
        }
        // save arguments
        main.arguments = args;
        // search
        String command = args[0].toLowerCase();
        if (main.commandExists(command)) {
            try {
                Runnable instance = (Runnable) Class.forName("app."+command).newInstance();
                instance.run();
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                System.err.println(e);
            }
        }
        else{
            System.err.println("Could not find command.");
        }
    }

    /**
     * Print usage for the app
     */
    private static void printUsage() {
        System.out.println("Available commands (e.g. 'gradr [command] ...'):");
        System.out.println("  scaffold - ...");
        System.out.println("  extract - ...");
        System.out.println("  test - ...");
        System.out.println("  grade - ...");
        System.out.println("  report - ...");
        System.out.println("  mail - ...");
        System.out.println();
    }
    
    private static boolean commandExists(String command){
        String[] availableCommands = {"scaffold", "extract", "test", "grade", "report", "mail"};
        for(String a : availableCommands){
            if(command.equals(a)){ return true; }
        }
        return false;
    }
}
