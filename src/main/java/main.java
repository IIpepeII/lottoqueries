import models.TestModel;
import rules.Rules;

public class main {

    public static void main(String[] args) {

        TestModel t1 = new TestModel("Model1", true);
        TestModel t2 = new TestModel("Model1", false);
        Rules.executeRule(t1);
        Rules.executeRule(t2);

    }
}
