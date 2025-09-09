
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


    @Test
    void getEmployeeById_Should_Return_Employee_When_Exists() throws Exception {
        long id = createEmployeeAndReturnId("Tom", 25, "Male", 9000);

        mockMvc.perform(get("/employees/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is((int) id)))
                .andExpect(jsonPath("$.name", is("Tom")))
                .andExpect(jsonPath("$.age", is(25)))
                .andExpect(jsonPath("$.gender", is("Male")))
                .andExpect(jsonPath("$.salary", is(9000.0)));
    }


    @Test
    void getEmployeeById_Should_Return_404_When_Not_Exists() throws Exception {
        mockMvc.perform(get("/employees/{id}", 9999))
                .andExpect(status().isNotFound());
    }


    @Test
    void listEmployees_Should_Filter_By_Gender_Case_Insensitive() throws Exception {
        createEmployeeAndReturnId("A", 21, "Male", 6000);
        createEmployeeAndReturnId("B", 22, "Female", 7000);
        createEmployeeAndReturnId("C", 23, "male", 8000);

        mockMvc.perform(get("/employees").param("gender", "MALE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].gender", everyItem(anyOf(is("Male"), is("male")))));
    }


    @Test
    void listEmployees_ShouldReturnAll_WhenNoFilter() throws Exception {
        createEmployeeAndReturnId("A", 21, "Male", 6000);
        createEmployeeAndReturnId("B", 22, "Female", 7000);

        mockMvc.perform(get("/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("A")))
                .andExpect(jsonPath("$[1].name", is("B")));
    }




    @Test
    void update_Employee_Should_Update_Age_And_Salary_Only() throws Exception {
        long id = createEmployeeAndReturnId("Lily", 20, "Female", 8000);

        Map<String, Object> patch = new HashMap<>();
        patch.put("age", 30);
        patch.put("salary", 12000.5);

        mockMvc.perform(put("/employees/{id}", id)
                        .contentType("application/json")
                        .content(om.writeValueAsString(patch)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is((int) id)))
                .andExpect(jsonPath("$.name", is("Lily")))
                .andExpect(jsonPath("$.gender", is("Female")))
                .andExpect(jsonPath("$.age", is(30)))
                .andExpect(jsonPath("$.salary", is(12000.5)));
    }


    @Test
    void delete_Employee_Should_Return_204_And_Then_404() throws Exception {
        long id = createEmployeeAndReturnId("Tom", 25, "Male", 9000);
        mockMvc.perform(delete("/employees/{id}", id))
                .andExpect(status().isNoContent());
        mockMvc.perform(get("/employees/{id}", id))
                .andExpect(status().isNotFound());
    }


}
