/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package valuuttalaskuri.db;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Currency;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.sqlite.JDBC;

/**
 *
 * @author jaakko
 */
public class SQLFavoriteCurrencyDao implements FavoriteCurrencyDao {
    static {
        //Ei pitäisi olla pakollinen, mutta ei toimi ilmankaan
        try {
            DriverManager.registerDriver(new JDBC());
        } catch (SQLException ex) {
        }
    }
    
    private File file;
    
    public SQLFavoriteCurrencyDao(File file) {
        this.file = file;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:" + file.getAbsolutePath());
    }

    public void init() throws SQLException {
        try (Connection c = getConnection()) {
            c.prepareStatement("CREATE TABLE IF NOT EXISTS FavoriteCurrency ("
                    + "currency text PRIMARY KEY,"
                    + "favorite boolean)").execute();
        }
    }
    
    @Override
    public Collection<Currency> getFavorites() throws SQLException {
        try (Connection c = getConnection()) {
            Set<Currency> favorites = new HashSet<>();
            
            ResultSet rs = c.prepareStatement("SELECT currency FROM FavoriteCurrency WHERE favorite == 1").executeQuery();
            
            while (rs.next()) {
                favorites.add(Currency.getInstance(rs.getString("currency")));
            }
            
            return favorites;
        }
    }

    @Override
    public void setFavorite(Currency currency, boolean favorite) throws SQLException {
        try (Connection c = getConnection()) {
            PreparedStatement ps = c.prepareStatement("INSERT OR REPLACE INTO FavoriteCurrency VALUES (?,?)");
            ps.setString(1, currency.getCurrencyCode());
            ps.setBoolean(2, favorite);
            
            ps.execute();
        }
    }
}
