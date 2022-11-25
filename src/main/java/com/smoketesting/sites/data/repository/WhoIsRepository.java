package com.smoketesting.sites.data.repository;

import com.smoketesting.sites.data.obj.TestCase;
import com.smoketesting.sites.data.obj.WhoIsData;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface WhoIsRepository extends MongoRepository<WhoIsData, String> {
    WhoIsData getByDomainIgnoreCase(String domain);
    boolean existsByDomain(String domain);
}
