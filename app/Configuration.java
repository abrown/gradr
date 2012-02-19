package app;

import grading.Criterion;
import grading.Rubric;
import java.util.Hashtable;
import java.util.Scanner;

/**
 * Stores configuration options; TODO add load from and save to XML
 * @author andrew
 */
public class Configuration{
    
    // default configuration
    static{
        // initialize properties
        Configuration.properties = new Hashtable<>();
        // add rubric
        Rubric r = new Rubric();
        r.add(new Criterion("Compiles with no errors", 20));
        r.add(new Criterion("Runs with no errors", 20));
        r.add(new Criterion("Correct result and output", 20));
        r.add(new Criterion("All assignment features properly implemented", 20));
        r.add(new Criterion("Formatting and documentation", 20));
        Configuration.set("rubric", r);
        // add e-mail settings
        Configuration.set("email.host.imap", "imap.gmail.com");
        Configuration.set("email.host.smtp", "smtp.gmail.com");
        Configuration.set("email.host.smtp.port", new Integer(465));
        Configuration.set("email.username", "XXXXXX");
        Configuration.set("email.password", "XXXXXX");
        Configuration.set("email.from", "XXXXXX");
        // add checkstyle settings
        Configuration.set("checkstyle.jar", "checkstyle/checkstyle-5.5-all.jar");
        Configuration.set("checkstyle.module", "checkstyle/sun_checks.xml");
    }
    
    /**
     * Stores configuration values
     */
    protected static Hashtable<String, Object> properties;
    
    /**
     * Returns a property
     * @param propertyName
     * @return 
     */
    public static Object get(String propertyName){
        if( !Configuration.properties.containsKey(propertyName) ) Configuration.setDynamically(propertyName);
        return Configuration.properties.get(propertyName);
    }
    
    /**
     * Dynamically sets a property value from the console
     * @param propertyName 
     */
    protected static void setDynamically(String propertyName){
        System.out.print("[CONF] Set '" + propertyName + "': ");
        Scanner sc = new Scanner(System.in);
        String value = sc.nextLine();
        Configuration.properties.put(propertyName, value);
    }
    
    /**
     * Sets a property
     * @param propertyName
     * @param propertyValue 
     */
    public static void set(String propertyName, Object propertyValue){
        Configuration.properties.put(propertyName, propertyValue);
    }
    
    /**
     * Loads a configuration
     */
    public static void load(){
        throw new UnsupportedOperationException("TODO");
    }
    
    /**
     * Saves a configuration
     */
    public static void save(){
        throw new UnsupportedOperationException("TODO");
    }
}