package task;

import app.Configuration;
import app.Console;
import grading.Attachment;
import grading.Homework;
import com.sun.mail.util.BASE64DecoderStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Formatter;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author andrew
 */
public class Gmail {

    /**
     * Stores the open inbox connection
     */
    static public Folder inbox;

    /**
     * Downloads message headers from inbox
     * @return 
     */
    static public GmailMessage[] getMessages() {
        // setup
        Console.message("Loading assignments from Gmail...");
        ArrayList<GmailMessage> assignments = new ArrayList<>();
        Properties props = System.getProperties();
        props.setProperty("mail.store.protocol", "imaps");
        props.setProperty("mail.imaps.partialfetch", "false");
        //props.setProperty("mail.mime.base64.ignoreerrors", "true");
        // connect to gmail
        try {
            // connect
            Session session = Session.getDefaultInstance(props, null);
            Store store = session.getStore("imaps");
            store.connect("imap.gmail.com", (String) Configuration.get("email.username"), (String) Configuration.get("email.password"));
            Console.message("Connected to IMAP Server");
            // open folder
            Gmail.inbox = store.getFolder("Inbox");
            Gmail.inbox.open(Folder.READ_ONLY);
            if (!Gmail.inbox.isOpen()) {
                throw new Exception("Could not open inbox.");
            }
            // get messages
            System.out.print("Downloading messages");
            Message[] messages = Gmail.inbox.getMessages();
            for (Message message : messages) {
                System.out.print(".");
                // get name
                Address[] addresses = message.getFrom();
                String name = addresses[0].toString();
                // get title
                String subject = message.getSubject();
                // get submitted
                Date submitted = message.getSentDate();
                // get index
                int index = message.getMessageNumber();
                // create string
                Formatter f = new Formatter();
                String title = f.format("'%s' by %s on %s", subject, name, submitted).toString();
                // save
                assignments.add(new GmailMessage(title, index));
            }
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (MessagingException e) {
            e.printStackTrace();
            System.exit(2);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(2);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(2);
        }
        Console.message(".");
        // return
        return assignments.toArray(new GmailMessage[1]);
    }

    /**
     * Returns a fully parsed Homework assignment from an open e-mail inbox
     * @param inbox
     * @param index
     * @return
     * @throws Exception 
     */
    public static Homework getAssignment(Folder inbox, int index) throws Exception {
        Message message = inbox.getMessage(index);
        // get name
        Address[] addresses = message.getFrom();
        String name = addresses[0].toString();
        // get title
        String title = message.getSubject();
        // get submitted
        Date submitted = message.getSentDate();
        // get files and message
        Multipart mp = (Multipart) message.getContent();
        ArrayList parts = Gmail.getParts(mp);
        ArrayList<Attachment> files = new ArrayList<>();
        String msg = "";
        for (Object part : parts) {
            if (part == null) {
                continue;
            } else if (part instanceof Attachment) {
                files.add((Attachment) part);
            } else {
                msg += (String) part;
            }
        }
        // create
        return new Homework(name, title, msg, submitted, files.toArray(new Attachment[files.size()]));
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
                ArrayList<Object> cs = Gmail.getParts((Multipart) bp.getContent());
                contents.addAll(cs);
            } else if (bp.getContentType().contains("name=")) {
                // get file name
                String type = bp.getContentType();
                int start = type.indexOf("name=") + 5;
                String name = type.substring(start);
                // get file contents
                String file = "";
                if (bp.getContent() instanceof BASE64DecoderStream) {
                    file = Gmail.decodeBase64((BASE64DecoderStream) bp.getContent());
                } else {
                    file = bp.getContent().toString();
                }
                // add file
                contents.add(new Attachment(name, file));
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

    /**
     * Sends an e-mail
     * @param from
     * @param to
     * @param subject
     * @param text
     * @param cc
     * @return 
     */
    public static boolean send(String from, String to[], String subject, String text, String[] cc) {
        // setup
        Properties props = System.getProperties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        try {
            // connect
            Session session = Session.getInstance(props);
            // create message
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            InternetAddress[] addresses = new InternetAddress[to.length];
            for (int i = 0; i < to.length; i++) {
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(to[i]));
                addresses[i] = new InternetAddress(to[i]);
            }
            for (String c : cc) {
                message.addRecipient(Message.RecipientType.CC, new InternetAddress(c));
            }
            message.setSubject(subject);
            message.setText(text);
            message.setHeader("Content-Transfer-Encoding", "7bit");
            // send
            Console.message("Sending message to " + Arrays.toString(message.getAllRecipients()));
            Transport transport = session.getTransport("smtps");
            transport.connect("smtp.gmail.com", 465, (String) Configuration.get("email.username"), (String) Configuration.get("email.password"));
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch (MessagingException e) {
            Console.error(e);
            return false;
        }
        return true;
    }
}
