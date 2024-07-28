package com.edigest.journalApp.service;

import com.edigest.journalApp.entity.JournalEntry;
import com.edigest.journalApp.entity.User;
import com.edigest.journalApp.repository.JournalEntryRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class JournalEntryService {

@Autowired
    private JournalEntryRepository journalEntryRepository;
@Autowired
   private UserService userService;

    @Transactional
    public void saveEntry(JournalEntry journalEntry, String userName){
        try{
            User user = userService.findByUserName(userName);
            journalEntry.setDate(LocalDateTime.now());
            JournalEntry saved = journalEntryRepository.save(journalEntry);
            user.getJournalEntryList().add(0,saved);
            userService.saveUser(user);
        }catch(Exception e){
            log.error("Exception ",e);
            throw new RuntimeException("Error in saving the entry",e);
        }
    }

    //PlatformTransactionalManager --> Interface
    //MongoTransactionalManager --> Implementation

    public void saveEntry(JournalEntry journalEntry){
        try{
            journalEntryRepository.save(journalEntry);
        }catch(Exception e){
            log.error("Exception ",e);
        }
    }

    public List<JournalEntry> getAll(){
        return journalEntryRepository.findAll();
    }

    public Optional<JournalEntry> findById(ObjectId objectId){
        return  journalEntryRepository.findById(objectId);
    }

    @Transactional
    public boolean deleteById(ObjectId objectId, String userName){
        boolean check = false;
        try {
            User user = userService.findByUserName(userName);
            check = user.getJournalEntryList().removeIf(x -> x.getId().equals(objectId));
            if(check){
                userService.saveUser(user);
                journalEntryRepository.deleteById(objectId);
            }
        }catch(Exception e){
            throw  new RuntimeException("An error occurred while deleting the entry.",e);
        }
        return check;
    }
}


//controller ---> service ---> repository
