package com.employee.config;

import com.employee.constants.EmployeeEvents;
import com.employee.constants.EmployeeStates;
import com.employee.entities.Employee;
import com.employee.repo.EmployeeRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;


@Slf4j
@Configuration
@AllArgsConstructor
public class StateMachineInterceptorConfig extends StateMachineInterceptorAdapter<EmployeeStates, EmployeeEvents> {

    private EmployeeRepository employeeRepository;


    @Override
    public void postStateChange(State<EmployeeStates, EmployeeEvents> state, Message<EmployeeEvents> message, Transition<EmployeeStates, EmployeeEvents> transition, StateMachine<EmployeeStates, EmployeeEvents> stateMachine, StateMachine<EmployeeStates, EmployeeEvents> stateMachine1) {
        Employee employee = employeeRepository.findById(Long.valueOf(stateMachine1.getId())).
                                              orElseThrow(() -> new IllegalArgumentException("Employee Not Exist"));

        employee.setState(stateMachine1.getState().getId());
        employeeRepository.save(employee);
    }

    @Override
    public Exception stateMachineError(StateMachine<EmployeeStates, EmployeeEvents> stateMachine, Exception exception) {
        log.error("Error during stateMachineError", exception.getMessage());
        return super.stateMachineError(stateMachine, exception);
    }
}
