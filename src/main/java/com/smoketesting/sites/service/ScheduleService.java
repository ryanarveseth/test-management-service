package com.smoketesting.sites.service;

import com.smoketesting.sites.data.obj.TestCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class ScheduleService {
    static final long ONE_MINUTE = 60 * 1000;
    static final long ONE_HOUR = ONE_MINUTE * 60;
    static final long ONE_DAY = ONE_HOUR * 24;

    @Autowired
    TestService testService;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

//    @Scheduled(fixedRate = ONE_HOUR * 12)
//    public void cleanUpTests() {
//        try {
//            System.out.println("Starting cleaning tests at: " + dateFormat.format(new Date()));
//            List<TestCase> cases = testService.getAllTests();
//            cases.forEach(test -> {
//                if (test.getTestUrl() == null || test.getValue() == null || test.getName().contains("Freebates Test #")) {
//                    testService.deleteTestCase(test.getId());
//                }
//            });
//            System.out.println("Finished cleaning tests at: " + dateFormat.format(new Date()));
//        } catch (Exception e) {
//            System.out.println("There was an error running your tests.");
//        }
//    }

    @Scheduled(fixedRate = ONE_MINUTE)
    public void runAllTestsAndLogTime() {
        try {
            List<TestCase> tests = testService.getAllTests();
            tests.forEach(test -> {
                String url = test.getTestUrl();
                try {
                    SiteService.getWhoIsInfo(url);
                    Thread.sleep(2000);
                } catch (URISyntaxException | InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            });

            System.out.println("Finished all tests at: " + dateFormat.format(new Date()));
        } catch (Exception e) {
            System.out.println("There was an error running your tests.");
        }
    }
}
