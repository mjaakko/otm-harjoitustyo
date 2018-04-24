/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package valuuttalaskuri.ui;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Currency;
import java.util.List;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import valuuttalaskuri.common.CurrencyConverterService;
import valuuttalaskuri.common.HistoricalExchangeRateService;
import valuuttalaskuri.db.SQLExchangeRateDao;
import valuuttalaskuri.exchangerateprovider.ECBExchangeRateProvider;
import valuuttalaskuri.exchangerateprovider.ECBHistoricalExchangeRateProvider;
import valuuttalaskuri.exchangerateprovider.ExchangeRateProvider;
import valuuttalaskuri.exchangerateprovider.HistoricalExchangeRateProvider;

/**
 *
 * @author jaakko
 */
public class CurrencyConverterGui extends Application {
    private CurrencyConverterService service;
    
    private HistoricalExchangeRateService historyService;
    
    private ComboBox<Currency> fromComboBox;
    private ComboBox<Currency> toComboBox;
    private TextField fromAmount;
    private Label resultAmount;
    
    @Override
    public void init() {
        historyService = new HistoricalExchangeRateService(new ECBHistoricalExchangeRateProvider());
        
        ExchangeRateProvider erp = new ECBExchangeRateProvider();
        SQLExchangeRateDao erd = new SQLExchangeRateDao(new File("exchangerates.db"));
        try {
            erd.init();
        } catch (SQLException ex) {
            //Näytä virhedialogi
        }
        
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
        
        //Järjestä valuuttakoodin mukaan
        List<Currency> currencies = new ArrayList<>(service.getCurrencyConverter().getSupportedCurrencies());
        Collections.sort(currencies, (Currency currency1, Currency currency2) -> currency1.getCurrencyCode().compareTo(currency2.getCurrencyCode()));
        
        //Oikeassa sovelluksessa varmistetaan, että valuuttatiedot on ladattu
        toComboBox = new ComboBox(FXCollections.observableArrayList(currencies));
        toComboBox.setCellFactory(c -> new CurrencyListCell());
        toComboBox.setMaxWidth(200);
        toComboBox.setMinWidth(200);
        toComboBox.setButtonCell(new CurrencyListCell());
        toComboBox.getSelectionModel().selectFirst();
        toComboBox.getSelectionModel().selectedItemProperty().addListener(c -> doConversion());
        to.getChildren().add(toComboBox);
        
        resultAmount = new Label();
        resultAmount.prefWidthProperty().bind(to.widthProperty());
        to.getChildren().add(resultAmount);
        
        
        HBox from = new HBox();
        from.setSpacing(10);
        
        fromComboBox = new ComboBox(FXCollections.observableArrayList(currencies));
        fromComboBox.setCellFactory(c -> new CurrencyListCell());
        fromComboBox.setMaxWidth(200);
        fromComboBox.setMinWidth(200);
        fromComboBox.setButtonCell(new CurrencyListCell());
        fromComboBox.getSelectionModel().selectFirst();
        fromComboBox.getSelectionModel().selectedItemProperty().addListener(c -> doConversion());
        from.getChildren().add(fromComboBox);
        
        fromAmount = new TextField();
        fromAmount.prefWidthProperty().bind(from.widthProperty());
        fromAmount.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) {
                fromAmount.setText(oldValue);
            }
            
            doConversion();
        });
        from.getChildren().add(fromAmount);
        
        content.getChildren().add(from);
        content.getChildren().add(to);
        
        Button history = new Button("Historia");
        history.setOnAction(e -> {
            new HistoricalExchangeRateDialog(historyService, fromComboBox.getSelectionModel().getSelectedItem(), toComboBox.getSelectionModel().getSelectedItem());
        });
        
        content.getChildren().add(history);
        
        Scene scene = new Scene(root, 350, 150);
        
        stage.setScene(scene);
        stage.setTitle("Valuuttalaskuri");
        stage.setResizable(false);
        stage.show();
        
        /*new ExchangeRateUpdateDialog(service, (boolean updated) -> {
            if (!updated) {
                Platform.exit();
            }
        });*/
    }
    
    private void doConversion() {
        try {
            if (fromAmount.getText() == null || fromAmount.getText().isEmpty()) {
                resultAmount.setText("");
                return;
            }
            
            //Näytä tulokset oikealla määrällä desimaaleja
            int scale = toComboBox.getSelectionModel().getSelectedItem().getDefaultFractionDigits();
            if (scale == -1) {
                scale = 0;
            }
            
            BigDecimal result = service.getCurrencyConverter().convertFrom(fromComboBox.getSelectionModel().getSelectedItem(),
                    toComboBox.getSelectionModel().getSelectedItem(),
                    new BigDecimal(fromAmount.getText())).setScale(scale, RoundingMode.HALF_UP);
            
            resultAmount.setText(result.toPlainString()+" "+toComboBox.getSelectionModel().getSelectedItem().getCurrencyCode());
        } catch (Exception ex) {
        }
    }
}

