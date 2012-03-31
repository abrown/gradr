/*
 * @copyright Copyright 2012 Andrew Brown. All rights reserved.
 * @license GNU/GPL.
 */
package task;

import com.sun.mail.util.BASE64DecoderStream;
import grading.Homework;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Arrays;
import java.util.Date;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Multipart;

/**
 * Downloads files from an e-mail message and creates homework assignment
 * @author Andrew Brown
 */
public class DownloadEmail extends Task {

    public String username;
    public String password;
    public String host;
    public Integer emailIndex;
    public Path saveDirectory;

    /**
     * Downloads an e-mail from a mail server
     */
    public void run() {
        assert this.username != null : "Error: name must be set.";
        assert this.password != null : "Error: title must be set.";
        assert this.host != null : "Error: host must be set.";
        assert this.saveDirectory != null : "this.saveDirectory not set.";
        assert this.emailIndex != -1 : "this.emailIndex not set.";
        // setup
        Properties props = System.getProperties();
        props.setProperty("mail.store.protocol", "imaps");
        props.setProperty("mail.imaps.partialfetch", "false");
        //props.setProperty("mail.mime.base64.ignoreerrors", "true");
        // connect to mail server
        try {
            // connect
            Session session = Session.getDefaultInstance(props, null);
            Store store = session.getStore("imaps");
            store.connect(this.host, this.username, this.password);
            assert store.isConnected() : "Error: could not connect to " + this.host;
            // open folder
            Folder inbox = store.getFolder("Inbox");
            inbox.open(Folder.READ_ONLY);
            assert inbox.isOpen() : "Error: could not open inbox";
            // get message
            Message message = inbox.getMessage(this.emailIndex);
            assert message != null : "Error: could not access message " + this.emailIndex;
            // create homework
            Address[] addresses = message.getFrom();
            String name = addresses[0].toString();
            String title = message.getSubject();
            Date submitted = message.getSentDate();
            Homework homework = new Homework(name, title, "", submitted);
            // save attached files
            Multipart mp = (Multipart) message.getContent();
            ArrayList parts = DownloadEmail.getParts(mp);
            String msg = "";
            for (Object part : parts) {
                if (part == null) {
                    continue;
                } else if (part instanceof Attachment) {
                    Attachment a = (Attachment) part;
                    a.save(this.saveDirectory);
                } else {
                    msg += (String) part;
                }
            }
            homework.message = msg;
            // create
            Homework.save(saveDirectory.resolve("homework-data.ser").toFile(), homework);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Runs the task from the command line
     * @param args 
     */
    public static void main(String[] arguments) {
        DownloadEmail c = new DownloadEmail();
        c.bind(arguments);
        c.run();
    }

    /**
     * Parses and returns the parts of a multipart e-mail message
     * @param mp
     * @return
     * @throws Exception 
     */
    public static ArrayList<Object> getParts(Multipart mp) throws Exception {
        ArrayList<Object> contents = new ArrayList<Object>();
        // look through message parts
        for (int i = 0; i < mp.getCount(); i++) {
            BodyPart bp = mp.getBodyPart(i);
            // add parts of a multipart
            if (bp.getContentType().contains("multipart")) {
                ArrayList<Object> cs = DownloadEmail.getParts((Multipart) bp.getContent());
                contents.addAll(cs);
            } else if (bp.getContentType().contains("name=")) {
                // get file name
                String type = bp.getContentType();
                int start = type.indexOf("name=") + 5;
                String name = type.substring(start);
                // get file contents
                String content = "";
                if (bp.getContent() instanceof BASE64DecoderStream) {
                    content = DownloadEmail.decodeBase64((BASE64DecoderStream) bp.getContent());
                } else {
                    content = bp.getContent().toString();
                }
                // add file
                contents.add(new Attachment(name, content));
            } else {
                // add a message
                contents.add(bp.getContent().toString());
            }
        }
        return contents;
    }

    /**
     * Decodes base64-encoded file attachments
     * @param stream
     * @return 
     */
    public static String decodeBase64(BASE64DecoderStream stream) {
        ByteArrayOutputStream reader = new ByteArrayOutputStream(256);
        try {
            int c = 0;
            while ((c = stream.read()) != -1) {
                reader.write(c);
            }
            String out = reader.toString();
            return out;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "[ERROR DECODING FILE]";
    }
}

class Attachment {

    public String name;
    public String content;

    public Attachment(String name, String content) {
        this.name = name;
        this.content = content;
    }

    public void save(Path directory) {
        File f = directory.resolve(this.name).toFile();
        try {
            // create file
            if (f.exists()) {
                f.delete();
            }
            f.createNewFile();
            // save contents
            BufferedWriter out = new BufferedWriter(new FileWriter(f));
            out.write(this.content);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
