
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package valuuttalaskuri.common;

import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import valuuttalaskuri.currencyconverter.CurrencyConverter;
import valuuttalaskuri.exchangerateprovider.ExchangeRateProvider;
import valuuttalaskuri.db.ExchangeRateDao;

/**
 *
 * @author jaakko
 */
public class CurrencyConverterService {
    private ExchangeRateProvider exchangeRateProvider;
    private ExchangeRateDao exchangeRateDao;
    
    private CurrencyConverter converter;
    
    public CurrencyConverterService(ExchangeRateProvider provider, ExchangeRateDao dao) {
        exchangeRateProvider = provider;
        exchangeRateDao = dao;
    }
    
    /**
     * Tarkistaa onko valuuttamuunnin valmis käytettäväksi.
     * Jos valuuttamuunnin ei ole valmis käytettäväksi, kutsu metodia {@link #update()}
     * 
     * @return true, jos valuuttamuunnin on valmis käytettäväksi (eli sisältää datan vähintään kahdelle valuutalle)
     */
    public boolean isReady() throws Exception {
        return exchangeRateDao.findAll().size() >= 2;
    }
    
    /**
     * Lataa uudet valuuttakurssit ja päivittää ne tietokantaan
     * @throws Exception Jos kurssien lataaminen epäonnistuu (ei verkkoyhteyttä tms.) tai tietokantaan tallennnus epäonnistuu
     */
    public void update() throws Exception {
        exchangeRateDao.update(exchangeRateProvider.getExchangeRates());
       
        converter = new CurrencyConverter(listToMap(exchangeRateDao.findAll()));    
    }
    
    /**
     * Palauttaa CurrencyConverter-olion, jolla voi tehdä valuuttamuunnoksia
     * @throws IllegalStateException Jos valuuttamuunnin ei ole valmiskäytettäväksi, katso {@link #isReady()}
     * @return CurrencyConverter-olio valuuttamuunnoksiin
     */
    public CurrencyConverter getCurrencyConverter() throws Exception {
        if (!isReady()) {
            throw new IllegalStateException("Currency converter service is not ready for use");
        }
        
        if (converter == null) {
            converter = new CurrencyConverter(listToMap(exchangeRateDao.findAll()));
        }
    
        return converter;
    }
    
    private static Map<Currency, ExchangeRate> listToMap(List<ExchangeRate> list) {
        Map<Currency, ExchangeRate> map = new HashMap<>(list.size());
        for (ExchangeRate e : list) {
            map.put(e.getCurrency(), e);
        }
        return map;
    }
}
