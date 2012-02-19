package grading;

import java.util.Formatter;
import java.util.Scanner;

/**
 *
 * @author andrew
 */
public class Criterion {

    public String message;
    public int possible_points;
    public float received_points;

    /**
     * Constructor
     * @param message
     * @param possible_points
     */
    public Criterion(String message, int possible_points){
        this.message = message;
        this.possible_points = possible_points;
    }
    
    /**
     * Inputs a grade from the CLI for this criterion
     */
    public void grade(){
        float points = -99;
        while ( points == -99 ){
            System.out.print(message);
            System.out.print(" (of "+this.possible_points+" possible; "
                    + "press enter for max points): ");
            Scanner sc = new Scanner(System.in);
            String line = sc.nextLine();
            if( line.isEmpty() ) points = this.possible_points;
            else points = new Float(line);
        }
        this.received_points = (float) points;
    }

    /**
     * Returns string representation
     * @return 
     */
    public String toString(){
        Formatter f = new Formatter();
        f.format("%s (of %s possible): %.1f", this.message, 
                this.possible_points, this.received_points);
        return f.toString();
    }
    
    /**
     * Returns a string representation formatted to columns
     * @param column_width
     * @return string
     */
    public String toFormattedString(int column_width) {
        // left column
        Formatter f = new Formatter();
        f.format("%s (of %s possible): ", this.message, this.possible_points);
        String left = f.toString();
        // column
        StringBuilder whitespace = new StringBuilder();
        int end = column_width - left.length();
        for (int i = 0; i < end; i++) {
            whitespace.append(" ");
        }
        // right column
        Formatter f2 = new Formatter();
        f.format("%.1f", this.received_points);
        String right = f.toString();
        // return
        return left + whitespace + right;
    }
}
