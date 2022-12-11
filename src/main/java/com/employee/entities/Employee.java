package com.employee.entities;

import com.employee.constants.EmployeeStates;
import lombok.Data;

import javax.persistence.*;

@Table
@Entity
@Data
public class Employee {

    @Id
    @GeneratedValue
    private Long employeeId;

    @Enumerated(EnumType.STRING)
    private EmployeeStates state;

    private String name;
}
