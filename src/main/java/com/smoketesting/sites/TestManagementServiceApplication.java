package com.smoketesting.sites;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@EnableMongoRepositories
public class TestManagementServiceApplication {

    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", ".\\drivers\\chromedriver.exe");
        SpringApplication.run(TestManagementServiceApplication.class, args);
    }

}
