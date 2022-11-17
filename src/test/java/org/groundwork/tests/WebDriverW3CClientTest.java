package org.groundwork.tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.groundwork.pages.UspsPage;
import org.groundwork.support.WebDriverW3CClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.openqa.selenium.Dimension;
import java.util.Arrays;
import java.util.HashMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.groundwork.support.TestContext.getConfig;
import static org.junit.Assume.assumeTrue;

@RunWith(value = Parameterized.class)
public class WebDriverW3CClientTest extends BaseTest {

    private final UspsPage uspsPage = new UspsPage();
    private final HashMap<String, String> strategyAndSelector;

    @Parameters(name = "{index}: {1}")
    public static Iterable<Object[]> inputs() {
        return Arrays.asList(new Object[][]{
                {new HashMap<String, String>(){{put("using", "css selector");put("value", "#link-lang");}},"css locator"},
                {new HashMap<String, String>(){{put("using", "xpath");put("value", "//a[@id='link-lang']");}},"xpath locator"}});
    }

    public WebDriverW3CClientTest(HashMap<String, String> strategyAndSelector, String label) {
        this.strategyAndSelector = strategyAndSelector;
    }

    @Before
    public void beforeTest() {
        String os = System.getProperty("os.name").toLowerCase();
        assumeTrue("Selenium grid or local linux environment is currently required to run this test",
                getConfig().getEnvironment().equals("grid") || os.contains("linux"));
    }

    @Test
    @DisplayName("Selenium library truncates W3C reported size")
    @Description("If W3C protocol call returns decimal value for element's width or height, Selenium library truncates it")
    public void seleniumLibTruncatesW3CelementSize() {
        uspsPage.open();
        Dimension libSize = uspsPage.getLangLinkDimension();
        WebDriverW3CClient client = new WebDriverW3CClient();
        String[] w3cSize = client.getElementSizeViaW3C(client.findElementViaW3C(strategyAndSelector));
        assumeTrue("Element's width or height reported by specific webdriver via W3C protocol " +
                        "is a decimal value, similar to values shown by DevTools' Inspector tool",
                    !w3cSize[0].equals(String.valueOf(libSize.getWidth())) ||
                       !w3cSize[1].equals(String.valueOf(libSize.getHeight())));
        assertThat((int) Math.floor(Double.parseDouble(w3cSize[0]))).isEqualTo(libSize.getWidth());
        assertThat((int) Math.floor(Double.parseDouble(w3cSize[1]))).isEqualTo(libSize.getHeight());
    }

}
