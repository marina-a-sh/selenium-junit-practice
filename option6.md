### Option 6
**Run single test case<br>
by maven<br>
from command line**

1. Open Terminal app and cd to project folder or open IntelliJ's Terminal Tool Window (Alt+F12)
   ```
   mvn clean test -Dtest=org.groundwork.tests.ElementsStatsTest#testStats jetty:stop
   mvn clean test -Dtest=org.groundwork.tests.CnnFiveThingsSmokeTest#recentSundayArticleHasThursdayQuizLink
   mvn clean test -Dtest="org.groundwork.tests.WebDriverW3CClientTest#seleniumLibTruncatesW3CelementSize[0: css locator]"
   ```
   - Note: Jetty server is started as part of 'process-test-classes' phase right before 'test' phase as described in pom.xml