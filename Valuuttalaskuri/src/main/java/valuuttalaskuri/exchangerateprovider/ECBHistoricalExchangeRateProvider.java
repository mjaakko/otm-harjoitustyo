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
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Currency;
import java.util.List;
import java.util.Locale;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import valuuttalaskuri.common.ExchangeRate;

import static valuuttalaskuri.util.XMLUtil.skip;

/**
 *
 * @author jaakko
 */
public class ECBHistoricalExchangeRateProvider implements HistoricalExchangeRateProvider {
    private static final LocalDate EUR_INTRODUCTION_DATE = LocalDate.of(1999, 1, 1);
    
    private static String createURL(Currency currency) {
        return "https://www.ecb.europa.eu/stats/policy_and_exchange_rates/euro_reference_exchange_rates/html/" + currency.getCurrencyCode().toLowerCase(Locale.US) + ".xml";
    }
    
    /**
     * Lataa historialliset valuuttakurssit yhdelle valuutalle
     * @param currency Valuutta, jolle historialliset valuuttakurssit ladataan
     * @return Lista historiallisia valuuttakursseja. Lista ei välttämättä ole järjestyksessä..
     * @throws Exception Jos valuuttakurssien lataaminen epäonnistuu (esim. ei verkkoyhteyttä)
     */
    @Override
    public Collection<ExchangeRate> getExchangeRates(Currency currency) throws Exception {
        //1 euro on aina 1 euro..
        if ("EUR".equals(currency.getCurrencyCode())) {
            List<ExchangeRate> eurExchangeRates = new ArrayList<>(8000);
            
            LocalDate date = LocalDate.now(ZoneId.of("Europe/Berlin"));
            
            while (date.isAfter(EUR_INTRODUCTION_DATE)) {
                eurExchangeRates.add(new ExchangeRate(Currency.getInstance("EUR"), BigDecimal.ONE, date));
                
                date = date.minusDays(1);
            }
            
            return eurExchangeRates;
        }
        
        //TODO: tarkista 404
        URLConnection conn = new URL(createURL(currency)).openConnection();
        
        try (InputStream is = new BufferedInputStream(conn.getInputStream())) {
            return parse(is);
        }
    }

    /**
     * Parsii valuuttakurssit InputStreamista
     * @param is InputStream
     * @return Lista valuuttakursseista
     * @throws XmlPullParserException Jos data ei ole validia
     * @throws IOException Jos datan lukeminen epäonnistuu
     */
    public static List<ExchangeRate> parse(InputStream is) throws XmlPullParserException, IOException {
        XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
        parser.setInput(is, "UTF-8");
        
        parser.nextTag();
        return readCompactData(parser);
    }
    
    private static List<ExchangeRate> readCompactData(XmlPullParser xpp) throws XmlPullParserException, IOException {
        List<ExchangeRate> exchangeRates = null;
        
        while (xpp.next() != XmlPullParser.END_TAG) {
            if (xpp.getEventType() != XmlPullParser.START_TAG) {
                continue;
            } 

            String name = xpp.getName();
            if ("DataSet".equals(name)) {
                exchangeRates = readDataSet(xpp);
            } else {
                skip(xpp);
            }
        }
        
        return exchangeRates;
    }
    
    private static List<ExchangeRate> readDataSet(XmlPullParser xpp) throws XmlPullParserException, IOException {
        List<ExchangeRate> exchangeRates = null;
        
        while (xpp.next() != XmlPullParser.END_TAG) {
            if (xpp.getEventType() != XmlPullParser.START_TAG) {
                continue;
            } 

            String name = xpp.getName();
            if ("Series".equals(name)) {
                exchangeRates = readSeries(xpp);
            } else {
                skip(xpp);
            }
        }
        
        return exchangeRates;
    }
    
    private static List<ExchangeRate> readSeries(XmlPullParser xpp) throws XmlPullParserException, IOException {
        List<ExchangeRate> exchangeRates = new ArrayList<>();
        
        Currency currency = Currency.getInstance(xpp.getAttributeValue(null, "CURRENCY"));
        
        while (xpp.next() != XmlPullParser.END_TAG) {
            if (xpp.getEventType() != XmlPullParser.START_TAG) {
                continue;
            } 

            String name = xpp.getName();
            if ("Obs".equals(name)) {
                BigDecimal rate = new BigDecimal(xpp.getAttributeValue(null, "OBS_VALUE"));
                LocalDate date = LocalDate.parse(xpp.getAttributeValue(null, "TIME_PERIOD"));
                
                exchangeRates.add(new ExchangeRate(currency, rate, date));
                
                skip(xpp);
            } else {
                skip(xpp);
            }
        }
        
        return exchangeRates;
    }
}
