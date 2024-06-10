package com.edigest.journalApp.service;

import com.edigest.journalApp.api.response.WeatherResponse;
import com.edigest.journalApp.cache.AppCache;
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

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AppCache appCache;
    public WeatherResponse getWeather(String city){
        String finalAPI = appCache.APP_CACHE.get("weather_api").replace("CITY",city).replace("API_KEY",apiKey);
        ResponseEntity<WeatherResponse> response = restTemplate.exchange(finalAPI, HttpMethod.GET, null, WeatherResponse.class);
        return response.getBody();
    }
//    public WeatherResponse postWeather(String city){
//        String finalAPI = API.replace("CITY",city).replace("API_KEY",apiKey);

//        //Post-body
//        User user = User.builder().userName("paps").password("1234").build();
//        HttpEntity<User> httpEntity = new HttpEntity<>(user);
//
//        //Headers
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.set("key","value");
//        HttpEntity<User> httpEntity2 = new HttpEntity<>(user,httpHeaders);
//        ResponseEntity<WeatherResponse> response = restTemplate.exchange(finalAPI, HttpMethod.POST, httpEntity, WeatherResponse.class);

//        ResponseEntity<WeatherResponse> response2 = restTemplate.exchange(finalAPI, HttpMethod.POST, null, WeatherResponse.class);
//        return response2.getBody();
//    }

}
