package org.twitbot;

/*
 * Copyright 2018 NAVER Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(TwitTest.APPLICATION_CONTEXT_CONFIG)
public class TwitTest {
    static final String APPLICATION_CONTEXT_CONFIG = "/applicationContext.xml";

    private String OAUTH_CONSUMER_KEY = "Falsekey";
    private String OAUTH_CONSUMER_SECRET = "Falsekey";
    private String OAUTH_ACCESS_TOKEN = "Falsekey";
    private String OAUTH_ACCESS_TOKEN_SECRET = "Falsekey";

    private Twit twit;
    private Twitter twitter;

    @Test
    public void test_updatestatus_function_call() throws TwitterException {

        this.twit = new Twit();
        this.twitter = Mockito.mock(Twitter.class);

        twit.setTwitter(twitter);

        //twit.setTwitter(this.twitter);
        // Given
        TwitterException exception = Mockito.mock(TwitterException.class);
        when(twitter.updateStatus(any(StatusUpdate.class))).thenThrow(exception);

        // When
        TwitterException ex = null;

        try {
            this.twit.update("xxx2");
        } catch (TwitterException e) {
            ex = e;
        }

        // Then
        assertNotNull(ex);
        assertEquals(exception, ex);
    }

    @Test
    public void test_twitter_authorization_fail() {

        // Given
        Twit twit = new Twit();

        // When
        twit.setOAUTH_CONSUMER_KEY(OAUTH_CONSUMER_KEY);
        twit.setOAUTH_CONSUMER_SECRET(OAUTH_CONSUMER_SECRET);
        twit.setOAUTH_ACCESS_TOKEN(OAUTH_ACCESS_TOKEN);
        twit.setOAUTH_ACCESS_TOKEN_SECRET(OAUTH_ACCESS_TOKEN_SECRET);

        TwitterException ex = null;
        try {
            twit.postConstruct();
            twit.update("Fail message");
        } catch (TwitterException e) {
            ex = e;
        }
        // Then
        assertNotNull(ex);
        assertEquals(ex.getStatusCode(), 401);
    }
}