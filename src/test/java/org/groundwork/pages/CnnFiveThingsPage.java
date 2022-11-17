package org.groundwork.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.groundwork.support.DateHelper.daysToRecentSundayFrom;

public class CnnFiveThingsPage extends BasePage {

    public CnnFiveThingsPage() {
        url = "https://www.cnn.com/specials/5-things";
    }

    @FindBy(xpath="//ul[contains(@class,'list-large-horizontal')]/li")
    private List<WebElement> articles;

    private WebElement articleHeadline(WebElement article) {
        return article.findElement(By.xpath(".//span[contains(@class,'headline-text')]"));
    }

    private String articleLink(WebElement article) {
        return article.findElement(By.xpath(".//div[contains(@class,'content')]" +
                                             "//a[contains(@href,'five-things')]")).getAttribute("href");
    }

    public int daysToRecentSundayFromTopArticleDate() {
        return 1+daysToRecentSundayFrom(dateOfArticle(1));
    }

    public WebElement articleHeadline(int ordinalNum) {
        return articleHeadline(articles.get(ordinalNum-1));
    }

    public String articleHeadlineText(int ordinalNum) {
        return articleHeadline(ordinalNum).getText();
    }

    public void clickArticle(int ordinalNum) {
        articleHeadline(articles.get(ordinalNum-1)).click();
    }

    public String dateOfArticle(WebElement article) {
        String link = articleLink(article);
        Matcher m = Pattern.compile(".*(\\d\\d\\d\\d/\\d\\d/\\d\\d).*").matcher(link);
        m.find();
        return m.group(1);
    }

    public LocalDate dateOfArticle(int ordinalNum) {
        DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("yyyy/MM/dd", Locale.US);
        return LocalDate.parse(dateOfArticle(articles.get(ordinalNum-1)), dateFmt);
    }

}
