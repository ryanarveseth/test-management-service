package com.smoketesting.sites.controller;

import com.smoketesting.sites.data.obj.Result;
import com.smoketesting.sites.data.obj.TestCase;
import com.smoketesting.sites.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:8080", "http://smoke-test.local:3001"})
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    TestService testService;

    @PostMapping("/create")
    public TestCase createTestCase(@RequestBody TestCase test) {
        double start = System.nanoTime();
        TestCase result = testService.createTest(test);
        double totalTime = (System.nanoTime() - start) / 1_000_000_000;
        System.out.println("Total Seconds to create test case: " + totalTime);
        return result;
    }

    @PostMapping("/update")
    public TestCase updateTestCase(@RequestBody TestCase test) {
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

    @PostMapping("/run")
    public List<Result> submitTests(@RequestBody List<String> tests) {
        double start = System.nanoTime();
        List<Result> result = testService.runTests(tests);
        double totalTime = (System.nanoTime() - start) / 1_000_000_000;
        System.out.println("Total Seconds to run specified tests " + tests + ": " + totalTime);
        return result;
    }

    @GetMapping("/run/{id}")
    public List<Result> submitTests(@PathVariable String id) {
        double start = System.nanoTime();
        List<Result> result = testService.runTests(Collections.singletonList(id));
        double totalTime = (System.nanoTime() - start) / 1_000_000_000;
        System.out.println("Total Time to run test id " + id + ": " + totalTime);
        return result;
    }

    @GetMapping("/run/all")
    public List<Result> submitTests() {
        double start = System.nanoTime();
        List<Result> result = testService.runAllTests();
        double totalTime = (System.nanoTime() - start) / 1_000_000_000;
        System.out.println("Total Time to run all tests: " + totalTime);
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

//    @GetMapping("/ping/{id}")
//    public List<TestCase> pingUrlForId(@PathVariable  String id) {
//        double start = System.nanoTime();
//        result = testService.getAllTests();
//        double totalTime = (System.nanoTime() - start) / 1_000_000_000;
//        System.out.println("Total Time to get all cases: " + totalTime);
//        return result;
//    }

}


