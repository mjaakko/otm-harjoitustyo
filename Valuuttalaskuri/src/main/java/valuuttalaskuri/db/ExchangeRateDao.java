/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package valuuttalaskuri.db;

import java.util.Collection;
import java.util.List;
import valuuttalaskuri.common.ExchangeRate;

/**
 *
 * @author jaakko
 */
public interface ExchangeRateDao {
    void update(Collection<ExchangeRate> exchangeRates) throws Exception;
    List<ExchangeRate> findAll() throws Exception;
}
