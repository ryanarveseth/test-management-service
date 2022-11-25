package com.smoketesting.sites.data.obj;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Setter
@Document("whois")
public class WhoIsData {
    @Id
    private String id;
    @Indexed(unique = true)
    private String domain;
    private String registrar;
    private String registeredOn;
    private String expiresOn;
    private String updatedOn;
    private String DNS;
    private String rawData;
    private Date lastChecked;
}
