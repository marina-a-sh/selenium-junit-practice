package org.groundwork.support;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import static org.groundwork.support.TestContext.getConfig;
import static org.groundwork.support.TestContext.getDriver;

public class WebDriverW3CClient {

    private String getUri() {
        if (getConfig().getEnvironment().equals("local")) {
            if (System.getProperty("os.name").toLowerCase().contains("linux")) {
                try {
                    Process p = Runtime.getRuntime().exec(new String[]{"bash", "-c", "netstat -plnt |" +
                            " grep -E 'chromedriver|geckodriver' |" +
                            " grep 'LISTEN' | grep 'tcp ' | tr -s ' ' | cut -d' ' -f4 | cut -d':' -f2"});
                    p.waitFor();
                    BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
                    return "http://localhost:" + in.readLine();
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                    return "";
                }
            } else {
                throw new Error("Request cannot be sent. " +
                        "Webdriver's port is random and its inquiry is not implemented in this environment.");
            }
        }
        return getConfig().getHub();
    }

    public String findElementViaW3C(Map<String, String> criterion) {

        // prepare a request (https://w3c.github.io/webdriver/)
        RequestSpecification request = RestAssured
                .given().filter(new RestAssuredRequestFilter())
                .baseUri(getUri())
                .header("Content-Type", "application/json")
                .body(criterion);

        // execute request
        String sessionId = ((RemoteWebDriver) getDriver()).getSessionId().toString();
        Response response = request
                .when()
                .post("/session/" + sessionId + "/element");

        // parse response
        return (String) response
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getMap("value")
                .values().iterator().next();
    }


    public String[] getElementSizeViaW3C(String elementId) {

        RequestSpecification request = RestAssured
                .given().filter(new RestAssuredRequestFilter())
                .baseUri(getUri());

        String sessionId = ((RemoteWebDriver) getDriver()).getSessionId().toString();
        Response response = request
                .when()
                .get("/session/" + sessionId + "/element/" + elementId + "/rect");

        Map<String, Object> result = response
                .then()
                .statusCode(200)
                .statusLine("HTTP/1.1 200 OK")
                .extract()
                .jsonPath()
                .getMap("value");

        return new String[]{result.get("width").toString(),result.get("height").toString()};
    }

}