### Option 2
**Run test cases<br>
by maven<br>
from IntelliJ<br>
with browsers provided by dockerized Selenoid**

1. Configure and start selenoid and selenoid-ui
    - download the latest configuration manager for Selenoid at https://aerokube.com/cm/latest/
    - download the latest browser images and copy config file to modify
      ```
      cm_linux_amd64 selenoid update --last-versions 1
      mkdir --parents [full_local_path]/selenoid_conf
      cp .aerokube/selenoid/browsers.json [full_local_path]/selenoid_conf
      ```
    - modify config file adding mapping to "hosts" to allow browser to reach localhost on the host where Jetty server serves static html file; add "volumes" to Chrome browser configuration to prevent [crash](https://github.com/markhobson/docker-maven-chrome#chrome-crashes)
      ```
      {
        "chrome": {
            "default": "109.0",
            "versions": {
                "109.0": {
                    "image": "selenoid/chrome:109.0",
                    "port": "4444",
                    "path": "/",
                    "hosts" : ["host.docker.internal:host-gateway"],
                    "volumes" : ["/dev/shm:/dev/shm"]
                }
            }
        },
        "firefox": {
        ...
      ```
      ```
      cm_linux_amd64 selenoid start --config-dir [full_local_path]/selenoid_conf --browsers-json [full_local_path]/selenoid_conf/browsers.json --vnc
      cm_linux_amd64 selenoid-ui start
      ```
    - [Change time zone](https://wiki.alpinelinux.org/wiki/Setting_the_timezone) in selenoid container with Alpine Linux to get video file with local time in filenames:
      ```
      docker exec -u root -it selenoid /bin/sh
      apk add tzdata
      # ls /usr/share/zoneinfo
      cp /usr/share/zoneinfo/America/Los_Angeles /etc/localtime
      echo "America/Los_Angeles" > /etc/timezone
      # date
      ```
2. Expected state of project configuration file selenium-junit-practice/src/test/resources/config.yml
   ```
   browser: chrome    # or firefox or any browser supported by selenoid and selenium_junit_practice 
   environment: grid
   hub: http://localhost:4444/wd/hub
   headless: false
   pageLoadTimeOut: 150 
   implicitTimeOut: 3
   explicitTimeOut: 10
   ```
3. Run tests first and then separately generate reports
    - Open IntelliJ's [Maven Tool Window](https://www.jetbrains.com/help/idea/maven-projects-tool-window.html):  View > Tool Windows > Maven
    - Click 'clean', then 'test'.  
      - Note: Above is not the best option, since in case of test failures, jetty:stop goal that is bound to the test phase in the POM file would not run.
    - Better, following command should be executed in the IntelliJ's Terminal Tool Window (Alt+F12):
      ```
      mvn clean test jetty:stop
      ```
      - Note: If there are no failures, jetty:stop will be run twice, but second time it just says "Jetty not running!"
    - To generate test reports after tests ran without rerunning them
      ```
      mvn site -DskipTests allure:report
      ```
    - Find allure report in target/site/allure-maven-plugin, right click on index.html > Open in > Browser > Chrome
    - Find surefire report by opening target/site/index.html and on the left sidebar click Project Reports > Surefire Report
    - Find junitreport by opening target/junitreport/index.html
    - Find *.mp4 videos recorded by selenoid on localhost:8080 'VIDEOS' link or in [full_local_path]/selenoid_conf/video folder