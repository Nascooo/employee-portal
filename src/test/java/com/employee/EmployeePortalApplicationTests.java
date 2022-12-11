package com.employee;

import com.employee.constants.EmployeeEvents;
import com.employee.constants.EmployeeStates;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;

import static com.employee.constants.EmployeeEvents.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class EmployeePortalApplicationTests {

    @Autowired
    StateMachineFactory<EmployeeStates, EmployeeEvents> factory;

    @Test
    void contextLoads() {
    }

    @Test
    void test_newState_to_active_state() {
        StateMachine<EmployeeStates, EmployeeEvents> sm = factory.getStateMachine();

        sm.start();

        sm.sendEvent(BEGIN_CHECK);

        System.out.println(sm.getState().getId().toString());

        sm.sendEvent(FINISH_SECURITY_CHECK);

        System.out.println(sm.getState().getId().toString());

        sm.sendEvent(COMPLETE_INITIAL_WORK_PERMIT_CHECK);

        System.out.println(sm.getState().getId().toString());

        sm.sendEvent(FINISH_WORK_PERMIT_CHECK);

        System.out.println(sm.getState().getId().toString());

        sm.sendEvent(ACTIVATE);

        System.out.println(sm.getState().getId().toString());

        String smState = sm.getState().getId().toString();

        assertEquals(smState, EmployeeStates.ACTIVE.toString());

    }
}
