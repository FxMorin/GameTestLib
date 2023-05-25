package ca.fxco.gametestlib.gametest.expansion;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.gametest.framework.GameTestHelper;

import java.util.ArrayList;
import java.util.List;

@Getter
public class GameTestGroupConditions {

    private final List<TestCondition> testConditions = new ArrayList<>();

    public void runTick(GameTestHelper helper) {
        for (TestCondition testCondition : testConditions) {
            if (testCondition.isSingleTick()) {
                if (testCondition.canRunThisTick(helper.getTick())) {
                    if (!testCondition.runCheck(helper)) {
                        break;
                    }
                    testCondition.setSuccess(true);
                }
            } else {
                Boolean result = testCondition.runCheck(helper);
                if (result != null) {
                    if (!result) {
                        break;
                    }
                    testCondition.setSuccess(true);
                }
            }
        }
    }

    public boolean isSuccess() {
        for (TestCondition testCondition : testConditions) {
            if (!testCondition.isSuccess()) {
                return false;
            }
        }
        return true;
    }

    public void addCondition(TestCondition testCondition) {
        testConditions.add(testCondition);
    }

    public interface TestCondition {
        boolean isSuccess();
        void setSuccess(boolean state);
        boolean isSingleTick();
        boolean canRunThisTick(long tick);
        Boolean runCheck(GameTestHelper helper);
    }

    public static abstract class BasicTestCondition implements TestCondition {

        @Getter
        @Setter
        private boolean success = false;

        @Override
        public boolean isSingleTick() {
            return false;
        }

        @Override
        public boolean canRunThisTick(long tick) {
            return false;
        }
    }
}
