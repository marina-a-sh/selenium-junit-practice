### Option 7
**Run test suite<br>
without maven<br>
from jar package with tests<br>
on the command line<br>
with static html file served by manually started Jetty server**

1. In the [Maven tool window](https://www.jetbrains.com/help/idea/work-with-tests-in-maven.html) (View > Tool windows > maven) under the Lifecycle section right click package > Modify Run Configuration, provide Name and set 'Run' line to
   ```
   clean package -DskipTests=true -f pom.xml
   ```
   then in Run Configurations section double-click the created configuration  
   **OR** on the command line run:
   ```
   mvn clean package -DskipTests=true
   ```
   - Note: skipping test runs because if there are failures 'package' phase is not run

2. Start local Jetty server manually, see how to do it in [Option 5](option5.md) step 1

3. In target folder there are 3 jar files created:
    - selenium-junit-practice-1.0.jar
        - by jar:jar goal bound by default to 'package' phase
      ```
      Hello World!
      ```
      This jar includes application classes only. The only application class this project provides is placeholder App class which is packaged and executed in this jar.
    - selenium-junit-practice-1.0-tests.jar
        - by maven-jar-plugin
      ```
      Error: Unable to initialize main class org.groundwork.SuiteRunner
      Caused by: java.lang.NoClassDefFoundError: org/junit/runner/notification/RunListener
      ```
      This jar includes test classes only. Since dependencies are not included, it can be used only as a dependency to some other project.
    - selenium-junit-practice-1.0-fat-tests.jar
        - by maven-assembly-plugin using configuration in src/main/assembly/assembly.xml
          Run this jar with this command:
      ```
      java -jar selenium-junit-practice-1.0-fat-tests.jar
      ```
      Result of the tests are printed to the console
      ```
      Running tests...
      JUnitTestSuite is the test suite grouping all UI and API tests
      ...
      FAILURES!!!
      Tests run: 7,  Failures: 1
      
      Finished.
      Test run was successful: false
      Failures: 1.
      Ignored: 0.
      Tests run: 7.
      Time: 100439ms.
      failingTest(org.groundwork.tests.ElementsStatsTest):
      Expecting value to be false but was true expected:<[fals]e> but was:<[tru]e>
      ```
      and in the directory from which jar file is executed 'target' folder is still created:
      ```
      # tree target
      target/
      ├── artifacts
      │        └── org.groundwork.tests.ElementsStatsTest.failingTest.png
      └── webdriver.log
      ```
      Warning: Since executable jars can create local files and folders, it is preferable to run them in isolated environments.