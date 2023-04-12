package org.groundwork.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

import static org.groundwork.support.TestContext.getConfig;
import static org.groundwork.support.TestContext.getDriver;

public class BasePage {

    protected String url = "";
    protected WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(getConfig().getExplicitTimeOut()),
                                                                  Duration.ofMillis(200));

    public BasePage() {
        PageFactory.initElements(getDriver(), this);
    }

    @FindBy(xpath="//div[contains(@class,'bx-type-overlay')]")
    private List<WebElement> overlays;

    public void open() {
        getDriver().get(url);
        closeOverlays();
    }

    public void closeOverlays() {
        if (overlays.size() != 0) {
            for (WebElement overlay : overlays.stream().filter(WebElement::isDisplayed).collect(Collectors.toList())) {
                overlay.findElement(By.xpath(".//a[contains(@class,'bx-close-link')]")).click();
            }
            wait.until(driver -> overlays.stream().noneMatch(WebElement::isDisplayed));
        }
    }

    public void waitToLoadWithJS() {
        wait.until(driver -> ((JavascriptExecutor)
                              getDriver()).executeScript("return document.readyState").equals("complete"));
    }

    public String getUrl() {
        return url;
    }
}
