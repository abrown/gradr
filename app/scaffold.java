package app;

/*
 * @copyright Copyright 2013 Andrew Brown. All rights reserved.
 * @license GNU/GPL.
 */

import java.io.File;

/**
 * Scaffold sets up a folder structure in which to save assignments to grade.
 *
 * @author Andrew Brown
 */
public class scaffold implements Runnable{

    /**
     * The 'scaffold' command is runnable from the command line
     *
     * @param main.arguments
     */
    @Override
    public void run() {
        // get folders
        int number_of_folders = 0;
        if (main.arguments.length >= 1 && lib.Console.isNumeric(main.arguments[0])) {
            number_of_folders = new Integer(main.arguments[0]);
        } else {
            number_of_folders = lib.Console.inputInteger("How many student folders?");
        }
        // get assignments
        int number_of_assignments = 0;
        if (main.arguments.length >= 2 && lib.Console.isNumeric(main.arguments[1])) {
            number_of_assignments = new Integer(main.arguments[1]);
        } else {
            number_of_assignments = lib.Console.inputInteger("How many assignment folders?");
        }
        // create folders
        for (int i = 0; i < number_of_folders; i++) {
            String student_name = lib.Console.input(String.format("[%d/%d] Enter student name:", i + 1, number_of_folders));
            // make student folder
            File student = new File(student_name);
            student.mkdir();
            // make a log file
            lib.log.write("Student folder created.", student);
            // make assignment folders
            for (int j = 1; j <= number_of_assignments; j++) {
                File assignment = new File(student, Integer.toString(j));
                assignment.mkdir();
            }
        }
    }
}