/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package valuuttalaskuri;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import valuuttalaskuri.common.CurrencyConverterService;
import valuuttalaskuri.common.ExchangeRate;
import valuuttalaskuri.db.ExchangeRateDao;
import valuuttalaskuri.exchangerateprovider.ExchangeRateProvider;

public class CurrencyConverterServiceTest {
    private ExchangeRateDao dao;
    private ExchangeRateProvider provider;
    
    @Before
    public void setup() {
        provider = new MockExchangeRateProvider(new ExchangeRate(Currency.getInstance("IRR"), BigDecimal.ONE, LocalDate.now()), 
                new ExchangeRate(Currency.getInstance("USD"), BigDecimal.TEN, LocalDate.now()),
                new ExchangeRate(Currency.getInstance("OMR"), new BigDecimal("2"), LocalDate.now()));
        dao = new MockExchangeRateDao();
    }
    
    @Test
    public void notReadyBeforeUpdate() throws Exception {
        CurrencyConverterService service = new CurrencyConverterService(provider, dao);
        
        assertFalse(service.isReady());
    }
    
    @Test
    public void readyAfterUpdate() throws Exception {
        CurrencyConverterService service = new CurrencyConverterService(provider, dao);
        
        assertFalse(service.isReady());
        service.update();
        assertTrue(service.isReady());
    }
    
    @Test
    public void currencyConverterNotNullAfterUpdate() throws Exception {
        CurrencyConverterService service = new CurrencyConverterService(provider, dao);
        service.update();
        
        assertNotNull(service.getCurrencyConverter());
    }
}
