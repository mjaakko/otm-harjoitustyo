/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package valuuttalaskuri;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import org.xmlpull.v1.XmlPullParserException;
import valuuttalaskuri.common.ExchangeRate;
import valuuttalaskuri.exchangerateprovider.ECBExchangeRateProvider;
import valuuttalaskuri.ui.CurrencyConverterGui;

/**
 *
 * @author jaakko
 */
public class Main {
    public static void main(String... args) {
        Application.launch(CurrencyConverterGui.class);
    }
}
