package com.edigest.journalApp.repository;

import com.edigest.journalApp.entity.JournalEntry;
import com.edigest.journalApp.entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, ObjectId> {
      User findByUserName(String userName);


    User findByEmail(String email);

    void deleteByUserName(String userName);
}


