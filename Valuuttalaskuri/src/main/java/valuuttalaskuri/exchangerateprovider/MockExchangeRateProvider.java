/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package valuuttalaskuri.exchangerateprovider;

import java.util.Arrays;
import java.util.Collection;
import valuuttalaskuri.common.ExchangeRate;

/**
 *
 * @author jaakko
 */
public class MockExchangeRateProvider implements ExchangeRateProvider {
    private Collection<ExchangeRate> exchangeRates;
    
    public MockExchangeRateProvider(ExchangeRate... exchangeRates) {
        this.exchangeRates = Arrays.asList(exchangeRates);
    }
    
    @Override
    public Collection<ExchangeRate> getExchangeRates() throws Exception {
        return exchangeRates;
    }
    
}
