package ca.fxco.testmod.testSuites;

import ca.fxco.gametestlib.gametest.GameTestUtil;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;

public class BasicTestSuite {

    // Make sure obsidian is still immovable
    @GameTest(timeoutTicks = 4)
    public void immovable(GameTestHelper helper) {
        GameTestUtil.initializeGameTestLib(helper);
    }
}
