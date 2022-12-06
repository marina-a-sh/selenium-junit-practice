package org.groundwork.pages;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import static org.groundwork.support.TestContext.getConfig;
import static org.groundwork.support.TestContext.getDriver;

public class BasePage {

    protected String url = "";
    protected WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(getConfig().getExplicitTimeOut()),
                                                                  Duration.ofMillis(200));

    public BasePage() {
        PageFactory.initElements(getDriver(), this);
    }

    public void open() {
        getDriver().get(url);
    }

    public void waitToLoadWithJS() {
        wait.until(driver -> ((JavascriptExecutor)
                              getDriver()).executeScript("return document.readyState").equals("complete"));
    }

    public String getUrl() {
        return url;
    }
}
