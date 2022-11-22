package com.smoketesting.sites.controller;

import com.smoketesting.sites.data.obj.TestCase;
import com.smoketesting.sites.data.obj.WhoIsData;
import com.smoketesting.sites.service.SiteService;
import com.smoketesting.sites.service.TestService;
import com.smoketesting.sites.util.WhoIsConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.net.InetAddress;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.List;

import static com.smoketesting.sites.service.SiteService.queryWhoIsAndPingUrl;

@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:8080", "http://smoke-test.local:3001"})
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    TestService testService;

    @PostMapping("/create")
    public TestCase createTestCase(@RequestBody TestCase test) throws Exception {
        double start = System.nanoTime();
        TestCase result = testService.createTest(test);
        double totalTime = (System.nanoTime() - start) / 1_000_000_000;
        System.out.println("Total Seconds to create test case: " + totalTime);
        return result;
    }

    @PostMapping("/update")
    public TestCase updateTestCase(@RequestBody TestCase test) throws Exception {
        double start = System.nanoTime();
        TestCase result = testService.updateTest(test);
        double totalTime = (System.nanoTime() - start) / 1_000_000_000;
        System.out.println("Total Seconds to create test case: " + totalTime);
        return result;
    }

    @GetMapping("/get/{id}")
    public TestCase getTestById(@PathVariable String id) {
        double start = System.nanoTime();
        TestCase result = testService.getTestById(id);
        double totalTime = (System.nanoTime() - start) / 1_000_000_000;
        System.out.println("Total Seconds to getTestById " + id + ": " + totalTime);
        return result;
    }

    @GetMapping("/get/all")
    public List<TestCase> getAllCases() {
        double start = System.nanoTime();
        List<TestCase> result = testService.getAllTests();
        double totalTime = (System.nanoTime() - start) / 1_000_000_000;
        System.out.println("Total Time to get all cases: " + totalTime);
        return result;
    }

    @DeleteMapping("/delete/{id}")
    public String deleteCase(@PathVariable String id) {
        double start = System.nanoTime();
        String result = testService.deleteTestCase(id);
        double totalTime = (System.nanoTime() - start) / 1_000_000_000;
        System.out.println("Total Time to get all cases: " + totalTime);
        return result;
    }

    @PostMapping("/verify/whoIs")
    public TestCase getWhoIsForData(@RequestBody TestCase test) throws Exception {
        queryWhoIsAndPingUrl(test);
        return test;
    }
}


