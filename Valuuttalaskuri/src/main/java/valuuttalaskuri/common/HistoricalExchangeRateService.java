/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package valuuttalaskuri.common;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Currency;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import valuuttalaskuri.exchangerateprovider.HistoricalExchangeRateProvider;

/**
 *
 * @author jaakko
 */
public class HistoricalExchangeRateService {
    private HistoricalExchangeRateProvider provider;
    
    public HistoricalExchangeRateService(HistoricalExchangeRateProvider provider) {
        this.provider = provider;
    }
    
    public Map<LocalDate, BigDecimal> getRatesForCurrencies(Currency from, Currency to, int maxDays) throws Exception {
        if (maxDays < 2) {
            throw new IllegalArgumentException("Data must be requested for atleast 2 days");
        }
        
        LocalDate fromDate = LocalDate.now().minusDays(maxDays);
        
        Map<LocalDate, BigDecimal> rates = new TreeMap<>();
        
        SortedMap<LocalDate, ExchangeRate> ratesFrom = collectionToTreeMap(provider.getExchangeRates(from)).tailMap(fromDate);
        SortedMap<LocalDate, ExchangeRate> ratesTo = collectionToTreeMap(provider.getExchangeRates(to)).tailMap(fromDate);
        
        for (ExchangeRate fromRate : ratesFrom.values()) {
            ExchangeRate toRate = ratesTo.get(fromRate.getDate());
            
            if (toRate == null) {
                continue;
            }
            
            BigDecimal rate = toRate.getRate().divide(fromRate.getRate(), toRate.getCurrency().getDefaultFractionDigits(), RoundingMode.HALF_UP);
            
            rates.put(fromRate.getDate(), rate);
        }
        
        return rates;
    }
    
    
    private static TreeMap<LocalDate, ExchangeRate> collectionToTreeMap(Collection<ExchangeRate> exchangeRates) {
        TreeMap<LocalDate, ExchangeRate> treeMap = new TreeMap<>();
        
        for (ExchangeRate exchangeRate : exchangeRates) {
            treeMap.put(exchangeRate.getDate(), exchangeRate);
        }
        
        return treeMap;
    }
}
