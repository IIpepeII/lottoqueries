package rules;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

public class Rules {


    public static void executeRule(Object testModel) {

        try {
            KieServices kieServices = KieServices.Factory.get();
            KieContainer kContainer = kieServices.getKieClasspathContainer();
            KieSession kSession = kContainer.newKieSession("ksession-rule");
            kSession.insert(testModel);
            kSession.fireAllRules(10);
        }
        catch (Throwable t) {
            t.printStackTrace();
        }


    }
}
