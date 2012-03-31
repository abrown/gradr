package task;

import app.ConsoleThread;
import grading.Attachment;
import grading.Homework;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 *
 * @author andrew
 */
public class JavaRunAll extends Task {

    Homework homework;

    /**
     * Constructor
     */
    public JavaRunAll() {
    }

    /**
     * Constructor
     * @param h 
     */
    public JavaRunAll(Homework h) {
        this.homework = h;
    }

    /**
     * Run task
     */
    public void run() {
        assert this.homework != null : "Error: homework must be set.";
        // get attachments
        ArrayList<String> files = new ArrayList<String>();
        for (Attachment a : this.homework.attachments) {
            // check for .java extension
            if (a.getLocalFile().getPath().endsWith(".java")) {
                // add to list
                files.add(a.getLocalFile().getPath());
                // view
                String command = "notepad " + a.getLocalFile().getPath();
                ConsoleThread cli3 = new ConsoleThread(command);
                cli3.start();
            }
        }
        // compile
        String command = "javac " + this.implode(" ", files.toArray(new String[0]));
        ConsoleThread cli = new ConsoleThread(command);
        cli.run();
        // run
        Path dir = this.homework.attachments[0].getLocalFile().toPath().getParent();
        command = "cmd.exe /c cd " + dir + " & start cmd.exe /k dir";
        ConsoleThread cli2 = new ConsoleThread(command);
        cli2.start();
    }

    /**
     * Run task from CLI
     * @param arguments 
     */
    public static void main(String[] arguments) {
        JavaRunAll c = new JavaRunAll();
        c.bind(arguments);
        c.run();
    }

    /**
     * Implodes a String array into a single String
     * @param separator
     * @param list
     * @return 
     */
    public String implode(String separator, String[] list) {
        StringBuilder s = new StringBuilder();
        if (list.length > 0) {
            s.append(list[0]);
            for (int i = 1; i < list.length; i++) {
                s.append(separator);
                s.append(list[i]);
            }
        }
        return s.toString();
    }
}