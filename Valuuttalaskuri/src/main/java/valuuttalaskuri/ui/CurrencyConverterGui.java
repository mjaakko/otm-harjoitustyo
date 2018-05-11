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
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Currency;
import java.util.List;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import valuuttalaskuri.common.CurrencyConverterService;
import valuuttalaskuri.common.HistoricalExchangeRateService;
import valuuttalaskuri.db.SQLExchangeRateDao;
import valuuttalaskuri.db.SQLFavoriteCurrencyDao;
import valuuttalaskuri.exchangerateprovider.ECBExchangeRateProvider;
import valuuttalaskuri.exchangerateprovider.ECBHistoricalExchangeRateProvider;
import valuuttalaskuri.exchangerateprovider.ExchangeRateProvider;

/**
 *
 * @author jaakko
 */
public class CurrencyConverterGui extends Application {
    private Image favorite;
    private Image unfavorite;
    
    private CurrencyConverterService service;
    
    private HistoricalExchangeRateService historyService;
    
    private SQLFavoriteCurrencyDao favoriteCurrencyDao;
    
    private ComboBox<Currency> fromComboBox;
    private ComboBox<Currency> toComboBox;
    private TextField fromAmount;
    private Label resultAmount;
    private Button toFavorite;
    private Button fromFavorite;
    
    @Override
    public void init() {
        favorite = new Image(getClass().getResource("/favorite.png").toString(), 18, 18, true, true, true);
        unfavorite = new Image(getClass().getResource("/unfavorite.png").toString(), 18, 18, true, true, true);
        
        historyService = new HistoricalExchangeRateService(new ECBHistoricalExchangeRateProvider());
        
        ExchangeRateProvider erp = new ECBExchangeRateProvider();
        SQLExchangeRateDao erd = new SQLExchangeRateDao(new File("exchangerates.db"));
        try {
            erd.init();
        } catch (SQLException ex) {
            showErrorDialog("Virhe tietokannan luomisessa", "Tarkista kirjoitusoikeudet kansiossa "+new File("").getAbsolutePath());
            Platform.exit();
        }
        
        service = new CurrencyConverterService(erp, erd);
        
        favoriteCurrencyDao = new SQLFavoriteCurrencyDao(new File("favorite_currencies.db"));
        try {
            favoriteCurrencyDao.init();
        } catch (SQLException ex) {
            showErrorDialog("Virhe tietokannan luomisessa", "Tarkista kirjoitusoikeudet kansiossa "+new File("").getAbsolutePath());
            Platform.exit();
        }
        
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        MenuBar menu = new MenuBar();
        Menu settings = new Menu("Asetukset");
        MenuItem update = new MenuItem("Päivitä valuuttakurssit");
        update.setOnAction(e -> {
            new ExchangeRateUpdateDialog(service, u -> {
                
            });
        });
        settings.getItems().add(update);
        
        menu.getMenus().add(settings);
        
        BorderPane root = new BorderPane();
        root.setTop(menu);
        
        VBox content = new VBox();
        content.setPadding(new Insets(10, 10, 10, 10));
        content.setSpacing(10);
        
        root.setCenter(content);
        
        HBox to = new HBox();
        to.setSpacing(10);
        
        toComboBox = new ComboBox();
        toComboBox.setCellFactory(c -> new CurrencyListCell());
        toComboBox.setMaxWidth(200);
        toComboBox.setMinWidth(200);
        toComboBox.setButtonCell(new CurrencyListCell());
        toComboBox.getSelectionModel().selectedItemProperty().addListener(c -> {
            try {
                updateFavorites();
            } catch (Exception ex) {}
            doConversion();
        });
        to.getChildren().add(toComboBox);
        
        toFavorite = new Button();
        toFavorite.maxHeightProperty().bind(toComboBox.heightProperty());
        toFavorite.maxWidthProperty().bind(toFavorite.heightProperty());
        toFavorite.setOnAction(e -> {
            try {
                Currency currency = toComboBox.getSelectionModel().getSelectedItem();
                if (currency == null) {
                    return;
                }
                
                boolean favorite = favoriteCurrencyDao.getFavorites().contains(currency);
                
                favoriteCurrencyDao.setFavorite(currency, !favorite);
                
                updateFavorites();
                updateComboBox();
            } catch (Exception ex) {
            }
        });
        to.getChildren().add(toFavorite);
        
        resultAmount = new Label();
        resultAmount.prefWidthProperty().bind(to.widthProperty());
        to.getChildren().add(resultAmount);
        
        HBox from = new HBox();
        from.setSpacing(10);
        
        fromComboBox = new ComboBox();
        fromComboBox.setCellFactory(c -> new CurrencyListCell());
        fromComboBox.setMaxWidth(200);
        fromComboBox.setMinWidth(200);
        fromComboBox.setButtonCell(new CurrencyListCell());
        fromComboBox.getSelectionModel().selectedItemProperty().addListener(c -> {
            try {
                updateFavorites();
            } catch (Exception ex) {}
            doConversion();
        });
        from.getChildren().add(fromComboBox);
        
        fromFavorite = new Button();
        fromFavorite.maxHeightProperty().bind(fromComboBox.heightProperty());
        fromFavorite.maxWidthProperty().bind(fromFavorite.heightProperty());
        fromFavorite.setOnAction(e -> {
            try {
                Currency currency = fromComboBox.getSelectionModel().getSelectedItem();
                if (currency == null) {
                    return;
                }
                
                boolean favorite = favoriteCurrencyDao.getFavorites().contains(currency);
                
                favoriteCurrencyDao.setFavorite(currency, !favorite);
                
                updateFavorites();
                updateComboBox();
            } catch (Exception ex) {
            }
        });
        from.getChildren().add(fromFavorite);
        
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
        
        Scene scene = new Scene(root, 380, 150);
        
        stage.setScene(scene);
        stage.setTitle("Valuuttalaskuri");
        stage.setResizable(false);
        stage.show();
        
        if (!service.isReady()) {
            new ExchangeRateUpdateDialog(service, (boolean updated) -> {
                if (updated) {
                    try {
                        updateComboBox();
                        updateFavorites();
                    } catch (Exception ex) {
                    }
                } else {
                    Platform.exit();
                }
            });
        } else {
            updateComboBox();
            updateFavorites();
        }
    }
    
    private void updateFavorites() throws Exception {
        Collection<Currency> favorites = favoriteCurrencyDao.getFavorites();
        
        if (favorites.contains(toComboBox.getSelectionModel().getSelectedItem())) {
            toFavorite.setGraphic(new ImageView(favorite));
        } else {
            toFavorite.setGraphic(new ImageView(unfavorite));
        }
        
        if (favorites.contains(fromComboBox.getSelectionModel().getSelectedItem())) {
            fromFavorite.setGraphic(new ImageView(favorite));
        } else {
            fromFavorite.setGraphic(new ImageView(unfavorite));
        }
    }
    
    private void updateComboBox() throws Exception {
        //Järjestä valuuttakoodin mukaan
        List<Currency> currencies = new ArrayList<>(service.getCurrencyConverter().getSupportedCurrencies());
        Collections.sort(currencies, new Comparator<Currency>() {
            private Collection<Currency> favorites = favoriteCurrencyDao.getFavorites();
            
            @Override
            public int compare(Currency c1, Currency c2) {
                if (favorites.contains(c1) && !favorites.contains(c2)) {
                    return -1;
                } else if (!favorites.contains(c1) && favorites.contains(c2)) {
                    return 1;
                } else {
                    return c1.getCurrencyCode().compareTo(c2.getCurrencyCode());
                }
            }
            
        });
        
        Currency oldFrom = fromComboBox.getSelectionModel().getSelectedItem();
        Currency oldTo = toComboBox.getSelectionModel().getSelectedItem();
        
        fromComboBox.setItems(FXCollections.observableArrayList(currencies));
        if (oldFrom == null) {
            fromComboBox.getSelectionModel().selectFirst();
        } else {
            fromComboBox.getSelectionModel().select(oldFrom);
        }
        
        toComboBox.setItems(FXCollections.observableArrayList(currencies));
        if (oldTo == null) {
            toComboBox.getSelectionModel().selectFirst();
        } else {
            toComboBox.getSelectionModel().select(oldTo);
        }
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
    
    private static void showErrorDialog(String header, String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Valuuttalaskuri");
        alert.setHeaderText(header);
        alert.setContentText(content);

        alert.showAndWait();
    }
}

