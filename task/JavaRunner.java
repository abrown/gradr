package task;

import app.ConsoleThread;
import grading.Attachment;
import grading.Homework;
import java.nio.file.Path;

/**
 *
 * @author andrew
 */
public class JavaRunner {

    Homework homework;
    Attachment attachment;

    public JavaRunner(Homework h, Attachment a) {
        this.homework = h;
        this.attachment = a;
    }

    public void run() {
        // compile
        String command = "javac " + this.attachment.getLocalFile().getPath();
        ConsoleThread cli = new ConsoleThread(command);
        cli.run();
        // run
        Path dir = this.attachment.getLocalFile().toPath().getParent();
        String file = this.attachment.getLocalFile().toPath().getFileName().toString().replace(".java", "");
        command = "cmd.exe /c cd " + dir + " & start cmd.exe /k \"java " + file + "\"";
        ConsoleThread cli2 = new ConsoleThread(command);
        cli2.start();
        // view
        command = "notepad " + this.attachment.getLocalFile().getPath();
        ConsoleThread cli3 = new ConsoleThread(command);
        cli3.start();
    }
}