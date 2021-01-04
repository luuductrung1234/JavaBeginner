package com.learning.simple.weather;

import java.io.StringReader;

import com.learning.simple.common.util.exception.ExceptionExtensions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {
    private static Logger logger = LogManager.getLogger();

    public static void main(String[] args) {
        String zipCode = "2502265";
        try {
            zipCode = args[0];
        } catch (Exception e) {
            ExceptionExtensions.logMe(e, logger);
        }

        new Main(zipCode).start();
    }

    private String zipCode;

    public Main(String zipCode) {
        this.zipCode = zipCode;
    }

    private void start() {
        logger.warn("Start...\n");
        try {
            // retrieve data
            String dataIn = new YahooRetriever().retrieveString(this.zipCode);

            // parse data
            Weather weather = new YahooParser().parse(new StringReader(dataIn));

            // print formatted data
            String formattedWeather = new WeatherFormatter().format(weather);

            logger.info("\n\n{}", formattedWeather);
        } catch (Exception e) {
            ExceptionExtensions.logMe(e, logger);
        }
        logger.warn("End");
    }
}
