package com.r17a.selvlet.service;


import org.apache.log4j.PropertyConfigurator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Log4j2Test {
    public static final Logger LOGGER = LogManager.getLogger(Log4j2Test.class);

    public void test(String str){
        PropertyConfigurator.configure( "./iast_demo/servlet_demo/src/main/webapp/WEB-INF/log4j2.properties" );
        System.out.println("test for log4j2");
        LOGGER.error(str);
    }
}
