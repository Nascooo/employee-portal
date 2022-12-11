package com.employee.dto;

import com.employee.constants.EmployeeStates;
import lombok.Data;

@Data
public class EmployeeDto {

    private Long employeeId;

    private EmployeeStates state;

    private String name;
}
