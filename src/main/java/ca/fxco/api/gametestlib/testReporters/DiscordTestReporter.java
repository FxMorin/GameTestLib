package ca.fxco.api.gametestlib.testReporters;

import com.google.common.base.Stopwatch;
import net.minecraft.gametest.framework.GameTestInfo;
import net.minecraft.gametest.framework.TestReporter;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
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

    private int exceptions = 0;
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
        if (this.exceptions > 0) {
            System.out.println("Discord Webhook TestReporter had " + this.exceptions + " exception" + (this.exceptions > 1 ? "s" : "") + " during operation!");
        }
    }

    private void sendDiscordWebhook(@Nullable GameTestInfo gameTestInfo, boolean success, boolean report) {
        URL url;
        try {
            url = new URL(this.webhookUrl);
        } catch (MalformedURLException e) {
            exceptions++;
            e.printStackTrace();
            return;
        }
        HttpURLConnection con;
        try {
            con = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            exceptions++;
            e.printStackTrace();
            return;
        }
        try {
            con.setRequestMethod("POST");
        } catch (ProtocolException e) {
            exceptions++;
            e.printStackTrace();
            return;
        }
        con.addRequestProperty("User-Agent", "Mozilla");
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);
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
        String jsonInputString = "{" +
                "\"username\": \"" + this.username + "\", " +
                "\"embeds\": [{" +
                    "\"color\": " + (report && this.failedOptionalTests > 0 ? YELLOW : (success ? GREEN : RED)) + ", " +
                    "\"title\": \"" + title + "\"," +
                    "\"description\": \"" + desc + "\"" +
                "}]}";
        try(OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        } catch (IOException e) {
            exceptions++;
            e.printStackTrace();
        }
    }

    private static String toTimeFormat(long millis) {
        long ms = millis % 1000;
        long s = (millis / 1000) % 60;
        long m = (millis / (1000 * 60)) % 60;
        long h = (millis / (1000 * 60 * 60)) % 24;
        return String.format("%02d:%02d:%02d.%d", h, m, s, ms);
    }

    enum SendRule {
        NEVER,
        IF_REQUIRED,
        ALWAYS
    }
}
