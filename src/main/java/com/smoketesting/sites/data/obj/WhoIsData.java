package com.smoketesting.sites.data.obj;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WhoIsData {
    private String domain;
    private String registrar;
    private String registeredOn;
    private String expiresOn;
    private String updatedOn;
    private String DNS;
    private String rawData;
}
