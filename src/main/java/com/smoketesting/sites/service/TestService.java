package com.smoketesting.sites.service;

import com.smoketesting.sites.config.Field;
import com.smoketesting.sites.data.obj.Result;
import com.smoketesting.sites.data.obj.TestCase;
import com.smoketesting.sites.data.repository.TestRepository;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class TestService {

    @Autowired
    TestRepository testRepo;

    public WebDriver setupDriver() {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--host-resolver-rules=MAP www.google-analytics.com 127.0.0.1");
        chromeOptions.addArguments("--no-sandbox");
        chromeOptions.addArguments("--window-size=1420,1080");
        return new ChromeDriver(chromeOptions);
    }

    public TestCase createTest(TestCase testCase) {
        testCase.setFailures(0);
        return testRepo.save(testCase);
    }

    public TestCase updateTest(TestCase testCase) {
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

    public List<Result> runTests(List<String> tests) {
        List<Result> results = new ArrayList<>();

        testRepo.findAllById(tests).forEach(test -> {
            results.add(runTestCase(test));
        });
        return results;
    }

    public List<Result> runAllTests() {
        List<Result> results = new ArrayList<>();

        testRepo.findAll().forEach(test -> {
            results.add(runTestCase(test));
        });
        return results;
    }

    public void findById(TestCase testCase, WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        By findBy = By.id(testCase.getValue());
                wait.until(ExpectedConditions.visibilityOfElementLocated(findBy));
                if (driver.findElements(findBy).size() == 0)
                    throw new AssertionError("Id: '" + testCase.getValue() + "' could not be found");
    }

    public void findByClass(TestCase testCase, WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        By findBy = By.className(testCase.getValue());
        wait.until(ExpectedConditions.visibilityOfElementLocated(findBy));
        if (driver.findElements(findBy).size() == 0)
            throw new AssertionError("ClassName: '" + testCase.getValue() + "' could not be found");
    }

    public void findByText(TestCase testCase, WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        String text = testCase.getValue().toLowerCase();
        By findBy = By.xpath("//*[text()[contains(translate(., "
                + "'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), \""
                + text + "\")]]");
        wait.until(ExpectedConditions.visibilityOfElementLocated(findBy));
        if (driver.findElements(findBy).size() == 0)
            throw new AssertionError("Text: '" + testCase.getValue() + "' could not be found");
    }

    public Result runTestCase(TestCase testCase) {
        System.out.println(
                "Running TestCase: '" + testCase.getId()
                        + "' to find '" + testCase.getField()
                        + "' with value '" + testCase.getValue().toLowerCase() + "'"
        );
        WebDriver driver = setupDriver();
        Result runResult = new Result();
        runResult.setTestCaseId(testCase.getId());
        runResult.setField(testCase.getField());
        runResult.setSearchValue(testCase.getValue());
        Date runTime;

        try {
            driver.get(testCase.getTestUrl());

            if (testCase.getField().equals(Field.ID))
                findById(testCase, driver);
            else if (testCase.getField().equals(Field.CLASS))
                findByClass(testCase, driver);
            else
                findByText(testCase, driver);
            runResult.setPassed(true);
            testCase.setPreviousResult("passed");
        } catch (Exception e) {
            e.printStackTrace();
            getScreenshot(testCase, (TakesScreenshot) driver);
            runResult.setPassed(false);
            testCase.setFailures(testCase.getFailures() + 1);
            testCase.setPreviousResult("failed");
        } finally {
            runTime = new Date();
            testCase.setLastRunTime(runTime);
            testRepo.save(testCase);
            cleanup(driver);
        }

        runResult.setLastRunTime(runTime);
        return runResult;
    }

    private void getScreenshot(TestCase testCase, TakesScreenshot driver) {
        try {
            File scrFile = driver.getScreenshotAs(OutputType.FILE);
            FileCopyUtils.copy(scrFile, new File("\\screenshots\\" + testCase.getId() +  "-screenshot.png"));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void cleanup(WebDriver driver) {
        if (Objects.nonNull(driver)) {
            driver.quit();
            driver = null;
        }
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

    /*

    // code for scrolling to an element to be clicked
    try {
                if (testCase.getField().equals(Field.ID))
                    findBy = By.id(testCase.getValue());
                else if (testCase.getField().equals(Field.CLASS))
                    findBy = By.className(testCase.getValue());
                else
                    findBy = By.xpath("//*[text()[contains(.,\"" + testCase.getValue() + "\")]]");

                JavascriptExecutor js = (JavascriptExecutor) driver;
                WebElement element = driver.findElement(findBy);
                js.executeScript("arguments[0].scrollIntoView(true);", element);
                if (driver.findElements(findBy).size() == 0) throw new Exception("Still could not find what you were searching for.");
            } catch (Exception e2) {
                e2.printStackTrace();
            }

     */
}
