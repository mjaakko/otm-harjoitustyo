/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package valuuttalaskuri;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import valuuttalaskuri.common.ExchangeRate;
import valuuttalaskuri.currencyconverter.CurrencyConverter;

/**
 *
 * @author jaakko
 */
public class CurrencyConverterTest {
    private CurrencyConverter converter;
    
    @Before
    public void setup() {
        Map<Currency, ExchangeRate> exchangeRates = new HashMap<>();
        exchangeRates.put(Currency.getInstance("USD"), new ExchangeRate(Currency.getInstance("USD"), new BigDecimal("2"), LocalDate.now()));
        exchangeRates.put(Currency.getInstance("EUR"), new ExchangeRate(Currency.getInstance("EUR"), new BigDecimal("1"), LocalDate.now()));
        
        converter = new CurrencyConverter(exchangeRates);
    }
    
    @Test
    public void testConversionPositive() {
        BigDecimal result = converter.convertFrom(Currency.getInstance("EUR"), Currency.getInstance("USD"), BigDecimal.ONE);
        
        assertEquals(2.0, result.doubleValue(), 0.001);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConversionNegative() {
        BigDecimal result = converter.convertFrom(Currency.getInstance("EUR"), Currency.getInstance("USD"), new BigDecimal("-1"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConversionInvalidCurrency() {
        BigDecimal result = converter.convertFrom(Currency.getInstance("AZN"), Currency.getInstance("USD"), new BigDecimal("-1"));
    }
    
    @Test
    public void testCurrencyList() {
        assertTrue(converter.getSupportedCurrencies().contains(Currency.getInstance("USD")));
    }
}
