/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package valuuttalaskuri.exchangerateprovider;

import java.util.Collection;
import valuuttalaskuri.common.ExchangeRate;

/**
 *
 * @author jaakko
 */
public interface ExchangeRateProvider {
    Collection<ExchangeRate> getExchangeRates() throws Exception;
}
