### Option 5
**Run single test case<br>
from IntelliJ**

1. If running single test from ElementsStatsTest test class, start local [Jetty server](https://happycoding.io/tutorials/java-server/jetty-setup) manually
    - download Jetty 11.0.12 from https://www.eclipse.org/jetty/download.php
    - unpack jetty archive, jetty-home-11.0.12 folder will be created
    - setup jetty-base
      ```
      mkdir jetty-base
      cd jetty-base
      java -jar ../jetty-home-11.0.12/start.jar --add-module=server,http,deploy,annotations
      vim webapps/context.xml
      ```
    - paste, change resourceBase to full path to the html folder containing elements_for_experiments.html, save
      ```
      <?xml version="1.0"  encoding="ISO-8859-1"?>
      <!DOCTYPE Configure PUBLIC "-//Mort Bay Consulting//DTD Configure//EN" "http://jetty.eclipse.org/configure.dtd">
      <Configure class="org.eclipse.jetty.server.handler.ContextHandler">
        <Set name="contextPath">/</Set>
        <Set name="handler">
          <New class="org.eclipse.jetty.server.handler.ResourceHandler">
            <Set name="resourceBase">[full_path_to_project_folder]/src/test/resources/html</Set>
            <Set name="directoriesListed">false</Set>
          </New>
        </Set>
      </Configure>
      ```
    - start jetty
      ```
      java -jar ../jetty-home-11.0.12/start.jar jetty.http.port=8889
      ```
    - page is now available at http://localhost:8889/elements_for_experiments.html
2. Expected state of project configuration file selenium-junit-practice/src/test/resources/config.yml
   ```
   browser: chrome    # or firefox or any browser supported by selenoid and selenium_junit_practice 
   environment: local
   ...
   ```
3. Click arrow in the gutter of the test class next to the test case and choose Run
4. See results in IntelliJ's Run Tool Window
5. Ctrl+C in the terminal to stop Jetty server