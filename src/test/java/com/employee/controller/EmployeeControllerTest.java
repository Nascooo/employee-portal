package com.employee.controller;

import com.employee.constants.EmployeeEvents;
import com.employee.constants.EmployeeStates;
import com.employee.dto.EmployeeDto;
import com.employee.entities.Employee;
import com.employee.repo.EmployeeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.util.Assert;

import static com.employee.constants.EmployeeEvents.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
class EmployeeControllerTest {

    private static final String BASE_URL = "http://localhost:8080/api/v1/employee";
    public static final String NAME = "Ahmed";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    EmployeeRepository employeeRepository;

    @Test
    void test_create_employee() throws Exception {
        EmployeeDto dto = new EmployeeDto();
        dto.setName(NAME);

        String payload = objectMapper.writeValueAsString(dto);

        MockHttpServletRequestBuilder content = post(BASE_URL).contentType(MediaType.APPLICATION_JSON_VALUE).content(payload);

        MockHttpServletResponse httpResponse = mockMvc.perform(content).andReturn().getResponse();
        String contentAsString = httpResponse.getContentAsString();

        EmployeeDto response = objectMapper.readValue(contentAsString, EmployeeDto.class);


        assertEquals(httpResponse.getStatus(), HttpStatus.CREATED.value());
        Assert.notNull(response.getEmployeeId());
        assertEquals(NAME, response.getName());
        assertEquals(response.getState(), EmployeeStates.IN_CHECK);

    }

    @Test
    void test_update_employee_state_Finish_security_check() throws Exception {
        Employee employee = new Employee();
        employee.setName(NAME);
        employee.setState(EmployeeStates.IN_CHECK);
        Employee savedEmployee = employeeRepository.save(employee);

        MockHttpServletRequestBuilder content = patch(BASE_URL + "/" + savedEmployee.getEmployeeId()).contentType(MediaType.APPLICATION_JSON_VALUE)
                                                                                                     .content(String.valueOf(FINISH_SECURITY_CHECK));

        String contentAsString = mockMvc.perform(content).andReturn().getResponse().getContentAsString();

        EmployeeDto response = objectMapper.readValue(contentAsString, EmployeeDto.class);

        Assert.notNull(response.getEmployeeId());
        assertEquals(NAME, response.getName());
        assertEquals(response.getState(), EmployeeStates.IN_CHECK);
    }

    @Test
    void test_update_employee_state_until_active() throws Exception {
        Employee employee = new Employee();
        employee.setName(NAME);
        employee.setState(EmployeeStates.IN_CHECK);
        Employee savedEmployee = employeeRepository.save(employee);

        EmployeeEvents[] events = {FINISH_SECURITY_CHECK, COMPLETE_INITIAL_WORK_PERMIT_CHECK, FINISH_WORK_PERMIT_CHECK, ACTIVATE};
        for (int i = 0; i < events.length; i++) {
            MockHttpServletRequestBuilder content = patch(BASE_URL + "/" + savedEmployee.getEmployeeId()).contentType(MediaType.APPLICATION_JSON_VALUE)
                                                                                                         .content(String.valueOf(events[i]));

            String contentAsString = mockMvc.perform(content).andReturn().getResponse().getContentAsString();

            EmployeeDto response = objectMapper.readValue(contentAsString, EmployeeDto.class);

            Assert.notNull(response.getEmployeeId());
            assertEquals(NAME, response.getName());
            if (events[i] != ACTIVATE || events[i] != FINISH_WORK_PERMIT_CHECK) {
                assertEquals(response.getState(), EmployeeStates.IN_CHECK);
            } else if (events[i] == FINISH_WORK_PERMIT_CHECK) {
                assertEquals(response.getState(), EmployeeStates.APPROVED);
            } else if (events[i] == ACTIVATE) {
                assertEquals(response.getState(), EmployeeStates.ACTIVE);
            }

        }
    }

    @Test
    void test_get_employee_details() throws Exception {

        Employee employee = new Employee();
        employee.setName(NAME);
        employee.setState(EmployeeStates.IN_CHECK);
        Employee savedEmployee = employeeRepository.save(employee);

        MockHttpServletRequestBuilder content = get(BASE_URL + "/" + savedEmployee.getEmployeeId());

        MockHttpServletResponse httpResponse = mockMvc.perform(content).andReturn().getResponse();
        String contentAsString = httpResponse.getContentAsString();

        EmployeeDto response = objectMapper.readValue(contentAsString, EmployeeDto.class);


        assertEquals(savedEmployee.getEmployeeId(), response.getEmployeeId());
        assertEquals(NAME, response.getName());
        assertEquals(response.getState(), EmployeeStates.IN_CHECK);

    }
}