FROM maven:3.8.6-jdk-11

WORKDIR /qa
RUN chmod -R 777 /qa

RUN apt-get update -y \
  && apt-get install -y wget unzip curl net-tools

# install Chrome browser
RUN curl -fSsL https://dl.google.com/linux/linux_signing_key.pub | \
    gpg --dearmor | \
    tee /usr/share/keyrings/google-chrome.gpg > /dev/null
RUN echo deb [arch=amd64 signed-by=/usr/share/keyrings/google-chrome.gpg] http://dl.google.com/linux/chrome/deb/ stable main | \
    tee /etc/apt/sources.list.d/google-chrome.list

RUN apt-get update \
  && apt-get install -y google-chrome-stable

# install Firefox browser
RUN echo "deb http://deb.debian.org/debian/ unstable main contrib non-free" >> /etc/apt/sources.list.d/debian.list
RUN apt-get update -qqy \
  && apt-get -qqy --no-install-recommends install firefox

# install allure test report tool
RUN wget https://repo.maven.apache.org/maven2/io/qameta/allure/allure-commandline/2.19.0/allure-commandline-2.19.0.zip -O allure-commandline.zip \
  && unzip allure-commandline.zip \
  && rm -rf allure-commandline.zip
ENV PATH allure-2.19.0/bin:$PATH

#copy source code and pom file
COPY src /qa/src
COPY pom.xml /qa
COPY src/test/resources/junit-frames.xsl /qa

RUN mvn dependency:go-offline dependency:resolve-plugins dependency:resolve
RUN ["/usr/local/bin/mvn-entrypoint.sh", "mvn", "site", "clean", "--fail-never"]

ENTRYPOINT mvn clean -Ppurge site allure:report
