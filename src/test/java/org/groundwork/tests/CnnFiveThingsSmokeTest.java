package org.groundwork.tests;

import org.groundwork.pages.ArticlePage;
import org.groundwork.pages.CnnFiveThingsPage;
import org.groundwork.pages.QuizPage;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.groundwork.support.DateHelper.*;
import static org.groundwork.support.TestContext.getDriver;

public class CnnFiveThingsSmokeTest extends BaseTest {

    CnnFiveThingsPage fiveThingsPage = new CnnFiveThingsPage();

    @Test
    public void topArticleIsPostedTodayUnlessSaturday() {
        fiveThingsPage.open();
        LocalDate articleDate = fiveThingsPage.dateOfArticle(1);
        if (todayIsSaturday()) {
            assertThat(articleDate).isEqualTo(LocalDate.now().minusDays(1));
        } else {
            assertThat(articleDate).isEqualTo(LocalDate.now());
        }
    }

    @Test
    public void recentSundayArticleIsNoSkippedDaysFromTop() {
        fiveThingsPage.open();
        int daysFromTop = fiveThingsPage.daysToRecentSundayFromTopArticleDate();
        assertThat(dayOfWeek(fiveThingsPage.dateOfArticle(daysFromTop))).isEqualTo(sunday());
        String headline = fiveThingsPage.articleHeadlineText(daysFromTop);
        fiveThingsPage.clickArticle(daysFromTop);
        assertThat((new ArticlePage()).articleTitle()).isEqualTo(headline);
    }

    @Test
    public void recentSundayArticleHasThursdayQuizLink() {
        ArticlePage articlePage = new ArticlePage(recentSunday());
        articlePage.open();
        String mainWindow = getDriver().getWindowHandle();
        articlePage.goToQuiz();
        QuizPage quizPage = new QuizPage(recentSunday().minusDays(3));
        checkQuizPage(quizPage, mainWindow);
        getDriver().switchTo().window(mainWindow);
    }

    private void checkQuizPage(QuizPage quizPage, String parent) {
        Set<String> listOfTabs = getDriver().getWindowHandles();
        boolean quizPageVisited = false;
        for (String tab : listOfTabs) {
            if (!tab.equalsIgnoreCase(parent)) {
                getDriver().switchTo().window(tab);
                quizPage.waitToLoadWithJS();
                assertThat(quizPage.checkTitleCorrectForTheDate()).isTrue();
                quizPageVisited = true;
                getDriver().close();
            }
        }
        assertThat(quizPageVisited)
              .as("Quiz page was opened in a new tab and checked for title correctness").isTrue();
    }
}
