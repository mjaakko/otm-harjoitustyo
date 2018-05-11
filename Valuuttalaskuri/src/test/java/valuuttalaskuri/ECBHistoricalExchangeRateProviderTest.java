package valuuttalaskuri;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Currency;
import static org.junit.Assert.*;
import org.junit.Test;
import org.xmlpull.v1.XmlPullParserException;
import valuuttalaskuri.common.ExchangeRate;
import valuuttalaskuri.exchangerateprovider.ECBHistoricalExchangeRateProvider;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jaakko
 */
public class ECBHistoricalExchangeRateProviderTest {
    private static final String MOCK_DATA = "<CompactData xmlns=\"http://www.SDMX.org/resources/SDMXML/schemas/v2_0/message\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.SDMX.org/resources/SDMXML/schemas/v2_0/message https://stats.ecb.europa.eu/stats/vocabulary/sdmx/2.0/SDMXMessage.xsd\"><Header><ID>EXR-HIST_2018-04-30</ID><Test>false</Test><Name xml:lang=\"en\">Euro foreign exchange reference rates</Name><Prepared>2018-04-30T15:55:13</Prepared><Sender id=\"4F0\"><Name xml:lang=\"en\">European Central Bank</Name><Contact><Department xml:lang=\"en\">DG Statistics</Department><URI>mailto:statistics@ecb.europa.eu</URI></Contact></Sender><DataSetAgency>ECB</DataSetAgency><DataSetID>ECB_EXR_WEB</DataSetID><Extracted>2018-04-30T15:55:13</Extracted></Header><DataSet xmlns=\"http://www.ecb.europa.eu/vocabulary/stats/exr/1\" xsi:schemaLocation=\"http://www.ecb.europa.eu/vocabulary/stats/exr/1 https://stats.ecb.europa.eu/stats/vocabulary/exr/1/2006-09-04/sdmx-compact.xsd\"><Group CURRENCY=\"RON\" CURRENCY_DENOM=\"EUR\" EXR_TYPE=\"SP00\" EXR_SUFFIX=\"A\" DECIMALS=\"4\" UNIT=\"RON\" UNIT_MULT=\"0\" TITLE_COMPL=\"ECB reference exchange rate, Romanian leu/Euro, 2:15 pm (C.E.T.)\"/><Series FREQ=\"D\" CURRENCY=\"RON\" CURRENCY_DENOM=\"EUR\" EXR_TYPE=\"SP00\" EXR_SUFFIX=\"A\" TIME_FORMAT=\"P1D\" COLLECTION=\"A\"><Obs TIME_PERIOD=\"2005-07-01\" OBS_VALUE=\"3\" OBS_STATUS=\"A\" OBS_CONF=\"F\"/><Obs TIME_PERIOD=\"2005-07-04\" OBS_VALUE=\"4\" OBS_STATUS=\"A\" OBS_CONF=\"F\"/><Obs TIME_PERIOD=\"2005-07-05\" OBS_VALUE=\"5\" OBS_STATUS=\"A\" OBS_CONF=\"F\"/></Series></DataSet></CompactData>";


    @Test
    public void testParsing() throws XmlPullParserException, IOException {
        assertEquals(ECBHistoricalExchangeRateProvider.parse(new ByteArrayInputStream(MOCK_DATA.getBytes("UTF-8"))).size(), 3);
    }
    
    @Test
    public void testCorrectData() throws XmlPullParserException, IOException {
        Collection<ExchangeRate> l = ECBHistoricalExchangeRateProvider.parse(new ByteArrayInputStream(MOCK_DATA.getBytes("UTF-8")));
        
        boolean found = false;
        
        for (ExchangeRate ex : l) {
            if (ex.getRate().equals(new BigDecimal("4"))) {
                found = true;
                break;
            }
        }
        
        assertTrue(found);
    }
    
    @Test
    public void testEUR() throws Exception {
        assertFalse(new ECBHistoricalExchangeRateProvider().getExchangeRates(Currency.getInstance("EUR")).isEmpty());
    }
    
    @Test(expected = XmlPullParserException.class)
    public void testInvalidDataThrowsError() throws UnsupportedEncodingException, XmlPullParserException, IOException {
        ECBHistoricalExchangeRateProvider.parse(new ByteArrayInputStream("<testi></testi>".getBytes("UTF-8")));
    }
}
