package org.groundwork.tests;

import org.groundwork.support.ScreenshotOnFailRule;
import org.groundwork.support.TestContext;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;

import java.time.Duration;

import static org.groundwork.support.TestContext.getConfig;
import static org.groundwork.support.TestContext.getDriver;

public class BaseTest {

    @Rule
    public ScreenshotOnFailRule screenShotOnFailRule = new ScreenshotOnFailRule();

    @BeforeClass
    public static void setUp() {
        TestContext.initialize();
        getDriver().manage().deleteAllCookies();
        getDriver().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(getConfig().getPageLoadTimeOut()));
        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(getConfig().getImplicitTimeOut()));
    }

    @AfterClass
    public static void tearDown() {
        TestContext.teardown();
    }

}
