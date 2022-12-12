package com.employee.config;

import com.employee.constants.EmployeeEvents;
import com.employee.constants.EmployeeStates;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

@Slf4j
@Configuration
@EnableStateMachineFactory
public class StateMachineConfig extends EnumStateMachineConfigurerAdapter<EmployeeStates, EmployeeEvents> {

    @Bean
    public StateMachineListener<EmployeeStates, EmployeeEvents> listener() {
        return new StateMachineListenerAdapter<>() {
            @Override
            public void stateChanged(State<EmployeeStates, EmployeeEvents> from, State<EmployeeStates, EmployeeEvents> to) {
                EmployeeStates fromState = from != null ? from.getId() : null;
                log.info(String.format("stateChanged(from: %s, to: %s)", fromState + "", to.getId() + ""));
            }
        };
    }

    @Override
    public void configure(StateMachineStateConfigurer<EmployeeStates, EmployeeEvents> states) throws Exception {
        states
                .withStates()
                .initial(EmployeeStates.ADDED)
                .fork(EmployeeStates.IN_CHECK)
                .join(EmployeeStates.JOIN)
                .state(EmployeeStates.APPROVED)
                .end(EmployeeStates.ACTIVE)
                .and()
                .withStates()
                .parent(EmployeeStates.IN_CHECK)
                .initial(EmployeeStates.SECURITY_CHECK_STARTED)
                .end(EmployeeStates.SECURITY_CHECK_FINISHED)
                .and()
                .withStates()
                .parent(EmployeeStates.IN_CHECK)
                .initial(EmployeeStates.WORK_PERMIT_CHECK_STARTED)
                .state(EmployeeStates.WORK_PERMIT_CHECK_PENDING_VERIFICATION)
                .end(EmployeeStates.WORK_PERMIT_CHECK_FINISHED);

    }

    @Override
    public void configure(StateMachineTransitionConfigurer<EmployeeStates, EmployeeEvents> transitions) throws Exception {
        transitions.withExternal()
                   .source(EmployeeStates.ADDED).target(EmployeeStates.IN_CHECK).event(EmployeeEvents.BEGIN_CHECK)
                   .and()
                   .withExternal()
                   .source(EmployeeStates.SECURITY_CHECK_STARTED).target(EmployeeStates.SECURITY_CHECK_FINISHED).event(EmployeeEvents.FINISH_SECURITY_CHECK)
                   .and()
                   .withExternal()
                   .source(EmployeeStates.WORK_PERMIT_CHECK_STARTED).target(EmployeeStates.WORK_PERMIT_CHECK_PENDING_VERIFICATION).event(EmployeeEvents.COMPLETE_INITIAL_WORK_PERMIT_CHECK)
                   .and()
                   .withExternal()
                   .source(EmployeeStates.WORK_PERMIT_CHECK_PENDING_VERIFICATION).target(EmployeeStates.WORK_PERMIT_CHECK_FINISHED).event(EmployeeEvents.FINISH_WORK_PERMIT_CHECK)
                   .and()
                   .withExternal()
                   .source(EmployeeStates.JOIN).target(EmployeeStates.APPROVED)
                   .and()
                   .withExternal()
                   .source(EmployeeStates.APPROVED).target(EmployeeStates.ACTIVE).event(EmployeeEvents.ACTIVATE)
                   .and()
                   .withFork()
                   .source(EmployeeStates.IN_CHECK)
                   .target(EmployeeStates.SECURITY_CHECK_STARTED)
                   .target(EmployeeStates.WORK_PERMIT_CHECK_STARTED)
                   .and()
                   .withJoin()
                   .source((EmployeeStates.SECURITY_CHECK_FINISHED))
                   .source(EmployeeStates.WORK_PERMIT_CHECK_FINISHED)
                   .target(EmployeeStates.JOIN);
    }

    @Override
    public void configure(StateMachineConfigurationConfigurer<EmployeeStates, EmployeeEvents> config) throws Exception {
        config.withConfiguration().listener(listener());
    }


}
