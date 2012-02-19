package grading;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Provides a system for grading an assignment; the points are broken down into
 * criteria
 * @author andrew
 */
public class Rubric implements Cloneable{
    
    public ArrayList<Criterion> criteria = new ArrayList<>();
    public int additional;
    public String assessment;
    
    /**
     * Adds a criterion to test for
     * @param c 
     */
    public void add(Criterion c){
        this.criteria.add(c);
    }
    
    /**
     * Grade the assignment in question
     */
    public void grade(){
        System.out.println("Grading Rubric");
        // grade criteria
        for(Criterion c : this.criteria){
            c.grade();
        }
        // grade additional
        Scanner sc1 = new Scanner(System.in);
        System.out.print("Additional Points: ");
        String line = sc1.nextLine();
        if( line.isEmpty() ) this.additional = 0;
        else this.additional = new Integer(line);
        // append assessment
        Scanner sc2 = new Scanner(System.in);
        System.out.print("Assessment: ");
        this.assessment = sc2.nextLine();
        // extra newline
        System.out.println();
    }
    
    /**
     * Returns string representation
     * @return 
     */
    public String toString(){
        StringBuilder s = new StringBuilder();
        // criteria
        for(Criterion c : this.criteria){
            s.append("\t");
            s.append(c.toString());
            s.append("\n");
        }
        // additional
        s.append("\t");
        s.append("Additional points: ");
        s.append(this.additional);
        s.append("\n");
        // return
        return s.toString();
    }
    
    /**
     * Calculates score
     * @return 
     */
    public float getScore(){
        // sum
        int possible = 0;
        float actual = 0;
        for(int i=0; i<this.criteria.size(); i++){
            possible += this.criteria.get(i).possible_points;
            actual += this.criteria.get(i).received_points;
        }
        // calculate
        float total = (actual/possible)*100;
        // add in additional
        total = total + this.additional;
        // return
        return Math.round(total);
    }
    
    /**
     * Returns a copy of this object with no values set
     * @return 
     */
    public Rubric clone() throws CloneNotSupportedException{
        Rubric clone = (Rubric) super.clone();
        return (Rubric) super.clone();
    }
}
