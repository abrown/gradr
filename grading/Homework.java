package grading;

import app.Configuration;
import app.Console;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Formatter;

/**
 *
 * @author andrew
 */
public class Homework implements Comparable {

    public String name;
    public String title;
    public String message;
    public Date submitted;
    public Attachment[] attachments;
    public String[] style_mistakes;
    public String assessment;
    public Rubric rubric;

    /**
     * Constructor
     * @param name
     * @param title
     * @param message
     * @param submitted
     * @param files 
     */
    public Homework(String name, String title, String message, Date submitted, Attachment[] files) {
        this.name = name;
        this.title = title;
        this.message = message;
        this.submitted = submitted;
        this.attachments = files;
    }
    
    /**
     * Grades this assignment
     */
    public void grade(){
        Rubric config = (Rubric) Configuration.get("rubric");
        try{
            this.rubric = config.clone();
            this.rubric.grade();
        }
        catch(CloneNotSupportedException e){
            Console.error(e);
            return;
        }       
    }

    /**
     * Returns the directory in which to save the homework files
     * @return 
     */
    public Path getDirectory(){
        String saveDirectory = (String) Configuration.get("savedirectory");
        return Paths.get("homework", saveDirectory, this.getCondensedName());
    }
    
    /**
     * Returns the path to the saved e-mail file
     * @return 
     */
    public Path getEmail(){
        return this.getDirectory().resolve("email.txt");
    }
//    
//    public Attachment[] getAttachments() {
//        return attachments;
//    }
//
//    public void setAttachments(Attachment[] files) {
//        this.attachments = files;
//    }

    public void saveAttachments() throws Exception {
        // get and create directories
        this.getDirectory().toFile().mkdirs();
        // save attachments
        for (Attachment a : this.attachments) {
            a.save(this.getDirectory());
            Console.message("Saved "+a.getLocalFile().getAbsolutePath());
        }
    }

//    public String getMessage() {
//        return message;
//    }
//
//    public void setMessage(String message) {
//        this.message = message;
//    }

    public String getFirstName(){
        int first_space = this.name.indexOf(" ");
        if( first_space == -1 ) return "Unknown";
        return this.name.substring(0, first_space);
    }
    
    public String getCondensedName() {
        return this.name.replaceAll("<.+>", "").replace(" ", "");
    }
    
    
//    public String getName(){
//        return this.name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public Date getSubmitted() {
//        return submitted;
//    }
//
//    public void setSubmitted(Date submitted) {
//        this.submitted = submitted;
//    }
//
//    public String getTitle() {
//        return title;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }
//    
//    public String[] getStyleMistakes() {
//        return style_mistakes;
//    }
//
//    public void setStyleMistakes(String[] style_mistakes) {
//        this.style_mistakes = style_mistakes;
//    }

    /**
     * Returns short string representation of the class
     * @return 
     */
    public String toString() {
        Formatter f = new Formatter();
        return f.format("'%s' by %s on %s", this.title, this.getCondensedName(), this.submitted).toString();
    }

    /**
     * Returns a full description of the assignment
     * @return 
     */
    public String toPreviewString() {
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
        for(int i = 0; i < this.attachments.length; i++){
            s.append( this.attachments[i] );
            if( i < this.attachments.length - 1 ) s.append(", ");
        }
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