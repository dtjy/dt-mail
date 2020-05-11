package com.dt.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @Author jiangyao
 * @Date 2019/6/27 9:35
 **/
@SpringBootApplication
@ComponentScan(basePackages = {"com.dt.utils"})
@EnableScheduling
@EnableJms
public class MailApplication {

    private final static Logger LOGGER = LoggerFactory.getLogger(MailApplication.class);

    public static void main(String[] args) {

        SpringApplication app = new SpringApplication(MailApplication.class);
        app.addListeners(new ApplicationPidFileWriter());
        app.run(args);

        LOGGER.info("dt-mail start Success");
    }
}
