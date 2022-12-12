package com.employee.config;

import com.employee.constants.EmployeeEvents;
import com.employee.constants.EmployeeStates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.service.DefaultStateMachineService;
import org.springframework.statemachine.service.StateMachineService;

@Configuration
public class ApplicationConfig {

    @Bean
    public StateMachineService<EmployeeStates, EmployeeEvents> stateMachineService(
            StateMachineFactory<EmployeeStates, EmployeeEvents> stateMachineFactory) {
        return new DefaultStateMachineService<>(stateMachineFactory);
    }

}
