package com.edigest.journalApp.controller;

import com.edigest.journalApp.entity.JournalEntry;
import com.edigest.journalApp.entity.User;
import com.edigest.journalApp.service.JournalEntryService;
import com.edigest.journalApp.service.UserService;
import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/journal")
@AllArgsConstructor
public class JournalEntryControllerV2 {

    private final JournalEntryService journalEntryService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<JournalEntry>> getAllJournalEntriesOfUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByUserName(userName);
        List<JournalEntry> all = user.getJournalEntryList();
        if(all != null && !all.isEmpty()){
            return  new ResponseEntity<>(all,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @PostMapping
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry journalEntry){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();
            journalEntryService.saveEntry(journalEntry,userName);
            return new ResponseEntity<>(journalEntry,HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/id/{myId}")
    public ResponseEntity<JournalEntry> getJournalEntryById(@PathVariable ObjectId myId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByUserName(userName);
        List<JournalEntry> collect = user.getJournalEntryList().stream().filter(x ->x.getId().equals(myId)).toList();
        if(!collect.isEmpty()){
            Optional<JournalEntry> journalEntry =  journalEntryService.findById(myId);
            if(journalEntry.isPresent()){
                return new ResponseEntity<>(journalEntry.get(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/id/{myId}")
    public ResponseEntity<Void> deleteJournalEntryById(@PathVariable ObjectId myId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        boolean removed = journalEntryService.deleteById(myId,userName);
        if(removed){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/id/{id}")
    public ResponseEntity<Optional<JournalEntry>> updateJournalEntryById(@PathVariable ObjectId id ,@RequestBody JournalEntry myEntry){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByUserName(userName);
        List<JournalEntry> collect = user.getJournalEntryList().stream().filter(x ->x.getId().equals(id)).toList();
        if(!collect.isEmpty()){
            Optional<JournalEntry> journalEntry =  journalEntryService.findById(id);
            if(journalEntry.isPresent()){
                JournalEntry old = journalEntry.get();
                old.setTitle(!myEntry.getTitle().isEmpty() ? myEntry.getTitle() : old.getTitle());
                old.setContent(myEntry.getContent() != null && !myEntry.getContent().isEmpty()? myEntry.getContent() : old.getContent());
                journalEntryService.saveEntry(old);
                return new ResponseEntity<>(journalEntry,HttpStatus.OK);
            }
        }
        return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/generate-pdf/id/{id}")
    public ResponseEntity<byte[]> getPdfFile(@PathVariable ObjectId id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByUserName(userName);
        List<JournalEntry> collect = user.getJournalEntryList().stream().filter(x ->x.getId().equals(id)).toList();
        if(!collect.isEmpty()){
            Optional<JournalEntry> journalEntry = journalEntryService.findById(id);
            if(journalEntry.isPresent()){
                JournalEntry entry = journalEntry.get();
                String title = entry.getTitle();
                String content = entry.getContent();
                byte[] pdfBytes = journalEntryService.getPdf(title,content);
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_PDF);
                headers.setContentDispositionFormData("filename","document.pdf");
                return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
