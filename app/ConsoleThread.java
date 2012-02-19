package app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author andrew
 */
public class ConsoleThread extends Thread{
    private String command;
    public ConsoleThread(String command){
        this.command = command;
    }
    public void run(){
        try {
            Process p = Runtime.getRuntime().exec(this.command);
            // setup
            BufferedReader bri = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader bre = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            // get stdout
            String line;
            while ((line = bri.readLine()) != null) {
                System.out.println(line);
            }
            bri.close();
            // get stderr
            while ((line = bre.readLine()) != null) {
                //error.add(line);
                System.err.println(line);
            }
            bre.close();
            // end process
            p.waitFor();
        } 
        catch (IOException | InterruptedException err) {
            err.printStackTrace();
        }
    }
}
