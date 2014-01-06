/*
 * @copyright Copyright 2013 Andrew Brown. All rights reserved.
 * @license GNU/GPL.
 */
package lib;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

/**
 *
 * @author andrew
 */
public class log {

    /**
     * Write a line to the log, in the current user directory
     * @param line 
     */
    public static void write(String line){
        log.write(line, new File(System.getProperty("user.dir")));
    }
    
    /**
     * Write a line to the log
     * @param line
     * @param folder 
     */
    public static void write(String line, File folder){
        File file = log.getLog(folder);
        try(PrintWriter pw = new PrintWriter(file)){
            pw.append(String.format("[%s] %s\n", new Date(), line));
        }
        catch(Exception e){
            System.err.println(e);
        } 
    }
    
    /**
     * Get log file and create if necessary
     * @param folder
     * @return 
     */
    public static File getLog(File folder) {
        File file = new File(folder, "log.txt");
        if (!file.exists()) {
            log.createLog(folder);
        }
        return file;
    }

    /**
     * Create log file
     * @param folder 
     */
    private static void createLog(File folder) {
        File file = new File(folder, "log.txt");
        try {
            file.createNewFile();
        } catch (IOException e) {
            System.err.println(e);
        }
        try (FileWriter f = new FileWriter(file, true)) {
            f.write("Created on " + new Date() + "\n");
        } catch (IOException e) {
            System.err.println(e);
        }
    }
}
