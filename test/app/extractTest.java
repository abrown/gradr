/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.app;

import app.extract;
import java.io.File;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author andrew
 */
public class extractTest {

    public extractTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of run method, of class extract.
     */
//    @Test
//    public void testRun() {
//        System.out.println("run");
//        extract instance = new extract();
//        instance.run();
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of extractZipped method, of class extract.
     */
    @Test
    public void testExtractZipped() {
        File zippedFile = null;
        try {
            zippedFile = new File(extractTest.class.getResource("ZippedAssignment.zip").toURI());
        } catch (Exception e) {
            System.err.println(e);
            fail("Could not access zipped assignment file.");
        }
        // extract
        File parentDirectory = new File(extractTest.class.getResource(".").getPath());
        extract.extractZipped(zippedFile, parentDirectory);
        // test 
        
    }
}
