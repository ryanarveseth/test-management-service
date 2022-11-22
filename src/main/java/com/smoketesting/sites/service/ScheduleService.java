package com.smoketesting.sites.service;

import com.smoketesting.sites.data.obj.Alert;
import com.smoketesting.sites.data.obj.Constants;
import com.smoketesting.sites.data.obj.TestCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.smoketesting.sites.service.SiteService.queryWhoIsAndPingUrl;

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

    @Scheduled(fixedRate = ONE_DAY)
    public void runAllTestsAndLogTime() {
        try {
            List<TestCase> tests = testService.getAllTests();
            tests.forEach(test -> {
                try {
                    TestCase updatedCase = TestCase.copy(test);
                    queryWhoIsAndPingUrl(updatedCase);
                    Date now = new Date();
                    updatedCase.setLastChecked(now);

                    if (test.getIp() != null && !Objects.equals(test.getIp(), updatedCase.getIp())) {
                        Alert alert = new Alert(now, String.format("Current IP (%s) does not match previous IP (%s)!", updatedCase.getIp(), test.getIp()));
                        updatedCase.addAlert(alert);
                    }

                    if (updatedCase.getStatusCode() == 404) {
                        Alert alert = new Alert(now, "URL status returned a 404!");
                        updatedCase.addAlert(alert);
                    } else if (!Constants.STATUSES.contains(updatedCase.getStatusCode())) {
                        Alert alert = new Alert(now, String.format("Current URL status code (%s) does not match valid statuses (%s)!", updatedCase.getStatusCode(), Constants.STATUSES));
                        updatedCase.addAlert(alert);
                    }

                    testService.updateTest(updatedCase);
                    Thread.sleep(1000);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });

            System.out.println("Finished all tests at: " + dateFormat.format(new Date()));
        } catch (Exception e) {
            System.out.println("There was an error running your tests.");
            e.printStackTrace();
        }
    }
}
