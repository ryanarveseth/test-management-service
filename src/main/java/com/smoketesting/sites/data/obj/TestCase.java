package com.smoketesting.sites.data.obj;

import com.smoketesting.sites.config.Field;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document("tests")
@Getter
@Setter
public class TestCase {

    @Id
    private String id;

    @NonNull
    private String name;
    @NonNull
    private Field field;
    @NonNull
    private String value;
    @NonNull
    private String testUrl;

    private int failures;
    private String previousResult;
    private Date lastRunTime;

}
