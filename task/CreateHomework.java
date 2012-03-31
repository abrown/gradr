/*
 * @copyright Copyright 2012 Andrew Brown. All rights reserved.
 * @license GNU/GPL.
 */
package task;

import grading.Homework;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

/**
 *
 * @author Andrew Brown
 */
public class CreateHomework extends Task {

    public String name;
    public String title;
    public String message;
    public String parentDirectory;

    /**
     * Run task
     */
    public void run() {
        assert this.name != null : "Error: name must be set.";
        assert this.title != null : "Error: title must be set.";
        assert this.message != null : "Error: message must be set.";
        assert this.parentDirectory != null : "Error: parent directory must be set.";
        // find parent directory
        Path _parentDirectory = Paths.get(parentDirectory);
        assert _parentDirectory.toFile().exists() : "Error: parent directory could not be found.";
        // create homework directory
        Path homeworkDirectory = _parentDirectory.resolve(this.removeSpaces(this.name));
        homeworkDirectory.toFile().mkdir();
        // create homework
        Homework h = new Homework(name, title, message, new Date());
        // serialize
        try {
            Homework.save(homeworkDirectory.resolve("homework-data.ser").toFile(), h);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Run task from CLI
     * @param arguments 
     */
    public static void main(String[] arguments) {
        CreateHomework c = new CreateHomework();
        c.bind(arguments);
        c.run();
    }

    /**
     * Replaces spaces in a string
     * @param text
     * @return 
     */
    public String removeSpaces(String text) {
        return text.replaceAll(" ", "");
    }
}
