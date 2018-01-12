package org.twitbot;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GitHubChecker {
    private static final String GIT_API_VERSION = "application/vnd.github.v3.full+json";
    private static final int RESPONSE_CODE_SUCCESS = 200;
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

    private static final Logger LOGGER = LoggerFactory.getLogger(GitHubChecker.class);

    private String url;
    private ReleaseInformation releaseInformation;

    public void checkLatestRelease() throws Exception {

        URL url = new URL(this.url);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        //property setting for response schema
        con.setRequestProperty("Accept", GIT_API_VERSION);

        LOGGER.debug("\nSending 'GET' request to URL : " + url);
        if (con.getResponseCode() != RESPONSE_CODE_SUCCESS) {
            LOGGER.error("Failed : HTTP error code : " + con.getResponseCode());
            return;
        }

        // TODO: try catch and extract
        String response = getResponse(con);
        releaseInformation = OBJECT_MAPPER.readValue(response, ReleaseInformation.class);
    }

    private String getResponse(HttpURLConnection con) throws Exception {
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

        StringBuilder response = null;
        try {
            String inputLine;
            response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
        } catch (IOException e) {
            LOGGER.error("IOException", e);
            throw e;
        } finally {
            in.close();
        }
        return response.toString();
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ReleaseInformation getReleaseInformation() {
        return releaseInformation;
    }

}
