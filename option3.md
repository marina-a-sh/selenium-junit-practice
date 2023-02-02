### Option 3
**Run test cases<br>
by maven<br>
from dockerized project<br>
with browsers provided by dockerized Selenoid**

1. Configure and start selenoid and selenoid-ui like in [Option 2](option2.md) step 1
2. Create docker_target folder so the results of test runs are available to review after container exits when its main process (ENTRYPOINT line in the Dockerfile) finishes.
3. Expected state of project configuration file selenium-junit-practice/src/test/resources/config.yml
   ```
   browser: chrome    # or firefox
   environment: grid
   hub: http://host.docker.internal:4444/wd/hub  # reaching grid from docker container through host
   headless: false
   ```
4. In Terminal from the project base directory where Dockerfile is located create docker image
   ```
   docker build -t selenium-junit-practice:1.0 . 
   docker container run --rm --network selenoid --name agent --add-host host.docker.internal:host-gateway -v /etc/timezone:/etc/timezone:ro -v /etc/localtime:/etc/localtime:ro -v [full_path]/docker_target:/qa/target selenium-junit-practice:1.0
   # optionally mount external configuration file into the place of the project's configuration file
   # -v [full_path_on_the_host]/config.yml:/qa/src/test/resources/config.yml
   ```
   - Note 1: "--network selenoid --name agent" allows browser spun by Selenoid to reach file served by Jetty's server in dockerized project container, while Selenoid is reached through direct connection from inside of this container to the local host machine where Selenoid is available on all interfaces at port 4444 by default.<br>
   - Note 2: TO DO: Url to reach file served by Jetty server is generated in the project code in TestContext class. Instead of adding new conditions how url is produced better to make it a property in config.yml file.
5. Alternatively:     
   Above Docker image build takes more than 5 minutes to finish since all dependencies and plugins are downloaded from maven central repository from the Internet. [Another approach](https://hub.docker.com/_/maven) is "Reusing the Maven local repository": instead of Docker image that includes project, browsers, dependencies and plugins, have an image based on maven:3.8.6-jdk-11 with browsers only and mount project folder and maven local (host's) repository when container runs.
   ```
   docker run -it --rm --network selenoid --name agent --add-host host.docker.internal:host-gateway -v /etc/timezone:/etc/timezone:ro -v /etc/localtime:/etc/localtime:ro -v "$PWD":/qa:ro -v "$HOME/.m2":/root/.m2 -v "$PWD/target:/qa/target" -w /qa practice-base:1.0 mvn clean -Ppurge site allure:report
   ```
   - Note 1: Command "docker build -t practice-base:1.0 ." to build image was run with all the lines in the project's Dockerfile after "#copy source code and pom file" commented out.<br>
   - Note 2: profile ['purge'](https://github.com/Alfresco/alfresco-sdk/issues/261#issuecomment-214994198) configures maven-clean-plugin to clear contents of the target directory, instead of removing it.