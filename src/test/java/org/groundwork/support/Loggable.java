package org.groundwork.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface Loggable {

    //SLF4J (Simple Logging Facade for Java) is a simple facade or abstraction for various logging frameworks,
    //such as Java Util Logging (JUL), Logback and Log4j2. https://howtodoinjava.com/logback/setting-up-slf4j/
    default Logger getLogger() {
        return LoggerFactory.getLogger(this.getClass());
    }

}
