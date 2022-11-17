package org.groundwork.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class LocalExperimentsPage extends ExperimentsBasePage {

    public LocalExperimentsPage() {
        super();
        url = "file://" + System.getProperty("user.dir") + "/src/test/resources/html/elements_for_experiments.html";
    }

    @FindBy(css=".experiment")
    List<WebElement> elements_for_experiments;

    @FindBy(id="one_px_height_horizontal_text")
    WebElement onePixelHeight;

    @FindBy(id="hidden_zero_height_horizontal_text")
    WebElement hiddenZeroHeight;

    @FindBy(id="zero_height_horizontal_text")
    WebElement zeroHeightHoriz;

    @FindBy(id="zero_width_vertical_text")
    WebElement zeroHeightVert;

    @Override
    public List<WebElement> getElementsForExperiments() {
        return elements_for_experiments;
    }

    public boolean[] getExperimentsExpectations(WebElement experiment) {
        if (experiment.equals(onePixelHeight)) {
            return new boolean[]{true, true, true, true, true, true, true};
        } else if (experiment.equals(hiddenZeroHeight)) {
            return new boolean[]{false, true, false, false, false, false, false};
        } else if (experiment.equals(zeroHeightHoriz)) {
            return new boolean[]{true, true, true, true, false, false, true};
        } else if (experiment.equals(zeroHeightVert)) {
            return new boolean[]{true, true, true, true, false, true, true};
        }
        return new boolean[]{};
    }
}