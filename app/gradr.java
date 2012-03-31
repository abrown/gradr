/*
 * @copyright Copyright 2012 Andrew Brown. All rights reserved.
 * @license GNU/GPL.
 */
package app;

/**
 *
 * @author Andrew Brown
 */
public class gradr {
    public static void main(String[] arguments){
        // use assertions
        boolean assertEnabled = false;
        assert assertEnabled = true;
        if( assertEnabled == false ) throw new UnsupportedOperationException("Assert must be enabled with -ea.");
    }

}
