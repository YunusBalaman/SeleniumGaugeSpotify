package com.spotify.web.step;

import com.spotify.web.driver.Driver;
import com.spotify.web.methods.MethodsUtil;
import com.spotify.web.methods.mail.ReadMail;
import com.thoughtworks.gauge.Step;
import jakarta.mail.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.junit.jupiter.api.Assertions;
import java.util.Arrays;
import java.util.regex.Pattern;

public class StepSpotifyUtilsImp {

    private static final Logger logger = LogManager.getLogger(StepSpotifyUtilsImp.class);
    MethodsUtil methodsUtil;

    public StepSpotifyUtilsImp(){

        methodsUtil = new MethodsUtil();
    }

    @Step("Read Mail <mail> mail <password> password after time <timeMillis> loopCount <loopCount> if <ifCondition>")
    public void readMail(String mail, String password, String timeMillisString, int loopCount, String ifCondition){

        if (Boolean.parseBoolean(methodsUtil.setValueWithMapKey(ifCondition))) {
            methodsUtil.waitBySeconds(1);
            String text = "";
            boolean success = false;
            long timeMillis = Long.parseLong(methodsUtil.setValueWithMapKey(timeMillisString));
            ReadMail readMail = new ReadMail();
            Session session = readMail.getSession(mail, password);
            Store store = readMail.getStore(session);
            Folder folder2 = null;
            try {
                for (int k = 0; k < loopCount; k++) {
                    logger.info("loop count " + (k+1));
                    folder2 = store.getFolder("INBOX");
                    folder2.open(Folder.READ_ONLY);
                    int messageCount = folder2.getMessageCount();
                    for (int i = 0; i < messageCount; i++) {
                        Message messages = folder2.getMessage(messageCount - i);
                        DateTime time = org.joda.time.LocalDateTime.parse(messages.getHeader("Date")[0]
                                        , DateTimeFormat.forPattern("EEE, dd MMM yyyy HH:mm:ss Z '(UTC)'"))
                                .toDateTime(DateTimeZone.forOffsetHours(0));
                        logger.info(messages.getReceivedDate().toString());
                        System.out.println(timeMillis + "    " + time.getMillis() + "  " + (timeMillis / 1000 > time.getMillis() / 1000));
                        if (timeMillis / 1000 > time.getMillis() / 1000) {
                            break;
                        }
                        if (Arrays.stream(messages.getFrom())
                                .noneMatch(address -> address.toString().contains("@spotify.com")
                                        || address.toString().contains("@alerts.spotify.com"))) {
                            continue;
                        }
                        String subject = messages.getSubject().trim();
                        logger.info(subject);
                        if (subject.endsWith("- Spotify güvenlik kodun")) {
                            text = subject.replace("- Spotify güvenlik kodun", "").trim();
                            success = true;
                            break;
                        }
                    }
                    if (success)
                        break;
                    methodsUtil.waitBySeconds(10);
                }
                // 892398 - Spotify güvenlik kodun
                // "Spotify <no-reply@spotify.com>"
                //"TEXT/PLAIN; charset=utf-8"
                store.close();
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
            if (!success || text.isEmpty() || !Pattern.matches("[0-9]+", text)) {
                Assertions.fail("Güvenlik Kodu mailine ulaşılamadı");
            }
            Driver.TestMap.put("spotifySecurityCode", text);
        }
    }
}
