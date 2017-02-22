package com.ziyuan.perspective;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
//        assertTrue( true );
        System.out.println(Integer.MAX_VALUE >> 15);
        AtomicInteger ai = new AtomicInteger(0);
        ai.incrementAndGet();
        ai.incrementAndGet();
        System.out.println(ai.get());
    }
}
