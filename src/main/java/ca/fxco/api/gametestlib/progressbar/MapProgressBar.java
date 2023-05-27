package ca.fxco.api.gametestlib.progressbar;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestInfo;

import java.util.Collection;

public class MapProgressBar implements GameTestProgressBar {

    private static final char START_BRACKET_CHAR = '[';
    private static final char END_BRACKET_CHAR = ']';
    private static final char NOT_STARTED_TEST_CHAR = 'O';
    private static final char ONGOING_TEST_CHAR = '0';
    private static final char SUCCESSFUL_TEST_CHAR = '+';
    private static final char FAILED_OPTIONAL_TEST_CHAR = 'x';
    private static final char FAILED_REQUIRED_TEST_CHAR = 'X';

    @Override
    public String getProgressBar(Collection<GameTestInfo> tests) {
        int width = 0;
        int tempWidth = 0;
        BlockPos lastBlockPos = BlockPos.ZERO;
        for (GameTestInfo info : tests) {
            BlockPos pos = info.getStructureBlockPos();
            if (pos.getX() > lastBlockPos.getX()) {
                tempWidth++;
            } else if (pos.getZ() > lastBlockPos.getZ()) {
                if (tempWidth > width) {
                    width = tempWidth;
                }
                tempWidth = 0;
            }
            lastBlockPos = pos;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(START_BRACKET_CHAR);
        tempWidth = 0;
        lastBlockPos = BlockPos.ZERO;
        for (GameTestInfo gameTestInfo : tests) {
            BlockPos pos = gameTestInfo.getStructureBlockPos();
            if (pos.getZ() > lastBlockPos.getZ()) {
                stringBuilder.append(" ".repeat(width - tempWidth))
                        .append(END_BRACKET_CHAR)
                        .append("\n")
                        .append(START_BRACKET_CHAR);
                tempWidth = 0;
            } else {
                tempWidth++;
            }
            lastBlockPos = pos;
            if (!gameTestInfo.hasStarted()) {
                stringBuilder.append(NOT_STARTED_TEST_CHAR);
            } else if (gameTestInfo.hasSucceeded()) {
                stringBuilder.append(SUCCESSFUL_TEST_CHAR);
            } else if (gameTestInfo.hasFailed()) {
                stringBuilder.append(gameTestInfo.isRequired() ? FAILED_REQUIRED_TEST_CHAR : FAILED_OPTIONAL_TEST_CHAR);
            } else {
                stringBuilder.append(ONGOING_TEST_CHAR);
            }
        }
        stringBuilder.append(END_BRACKET_CHAR);
        return stringBuilder.toString();
    }
}
