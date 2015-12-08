package com.epam.core.common;

import com.google.common.base.Function;
import com.epam.core.driver.Driver;
import com.epam.core.logging.Logger;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import javax.mail.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class MessageReader {

    private static final String email = "easyfinancialepam@gmail.com";
    private static final String password = "easy12345easy";
    private static final String protocol = "mail.store.protocol";
    private static final String failMessage = "Fail to get messages";
    private Session session = null;
    private Store store = null;
    private Folder testEmailFolder = null;
    private Wait<WebDriver> wait;

    public void initMail() {
        Properties props = System.getProperties();
        props.setProperty(protocol, "imaps");
        session = Session.getDefaultInstance(props, null);

        try {
            store = session.getStore("imaps");
            store.connect("imap.gmail.com", email, password);
            testEmailFolder = store.getFolder("Inbox");
            testEmailFolder.open(Folder.READ_WRITE);

        } catch (NoSuchProviderException e) {
            Logger.logError("Fail to connect to the imap store");

        } catch (MessagingException e) {
            Logger.logError("Fail to connect to a specific email inbox ");

        }

    }

    public boolean waitForMailWithTopic(final String topic) {
        Logger.logInfo("Wait for message");
        wait = new FluentWait<WebDriver>(Driver.getDefault())
                .withTimeout(120, TimeUnit.SECONDS)
                .pollingEvery(2, TimeUnit.SECONDS)
                .ignoring(TimeoutException.class);
        try {
            return wait.until(new Function<WebDriver, Boolean>() {
                public Boolean apply(WebDriver driver) {
                    return isMessageExistWithTopic(topic);
                }
            });
        } catch (TimeoutException e) {
            Logger.logError("timeout waiting for mail with topic " + topic);
            return false;
        }

    }

    public String dumpPart(Part part, StringBuilder sBuilder)
            throws MessagingException, IOException {
        if (part.isMimeType("text/plain")) {

            if (!part.getContent().toString().isEmpty()) {
                sBuilder.append((String) part.getContent());
            }
        } else if (part.isMimeType("multipart/*")) {

            Multipart multiPArt = (Multipart) part.getContent();
            int count = multiPArt.getCount();
            for (int x = 0; x < count; x++) {

                BodyPart bodyPart = multiPArt.getBodyPart(x);
                if (bodyPart != null) {
                    sBuilder.append(bodyPart.getContent());
                }
            }
        }
        return sBuilder.toString();
    }

    public List<String> getNameFileAttachedToMail(String topic) {
        Multipart multipart;
        List<String> fileNames = new ArrayList<String>();

        try {

            Message message = getLastMessage();
            multipart = (Multipart) message.getContent();

            for (int i = 0; i < multipart.getCount(); i++) {
                if (multipart.getBodyPart(i) != null) {
                    BodyPart bodyPart = multipart.getBodyPart(i);
                    if (bodyPart.getFileName() != null) {
                        fileNames.add(bodyPart.getFileName());
                    }
                }
            }

        } catch (MessagingException e) {
            Logger.logError("Enaible to get files attached to mail");
            return null;
        } catch (IOException e) {
            Logger.logError("Enaible to get files attached to mail");
            return null;
        }

        return fileNames;
    }

    public String findMessageWithTopic(String topic) {

        Multipart multipart;
        try {
            Message message = getLastMessage();
            multipart = (Multipart) message.getContent();
            int multiPartCount = multipart.getCount();
            BodyPart bodyPart = null;
            for (int i = 0; i < multiPartCount; i++) {
                bodyPart = multipart.getBodyPart(i);
                if (bodyPart != null) {
                    StringBuilder sb = new StringBuilder();
                    return dumpPart(bodyPart, sb);
                }
            }
        } catch (MessagingException e) {
            Logger.logError(failMessage);

        } catch (IOException e) {
            Logger.logError("Enaible to get files attached to mail");

        }

        return "";
    }

    public boolean getNewMessageByDate(final String topic, final Date date) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, -1);
        final Date previousDate = cal.getTime();

        Logger.logInfo("Time the report was sent: " + getDate(previousDate));
        wait = new FluentWait<WebDriver>(Driver.getDefault())
                .withTimeout(120, TimeUnit.SECONDS)
                .pollingEvery(3, TimeUnit.SECONDS)
                .ignoring(NoSuchElementException.class);

        boolean newMessageAppeared = wait
                .until(new Function<WebDriver, Boolean>() {
                    public Boolean apply(WebDriver driver) {

                        try {
                            Message message = getLastMessage();
                            Date messageDate = message.getReceivedDate();
                            return previousDate.before(messageDate)
                                    && message.getSubject().contains(topic);
                        } catch (MessagingException e) {
                            Logger.logError(failMessage);

                        }
                        return false;

                    }
                });
        return newMessageAppeared;

    }

    private String getDate(Date date) {

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        return sdf.format(date);
    }

    private Message getLastMessage() {
        int messagesSize;
        try {
            messagesSize = testEmailFolder.getMessageCount();
            Message message = testEmailFolder.getMessage(messagesSize);
            return message;
        } catch (MessagingException e) {
            Logger.logError("Fail to get last message");
        }
        return null;

    }

    public boolean isMessageExistWithTopic(String topic) {

        try {
            Message message = getLastMessage();
            if (message.getSubject().contains(topic)) {
                {
                    Logger.logPass("Message with topic:'" + topic + "' exist");
                    return true;
                }
            }
        } catch (MessagingException e) {
            Logger.logError(failMessage);
            return false;
        }

        return false;
    }

    public void deleteMessageByTopic(final String topic) {
        try {

            Message message = getLastMessage();
            if (message.getSubject().equals(topic)) {
                message.setFlag(Flags.Flag.DELETED, true);
                testEmailFolder.expunge();
            }

        } catch (MessagingException e) {
            Logger.logError("Fail to find message");

        }

    }

    public void close() {
        try {
            testEmailFolder.close(true);
            store.close();
        } catch (MessagingException e) {
            Logger.logError("Fail to close resources");
        }
    }

}
