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
import valuuttalaskuri.db.SQLFavoriteCurrencyDao;
/**
 *
 * @author jaakko
 */
public class SQLFavoriteCurrencyDaoTest {
    private File file;
    private SQLFavoriteCurrencyDao dao;
    
    @Before
    public void setup() throws IOException, SQLException {
        this.file = File.createTempFile("test", ".db");
        this.dao = new SQLFavoriteCurrencyDao(file);
        this.dao.init();
    }
    
    @After
    public void cleanup() {
        file.delete();
    }
    
    @Test
    public void addFavorite() throws SQLException {
        dao.setFavorite(Currency.getInstance("OMR"), true);
        
        assertTrue(dao.getFavorites().contains(Currency.getInstance("OMR")));
    }
    
    @Test
    public void removeFavorite() throws SQLException {
       dao.setFavorite(Currency.getInstance("OMR"), true);
        
       assertTrue(dao.getFavorites().contains(Currency.getInstance("OMR")));
       
       dao.setFavorite(Currency.getInstance("OMR"), false);
        
       assertFalse(dao.getFavorites().contains(Currency.getInstance("OMR")));
    }
    
    @Test
    public void addMultiple() throws SQLException {
        dao.setFavorite(Currency.getInstance("OMR"), true);
        dao.setFavorite(Currency.getInstance("RON"), true);
        
        assertTrue(dao.getFavorites().contains(Currency.getInstance("RON")));
        assertTrue(dao.getFavorites().contains(Currency.getInstance("OMR")));
    }
    
    @Test
    public void emptyInitiallyTest() throws SQLException {
        assertEquals(dao.getFavorites().size(), 0);
    }
}
