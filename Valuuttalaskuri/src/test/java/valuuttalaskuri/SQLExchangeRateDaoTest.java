/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package valuuttalaskuri;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Currency;
import java.util.List;
import junit.framework.Assert;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import valuuttalaskuri.common.ExchangeRate;
import valuuttalaskuri.db.SQLExchangeRateDao;
/**
 *
 * @author jaakko
 */
public class SQLExchangeRateDaoTest {
    private File file;
    private SQLExchangeRateDao dao;
    
    @Before
    public void setup() throws IOException, SQLException {
        this.file = File.createTempFile("test", ".db");
        this.dao = new SQLExchangeRateDao(file);
        this.dao.init();
    }
    
    @After
    public void cleanup() {
        file.delete();
    }
    
    @Test
    public void updateCurrenciesTest() throws SQLException {
        List<ExchangeRate> exchangeRates = Arrays.asList(new ExchangeRate(Currency.getInstance("EUR"), BigDecimal.TEN, LocalDate.now()), 
                new ExchangeRate(Currency.getInstance("IRR"), BigDecimal.ONE, LocalDate.now()));
        
        dao.update(exchangeRates);
        
        assertEquals(dao.findAll().size(), 2);
    }
    
    @Test
    public void updateOldValuesTest() throws SQLException {
        dao.update(Arrays.asList(new ExchangeRate(Currency.getInstance("EUR"), BigDecimal.TEN, LocalDate.now()), 
                new ExchangeRate(Currency.getInstance("IRR"), BigDecimal.ONE, LocalDate.now())));
        
        dao.update(Arrays.asList(new ExchangeRate(Currency.getInstance("EUR"), BigDecimal.ONE, LocalDate.now())));
        
        assertEquals(dao.findAll().size(), 2);
    }
    
    @Test
    public void emptyInitiallyTest() throws SQLException {
        assertEquals(dao.findAll().size(), 0);
    }
}
