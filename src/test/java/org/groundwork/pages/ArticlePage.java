package org.groundwork.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class ArticlePage extends BasePage {

    LocalDate articleDate;

    public  ArticlePage() {}

    public ArticlePage(LocalDate date) {
        articleDate = date;
        url = constructUrl(articleDate);
    }

    private String constructUrl(LocalDate date) {
        DateTimeFormatter dateFmt1 = DateTimeFormatter.ofPattern("yyyy/MM/dd", Locale.US);
        DateTimeFormatter dateFmt2 = DateTimeFormatter.ofPattern("MMMM-dd", Locale.US);
        return "https://www.cnn.com/" + dateFmt1.format(date) + "/us/five-things-"
                                      + dateFmt2.format(date).toLowerCase() + "-trnd/index.html";
    }

    @FindBy(id = "maincontent")
    WebElement title;

    @FindBy(xpath = "//a[contains(@href,'news-quiz')]")
    WebElement quizLink;

    public String articleTitle() {
        return title.getText();
    }

    public void goToQuiz() {
        quizLink.click();
    }
}
