package org.groundwork.tests;

import org.groundwork.pages.ExperimentsBasePage;
import org.groundwork.pages.LocalExperimentsPage;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebElement;

import static org.assertj.core.api.Assertions.assertThat;

public class ElementsStatsTest extends BaseTest {

    ExperimentsBasePage experimentsPage = new LocalExperimentsPage();

    @Before
    public void beforeTest() {
        experimentsPage.open();
    }

    @Test
    public void testStats() {
        assertThat(experimentsPage.getElementsForExperiments().size()).isGreaterThan(0);
        for (WebElement elem : experimentsPage.getElementsForExperiments()) {
            assertThat(experimentsPage.statsResults(experimentsPage.gatherStats(elem)))
                    .as(elem.getAttribute("id")).isEqualTo(experimentsPage.getExperimentsExpectations(elem));
        }
    }

    @Test
    public void failingTest() {
        assertThat(true).isFalse();
    }
}