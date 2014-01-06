package app;

/*
 * @copyright Copyright 2013 Andrew Brown. All rights reserved.
 * @license GNU/GPL.
 */
import lib.Console;
import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lib.recognize;

/**
 *
 * @author andrew
 */
public class extract implements Runnable {

    /**
     * The 'extract' command is runnable from the command line
     */
    @Override
    public void run() {
        // get file to extract
        File zippedFile;
        if (main.arguments.length >= 2 && main.arguments[1].length() > 1) {
            zippedFile = lib.recognize.find(main.arguments[1]);
        } else {
            zippedFile = lib.recognize.find(lib.Console.input("What file do you want to extract?"));
        }
        // check file is present
        if (zippedFile == null) {
            System.err.println("No file found matching that description.");
            return;
        }
        // extract
        extractZipped(zippedFile, new File(System.getProperty("user.dir")));
    }

    /**
     * Extract files from a zip file and move them to their correct locations
     *
     * @param zippedFile
     * @param parentDirectory
     */
    public static void extractZipped(File zippedFile, File parentDirectory) {
        // extract to temp folder
        File tempFolder = new File("temp" + (int) (Math.random() * 10000000));
        tempFolder.mkdir();
        lib.unzip.extract(zippedFile, tempFolder);
        // determine moves to make
        HashMap<File, File> moveTo = new HashMap();
        File[] list = tempFolder.listFiles();
        for (File from : list) {
            try {
                File to = extractDestinationFile(from);
                moveTo.put(from, to);
            } catch (Exception e) {
                // do nothing
            }
        }
        // request confirmation
        System.out.println("The following moves will be made:");
        for (File from : list) {
            System.out.println(String.format("%s ---> %s", from, moveTo.get(from)));
        }
        if (Console.confirm("Is this correct?")) {
            // move files
            for (File from : list) {
                try {
                    from.renameTo(moveTo.get(from));
                } catch (Exception e) {
                    // do nothing
                }
            }
        }
        // delete temp folder
        extract.deleteFolder(tempFolder);
    }

    /**
     * From
     * http://stackoverflow.com/questions/7768071/java-delete-a-folder-content
     *
     * @param folder
     */
    private static void deleteFolder(File folder) {
        File[] files = folder.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory()) {
                    extract.deleteFolder(f);
                } else {
                    f.delete();
                }
            }
        }
        folder.delete();
    }

    /**
     * Extract where to move a downloaded file based on its Folio information
     *
     * @param file
     * @return
     */
    private static File extractDestinationFile(File file) {
        // get folio information
        FolioInformation info = extract.extractFolioInformation(file);
        if (info == null) {
            lib.log.write("Could not extract Folio information for " + file);
            return null;
        }
        // find student/assignment folder
        File studentFolder = recognize.guess(info.lastName);
        int assignmentNumber = extractAssignmentNumber(info.fileName);
        File assignmentFolder = new File(studentFolder, Integer.toString(assignmentNumber));
        // return
        return new File(assignmentFolder, info.fileName);
    }

    /**
     * For storing the chosen assignment
     */
    private static int defaultAssignment = -1;
    
    /**
     * Extract the assignment number from something like 'Assignment3.java'
     *
     * @param fileName
     * @return
     */
    private static int extractAssignmentNumber(String fileName) {
        Pattern p = Pattern.compile("Assignment(\\d+).*\\.\\w+");
        Matcher m = p.matcher(fileName);
        if (m.find()) {
            return new Integer(m.group(1));
        } else {
            if(defaultAssignment < 0){
                defaultAssignment = Console.inputInteger("What assignment is this for?");
            }
            return defaultAssignment;
        }
    }

    /**
     * Extract information from a Folio-formatted file. Folio is the system used
     * by Georgia Southern University to manage course work.
     *
     * @param f
     * @return
     */
    private static FolioInformation extractFolioInformation(File f) {
        // 137354-170053 - Powell-Sep 12, 2013 754 PM-PowellGiaVonniAssignment.zip
        DateFormat df = new SimpleDateFormat("MMM dd, yyyy hhmm a"); //("yyyy-MM-dd HH:mm:ss Z");
        Pattern p = Pattern.compile("\\d{6}-\\d{6} - (\\w+)-(.+)-(.+)");
        Matcher m = p.matcher(f.getName());
        if (m.find()) {
            FolioInformation i = new FolioInformation();
            i.lastName = m.group(1);
            i.fileName = m.group(3);
            try {
                i.date = df.parse(m.group(2));
            } catch (ParseException e) {
                System.err.println(e);
            }
            return i;
        }
        return null;
    }
}

/**
 * Stores information extracted from a Folio file
 *
 * @author andrew
 */
class FolioInformation {

    String lastName;
    Date date;
    String fileName;
}

class CouldNotDecipherException extends Exception {
}
