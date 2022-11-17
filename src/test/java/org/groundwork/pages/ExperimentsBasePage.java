package org.groundwork.pages;

import org.groundwork.support.Loggable;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;
import static org.groundwork.support.TestContext.getDriver;

public abstract class ExperimentsBasePage extends BasePage implements Loggable {

    protected String url;
    WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(2), Duration.ofMillis(200));

    public void open() {
        getDriver().get(url);
    }

    public abstract List<WebElement> getElementsForExperiments();
    public abstract boolean[] getExperimentsExpectations(WebElement experiment);

    protected class Stat {
        boolean result;
        String description;
        public Stat(boolean result, String description) {
            this.result = result;
            this.description = description;
        }
    }

    public Stat[] gatherStats(WebElement elem) {
        Stat[] stats = new Stat[7];
        stats[0] = checkDisplayed(elem);
        stats[1] = checkEnabled(elem);
        stats[2] = checkVisibility(elem);
        stats[3] = checkClickable(elem);
        stats[4] = checkWebElementClickSuccessful(elem);
        stats[5] = checkActionsClickSuccessful(elem);
        stats[6] = checkActionsClickWithOffsetSuccessful(elem);
        logGatheredStats(elem, stats);
        return stats;
    }

    public boolean[] statsResults(Stat[] stats) {
        boolean[] results = new boolean[stats.length];
        for (int i=0; i<stats.length; i++) {
            results[i] = stats[i].result;
        }
        return results;
    }

    private void logGatheredStats(WebElement elem, Stat[] stats) {
        StringBuilder sb = new StringBuilder("\n").append(logElement(elem));
        for (Stat stat : stats) {
            sb.append("\n  ");
            sb.append(logStat(stat));
        }
        getLogger().info(sb.append("\n").toString());
    }

    private String logStat(Stat stat) {
        return String.valueOf(stat.result).toUpperCase() + ": " + stat.description;
    }

    public String logElement(WebElement elem) {
        JavascriptExecutor executor = (JavascriptExecutor) getDriver();
        String elemDescription = executor.executeScript("var items = {};" +
                "for (index = 0; index < arguments[0].attributes.length; index++) { " +
                "items[arguments[0].attributes[index].name] = arguments[0].attributes[index].value "+
                "}; " +
                "return items;", elem).toString();
        elemDescription = "Element sized " + elem.getSize().width + "x" + elem.getSize().height + " " + elemDescription;
        return String.format("%" + elemDescription.length() + "s", "").replace(' ', '-') +
                                                                                                 "\n" + elemDescription;
    }

    public Stat checkDisplayed(WebElement elem) {
        return new Stat(elem.isDisplayed(), "element is displayed");
    }

    public Stat checkEnabled(WebElement elem) {
        return new Stat(elem.isEnabled(),"element is enabled");
    }

    public Stat checkVisibility(WebElement elem) {
        try {
            wait.until(ExpectedConditions.visibilityOf(elem));
            return new Stat(true,"wait visibilityOf is successful; Visibility means that the element " +
                                       "is not only displayed but also has a height and width that is greater than 0.");
        } catch (TimeoutException e) {
            return new Stat(false, "wait visibilityOf is successful; Visibility means that " +
                           "the element is not only displayed but also has a height and width that is greater than 0.");
        }
    }

    public Stat checkClickable(WebElement elem) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(elem));
            return new Stat(true,"wait elementToBeClickable is successful");
        } catch (TimeoutException e) {
            return new Stat(false, "wait elementToBeClickable is successful");
        }
    }

    public Stat checkWebElementClickSuccessful(WebElement elem) {
        try {
            elem.click();
            wait.until(ExpectedConditions.alertIsPresent());
            getDriver().switchTo().alert().accept();
            return new Stat(true,"WebElement click is performed, alert dialog is shown");
        } catch (ElementNotInteractableException e) {
            return new Stat(false, "WebElement click is performed; exception: "
                                         + e.toString().split("\n")[0]);
        }  catch (TimeoutException e) {
            return new Stat(false, "WebElement click is performed, alert dialog is shown; " +
                                                                                         "exception: TimeoutException");
        }
    }

    public Stat checkActionsClickSuccessful(WebElement elem) {
        try {
            (new Actions(getDriver())).click(elem).build().perform();
            wait.until(ExpectedConditions.alertIsPresent());
            getDriver().switchTo().alert().accept();
            return new Stat(true,"Actions click is performed, alert dialog is shown");
        } catch (ElementNotInteractableException e) {
            return new Stat(false,"Actions click is performed; exception: "
                                                                                   + e.toString().split("\n")[0]);
        } catch (TimeoutException e) {
            return new Stat(false, "Actions click is performed, alert dialog is shown; " +
                                                                                         "exception: TimeoutException");
        }
    }

    public Stat checkActionsClickWithOffsetSuccessful(WebElement elem) {
        int xOffset = -elem.getRect().getWidth() / 2;
        int yOffset = -elem.getRect().getHeight() / 2;
        try {
            (new Actions(getDriver())).moveToElement(elem, xOffset, yOffset).click().build().perform();
            wait.until(ExpectedConditions.alertIsPresent());
            getDriver().switchTo().alert().accept();
            return new Stat(true,"Actions click after move to top left corner (offset: " +
                                                     xOffset + "," + yOffset + ") is performed, alert dialog is shown");
        } catch (ElementNotInteractableException e) {
            return new Stat(false, "Actions click after move to top left corner (offset: " +
                           xOffset + "," + yOffset + ") is performed; exception: " + e.toString().split("\n")[0]);
        } catch (TimeoutException e) {
            return new Stat(false, "Actions click after move to top left corner (offset: " +
                        xOffset + "," + yOffset + ") is performed, alert dialog is shown; exception: TimeoutException");
        }
    }
}
