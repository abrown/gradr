package app;

import task.JavaRunner;
import task.Gmail;
import task.EmailBuilder;
import task.StyleChecker;
import task.GmailMessage;
import grading.Attachment;
import grading.Homework;
import grading.Criterion;
import grading.Rubric;

/**
 *
 * @author andrew
 */
public class Application {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // main appliation loop
        while (true) {
            // menu
            String[] options = {"Choose one", "Process all"};
            String choice = (String) Console.menu(options);
            // exit code
            if (choice == null) {
                System.exit(0);
            }
            // menu code
            switch (choice) {
                case "Process all":
                    Application.processAll();
                    break;
                case "Choose one":
                    Application.chooseOne();
                    break;
            }
        }
    }

    /**
     * Processes all assignments, one by one, until complete
     */
    public static void processAll() {
        // get assignments
        GmailMessage[] messages = Gmail.getMessages();
        // loop through each
        for (GmailMessage message : messages) {
            try {
                // check if needs grading
                String[] options = {"Check " + message.toString() + "?", "Skip"};
                String choice = (String) Console.menu(options);
                if( choice == null ){
                    return;
                }
                else if (choice.equals("Skip")){
                    continue;
                }
                // grade it
                Homework assignment = Gmail.getAssignment(Gmail.inbox, message.index);
                Application.gradeAssignment(assignment);
            } catch (Exception e) {
                Console.error(e);
            }
        }
    }

    /**
     * Lists all inbox messages, allowing the user to choose which to grade
     */
    public static void chooseOne() {
        // get assignments
        GmailMessage[] messages = Gmail.getMessages();
        // choose one
        GmailMessage message = (GmailMessage) Console.menu(messages);
        if (message == null) {
            return;
        }
        try {
            Homework assignment = Gmail.getAssignment(Gmail.inbox, message.index);
            Application.gradeAssignment(assignment);
        } catch (Exception e) {
            Console.error(e);
        }
    }

    /**
     * Performs all grading tasks on the assignment
     * @param h 
     */
    public static void gradeAssignment(Homework assignment) {
        
        // save attachments
        try {
            Console.message("Saving attachments...");
            assignment.saveAttachments();
        } catch (Exception e) {
            Console.error(e);
            return;
        }
        
        // check style
        Console.message("Checking style...");
        StyleChecker c = new StyleChecker(assignment);
        c.check();
        
        // show preview
        Console.message("Previewing assignment...");
        System.out.println(assignment.toString());
        System.out.println();
        
        // run each attachment
        try {
            // inner loop
            Attachment attachment;
            while(true){
                attachment = (Attachment) Console.menu(assignment.attachments);
                if( attachment == null ) break;
                JavaRunner r = new JavaRunner(assignment, attachment);
                r.run();
            }
        } catch (Exception e) {
            Console.error(e);
        }
        
        // grade
        Console.message("Grading assignment...");
        assignment.grade();

        // create e-mail
        Console.message("Building e-mail...");
        EmailBuilder email = new EmailBuilder(assignment);
        email.save();
        email.check();

        // send e-mail
        String[] options = {"Send e-mail now"};
        String choice = (String) Console.menu(options);
        if( choice == null ){
            return;
        }
        else{
            Console.message("Sending e-mail...");
            String from = (String) Configuration.get("email.from");
            String[] to = {assignment.name};
            String title = (String) Configuration.get("homework.title");
            String message = email.getEditedString();
            String[] cc = {(String) Configuration.get("email.cc")};
            Gmail.send(from, to, title, message, cc);
        }
    }
}
