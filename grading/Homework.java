package grading;

import app.Configuration;
import lib.Console;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.Formatter;

/**
 *
 * @author andrew
 */
public class Homework implements Comparable, Serializable {

    private static final long serialVersionUID = -5294572414935844547L;
    public String name;
    public String title;
    public Date submitted;
    public String message;
    public Path directory;
    public Path[] files;
    public Attachment[] attachments;
    public String[] style_mistakes;
    public String assessment;
    public Rubric rubric;

    /**
     * Constructor
     */
    public Homework() {
    }

    /**
     * Constructor; uses path string to find file attachments and serialized
     * homework data in a directory; used by CLI to instantiate homework
     * @param path 
     */
    public Homework(String homeworkDirectory) {
        // setup
        Path _homeworkDirectory = Paths.get(homeworkDirectory);
        assert _homeworkDirectory.toFile().exists() : "Error: could not find homework directory " + homeworkDirectory;
        Path serializedDataFile = _homeworkDirectory.resolve("homework-data.ser");
        assert _homeworkDirectory.toFile().exists() : "Error: could not find serialized file homework-data.ser";
        // unserialize
        try {
            Homework copy = Homework.load(serializedDataFile.toFile());
            // load this
            this.name = copy.name;
            this.title = copy.title;
            this.submitted = copy.submitted;
            this.message = copy.message;
            this.directory = copy.directory;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Constructor
     * @param name
     * @param title
     * @param message
     * @param submitted
     * @param files 
     */
    public Homework(String name, String title, String message, Date submitted) {
        this.name = name;
        this.title = title;
        this.message = message;
        this.submitted = submitted;
    }

    /**
     * Saves the homework data to file
     * @param file
     * @param net
     * @throws java.io.IOException
     * @throws java.io.FileNotFoundException 
     */
    public static void save(File file, Homework homework) throws java.io.IOException, java.io.FileNotFoundException {
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
        out.writeObject(homework);
        out.flush();
        out.close();
    }

    /**
     * Loads the homework data from file
     * @param file
     * @throws java.io.IOException
     * @throws java.io.FileNotFoundException
     * @throws ClassNotFoundException 
     */
    public static Homework load(File file) throws java.io.IOException, java.io.FileNotFoundException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
        Homework out = (Homework) in.readObject();
        out.directory = file.toPath().getParent();
        return out;
    }

    /**
     * Grades this assignment
     */
    public void grade() {
        Rubric config = (Rubric) Configuration.get("rubric");
        try {
            this.rubric = config.clone();
            this.rubric.grade();
        } catch (CloneNotSupportedException e) {
            Console.error(e);
            return;
        }
    }

    /**
     * Returns the directory in which to save the homework files
     * @return 
     */
    public Path getDirectory() {
        return this.directory;
    }

    /**
     * Returns the files in the homework directory
     * @return 
     */
    public Path[] getFiles() {
        ArrayList<Path> files = new ArrayList<Path>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(this.getDirectory())) {
            for (Path file : stream) {
                if (file.toFile().isDirectory()) {
                    // do nothing
                } else {
                    files.add(file);
                }
            }
            // return
            return files.toArray(new Path[0]);
        } catch (DirectoryIteratorException | IOException ex) {
            ex.printStackTrace();
        }
        // return
        assert false : "Error: should not reach this.";
        return null;
    }

    /**
     * Returns the path to the saved e-mail file
     * @return 
     */
    public Path getEmail() {
        return this.getDirectory().resolve("email.txt");
    }

    public void saveAttachments() throws Exception {
        // get and create directories
        this.getDirectory().toFile().mkdirs();
        // save attachments
        for (Attachment a : this.attachments) {
            a.save(this.getDirectory());
            Console.message("Saved " + a.getLocalFile().getAbsolutePath());
        }
    }

    public String getFirstName() {
        int first_space = this.name.indexOf(" ");
        if (first_space == -1) {
            return "Unknown";
        }
        return this.name.substring(0, first_space);
    }

    public String getCondensedName() {
        return this.name.replaceAll("<.+>", "").replace(" ", "");
    }

    /**
     * Returns short string representation of the class
     * @return 
     */
//    public String toString() {
//        Formatter f = new Formatter();
//        return f.format("'%s' by %s on %s", this.title, this.getCondensedName(), this.submitted).toString();
//    }
    /**
     * Returns a full description of the assignment
     * @return 
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("Author: ");
        s.append(this.getCondensedName());
        s.append("\n");
        s.append("Title: ");
        s.append(this.title);
        s.append("\n");
        s.append("Submitted: ");
        s.append(this.submitted);
        s.append("\n");
        s.append("Message: ");
        s.append(this.message);
        s.append("\n");
        s.append("Files: ");
        Path[] files = this.getFiles();
        if (files.length > 0) {
            s.append(files[0].getFileName());
            if (files.length > 1) {
                s.append(", ");
                for (int i = 1; i < files.length; i++) {
                    s.append(files[i].getFileName());
                    s.append(", "); 
                }
            }
        }
        // return
        return s.toString();
    }

    /**
     * Makes homework sortable by date submitted
     * @param other
     * @return 
     */
    public int compareTo(Object other) {
        Homework _other = null;
        if (other instanceof Homework) {
            _other = (Homework) other;
        } else {
            throw new ClassCastException("Cannot cast to Homework");
        }
        return this.submitted.compareTo(_other.submitted);
    }
}