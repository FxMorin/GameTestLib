package ca.fxco.testmod;

import ca.fxco.api.gametestlib.control.GameTestControl;
import ca.fxco.api.gametestlib.listener.ColoredFloorListener;
import ca.fxco.api.gametestlib.progressbar.GameTestProgressBar;
import ca.fxco.api.gametestlib.progressbar.MutableProgressBar;
import ca.fxco.api.gametestlib.testReporters.DiscordTestReporter;
import net.minecraft.gametest.framework.TestReporter;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TestGameTestControl implements GameTestControl {

    @Override
    public List<TestReporter> registerTestReporters() {
        return List.of(new DiscordTestReporter("TestMod", DiscordTestReporter.SendRule.ALWAYS, DiscordTestReporter.SendRule.ALWAYS, true, "https://discord.com/api/webhooks/1112095637668892782/rnGWvQMpcGEbCN3x9EYUUqsJiHnYg6PY9MlfHpEZSA_s3KNA46mN0j-x0i5JtfmjSSrt"));
    }

    public List<GameTestListenerFactory> registerGameTestListeners() {
        return List.of((a,b,c) -> new ColoredFloorListener());
    }

    /**
     * Only a single Custom Progress Bar can be used at a time!
     * You can set a priority if two mods are conflicting
     */
    @Nullable
    @Override
    public GameTestProgressBar registerGameTestProgressBar() {
        return new MutableProgressBar('{','}') {
            @Override
            public int getPriority() {
                return 1010;
            }
        };
    }
}
