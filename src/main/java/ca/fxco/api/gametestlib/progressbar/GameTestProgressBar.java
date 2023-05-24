package ca.fxco.api.gametestlib.progressbar;

import net.minecraft.gametest.framework.GameTestInfo;

import java.util.Collection;

public interface GameTestProgressBar {

    /**
     * Lower priority wins
     */
    default int getPriority() {
        return 1000;
    }

    String getProgressBar(Collection<GameTestInfo> tests);
}
