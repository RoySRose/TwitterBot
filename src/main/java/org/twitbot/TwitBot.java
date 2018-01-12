package org.twitbot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import twitter4j.TwitterException;

import java.io.*;

public class TwitBot {
    private static final String APPLICATION_CONTEXT_CONFIG = "/applicationContext.xml";
    private static final String HISTORIC_FILE_PATH = "records.txt";

    private static final String CHECK_GIT_BEAN_NAME = "checkGit";
    private static final String TWIT_BEAN_NAME = "twit";

    private static final Logger LOGGER = LoggerFactory.getLogger(TwitBot.class);

    public static void main(String[] args) {

        ApplicationContext context = new ClassPathXmlApplicationContext(APPLICATION_CONTEXT_CONFIG);
        GitHubChecker gitHub = (GitHubChecker) context.getBean(CHECK_GIT_BEAN_NAME);

        try {
            gitHub.checkLatestRelease();
        } catch (Exception e) {
            LOGGER.error("Checking Git Failed", e);
            return;
        }

        String currentReleaseId = gitHub.getReleaseInformation().getId();
        String lastReleaseId = readLastReleaseIdFromLocal();

        if (lastReleaseId == null || !lastReleaseId.equals(currentReleaseId)) {
            LOGGER.info("Update Twitter started");
            updateTwitter(context, gitHub);
            writeLastReleaseIdToLocal(currentReleaseId);
        } else {
            LOGGER.info("No Item to Update Twitter");
        }
    }

    private static void updateTwitter(ApplicationContext context, GitHubChecker git) {
        Twit twit = (Twit) context.getBean(TWIT_BEAN_NAME);
        String twitStr = "Project " + git.getReleaseInformation().getTag_name()
                + " is released: " + git.getReleaseInformation().getTarball_url() + "\n"
                + "GitHub: https://github.com/RoySRose/TwitterBot\n"
                + "Release notes: " + git.getReleaseInformation().getHtml_url() + "\n\n"
                + "Thanks to all developers, operators, and users of the Community\n"
                + "#TwitBot #AutoBot\n";

        // TODO check twitter if release note has already been updated.(not really necessary)
        try {
            twit.update(twitStr);
        } catch (TwitterException e) {
            LOGGER.error("Updating twitter has failed.", e);
            return;
        }
    }

    private static String readLastReleaseIdFromLocal() {
        File file = new File(HISTORIC_FILE_PATH);

        if (!file.exists()) {
            try {
                file.createNewFile();
                LOGGER.info("Creating new record file");
            } catch (Exception e) {
                LOGGER.error("Creating new file failed", e);
                throw new RuntimeException();
            }
        }

        String readLine;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
            readLine = br.readLine();
            LOGGER.info("Reading record file with path : " +  file.getAbsolutePath());
        } catch (IOException e) {
            LOGGER.error("Read historic file failed.", e);
            throw new RuntimeException(e);
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                LOGGER.warn("closing record file failed");
            }
        }
        return readLine;
    }


    private static void writeLastReleaseIdToLocal(String currentReleaseId) {
        BufferedWriter bw = null;
        try {
            File file = new File(HISTORIC_FILE_PATH);

            bw = new BufferedWriter(new FileWriter(file));
            bw.write(currentReleaseId);
        } catch (IOException e) {
            LOGGER.error("Writing twitted last release id failed");
            throw new RuntimeException("Writing twitted last release id failed", e);
        } finally {
            try {
                bw.close();
            } catch (IOException e) {
                LOGGER.warn("closing record file failed");
            }
        }
    }

}