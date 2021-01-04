package com.learning.simple.weather;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

public class WeatherFormatter {
    private static Logger logger = LogManager.getLogger(WeatherFormatter.class);

    public String format(Weather weather) throws IOException {
        logger.info("Formatting Weather Data");

        Reader reader = new InputStreamReader(getClass().getClassLoader().getResourceAsStream("output.vm"));
        VelocityContext context = new VelocityContext();
        context.put("weather", weather);
        StringWriter writer = new StringWriter();
        Velocity.evaluate(context, writer, "", reader);
        return writer.toString();
    }
}
