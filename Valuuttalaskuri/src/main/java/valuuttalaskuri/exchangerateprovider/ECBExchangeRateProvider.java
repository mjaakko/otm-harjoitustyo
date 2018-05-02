/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package valuuttalaskuri.exchangerateprovider;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Currency;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import valuuttalaskuri.common.ExchangeRate;

import static valuuttalaskuri.util.XMLUtil.skip;

/**
 *
 * @author jaakko
 */
public class ECBExchangeRateProvider implements ExchangeRateProvider {
    private static final String URL = "https://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml";
    
    /**
     * Lataa tuoreimmat valuuttakurssit Euroopan keskuspankilta ja palauttaa ne listana
     * @return Lista valuuttakursseista
     * @throws IOException Jos verkkoyhteys ei toimi tai muu IO-virhe
     * @throws XmlPullParserException Jos data ei ole validia
     */
    @Override
    public Collection<ExchangeRate> getExchangeRates() throws IOException, XmlPullParserException {
        URLConnection conn = new URL(URL).openConnection();
        try (InputStream is = new BufferedInputStream(conn.getInputStream())) {
            return parse(is);
        }
    }
    
    /**
     * Parsii annetusta InputStreamista XML-muodossa olevat valuuttakurssit
     * @param is InputStream
     * @return InputStreamista parsitut valuuttakurssit listana
     * @throws XmlPullParserException Jos InputStream ei sisällä validia dataa
     * @throws IOException Jos InputStreamin lukeminen epäonnistuu
     */
    public static List<ExchangeRate> parse(InputStream is) throws XmlPullParserException, IOException {
        XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
        parser.setInput(is, "UTF-8");
        
        parser.nextTag();
        return readEnvelope(parser);
    }
    
    private static List<ExchangeRate> readEnvelope(XmlPullParser xpp) throws XmlPullParserException, IOException {
        List<ExchangeRate> exchangeRates = null;
        
        while (xpp.next() != XmlPullParser.END_TAG) {
            if (xpp.getEventType() != XmlPullParser.START_TAG) {
                continue;
            } 

            String name = xpp.getName();
            if ("Cube".equals(name)) {
                exchangeRates = readCube1(xpp);
            } else {
                skip(xpp);
            }
        }
        
        return exchangeRates;
    }
    
    private static List<ExchangeRate> readCube1(XmlPullParser xpp) throws XmlPullParserException, IOException {
        List<ExchangeRate> exchangeRates = null;
        
        while (xpp.next() != XmlPullParser.END_TAG) {
            if (xpp.getEventType() != XmlPullParser.START_TAG) {
                continue;
            } 
           
            String name = xpp.getName();
            if ("Cube".equals(name)) {
                LocalDate date = LocalDate.parse(xpp.getAttributeValue(null, "time"));

                exchangeRates = readCube2(xpp, date);
                //Lisää dataan euron vaihtokurssi, 1 EUR = 1 EUR
                exchangeRates.add(new ExchangeRate(Currency.getInstance("EUR"), BigDecimal.ONE, date));
            } else {
                skip(xpp);
            }
        }
        
        return exchangeRates;
    }
    
    private static List<ExchangeRate> readCube2(XmlPullParser xpp, LocalDate date) throws XmlPullParserException, IOException {
        List<ExchangeRate> exchangeRates = new ArrayList<>();
        
        while (xpp.next() != XmlPullParser.END_TAG) {
            if (xpp.getEventType() != XmlPullParser.START_TAG) {
                continue;
            } 
           
            String name = xpp.getName();
            if ("Cube".equals(name)) {
                Currency currency = Currency.getInstance(xpp.getAttributeValue(null, "currency"));
                BigDecimal rate = new BigDecimal(xpp.getAttributeValue(null, "rate"));
                
                exchangeRates.add(new ExchangeRate(currency, rate, date));
            }
            skip(xpp);
        }
        
        return exchangeRates;
    }
}
