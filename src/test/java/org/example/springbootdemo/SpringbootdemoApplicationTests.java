
package org.example.springbootdemo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class EmployeeControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper om;

    private String employeeJson(String name, int age, String gender, double salary) throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put("name", name);
        body.put("age", age);
        body.put("gender", gender);
        body.put("salary", salary);
        return om.writeValueAsString(body);
    }


    private long createEmployeeAndReturnId(String name, int age, String gender, double salary) throws Exception {
        String response = mockMvc.perform(post("/employees")
                        .contentType("application/json")
                        .content(employeeJson(name, age, gender, salary)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/employees/")))
                .andExpect(jsonPath("$.id", greaterThan(0)))
                .andExpect(jsonPath("$.name", is(name)))
                .andReturn().getResponse().getContentAsString();
        Map<?, ?> map = om.readValue(response, Map.class);
        Number idNumber = (Number) map.get("id");
        return idNumber.longValue();
    }


    @Test
    void createEmployee_Should_Return_201_And_Body() throws Exception {
        mockMvc.perform(post("/employees")
                        .contentType("application/json")
                        .content(employeeJson("Lily", 20, "Female", 8000)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", greaterThan(0)))
                .andExpect(jsonPath("$.name", is("Lily")))
                .andExpect(jsonPath("$.age", is(20)))
                .andExpect(jsonPath("$.gender", is("Female")))
                .andExpect(jsonPath("$.salary", is(8000.0)));
    }



}
