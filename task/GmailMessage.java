package task;

/**
 *
 * @author andrew
 */
public class GmailMessage {
    
    public String title;
    public int index;

    public GmailMessage(String title, int index) {
        this.title = title;
        this.index = index;
    }
    
    public String toString(){
        return this.title;
    }
    
}
