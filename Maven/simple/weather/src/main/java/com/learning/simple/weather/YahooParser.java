package com.learning.simple.weather;

import java.io.InputStream;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentFactory;
import org.dom4j.io.SAXReader;
import org.xml.sax.SAXException;

public class YahooParser {
    private static Logger logger = LogManager.getLogger(YahooParser.class);

    public Weather parse(InputStream inputStream) throws SAXException, DocumentException {

        logger.info("Creating XML Reader");
        SAXReader xmlReader = createXmlReader();
        Document doc = xmlReader.read(inputStream);

        logger.info("Parsing XML Response");
        return extractDataFromXml(doc);
    }

    public Weather parse(Reader reader) throws SAXException, DocumentException {

        logger.info("Creating XML Reader");
        SAXReader xmlReader = createXmlReader();
        Document doc = xmlReader.read(reader);

        logger.info("Parsing XML Response");
        return extractDataFromXml(doc);
    }

    private SAXReader createXmlReader() throws SAXException {
        Map<String, String> uris = new HashMap<>();
        uris.put("y", "http://xml.weather.yahoo.com/ns/rss/1.0");

        DocumentFactory factory = new DocumentFactory();
        factory.setXPathNamespaceURIs(uris);

        SAXReader xmlReader = new SAXReader();

        // XML parsers should not be vulnerable to XXE attacks (java:S2755)
        // For dom4j library, ACCESS_EXTERNAL_DTD and ACCESS_EXTERNAL_SCHEMA are not
        // supported, thus a very strict fix is to disable doctype declarations:
        xmlReader.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true); // Compliant

        xmlReader.setDocumentFactory(factory);
        return xmlReader;
    }

    private Weather extractDataFromXml(Document doc) {
        Weather weather = new Weather();
        weather.setCity(doc.valueOf("/rss/channel/y:location/@city"));
        weather.setRegion(doc.valueOf("/rss/channel/y:location/@region"));
        weather.setCountry(doc.valueOf("/rss/channel/y:location/@country"));
        weather.setCondition(doc.valueOf("/rss/channel/item/y:condition/@text"));
        weather.setTemp(doc.valueOf("/rss/channel/item/y:condition/@temp"));
        weather.setChill(doc.valueOf("/rss/channel/y:wind/@chill"));
        weather.setHumidity(doc.valueOf("/rss/channel/y:atmosphere/@humidity"));
        return weather;
    }
}
