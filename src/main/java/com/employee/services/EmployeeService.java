package com.employee.services;

import com.employee.constants.EmployeeEvents;
import com.employee.constants.EmployeeStates;
import com.employee.repo.EmployeeRepository;
import lombok.AllArgsConstructor;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmployeeService {


    private StateMachineFactory<EmployeeStates, EmployeeEvents> stateMachineFactory;
    private EmployeeRepository employeeRepository;


}
