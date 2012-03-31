/*
 * @copyright Copyright 2012 Andrew Brown. All rights reserved.
 * @license GNU/GPL.
 */
package task;

import java.util.Arrays;
import java.util.Properties;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Folder;
import javax.mail.Message;

/**
 *
 * @author Andrew Brown
 */
public class ListEmails extends Task {

    public String username;
    public String password;
    public String host;

    /**
     * Run task
     */
    public void run() {
        assert this.username != null : "Error: name must be set.";
        assert this.password != null : "Error: title must be set.";
        assert this.host != null : "Error: host must be set.";
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
            // get messages
            Message[] messages = inbox.getMessages();
            for (Message message : messages) {
                StringBuilder s = new StringBuilder();
                // index
                s.append("[");
                s.append(message.getMessageNumber());
                s.append("] '");
                // title
                s.append(message.getSubject());
                s.append("' ");
                s.append(Arrays.toString(message.getFrom()));
                s.append(" ");
                // date
                s.append(message.getSentDate());
                // print
                System.out.println(s.toString());

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Run task from CLI
     * @param arguments 
     */
    public static void main(String[] arguments) {
        ListEmails c = new ListEmails();
        c.bind(arguments);
        c.run();
    }
}
