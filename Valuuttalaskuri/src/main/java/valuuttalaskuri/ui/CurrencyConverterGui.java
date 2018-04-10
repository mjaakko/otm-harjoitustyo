/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package valuuttalaskuri.ui;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Currency;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.converter.BigDecimalStringConverter;
import valuuttalaskuri.common.CurrencyConverterService;
import valuuttalaskuri.common.ExchangeRate;
import valuuttalaskuri.db.ExchangeRateDao;
import valuuttalaskuri.db.MockExchangeRateDao;
import valuuttalaskuri.exchangerateprovider.ExchangeRateProvider;
import valuuttalaskuri.exchangerateprovider.MockExchangeRateProvider;

/**
 *
 * @author jaakko
 */
public class CurrencyConverterGui extends Application {
    private CurrencyConverterService service;
    
    @Override
    public void init() {
        ExchangeRateProvider erp = new MockExchangeRateProvider(new ExchangeRate(Currency.getInstance("IRR"), new BigDecimal("2000"), LocalDate.now()),
            new ExchangeRate(Currency.getInstance("EUR"), new BigDecimal("1"), LocalDate.now()), 
            new ExchangeRate(Currency.getInstance("AMD"), new BigDecimal("300"), LocalDate.now()));
        ExchangeRateDao erd = new MockExchangeRateDao();
        
        service = new CurrencyConverterService(erp, erd);
        
        try {
            //Oikeassa sovelluksessa päivitys tehdään vain tarvittaessa
            service.update();
        } catch (Exception ex) {
        }
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        MenuBar menu = new MenuBar();
        Menu settings = new Menu("Asetukset");
        
        menu.getMenus().add(settings);
        
        BorderPane root = new BorderPane();
        root.setTop(menu);
        
        VBox content = new VBox();
        content.setPadding(new Insets(10, 10, 10, 10));
        content.setSpacing(10);
        
        root.setCenter(content);
        
        
        HBox to = new HBox();
        to.setSpacing(10);
        
        //Oikeassa sovelluksessa varmistetaan, että valuuttatiedot on ladattu
        ComboBox<Currency> toComboBox = new ComboBox(FXCollections.observableArrayList(service.getCurrencyConverter().getSupportedCurrencies()));
        toComboBox.setCellFactory(c -> new CurrencyListCell());
        toComboBox.setMaxWidth(200);
        toComboBox.setMinWidth(200);
        toComboBox.setButtonCell(new CurrencyListCell());
        toComboBox.getSelectionModel().selectFirst();
        to.getChildren().add(toComboBox);
        
        Label resultAmount = new Label();
        resultAmount.prefWidthProperty().bind(to.widthProperty());
        to.getChildren().add(resultAmount);
        
        
        HBox from = new HBox();
        from.setSpacing(10);
        
        ComboBox<Currency> fromComboBox = new ComboBox(FXCollections.observableArrayList(service.getCurrencyConverter().getSupportedCurrencies()));
        fromComboBox.setCellFactory(c -> new CurrencyListCell());
        fromComboBox.setMaxWidth(200);
        fromComboBox.setMinWidth(200);
        fromComboBox.setButtonCell(new CurrencyListCell());
        fromComboBox.getSelectionModel().selectFirst();
        from.getChildren().add(fromComboBox);
        
        TextField fromAmount = new TextField();
        fromAmount.prefWidthProperty().bind(from.widthProperty());
        fromAmount.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) {
                fromAmount.setText(oldValue);
            }
            
            
            try {
                resultAmount.setText(service.getCurrencyConverter().convertFrom(fromComboBox.getSelectionModel().getSelectedItem(), 
                        toComboBox.getSelectionModel().getSelectedItem(), 
                        new BigDecimal(fromAmount.getText())).setScale(2, RoundingMode.HALF_UP).toPlainString()+" "+toComboBox.getSelectionModel().getSelectedItem().getCurrencyCode());
            } catch (Exception ex) {
                //Oikeassa versiossa näytetään virheilmoitus 
                ex.printStackTrace();
            }
        });
        from.getChildren().add(fromAmount);
        
        content.getChildren().add(from);
        content.getChildren().add(to);
        
        Scene scene = new Scene(root, 320, 150);
        
        stage.setScene(scene);
        stage.setTitle("Valuuttalaskuri");
        stage.setResizable(false);
        stage.show();
    }
}

