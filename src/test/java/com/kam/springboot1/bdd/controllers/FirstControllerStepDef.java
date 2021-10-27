package com.kam.springboot1.bdd.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class FirstControllerStepDef {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @MockBean
    RestTemplate restTemplate;

    @Autowired
    ApplicationContext applicationContext;

    ResponseEntity<String> response;

    public FirstControllerStepDef() {
        super();
    }

    @Given("^the application is bootstraped$")
    public void ensureApplicationIsBootStrapped()
    {
        assertNotNull(applicationContext.getBean("firstController"));
    }

    @When("^I make a Get call$")
    public void makeCallToFirstController() throws MalformedURLException {
        response = testRestTemplate.getForEntity(
                new URL("http://localhost:" + port + "/firstProject/firstController").toString(), String.class);
    }

    @Then("^I should receive the expected output$")
    public void verifyReturnFromRestCall()
    {
        String expectedResponseBody = "Hello First Service - first controller\n"
                + "Status Code - 200 OK   Body : Dummy response from second service\n"
                + "Status Code - 200 OK   Body : Dummy response from third service";

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponseBody, response.getBody());
    }
}
