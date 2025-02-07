package com.edigest.journalApp.service;

import com.edigest.journalApp.entity.JournalEntry;
import com.edigest.journalApp.entity.User;
import com.edigest.journalApp.repository.JournalEntryRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class JournalEntryService {

    private final JournalEntryRepository journalEntryRepository;
    private final UserService userService;

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

    public byte[] getPdf(String title, String content) {
        try{
            String htmlContent = "<html><head><meta charset='UTF-8'/></head><body>" +
                    "<h1><center>" + title + "</center></h1>" +
                    "<p>"+content+"</p>"+
                    "</body></html>";
            return generatePDFFromHTML(htmlContent);
        }catch(Exception e){
            e.printStackTrace();
            return new byte[0];
        }
    }

    private byte[] generatePDFFromHTML(String htmlContent) throws IOException, com.lowagie.text.DocumentException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(htmlContent);
            renderer.layout();
            renderer.createPDF(outputStream);
            return outputStream.toByteArray();
        }catch(Exception e){
            return new byte[0];
        }
    }
}


//controller ---> service ---> repository
