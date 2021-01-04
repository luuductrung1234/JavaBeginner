package com.learning.simple.weather;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Base64.Encoder;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.learning.simple.common.util.exception.ExceptionExtensions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class YahooRetriever {
    private static Logger logger = LogManager.getLogger(YahooRetriever.class);
    private Random rand;
    private static String ENCODE = "UTF-8";

    public YahooRetriever() {
        try {
            rand = SecureRandom.getInstanceStrong(); // SecureRandom is preferred to Random
        } catch (Exception e) {
            ExceptionExtensions.logMe(e, logger);
        }
    }

    public InputStream retrieve(String zipCode) throws IOException {
        logger.info("Retrieving Weather Data");
        String url = "https://weather-ydn-yql.media.yahoo.com/forecastrss?woeid=" + zipCode;
        URLConnection conn = new URL(url).openConnection();
        return conn.getInputStream();
    }

    public String retrieveString(String zipCode) throws IOException, InterruptedException {
        logger.info("Retrieving Weather Data");

        final String appId = "xWG8mHJF";
        final String consumerKey = "dj0yJmk9TE1NUHk2RXR6MUVXJmQ9WVdrOWVGZEhPRzFJU2tZbWNHbzlNQT09JnM9Y29uc3VtZXJzZWNyZXQmc3Y9MCZ4PWM";
        final String consumerSecret = "847f6648a4bd24b10ff0e85978725a70e8949aaa";
        final String url = "https://weather-ydn-yql.media.yahoo.com/forecastrss";

        long timestamp = new Date().getTime() / 1000;
        byte[] nonce = new byte[32];
        rand.nextBytes(nonce);
        String oauthNonce = new String(nonce).replaceAll("\\W", "");

        List<String> parameters = new ArrayList<>();
        parameters.add("oauth_consumer_key=" + consumerKey);
        parameters.add("oauth_nonce=" + oauthNonce);
        parameters.add("oauth_signature_method=HMAC-SHA1");
        parameters.add("oauth_timestamp=" + timestamp);
        parameters.add("oauth_version=1.0");
        // Make sure value is encoded
        parameters.add("woeid=" + URLEncoder.encode(zipCode, ENCODE));
        Collections.sort(parameters);

        StringBuilder parametersList = new StringBuilder();
        for (int i = 0; i < parameters.size(); i++) {
            parametersList.append(((i > 0) ? "&" : "") + parameters.get(i));
        }

        String signatureString = "GET&" + URLEncoder.encode(url, ENCODE) + "&"
                + URLEncoder.encode(parametersList.toString(), ENCODE);

        String signature = null;
        try {
            SecretKeySpec signingKey = new SecretKeySpec((consumerSecret + "&").getBytes(), "HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(signingKey);
            byte[] rawHMAC = mac.doFinal(signatureString.getBytes());
            Encoder encoder = Base64.getEncoder();
            signature = encoder.encodeToString(rawHMAC);
        } catch (Exception e) {
            ExceptionExtensions.logMe(e, logger);
        }

        String authorizationLine = "OAuth " + "oauth_consumer_key=\"" + consumerKey + "\", " + "oauth_nonce=\""
                + oauthNonce + "\", " + "oauth_timestamp=\"" + timestamp + "\", "
                + "oauth_signature_method=\"HMAC-SHA1\", " + "oauth_signature=\"" + signature + "\", "
                + "oauth_version=\"1.0\"";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url + "?woeid=" + zipCode))
                .header("Authorization", authorizationLine).header("X-Yahoo-App-Id", appId).build();

        logger.info("Start send request");
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        logger.info("End send request");

        String result = response.body();
        logger.info("Result: \n {}", result);
        return result;
    }
}
