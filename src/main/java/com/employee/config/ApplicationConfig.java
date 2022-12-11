package com.employee.config;

import com.employee.constants.EmployeeEvents;
import com.employee.constants.EmployeeStates;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.data.jpa.JpaPersistingStateMachineInterceptor;
import org.springframework.statemachine.data.jpa.JpaStateMachineRepository;
import org.springframework.statemachine.persist.StateMachineRuntimePersister;
import org.springframework.statemachine.service.DefaultStateMachineService;
import org.springframework.statemachine.service.StateMachineService;

@Configuration
@EntityScan(basePackages = {"com.employee.entities", "org.springframework.statemachine.data.jpa"})
@EnableJpaRepositories(basePackages = {"org.springframework.statemachine.data.jpa", "com.employee.repo"})
public class ApplicationConfig {


    @Bean
    public StateMachineRuntimePersister<EmployeeStates, EmployeeEvents, String> stateMachineRuntimePersister(
            JpaStateMachineRepository jpaStateMachineRepository) {
        return new JpaPersistingStateMachineInterceptor<>(jpaStateMachineRepository);
    }

    @Bean
    public StateMachineService<EmployeeStates, EmployeeEvents> stateMachineService(
            StateMachineFactory<EmployeeStates, EmployeeEvents> stateMachineFactory,
            StateMachineRuntimePersister<EmployeeStates, EmployeeEvents, String> stateMachineRuntimePersister) {
        return new DefaultStateMachineService<>(stateMachineFactory, stateMachineRuntimePersister);
    }

}
