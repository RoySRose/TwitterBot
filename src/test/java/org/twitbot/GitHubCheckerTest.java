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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(GitHubCheckerTest.APPLICATION_CONTEXT_CONFIG)
public class GitHubCheckerTest {
    static final String APPLICATION_CONTEXT_CONFIG = "/applicationContext.xml";

    @Autowired
    private GitHubChecker gitHubChecker;

    private GitHubChecker modifiedGitHubChecker = new GitHubChecker();


    @Test
    public void check() throws Exception {
        // Given

        // When
        this.gitHubChecker.checkLatestRelease();

        // Then
        ReleaseInformation releaseInformation = this.gitHubChecker.getReleaseInformation();
        assertNotNull(releaseInformation);
        assertNotNull(releaseInformation.getId());
        assertNotNull(releaseInformation.getHtml_url());
        assertNotNull(releaseInformation.getTag_name());
        assertNotNull(releaseInformation.getTarball_url());
    }

    @Test
    public void check_fail() throws Exception {
        // Given
        this.modifiedGitHubChecker.setUrl("http://test.com");

        // When
        this.modifiedGitHubChecker.checkLatestRelease();

        // Then
        ReleaseInformation releaseInformation = this.gitHubChecker.getReleaseInformation();
        assertNull(releaseInformation);
    }
}