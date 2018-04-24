/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package valuuttalaskuri.db;

import java.util.Collection;
import java.util.Currency;
import java.util.Set;

/**
 *
 * @author jaakko
 */
public interface FavoriteCurrencyDao {
    Collection<Currency> getFavorites();
    void setFavorite(Currency currency, boolean favorite);
}
