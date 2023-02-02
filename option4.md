### Option 4
**Run test cases<br>
by maven<br>
from dockerized project<br>
with browsers spun inside the same container**

1. Build docker image to which project directory and maven local repository will be mounted during container run
   ```
   cd [full_path_to_project_directory_on_the_host]
   # comment out all the lines in the Dockerfile after "#copy source code and pom file"
   docker build -t practice-base:1.0 .
   ```
2. Expected state of project configuration file selenium-junit-practice/src/test/resources/config.yml
   ```
   browser: firefox  # or chrome
   environment: local
   headless: true       # notice headless mode
   browserWidth: 1920
   browserHeight: 1080
   pageLoadTimeOut: 150
   implicitTimeOut: 3
   explicitTimeOut: 10
   ```
3. Using image from [Option 3](option3.md) step 5
   ```
   cd [full_path_to_project_directory_on_the_host]
   docker run -it --rm --name practice -v /etc/timezone:/etc/timezone:ro -v /etc/localtime:/etc/localtime:ro -v "$PWD":/qa:ro -v "$HOME/.m2":/root/.m2 -v "$PWD/target:/qa/target" -v [full_path_to_optional_external_config]/config.yml:/qa/src/test/resources/config.yml -v /dev/shm:/dev/shm  -w /qa practice-base:1.0 mvn clean -Ppurge site allure:report
   ```