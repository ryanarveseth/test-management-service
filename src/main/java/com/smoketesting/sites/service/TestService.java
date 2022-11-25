package com.smoketesting.sites.service;

import com.smoketesting.sites.data.obj.Alert;
import com.smoketesting.sites.data.obj.Constants;
import com.smoketesting.sites.data.obj.TestCase;
import com.smoketesting.sites.data.repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TestService {

    @Autowired
    TestRepository testRepo;

    @Autowired
    SiteService siteService;

    private void validateWhoIsAndPingUrl(TestCase testCase) throws Exception {
        siteService.queryWhoIsAndPingUrl(testCase);
        Date now = new Date();
        testCase.setLastChecked(now);

        if (testCase.getStatusCode() == 404) {
            Alert alert = new Alert(now, "URL status returned a 404!");
            testCase.addAlert(alert);
        } else if (!Constants.STATUSES.contains(testCase.getStatusCode())) {
            Alert alert = new Alert(now, String.format("Current URL status code (%s) does not match valid statuses (%s)!", testCase.getStatusCode(), Constants.STATUSES));
            testCase.addAlert(alert);
        }
    }

    public TestCase createTest(TestCase testCase) throws Exception {
        validateWhoIsAndPingUrl(testCase);
        return testRepo.save(testCase);
    }

    public TestCase updateTest(TestCase testCase) throws Exception {
        validateWhoIsAndPingUrl(testCase);
        return testRepo.save(testCase);
    }

    public TestCase updateTestSkipWhoIs(TestCase testCase) throws Exception {
        return testRepo.save(testCase);
    }

    public TestCase getTestById(String id) {
        if (testRepo.findById(id).isPresent()) {
            return testRepo.findById(id).get();
        }
        return null;
    }

    public List<TestCase> getAllTests() {
        return testRepo.findAll();
    }

    public String deleteTestCase(String id) {
        try {
            testRepo.deleteById(id);
            return "Success";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed";
        }
    }
}
