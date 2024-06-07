package com.edigest.journalApp.service;

import com.edigest.journalApp.repository.UserRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTests {
    @Autowired
    private UserRepository userRepository;
    @ParameterizedTest
    @ValueSource(strings = {
            "ealham",
            "paps",
    })
    public void testFindByUserName(String name){
        assertNotNull(userRepository.findByUserName(name));
    }

    @ParameterizedTest
    @CsvSource({
            "1,1,2",
            "2,10,12",
    })
    public void test(int a, int b, int expected){
        assertEquals(expected,a+b);
    }

    //@BeforeEach   ---> Before each test
    //@BeforeAll    ---> Before all tests
    //@AfterAll     ---> After all tests
}
