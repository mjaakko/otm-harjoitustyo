/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package valuuttalaskuri.currencyconverter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Currency;
import java.util.Map;
import java.util.Set;
import valuuttalaskuri.common.ExchangeRate;

/**
 *
 * @author jaakko
 */
public class CurrencyConverter {
    private Map<Currency, ExchangeRate> exchangeRates;
    
    public CurrencyConverter(Map<Currency, ExchangeRate> exchangeRates) {
        this.exchangeRates = exchangeRates;
    }
    
    /**
     * 
     * @return Lista valuutoista, joilla muunnoksia voi tehdä 
     */
    public Set<Currency> getSupportedCurrencies() {
        return exchangeRates.keySet();
    }
    
    /**
     * Kertoo päivän, jolloin valuutan kurssi päivitettiin
     * @param currency Valuutta, jonka päivitysaika halutaan selvittää
     * @return Päivä, jolloin valuutan kurssi on päivitetty
     */
    public LocalDate getUpdateDate(Currency currency) {
        return exchangeRates.get(currency).getDate();
    }
    
    /**
     * Tekee muunnoksen valuutasta toiseen
     * @param from Valuutta, josta muunnetaan
     * @param to Valuutta, johon muunnetaan
     * @param amount Määrä
     * @return Määrä valuutassa, johon muunnettaan
     */
    public BigDecimal convertFrom(Currency from, Currency to, BigDecimal amount) {
        if (!exchangeRates.containsKey(from)) {
            throw new IllegalArgumentException("No data for currency " + from.getCurrencyCode());
        }
        if (!exchangeRates.containsKey(to)) {
            throw new IllegalArgumentException("No data for currency " + to.getCurrencyCode());
        }
        if (amount.signum() == -1) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        
        BigDecimal amountInBase = amount.divide(exchangeRates.get(from).getRate(), 10, RoundingMode.HALF_UP);
        
        return amountInBase.multiply(exchangeRates.get(to).getRate());
    }
}
