package com.smoketesting.sites.data.obj;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document("tests")
@Getter
@Setter
@NoArgsConstructor
public class TestCase {

    @Id
    private String id;
    @NonNull
    private String name;
    @NonNull
    private String url;
    private Date lastChecked;

    private WhoIsData whoIsData;
    private String ip;
    private int statusCode;
    private List<Alert> alerts;

    public void addAlert(Alert alert) {
        if (alerts == null) {
            this.alerts = new ArrayList<>();
        }
        this.alerts.add(alert);
    }

    public static TestCase copy(TestCase testCase) {
        TestCase test = new TestCase();

        test.setId(testCase.id);
        test.setName(testCase.name);
        test.setUrl(testCase.url);
        test.setLastChecked(testCase.lastChecked);
        test.setWhoIsData(testCase.whoIsData);
        test.setIp(testCase.ip);
        test.setStatusCode(testCase.statusCode);
        test.setAlerts(testCase.getAlerts());

        return test;
    }
}
