package app;

/*
 * @copyright Copyright 2013 Andrew Brown. All rights reserved.
 * @license GNU/GPL.
 */
import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.nio.file.Path;

/**
 *
 * @author andrew
 */
public class test implements Runnable {

    /**
     * The 'extract' command is runnable from the command line
     */
    @Override
    public void run() {
        // get student folder
        File studentFolder;
        if (main.arguments.length >= 2 && main.arguments[1].length() > 1) {
            studentFolder = lib.recognize.find(main.arguments[1]);
        } else {
            File root = new File(System.getProperty("user.dir"));
            File[] studentList = root.listFiles(new FileFilter() {
                public boolean accept(File file) {
                    return file.isDirectory();
                }
            });
            studentFolder = (File) lib.Console.menu(studentList);
            if( studentFolder == null){
                System.err.println("Could not find the right student folder.");
                return;
            }
        }
        // get assignment number
        File assignmentFolder;
        if (main.arguments.length >= 3 && main.arguments[2].length() >= 1) {
            assignmentFolder = lib.recognize.find(main.arguments[2], studentFolder.toString());
        } else {
            int assignmentNumber = lib.Console.inputInteger("What assignment?");
            assignmentFolder = lib.recognize.find(Integer.toString(assignmentNumber), studentFolder.toString());
        }
        // check that it is found
        if (assignmentFolder == null || !assignmentFolder.exists()) {
            System.err.println("Could not find the right assignment folder.");
            return;
        }
        // 
        File[] javaFiles = assignmentFolder.listFiles(new FilenameFilter() {
            public boolean accept(File directory, String fileName) {
                return fileName.endsWith(".java");
            }
        });
        if (javaFiles.length > 1) {
            test.runAllFiles(javaFiles);
        } else {
            test.runFile(javaFiles[0]);
        }
    }

    /**
     * Compile and run a single java file
     *
     * @param file
     */
    public static void runFile(File file) {
        // compile
        String command = "javac \"" + file + "\"";
        ConsoleThread cli = new ConsoleThread(command);
        cli.run();
        // run
        Path dir = file.getParentFile().toPath();
        String className = file.getName().replace(".java", "");
        command = "cmd.exe /c cd " + dir + " & start cmd.exe /k \"java " + className + "\"";
        ConsoleThread cli2 = new ConsoleThread(command);
        cli2.start();
        // view
        command = "notepad " + file.getAbsolutePath();
        ConsoleThread cli3 = new ConsoleThread(command);
        cli3.start();
    }

    /**
     * Compile several java files together; run with 'java [name of main class]'
     *
     * @param files
     */
    public static void runAllFiles(File[] files) {
        for (int i = 0; i < files.length; i++) {
            String command = "notepad " + files[i].getAbsolutePath();
            ConsoleThread cli3 = new ConsoleThread(command);
            cli3.start();
        }
        // compile
        String command = "javac \"" + test.implode("\" \"", files) + "\"";
        ConsoleThread cli = new ConsoleThread(command);
        cli.run();
        // run
        Path dir = files[0].getParentFile().toPath();
        command = "cmd.exe /c cd " + dir + " & start cmd.exe /k dir";
        ConsoleThread cli2 = new ConsoleThread(command);
        cli2.start();
    }

    /**
     * Implodes a String array into a single String
     *
     * @param separator
     * @param list
     * @return
     */
    public static String implode(String separator, Object[] list) {
        StringBuilder s = new StringBuilder();
        if (list.length > 0) {
            s.append(list[0]);
            for (int i = 1; i < list.length; i++) {
                s.append(separator);
                s.append(list[i].toString());
            }
        }
        return s.toString();
    }
}
