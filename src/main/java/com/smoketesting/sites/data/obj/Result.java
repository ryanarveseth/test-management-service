package com.smoketesting.sites.data.obj;

import com.smoketesting.sites.config.Field;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Result {
    String testCaseId;
    boolean passed;
    Field field;
    String searchValue;
    Date lastRunTime;
}
