/*
 * @copyright Copyright 2012 Andrew Brown. All rights reserved.
 * @license GNU/GPL.
 */
package task;

import grading.Homework;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author Andrew Brown
 */
public class ViewHomework extends Task {

    public String directory;

    /**
     * Run task
     */
    public void run() {
        assert this.directory != null : "Error: directory must be set.";
        // find  directory
        Path _directory = Paths.get(this.directory);
        assert _directory.toFile().exists() : "Error: directory could not be found.";
        // find serialized file
        Path _file = _directory.resolve("homework-data.ser");
        assert _file.toFile().exists() : "Error: homework-data.ser could not be found.";
        // load homework
        try {
            Homework h = Homework.load(_file.toFile());
            // print
            System.out.println(h);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Run task from CLI
     * @param arguments 
     */
    public static void main(String[] arguments) {
        ViewHomework c = new ViewHomework();
        c.bind(arguments);
        c.run();
    }
}
