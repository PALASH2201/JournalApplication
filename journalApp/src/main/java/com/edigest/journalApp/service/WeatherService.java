package com.edigest.journalApp.service;

import com.edigest.journalApp.api.response.WeatherResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {
    @Value("${external.api.key}")
    private String apiKey;
    private static final String API = "http://api.weatherapi.com/v1/current.json?key=API_KEY&q=CITY";

    @Autowired
    private RestTemplate restTemplate;
    public WeatherResponse getWeather(String city){
        String finalAPI = API.replace("CITY",city).replace("API_KEY",apiKey);
        ResponseEntity<WeatherResponse> response = restTemplate.exchange(finalAPI, HttpMethod.GET, null, WeatherResponse.class);
        return response.getBody();
    }
    public WeatherResponse postWeather(String city){
        String finalAPI = API.replace("CITY",city).replace("API_KEY",apiKey);

//        //Post-body
//        User user = User.builder().userName("paps").password("1234").build();
//        HttpEntity<User> httpEntity = new HttpEntity<>(user);
//
//        //Headers
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.set("key","value");
//        HttpEntity<User> httpEntity2 = new HttpEntity<>(user,httpHeaders);
//        ResponseEntity<WeatherResponse> response = restTemplate.exchange(finalAPI, HttpMethod.POST, httpEntity, WeatherResponse.class);

        ResponseEntity<WeatherResponse> response2 = restTemplate.exchange(finalAPI, HttpMethod.GET, null, WeatherResponse.class);
        return response2.getBody();
    }

}
