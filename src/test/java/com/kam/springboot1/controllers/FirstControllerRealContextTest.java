package com.kam.springboot1.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.net.URL;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;


//@SpringBootTest creates real application context.

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class FirstControllerRealContextTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @MockBean
    RestTemplate restTemplate;


    @Autowired
    ApplicationContext applicationContext;


    @BeforeEach
    public void setup()
    {
        when(restTemplate.getForEntity("http://localhost:8081/secondProject/secondController", String.class))
                .thenReturn(ResponseEntity.ok("Dummy response from second service"));
        when(restTemplate.getForEntity("http://localhost:8082/thirdProject/thirdController", String.class))
                .thenReturn(ResponseEntity.ok("Dummy response from third service"));
    }

    @Test
    void testApplicationContext() {

        assertNotNull(applicationContext);
        assertNotNull(applicationContext.getBean("firstController"));
    }

    @Test
    void getFirstControllerTest() throws Exception
    {
        ResponseEntity<String> response = testRestTemplate.getForEntity(
                new URL("http://localhost:" + port + "/firstProject/firstController").toString(), String.class);

        String expectedResponseBody = "Hello First Service - first controller\n"
                + "Status Code - 200 OK   Body : Dummy response from second service\n"
                + "Status Code - 200 OK   Body : Dummy response from third service";

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponseBody, response.getBody());

    }
}
