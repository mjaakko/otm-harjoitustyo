/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package valuuttalaskuri;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import org.xmlpull.v1.XmlPullParserException;
import valuuttalaskuri.common.CurrencyConverterService;
import valuuttalaskuri.common.ExchangeRate;
import valuuttalaskuri.db.SQLExchangeRateDao;
import valuuttalaskuri.exchangerateprovider.ECBExchangeRateProvider;
import valuuttalaskuri.exchangerateprovider.ExchangeRateProvider;
import valuuttalaskuri.ui.CurrencyConverterGui;

/**
 *
 * @author jaakko
 */
public class Main {
    public static void main(String... args) {
        if (args.length >= 1 && args[0] != null && "--update-exchange-rates".equals(args[0])) {
            ExchangeRateProvider erp = new ECBExchangeRateProvider();
            SQLExchangeRateDao erd = new SQLExchangeRateDao(new File("exchangerates.db"));
            try {
                erd.init();
            } catch (SQLException ex) {
                System.out.println("Tietokannan avaaminen epäonnistui, tarkista kirjoitusoikeudet kansioon " + new File("").getAbsolutePath());
                System.exit(1);
            }
            
            CurrencyConverterService service = new CurrencyConverterService(erp, erd);
            System.out.println("Päivitetään valuuttakursseja...");
            try {
                service.update();
                System.out.println("Valuuttakurssit päivitetty");
            } catch (Exception ex) {
                System.out.println("Valuuttakurssien päivitys epäonnistui, tarkista internet-yhteys.");
                System.exit(1);
            }
        } else {
            Application.launch(CurrencyConverterGui.class);
        }
    }
}
