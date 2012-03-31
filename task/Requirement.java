/*
 * @copyright Copyright 2012 Andrew Brown. All rights reserved.
 * @license GNU/GPL.
 */
package task;

/**
 *
 * @author Andrew Brown
 */
public class Requirement {

    Class type;
    String name;
    String description;

    public Requirement(Class type, String name, String description) {
        this.type = type;
        this.name = name;
        this.description = description;
    }

    public boolean isValid(Object value) {
        if (this.type.isInstance(value)) {
            return true;
        }
        // return
        return false;
    }
}
