package org.groundwork.support;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Config {

    @JsonProperty(required = true)
    private String browser;
    @JsonProperty(required = true)
    private String environment;
    @JsonProperty
    private String hub;
    @JsonProperty(required = true)
    private Boolean headless;
    @JsonProperty(value = "browserWidth") // can have different key name in config.yml
    private Integer browserWidth = -1;
    @JsonProperty(value = "browserHeight")
    private Integer browserHeight = -1;
    @JsonProperty(required = true)
    private Integer pageLoadTimeOut;
    @JsonProperty(required = true)
    private Integer implicitTimeOut;
    @JsonProperty(required = true)
    private Integer explicitTimeOut;

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public String getHub() {
        return hub;
    }

    public void setHub(String hub) {
        this.hub = hub;
    }

    public Boolean getHeadless() {
        return headless;
    }

    public void setHeadless(Boolean headless) {
        this.headless = headless;
    }

    public Integer getBrowserWidth() {
        return browserWidth;
    }

    public void setBrowserWidth(Integer browserWidth) {
        this.browserWidth = browserWidth;
    }

    public Integer getBrowserHeight() {
        return browserHeight;
    }

    public void setBrowserHeight(Integer browserHeight) {
        this.browserHeight = browserHeight;
    }

    public Integer getPageLoadTimeOut() {
        return pageLoadTimeOut;
    }

    public void setPageLoadTimeOut(Integer pageLoadTimeOut) {
        this.pageLoadTimeOut = pageLoadTimeOut;
    }

    public Integer getImplicitTimeOut() {
        return implicitTimeOut;
    }

    public void setImplicitTimeOut(Integer implicitTimeOut) {
        this.implicitTimeOut = implicitTimeOut;
    }

    public Integer getExplicitTimeOut() {
        return explicitTimeOut;
    }

    public void setExplicitTimeOut(Integer explicitTimeOut) {
        this.explicitTimeOut = explicitTimeOut;
    }

}
