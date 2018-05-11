/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package valuuttalaskuri;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Currency;
import java.util.Map;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import valuuttalaskuri.common.HistoricalExchangeRateService;

/**
 *
 * @author jaakko
 */
public class HistoricalExchangeRateServiceTest {
    private HistoricalExchangeRateService service;
    
    @Before
    public void setup() {
        service = new HistoricalExchangeRateService(new MockHistoricalExchangeRateProvider());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testThrowsErrorOnInvalidRequest() throws Exception {
        service.getRatesForCurrencies(Currency.getInstance("OMR"), Currency.getInstance("LKR"), 1);
    }
    
    @Test
    public void testContainsOnlyDataFromWantedTimeperiod() throws Exception {
        Map<LocalDate, BigDecimal> rates = service.getRatesForCurrencies(Currency.getInstance("AED"), Currency.getInstance("HKD"), 30);
        assertTrue(Collections.min(rates.keySet()).compareTo(LocalDate.now().minusDays(30)) >= 0);
    }
}
