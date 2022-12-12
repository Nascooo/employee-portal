package com.employee.controller;

import com.employee.constants.EmployeeEvents;
import com.employee.dto.EmployeeDto;
import com.employee.entities.Employee;
import com.employee.mappers.EmployeeMapper;
import com.employee.services.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/employee")
@AllArgsConstructor
public class EmployeeController {

    private EmployeeService employeeService;

    private EmployeeMapper employeeMapper;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public EmployeeDto createEmployee(@RequestBody EmployeeDto employeeDto) {
        Employee employee = employeeMapper.fromDtoToEntity(employeeDto);
        return employeeMapper.fromEntityToDto(employeeService.createEmployee(employee));
    }


    @PatchMapping(value = "{id}")
    public EmployeeDto updateStatus(@PathVariable("id") Long employeeId, @RequestBody String event) {
        EmployeeEvents employeeEvent = EmployeeEvents.valueOf(event);
        return employeeMapper.fromEntityToDto(employeeService.updateEmployeeStatus(employeeId , employeeEvent));
    }

    @GetMapping(value = "{id}")
    public EmployeeDto getEmployeeDetails(@PathVariable("id") Long employeeId) {
        return employeeMapper.fromEntityToDto(employeeService.getEmployeeDetails(employeeId));
    }


}
