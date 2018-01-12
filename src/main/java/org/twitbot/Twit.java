package org.twitbot;

import org.slf4j.LoggerFactory;
import twitter4j.*;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import javax.annotation.PostConstruct;


public class Twit {

    private String OAUTH_CONSUMER_KEY;
    private String OAUTH_CONSUMER_SECRET;
    private String OAUTH_ACCESS_TOKEN;
    private String OAUTH_ACCESS_TOKEN_SECRET;

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(Twit.class);

    private Twitter twitter;
    private Configuration configuration;

    public Twit() {
    }

    @PostConstruct()
    public void postConstruct() {
        this.configuration = new ConfigurationBuilder()
                .setDebugEnabled(true)
                .setOAuthConsumerKey(OAUTH_CONSUMER_KEY)
                .setOAuthConsumerSecret(OAUTH_CONSUMER_SECRET)
                .setOAuthAccessToken(OAUTH_ACCESS_TOKEN)
                .setOAuthAccessTokenSecret(OAUTH_ACCESS_TOKEN_SECRET)
                .build();
        TwitterFactory tf = new TwitterFactory(this.configuration);
        this.twitter = tf.getInstance();
    }

    public void update(String twitStr) throws TwitterException {

        StatusUpdate su = new StatusUpdate(twitStr);
        Status status = twitter.updateStatus(su);

        LOGGER.debug("Successfully updated the status to \n" + status.getText());
    }

    public void setTwitter(Twitter twitter) {
        this.twitter = twitter;
    }

    public void setOAUTH_CONSUMER_KEY(String OAUTH_CONSUMER_KEY) {
        this.OAUTH_CONSUMER_KEY = OAUTH_CONSUMER_KEY;
    }

    public void setOAUTH_CONSUMER_SECRET(String OAUTH_CONSUMER_SECRET) {
        this.OAUTH_CONSUMER_SECRET = OAUTH_CONSUMER_SECRET;
    }

    public void setOAUTH_ACCESS_TOKEN(String OAUTH_ACCESS_TOKEN) {
        this.OAUTH_ACCESS_TOKEN = OAUTH_ACCESS_TOKEN;
    }

    public void setOAUTH_ACCESS_TOKEN_SECRET(String OAUTH_ACCESS_TOKEN_SECRET) {
        this.OAUTH_ACCESS_TOKEN_SECRET = OAUTH_ACCESS_TOKEN_SECRET;
    }
}