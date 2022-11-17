package org.groundwork.support;

import org.junit.AssumptionViolatedException;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static org.groundwork.support.TestContext.getDriver;

public class ScreenshotOnFailRule implements TestRule, Loggable {

    @Override
    public Statement apply(final Statement base, final Description description) {
        return new Statement() {

            @Override
            public void evaluate() throws Throwable {
                try {
                    base.evaluate();
                } catch (AssumptionViolatedException e) {
                    throw e;
                } catch (Throwable t) {
                    // exception will be thrown only when a test fails
                    captureScreenshot();
                    // rethrow to allow the failure to be reported by JUnit
                    throw t;
                }
            }

            public void captureScreenshot() {
                TakesScreenshot screenshotTaker = (TakesScreenshot) getDriver();
                File screenshot = screenshotTaker.getScreenshotAs(OutputType.FILE);
                try {
                    File dir = new File(System.getProperty("user.dir") + File.separator + "target" +
                                         File.separator + "artifacts_" +
                                         DateTimeFormatter.ofPattern("yyyy_MM_dd").format(ZonedDateTime.now()));
                    boolean directoryCreated = dir.isDirectory() || dir.mkdir();
                    if (directoryCreated) {
                        File dest = new File(dir + File.separator + description.getMethodName() + "-" +
                                  DateTimeFormatter.ofPattern("HH:mm:ss").format(ZonedDateTime.now()) + ".png");
                        Files.move(screenshot.toPath(), dest.toPath());
                        getLogger().info("Saved screenshot to " + dest.getAbsolutePath());
                    } else {
                        getLogger().error("Directory for screenshots was not created: " + dir.getAbsolutePath());
                    }
                } catch (IOException e) {
                    getLogger().error("Screenshot failed after failure in: " + description.getClassName() + "." +
                                                                          description.getMethodName());
                    e.printStackTrace();
                }
            }
        };
    }
}