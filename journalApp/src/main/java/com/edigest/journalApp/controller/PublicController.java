package com.edigest.journalApp.controller;

import com.edigest.journalApp.entity.User;
import com.edigest.journalApp.service.UserService;
import com.edigest.journalApp.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
@Slf4j
public class PublicController {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @GetMapping("/health-check")
    public String healthCheck(){
        return "ok";
    }
    @PostMapping("/create-user")
    public void signUp(@RequestBody User user){
        userService.saveNewUser(user);
    }

//    @PostMapping("/login")
//    public ResponseEntity<?> loginUser(@RequestBody User loginRequest ){
//        boolean isAuthenticated = userService.authenticateUser(loginRequest.getUserName() , loginRequest.getPassword());
//        if(isAuthenticated){
//            return new ResponseEntity<>(HttpStatus.OK);
//        }else{
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or Password");
//        }
//    }

    @PostMapping("/login")
       public ResponseEntity<?> login(@RequestBody User user){
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword()));
            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUserName());
            String jwt = jwtUtil.generateToken(userDetails.getUsername());
            return new ResponseEntity<>(jwt,HttpStatus.OK);
        }catch (Exception e){
            log.error("Error:",e);
            return new ResponseEntity<>("Incorrect username or password",HttpStatus.BAD_REQUEST);
        }
    }

}
