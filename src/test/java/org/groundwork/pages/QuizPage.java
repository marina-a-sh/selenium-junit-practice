package org.groundwork.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class QuizPage extends BasePage {

    LocalDate quizDate;

    public QuizPage(LocalDate date) {
        quizDate = date;
        url = constructUrl(date);
    }

    private String constructUrl(LocalDate date) {
        DateTimeFormatter dateFmt1 = DateTimeFormatter.ofPattern("yyyy/MM", Locale.US);
        DateTimeFormatter dateFmt2 = DateTimeFormatter.ofPattern("MMMM-d", Locale.US);
        return "https://www.cnn.com/interactive/" + dateFmt1.format(date) + "/us/cnn-5-things-news-quiz-"
                                                  + dateFmt2.format(date).toLowerCase() + "-sec/";
    }

    @FindBy(xpath = "//h1[contains(@class,'heading-xxl')]")
    WebElement heading;

    public String getActualTitle() {
        return heading.getText();
    }

    public String getExpectedTitle() {
        DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.US);
        return "Weekly News Quiz: " + dateFmt.format(quizDate);
    }
}
