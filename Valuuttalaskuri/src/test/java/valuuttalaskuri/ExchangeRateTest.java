/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package valuuttalaskuri;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import valuuttalaskuri.common.ExchangeRate;

/**
 *
 * @author jaakko
 */
public class ExchangeRateTest {
    @Test
    public void testEquals() {
        ExchangeRate er1 = new ExchangeRate(Currency.getInstance("CAD"), BigDecimal.ONE, LocalDate.now());
        ExchangeRate er2 = new ExchangeRate(Currency.getInstance("CAD"), BigDecimal.ONE, LocalDate.now());
        
        assertEquals(er1, er2);
    }
    
    @Test
    public void testHashCode() {
        ExchangeRate er1 = new ExchangeRate(Currency.getInstance("CAD"), BigDecimal.ONE, LocalDate.now());
        ExchangeRate er2 = new ExchangeRate(Currency.getInstance("CAD"), BigDecimal.ONE, LocalDate.now());
        
        assertEquals(er1.hashCode(), er2.hashCode());
    }
}
