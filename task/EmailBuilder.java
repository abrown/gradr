package task;

import app.Configuration;
import app.Console;
import app.ConsoleThread;
import grading.Homework;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author andrew
 */
public class EmailBuilder {

    public Homework homework;
    protected String message;

    /**
     * Constructor
     * @param homework 
     */
    public EmailBuilder(Homework homework) {
        this.homework = homework;
    }

    /**
     * Returns the grading assignment as a formatted string
     * @return 
     */
    public String toString() {
        if (this.message != null) {
            return message;
        }
        // build message
        StringBuilder s = new StringBuilder();
        s.append(this.homework.getFirstName());
        s.append(",");
        s.append("\n\n");
        s.append("This e-mail records your grades for the assignment '");
        s.append((String) Configuration.get("homework.title"));
        s.append("'. Please reply back to this e-mail if you have any questions.");
        s.append("\n\n");
        s.append("Andrew");
        s.append("\n\n");
        s.append("---------------------------------------------\n");
        s.append("Name: ");
        s.append(this.homework.name);
        s.append("\n");
        s.append("Submitted: ");
        s.append(this.homework.submitted);
        s.append("\n");
        s.append("Score: ");
        s.append(this.homework.rubric.getScore());
        s.append("\n\n");
        s.append("Assessment: ");
        s.append(this.homework.rubric.assessment);
        s.append("\n\n");
        s.append("Grading Rubric: \n");
        s.append(this.homework.rubric.toString());
        s.append("\n\n");
        s.append("Formatting Issues (calculated automatically with ");
        s.append("checkstyle-5.5). These are here to show you what the ");
        s.append("Sun style checks would find in your code: ");
        s.append("\n");
//        for (int j = this.homework.style_mistakes.length - 1; j >= 0; j--){
//            s.append("\t");
//            s.append(this.homework.style_mistakes[j]);
//            s.append("\n");
//        }
        for (int j = 0; j < this.homework.style_mistakes.length; j++){
            s.append("\t");
            s.append(this.homework.style_mistakes[j]);
            s.append("\n");
        }
        s.append("\n\n");
        // save
        this.message = s.toString();
        // return
        return this.message;
    }
    
    /**
     * After editing in-file, this string returns the edited message
     * @return 
     */
    public String getEditedString(){
        try {
            BufferedReader br = new BufferedReader(new FileReader(this.homework.getEmail().toFile()));
            StringBuilder s = new StringBuilder();
            String line = "";
            while( (line = br.readLine()) != null ){
                s.append(line);
                s.append("\n");
            }
            br.close();
            return s.toString();
        }
        catch(IOException e){
            Console.error(e);
        }
        return "";
    }

    /**
     * Saves the created message to file
     */
    public void save() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(this.homework.getEmail().toFile()));
            String content = this.toString().replace("\n", System.getProperty("line.separator"));
            bw.write(content, 0, content.length());
            bw.close();
        }
        catch(IOException e){
            Console.error(e);
        }
    }

    /**
     * Opens notepad to edit the message
     */
    public void check() {
        // view
        String command = "notepad " + this.homework.getEmail();
        ConsoleThread cli3 = new ConsoleThread(command);
        cli3.start();
    }
}
