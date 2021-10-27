package com.kam.springboot1.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import javax.servlet.ServletContext;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import com.kam.springboot1.Springboot1Application;


//This uses mock mvc context.

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = { Springboot1Application.class })
class FirstControllerITTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @MockBean
    RestTemplate restTemplate;


    @BeforeEach
    public void setup()
    {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        when(restTemplate.getForEntity("http://localhost:8081/secondProject/secondController", String.class))
                .thenReturn(ResponseEntity.ok("Dummy response from second service"));
        when(restTemplate.getForEntity("http://localhost:8082/thirdProject/thirdController", String.class))
                .thenReturn(ResponseEntity.ok("Dummy response from third service"));
    }

    @Test
    void testApplicationContext() {
        ServletContext servletContext = webApplicationContext.getServletContext();
        assertNotNull(servletContext);
        assertTrue(servletContext instanceof MockServletContext);
        assertNotNull(webApplicationContext.getBean("firstController"));
    }

    @Test
    void getFirstControllerTest() throws Exception
    {
        MvcResult result = mockMvc.perform(get("/firstController")).andDo(print()).andReturn();
        String expectedResponseBody = "Hello First Service - first controller\n"
                + "Status Code - 200 OK   Body : Dummy response from second service\n"
                + "Status Code - 200 OK   Body : Dummy response from third service";
        assertEquals(result.getResponse().getStatus(), HttpStatus.OK.value());
        assertEquals(expectedResponseBody, result.getResponse().getContentAsString());

    }
}
