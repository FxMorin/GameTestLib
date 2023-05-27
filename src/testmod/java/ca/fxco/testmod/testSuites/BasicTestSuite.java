package ca.fxco.testmod.testSuites;

import ca.fxco.api.gametestlib.gametest.GameTestLib;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;

public class BasicTestSuite {

    // Make sure obsidian is still immovable
    @GameTestLib
    @GameTest(timeoutTicks = 4)
    public void immovable(GameTestHelper helper) {}
}
