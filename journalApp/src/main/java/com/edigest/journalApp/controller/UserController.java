package com.edigest.journalApp.controller;

import com.edigest.journalApp.entity.User;
import com.edigest.journalApp.service.UserService;
import com.edigest.journalApp.service.WeatherService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {
    
     private final UserService userService;
     private final WeatherService weatherService;

    @PutMapping
    public ResponseEntity<Void> updateUser(@RequestBody User user){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName =authentication.getName();
        User userInDb = userService.findByUserName(userName);
        if(!userInDb.getPassword().equals(user.getPassword())){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }else{
            userInDb.setUserName(user.getUserName());
            userInDb.setPassword(user.getPassword());
            userService.saveNewUser(userInDb);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
    }
    @DeleteMapping
    public ResponseEntity<Void> deleteUserByUsername(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        userService.deleteByUserName(authentication.getName());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<String> greeting(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return new ResponseEntity<>("Hi! "+authentication.getName()+", Current Temperature is: "+weatherService.getWeather("Mumbai").getCurrent().getTempC(),HttpStatus.OK);
    }

    @PutMapping("/profile")
    public ResponseEntity<Void> addExtraUserDetails(@RequestBody User user){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User userInDb = userService.findByUserName(userName);
        if(user.getName() == null || user.getEmail() == null || user.getPhoneNumber() == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }else{
            userInDb.setName(user.getName());
            userInDb.setEmail(user.getEmail());
            userInDb.setPhoneNumber(user.getPhoneNumber());
            userService.saveUser(userInDb);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
    }
}
