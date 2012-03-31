/*
 * @copyright Copyright 2012 Andrew Brown. All rights reserved.
 * @license GNU/GPL.
 */
package task;

/**
 * Every Taskable class must be able to run() from within a Java application
 * and run as main(...) from the CLI
 * @author andrew
 */
public interface Taskable {

    /**
     * Requirements to be fulfilled before run()
     * @return 
     */
    public Requirement[] getRequirements();

    /**
     * Every taskable class must be able to run() from within the application
     */
    public void run();

    /**
     * Returns result of run()
     * @return 
     */
    public Object getResult();
}
