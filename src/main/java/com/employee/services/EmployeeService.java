package com.employee.services;

import com.employee.config.StateMachineInterceptorConfig;
import com.employee.constants.EmployeeEvents;
import com.employee.constants.EmployeeStates;
import com.employee.entities.Employee;
import com.employee.repo.EmployeeRepository;
import lombok.AllArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.service.StateMachineService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Service
@AllArgsConstructor
public class EmployeeService {


    public static final String EMPLOYEE_HEADER = "employee_header";

    private StateMachineService<EmployeeStates, EmployeeEvents> stateMachineService;

    private EmployeeRepository employeeRepository;

    private StateMachineInterceptorConfig stateMachineInterceptorConfig;


    @Transactional
    public Employee createEmployee(Employee employee) {
        Assert.isNull(employee.getEmployeeId(), "ID Should Be Empty");

        Employee savedEmployee = employeeRepository.save(employee);
        executeEvent(savedEmployee.getEmployeeId(), EmployeeEvents.BEGIN_CHECK);
        return savedEmployee;
    }

    @Transactional
    public Employee updateEmployeeStatus(Long employeeId, EmployeeEvents event) {
        Employee savedEmployee = employeeRepository.findById(employeeId).orElseThrow(() -> new IllegalArgumentException("Employee Not Exist"));

        executeEvent(employeeId, event);
        return employeeRepository.getById(savedEmployee.getEmployeeId());
    }

    @Transactional(readOnly = true)
    public Employee getEmployeeDetails(Long employeeId) {
        return employeeRepository.findById(employeeId).orElseThrow(() -> new IllegalArgumentException("Employee Not Exist"));
    }

    private void executeEvent(Long employeeId, EmployeeEvents event) {
        Employee employee = employeeRepository.getOne(employeeId);

        StateMachine<EmployeeStates, EmployeeEvents> sm = stateMachineService.acquireStateMachine(employee.getEmployeeId().toString());

        Message<EmployeeEvents> msg = MessageBuilder.withPayload(event).setHeader(EMPLOYEE_HEADER, employeeId).build();

        sm.getStateMachineAccessor().doWithAllRegions(sma -> sma.addStateMachineInterceptor(stateMachineInterceptorConfig));

        sm.sendEvent(msg);
    }
}
