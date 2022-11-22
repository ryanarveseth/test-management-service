package com.smoketesting.sites.data.repository;

import com.smoketesting.sites.data.obj.TestCase;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TestRepository extends MongoRepository<TestCase, String> {
}
