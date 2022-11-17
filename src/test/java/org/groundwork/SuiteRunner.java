package org.groundwork;

import org.groundwork.support.JUnitTestSuite;
import org.junit.internal.TextListener;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class SuiteRunner {

    //https://www.baeldung.com/junit-tests-run-programmatically-from-java
    //https://ttddyy.github.io/generate-junit-xml-report-from-junitcore/

    public static void main(String[] args) {
        System.out.println("Running tests...");
        JUnitCore junit = new JUnitCore();
        junit.addListener(new TextListener(System.out));
        Result result = junit.run(JUnitTestSuite.class);
        resultReport(result);
    }

    public static void resultReport(Result result) {
        System.out.println("Finished. " +
            "\nTest run was successful: " + result.wasSuccessful() +
            "\nFailures: " + result.getFailureCount() + "." +
            "\nIgnored: " + result.getIgnoreCount() + "." +
            "\nTests run: " +  result.getRunCount() + "." +
            "\nTime: " + result.getRunTime() + "ms.");
        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }
    }

}