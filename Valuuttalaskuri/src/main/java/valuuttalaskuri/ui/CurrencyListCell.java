/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package valuuttalaskuri.ui;

import java.util.Currency;
import java.util.Locale;
import javafx.scene.control.ListCell;

/**
 *
 * @author jaakko
 */
public class CurrencyListCell extends ListCell<Currency> {
    @Override 
    protected void updateItem(Currency item, boolean empty) {
        super.updateItem(item, empty);

        if (item == null || empty) {
            setText(null);
        } else {
            setText(item.getCurrencyCode()+" - "+item.getDisplayName(Locale.US));
        }
    }
}
