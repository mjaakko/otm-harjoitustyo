/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package valuuttalaskuri.db;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Currency;
import java.util.List;
import valuuttalaskuri.common.ExchangeRate;

/**
 *
 * @author jaakko
 */
public class SQLExchangeRateDao implements ExchangeRateDao {
    private File file;
    
    public SQLExchangeRateDao(File file) {
        this.file = file;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:"+file.getAbsolutePath());
    }
    
    public void init() throws SQLException {
        try (Connection c = getConnection()) {
            c.prepareStatement("CREATE TABLE IF NOT EXISTS ExchangeRate ("
                    + "currency text PRIMARY KEY,"
                    + "rate real,"
                    + "date text)").execute();
        }
    }
    
    public void update(Collection<ExchangeRate> exchangeRates) throws SQLException {
        Connection c = getConnection();
        
        try {
            c.setAutoCommit(false);
            
            for (ExchangeRate e : exchangeRates) {
                PreparedStatement ps = c.prepareStatement("INSERT OR REPLACE INTO ExchangeRate VALUES (?, ?, ?)");
                ps.setString(1, e.getCurrency().getCurrencyCode());
                ps.setDouble(2, e.getRate().doubleValue());
                ps.setString(3, e.getDate().toString());
                
                ps.execute();
            }
            
            c.commit();
        } catch (Exception e) {
            c.rollback();
            
            throw e;
        } finally {
            c.close();
        }
    }
    
    public List<ExchangeRate> findAll() throws SQLException {
        try (Connection c = getConnection()) {
            ResultSet rs = c.prepareStatement("SELECT * FROM ExchangeRate").executeQuery();
            
            List<ExchangeRate> result = new ArrayList<>();
            while(rs.next()) {
                result.add(new ExchangeRate(Currency.getInstance(rs.getString("currency")), 
                        BigDecimal.valueOf(rs.getDouble("rate")), 
                        LocalDate.parse(rs.getString("date"))));
            }
            
            return result;
        }
    }
}

