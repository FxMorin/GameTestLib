package ca.fxco.api.gametestlib.progressbar;

import net.minecraft.gametest.framework.GameTestInfo;

import java.util.Collection;

public class MutableProgressBar implements GameTestProgressBar {

    private final char[] progressBarChars = new char[]{'[',' ','_','+','x','X',']'};

    public MutableProgressBar(char startBracket, char endBracket) {
        this.progressBarChars[0] = startBracket;
        this.progressBarChars[6] = endBracket;
    }

    public MutableProgressBar(String progressBar) {
        if (progressBar.length() != 7) {
            throw new IllegalStateException("String ProgressBar's must have all 7 characters in them!");
        }
        for (int i = 0; i <= 6; i++) {
            this.progressBarChars[i] = progressBar.charAt(i);
        }
    }

    public MutableProgressBar(char startBracket, char endBracket, char notStarted, char ongoing,
                              char successful, char failedOptional, char failedRequired) {
        this.progressBarChars[0] = startBracket;
        this.progressBarChars[1] = notStarted;
        this.progressBarChars[2] = ongoing;
        this.progressBarChars[3] = successful;
        this.progressBarChars[4] = failedOptional;
        this.progressBarChars[5] = failedRequired;
        this.progressBarChars[6] = endBracket;
    }

    @Override
    public String getProgressBar(Collection<GameTestInfo> tests) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(this.progressBarChars[0]);
        tests.forEach((gameTestInfo) -> {
            if (!gameTestInfo.hasStarted()) {
                stringBuffer.append(this.progressBarChars[1]);
            } else if (gameTestInfo.hasSucceeded()) {
                stringBuffer.append(this.progressBarChars[3]);
            } else if (gameTestInfo.hasFailed()) {
                stringBuffer.append(this.progressBarChars[gameTestInfo.isRequired() ? 5 : 4]);
            } else {
                stringBuffer.append(this.progressBarChars[2]);
            }
        });
        stringBuffer.append(this.progressBarChars[6]);
        return stringBuffer.toString();
    }
}
