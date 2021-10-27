package com.kam.springboot1.bdd;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import io.cucumber.java.Before;
import io.cucumber.spring.CucumberContextConfiguration;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class CucumberBootStrap {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    @MockBean
    RestTemplate restTemplate;

    @Autowired
    ApplicationContext applicationContext;

    @Before
    public void before() {
        log.info(">>> Before scenario!");

        assertNotNull(applicationContext);

        when(restTemplate.getForEntity("http://localhost:8081/secondProject/secondController", String.class))
                .thenReturn(ResponseEntity.ok("Dummy response from second service"));
        when(restTemplate.getForEntity("http://localhost:8082/thirdProject/thirdController", String.class))
                .thenReturn(ResponseEntity.ok("Dummy response from third service"));
    }
}
