package ca.fxco.api.gametestlib.testReporters;

import com.google.common.base.Stopwatch;
import lombok.SneakyThrows;
import net.minecraft.gametest.framework.GameTestInfo;
import net.minecraft.gametest.framework.TestReporter;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * Discord Webhook Test Reporter
 */
public class DiscordTestReporter implements TestReporter {

    private static final int RED = 16711680;
    private static final int GREEN = 65280;
    private static final int YELLOW = RED | GREEN;

    private static final @Nullable String WEBHOOK_URL = System.getenv().get("GAMETEST_WEBHOOK_URL");

    private final String username;
    private final String webhookUrl;

    private final SendRule failureRule;
    private final SendRule successRule;
    private final boolean sendReport;

    private final Stopwatch stopwatch;
    private boolean success = true;
    private int failedOptionalTests = 0;

    public DiscordTestReporter(String username) {
        this(username, SendRule.IF_REQUIRED);
    }

    public DiscordTestReporter(String username, SendRule failureRule) {
        this(username, failureRule, SendRule.NEVER);
    }

    public DiscordTestReporter(String username, SendRule failureRule, SendRule successRule) {
        this(username, failureRule, successRule, true);
    }

    public DiscordTestReporter(String username, SendRule failureRule, SendRule successRule, boolean sendReport) {
        this(username, failureRule, successRule, sendReport, WEBHOOK_URL);
    }

    public DiscordTestReporter(String username, SendRule failureRule, SendRule successRule,
                               boolean sendReport, String webhookUrl) {
        this.username = username;
        this.failureRule = failureRule;
        this.successRule = successRule;
        this.sendReport = sendReport;
        this.webhookUrl = webhookUrl;
        if (this.webhookUrl == null) {
            throw new IllegalStateException("DiscordTestReporter - Webhook URL cannot be null!");
        }
        if (this.failureRule == SendRule.NEVER && this.successRule == SendRule.NEVER && !this.sendReport) {
            throw new IllegalStateException("DiscordTestReporter has been declared although is not being used!");
        }
        this.stopwatch = this.sendReport ? Stopwatch.createStarted() : null;
    }

    @Override
    public void onTestFailed(GameTestInfo gameTestInfo) {
        if (this.failureRule == SendRule.ALWAYS ||
                (this.failureRule == SendRule.IF_REQUIRED && gameTestInfo.isRequired())) {
            sendDiscordWebhook(gameTestInfo, false, false);
        }
        if (gameTestInfo.isRequired()) {
            this.success = false;
        } else {
            failedOptionalTests++;
        }
    }

    @Override
    public void onTestSuccess(GameTestInfo gameTestInfo) {
        if (this.successRule == SendRule.ALWAYS ||
                (this.successRule == SendRule.IF_REQUIRED && gameTestInfo.isRequired())) {
            sendDiscordWebhook(gameTestInfo, true, false);
        }
    }

    @Override
    public void finish() {
        if (this.sendReport) {
            this.stopwatch.stop();
            sendDiscordWebhook(null, this.success, true);
        }
    }

    @SneakyThrows
    public void sendDiscordWebhook(@Nullable GameTestInfo gameTestInfo, boolean success, boolean report) {
        URL url = new URL(this.webhookUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        int embedColor = (report && this.failedOptionalTests > 0 ? YELLOW : (success ? GREEN : RED));
        String title;
        String desc;
        if (report) {
            title = Component.translatable("gametest.gametestlib.discord_test_reporter.report.title", success ? "Success" : "Failure").getString();
            desc = Component.translatable("gametest.gametestlib.discord_test_reporter.runTime", toTimeFormat(this.stopwatch.elapsed(TimeUnit.MILLISECONDS))).getString() + "\n" +
                    Component.translatable("gametest.gametestlib.discord_test_reporter.failedOptionalTests", this.failedOptionalTests).getString();
        } else {
            title = Component.translatable("gametest.gametestlib.discord_test_reporter." + (success ? "success" : "failure") + ".title", gameTestInfo.getTestName()).getString();
            if (success) {
                desc = Component.translatable("gametest.gametestlib.discord_test_reporter.runTime", toTimeFormat(gameTestInfo.getRunTime())).getString();
            } else {
                desc = Component.translatable("gametest.gametestlib.discord_test_reporter.failure.position", gameTestInfo.getStructureBlockPos().toString()).getString() + "\n" +
                        Component.translatable("gametest.gametestlib.discord_test_reporter.failure.error", gameTestInfo.getError() != null ? gameTestInfo.getError().getMessage() : "Error").getString();
            }
        }

        String payload = String.format(
                "{\"username\":\"%s\",\"embeds\":[{\"color\":%d,\"title\":\"%s\",\"description\":\"%s\"}]}",
                this.username,
                embedColor,
                title,
                desc
        );

        try (OutputStream outputStream = connection.getOutputStream()) {
            byte[] input = payload.getBytes(StandardCharsets.UTF_8);
            outputStream.write(input, 0, input.length);
        }

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_NO_CONTENT) {
            System.out.println("Webhook sent successfully.");
        } else {
            System.out.println("Failed to send webhook. Response Code: " + responseCode);
        }

        connection.disconnect();
    }

    private static String toTimeFormat(long millis) {
        long ms = millis % 1000;
        long s = (millis / 1000) % 60;
        long m = (millis / (1000 * 60)) % 60;
        long h = (millis / (1000 * 60 * 60)) % 24;
        return String.format("%02d:%02d:%02d.%d", h, m, s, ms);
    }

    public enum SendRule {
        NEVER,
        IF_REQUIRED,
        ALWAYS
    }
}
