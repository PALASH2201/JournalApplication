package com.edigest.journalApp.scheduler;

import com.edigest.journalApp.cache.AppCache;
import com.edigest.journalApp.entity.JournalEntry;
import com.edigest.journalApp.entity.User;
import com.edigest.journalApp.repository.UserRepositoryImpl;
import com.edigest.journalApp.service.EmailService;
import com.edigest.journalApp.enums.Sentiment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserScheduler {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepositoryImpl userRepository;

    @Autowired
    private AppCache appCache;

    @Scheduled(cron = "0 0 9 * * SUN")
    public void fetchUsersAndSendSAMail(){
        List<User> userForSA = userRepository.getUserForSA();
        for(User user: userForSA){
            List<JournalEntry> journalEntries = user.getJournalEntryList();
            List<Sentiment> collect = journalEntries.stream().filter(x -> x.getDate().isAfter(LocalDateTime.now().minusDays(7))).map(x -> x.getSentiment()).toList();
            Map<Sentiment,Integer> sentimentIntegerCounts = new HashMap<>();
            for(Sentiment sentiment : collect){
                if(sentiment != null)
                    sentimentIntegerCounts.put(sentiment,sentimentIntegerCounts.getOrDefault(sentiment,0)+1);
            }
            Sentiment mostFrequentSentiment = null;
            int maxCount = 0;
            for (Map.Entry<Sentiment,Integer> entry : sentimentIntegerCounts.entrySet()){
                if(entry.getValue() > maxCount){
                    maxCount = entry.getValue();
                    mostFrequentSentiment = entry.getKey();
                }
            }
            if (mostFrequentSentiment != null){
                emailService.sendEmail(user.getEmail(),"Sentiment for last 7 days",mostFrequentSentiment.toString());
            }
        }
    }
//    @Scheduled(cron = "0 0/10 * 1/1 * ? *")
//    public void clearAppCache(){
//        appCache.init();
//    }
}
