package rules;

import models.Person;
import org.drools.core.base.RuleNameEqualsAgendaFilter;
import org.drools.core.marshalling.impl.ProtobufMessages;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.kie.api.KieServices;
import org.kie.api.event.rule.AfterMatchFiredEvent;
import org.kie.api.event.rule.AgendaEventListener;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.mockito.ArgumentCaptor;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


public class TestRules {

    private static KieSession kieSession;
    private static KieContainer kieContainer;
    private static KieServices kieServices;

    @BeforeAll
    public static void setup() {
        try {
            kieServices = KieServices.Factory.get();
            kieContainer = kieServices.getKieClasspathContainer();
            kieSession = kieContainer.newKieSession("ksession-rule");
            kieSession.fireAllRules();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @Test
    public void testRulesFire() throws Exception {
        // ***********************************************************
        //    execute the scenario to be tested
        // ***********************************************************
        // insert the required data
        Person bob = new Person("Bob",
                35);
        kieSession.insert(bob);

        int rulesFired = kieSession.fireAllRules(10);

        // ***********************************************************
        //    verify the results
        // ***********************************************************
        assertEquals(2, rulesFired);
    }

    @Test
    public void testNameIsBobRuleOnly() throws Exception {
        // ***********************************************************
        //    execute the scenario to be tested
        // ***********************************************************
        // insert the required data
        Person bob = new Person( "Bob",
                35 );
        kieSession.insert( bob );

        // we only want to fire the Bob rule, so use an agenda filter for that
        int rulesFired = kieSession.fireAllRules( new RuleNameEqualsAgendaFilter( "Name is Bob" ) );

        // ***********************************************************
        //    verify the results
        // ***********************************************************
        assertEquals( 1, rulesFired );
    }

    @Test
    public void testRulesFired() throws Exception {
        // ***********************************************************
        //    create the mock listeners and add them to the session
        // ***********************************************************

        // AgendaEventListeners allow one to monitor and check rules that activate, fire, etc
        AgendaEventListener ael = mock( AgendaEventListener.class );
        kieSession.addEventListener( ael );

        // ***********************************************************
        //    execute the scenario to be tested
        // ***********************************************************
        // insert the required data
        Person bob = new Person( "Bob",
                35 );
        kieSession.insert( bob );

        kieSession.fireAllRules( 10 );

        // ***********************************************************
        //    verify the results
        // ***********************************************************

        verify( ael, times(2) ).afterMatchFired(any(AfterMatchFiredEvent.class));
    }

    @Test
    public void testRulesFiredInSequence() throws Exception {
        // ***********************************************************
        //    create the mock listeners and add them to the session
        // ***********************************************************
        // AgendaEventListeners allow one to monitor and check rules that activate, fire, etc
        AgendaEventListener ael = mock( AgendaEventListener.class );
        kieSession.addEventListener( ael );

        // ***********************************************************
        //    execute the scenario to be tested
        // ***********************************************************
        // insert the required data
        Person bob = new Person( "Bob",
                35 );
        kieSession.insert( bob );

        kieSession.fireAllRules( 10 );

        // ***********************************************************
        //    verify the results
        // ***********************************************************
        // create and argument captor for AfterActivationFiredEvent
        ArgumentCaptor<AfterMatchFiredEvent> afterMatchFiredEventArgumentCaptor = ArgumentCaptor.forClass( AfterMatchFiredEvent.class );

        // check that the method was called twice and capture the arguments
        verify( ael, times(2) ).afterMatchFired( afterMatchFiredEventArgumentCaptor.capture() );
        List<AfterMatchFiredEvent> events = afterMatchFiredEventArgumentCaptor.getAllValues();

        // check the rule name for the first rule to fire
        AfterMatchFiredEvent first = events.get( 0 );
        assertEquals( first.getMatch().getRule().getName(),"Name is Bob" );
        // check the rule name of the second rule to fire
        AfterMatchFiredEvent second = events.get( 1 );
        assertEquals( second.getMatch().getRule().getName(), "Person is 35 years old");
    }

}