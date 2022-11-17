package org.groundwork.pages;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class UspsPage extends BasePage{

    public UspsPage() {
        super();
        url = "https://www.usps.com/";
    }

    @FindBy(id = "link-lang")
    WebElement langLink;

    public Dimension getLangLinkDimension() {
        return langLink.getSize();
    }

}
