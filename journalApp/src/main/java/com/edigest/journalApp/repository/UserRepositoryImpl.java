package com.edigest.journalApp.repository;

import com.edigest.journalApp.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.schema.JsonSchemaObject;


import java.util.List;

public class UserRepositoryImpl {

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<User> getUserForSA(){
        Query query = new Query();
        query.addCriteria(Criteria.where("email").regex("^[A-Za-z0-9._%+1]+@[A-za-z0-9.-]+\\.[A-Z|a-z]{2,6}$"));
        query.addCriteria(Criteria.where("sentimentAnalysis").is(true));

//        Another way ---->
//        Criteria criteria = new Criteria();
//        query.addCriteria(criteria.andOperator(
//                  Criteria.where("email").exists(true),
//                  Criteria.where("sentimentAnalysis").is(true))
//        );

        List<User> users = mongoTemplate.find(query, User.class);
        return users;
    }
}
