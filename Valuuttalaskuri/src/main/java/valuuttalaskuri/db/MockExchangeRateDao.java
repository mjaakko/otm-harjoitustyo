/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package valuuttalaskuri.db;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import valuuttalaskuri.common.ExchangeRate;

/**
 *
 * @author jaakko
 */
public class MockExchangeRateDao implements ExchangeRateDao {
    private Collection<ExchangeRate> list;

    @Override
    public void update(Collection<ExchangeRate> exchangeRates) throws Exception {
        this.list = exchangeRates;
    }

    @Override
    public List<ExchangeRate> findAll() throws Exception {
        if (list == null) {
            return Collections.emptyList();
        }
        
        return new ArrayList<>(list);
    }
    
    
}
