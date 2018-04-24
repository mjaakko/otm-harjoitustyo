/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package valuuttalaskuri;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Test;
import org.xmlpull.v1.XmlPullParserException;
import valuuttalaskuri.common.ExchangeRate;
import valuuttalaskuri.exchangerateprovider.ECBExchangeRateProvider;

public class ECBExchangeRateProviderTest {
    private static final String MOCK_DATA = "<gesmes:Envelope xmlns:gesmes=\"http://www.gesmes.org/xml/2002-08-01\" xmlns=\"http://www.ecb.int/vocabulary/2002-08-01/eurofxref\">\n" +
"<gesmes:subject>Reference rates</gesmes:subject>\n" +
"<gesmes:Sender>\n" +
"<gesmes:name>European Central Bank</gesmes:name>\n" +
"</gesmes:Sender>\n" +
"<Cube>\n" +
"<Cube time=\"2018-04-24\">\n" +
"<Cube currency=\"USD\" rate=\"1\"/>\n" +
"<Cube currency=\"JPY\" rate=\"2\"/>\n" +
"</Cube>\n" +
"</Cube>\n" +
"</gesmes:Envelope>";
    
    @Test
    public void testParsing() throws UnsupportedEncodingException, XmlPullParserException, IOException {
        List<ExchangeRate> exchangeRates = ECBExchangeRateProvider.parse(new ByteArrayInputStream(MOCK_DATA.getBytes("UTF-8")));
        
        assertEquals(exchangeRates.size(), 3);
    }
    
    @Test
    public void testCorrectData() throws UnsupportedEncodingException, XmlPullParserException, IOException {
        List<ExchangeRate> exchangeRates = ECBExchangeRateProvider.parse(new ByteArrayInputStream(MOCK_DATA.getBytes("UTF-8")));
        
        assertEquals(exchangeRates.get(0).getDate(), LocalDate.of(2018, 4, 24));
    }
}
