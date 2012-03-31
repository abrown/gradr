/*
 * @copyright Copyright 2012 Andrew Brown. All rights reserved.
 * @license GNU/GPL.
 */
package task;

import java.lang.reflect.Field;

/**
 * Provides contract for actions to perform with gradeable items
 * @author andrew
 */
public class Task implements Taskable {

    /**
     * Runs the task on the current homework item;
     * assert that prerequisite public fields are set.
     */
    public void run() {
        throw new UnsupportedOperationException("run() must be overriden in child classes.");
    }

    /**
     * Runs the task from the CLI
     * @param arguments 
     */
    //public static void main(String[] arguments){
    //    throw new UnsupportedOperationException("run() must be overriden in child classes.");
    //}
    /**
     * Returns the prerequisite fields to run this task
     * @return 
     */
    public String getUsage() {
        // setup
        Class c = this.getClass();
        StringBuilder s = new StringBuilder("The following fields must be set: \n");
        // get public properties
        for (Field f : c.getFields()) {
            s.append(" ");
            s.append(f.getType().getName());
            s.append(" ");
            s.append(f.getName());
            s.append("\n");
        }
        // return
        return s.toString(); 
    }

    /**
     * Binds the CLI arguments to this class
     * @param arguments 
     */
    public void bind(String[] arguments) {
        // setup
        Class c = this.getClass();
        Field[] fields = c.getFields();
        // test
        assert arguments.length > 0 : "Error: must have arguments to bind.\n" + this.getUsage();
        assert fields.length == arguments.length : "Error: arguments must have " + fields.length + " elements.\n" + this.getUsage();
        // run through public properties
        for (int i = 0; i < fields.length; i++) {
            Field f = fields[i];
            try {
                f.set(this, f.getType().getDeclaredConstructor(String.class).newInstance(arguments[i]));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
