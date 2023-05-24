package ca.fxco.api.gametestlib.progressbar;

import net.minecraft.gametest.framework.GameTestInfo;

import java.util.Collection;

public class DefaultProgressBar implements GameTestProgressBar {

    private static final char START_BRACKET_CHAR = '[';
    private static final char END_BRACKET_CHAR = ']';
    private static final char NOT_STARTED_TEST_CHAR = ' ';
    private static final char ONGOING_TEST_CHAR = '_';
    private static final char SUCCESSFUL_TEST_CHAR = '+';
    private static final char FAILED_OPTIONAL_TEST_CHAR = 'x';
    private static final char FAILED_REQUIRED_TEST_CHAR = 'X';

    @Override
    public String getProgressBar(Collection<GameTestInfo> tests) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(START_BRACKET_CHAR);
        tests.forEach((gameTestInfo) -> {
            if (!gameTestInfo.hasStarted()) {
                stringBuffer.append(NOT_STARTED_TEST_CHAR);
            } else if (gameTestInfo.hasSucceeded()) {
                stringBuffer.append(SUCCESSFUL_TEST_CHAR);
            } else if (gameTestInfo.hasFailed()) {
                stringBuffer.append(gameTestInfo.isRequired() ? FAILED_REQUIRED_TEST_CHAR : FAILED_OPTIONAL_TEST_CHAR);
            } else {
                stringBuffer.append(ONGOING_TEST_CHAR);
            }
        });
        stringBuffer.append(END_BRACKET_CHAR);
        return stringBuffer.toString();
    }
}
