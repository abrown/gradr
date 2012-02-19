package task;

import app.Configuration;
import app.Console;
import grading.Attachment;
import grading.Homework;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 *
 * @author andrew
 */
public class StyleChecker {

    static final String JAR = "checkstyle/checkstyle-5.5-all.jar";
    static final String MODULE = "checkstyle/sun_checks.xml";
    Homework homework;
    ArrayList<String> errors;

    /**
     * Constructor
     * @param h 
     */
    public StyleChecker(Homework h) {
        this.homework = h;
        this.errors = new ArrayList<>();
    }

    /**
     * Checks each attachment for style rules
     * @return 
     */
    public void check() {
        ArrayList<File> files = new ArrayList<>();
        // create temporary files
        for (Attachment a : this.homework.attachments) {
            try {
                // run checkstyle on it
                this.errors.addAll(this.runCheckstyle(a.getLocalFile()));
            } catch (Exception e) {
                Console.error(e);
            }
        }
        // save
        this.homework.style_mistakes = this.errors.toArray(new String[this.errors.size()]);
    }

    /**
     * Runs the checkstyle application on a given file. Locations to JARs and modules
     * must be defined in the constants at the head of this class
     * @param f
     * @return 
     */
    public ArrayList<String> runCheckstyle(File f) {
        ArrayList<String> out = new ArrayList<>();
        try {
            String command = "java -jar " + Configuration.get("checkstyle.jar") + " -c " + Configuration.get("checkstyle.module") + " " + f.getPath();
            Process p = Runtime.getRuntime().exec(command);
            // setup
            BufferedReader bri = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader bre = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            // get stdout
            String line;
            while ((line = bri.readLine()) != null) {
                out.add(line);
            }
            bri.close();
            // get stderr
            while ((line = bre.readLine()) != null) {
                //error.add(line);
                System.err.println(line);
            }
            bre.close();
            // end process
            p.waitFor();
        } catch (Exception err) {
            err.printStackTrace();
        }
        // return
        return out;
    }
}