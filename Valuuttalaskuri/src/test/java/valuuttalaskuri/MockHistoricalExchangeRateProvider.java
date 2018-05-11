/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package valuuttalaskuri;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Currency;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import valuuttalaskuri.common.ExchangeRate;
import valuuttalaskuri.exchangerateprovider.HistoricalExchangeRateProvider;

/**
 *
 * @author jaakko
 */
public class MockHistoricalExchangeRateProvider implements HistoricalExchangeRateProvider {

    @Override
    public Collection<ExchangeRate> getExchangeRates(Currency currency) throws Exception {
        List<ExchangeRate> fakeData = new ArrayList<>();
        
        LocalDate date = LocalDate.now();
        
        for (int i = 0; i < 10000; i++) {
            fakeData.add(new ExchangeRate(currency, BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(0, 10000)), date));
            
            date = date.minusDays(1);
        }
        
        return fakeData;
    }
    
}
