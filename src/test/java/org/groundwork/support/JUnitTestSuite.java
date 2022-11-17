package org.groundwork.support;

import org.groundwork.tests.CnnFiveThingsSmokeTest;
import org.groundwork.tests.ElementsStatsTest;
import org.groundwork.tests.WebDriverW3CClientTest;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({WebDriverW3CClientTest.class, ElementsStatsTest.class, CnnFiveThingsSmokeTest.class})
public class JUnitTestSuite {

    @BeforeClass
    public static void printMe() {
        System.out.println("JUnitTestSuite is the test suite grouping all UI and API tests");
    }

}
