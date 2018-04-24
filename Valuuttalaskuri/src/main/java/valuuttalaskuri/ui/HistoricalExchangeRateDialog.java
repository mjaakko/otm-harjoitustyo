/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package valuuttalaskuri.ui;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ValueAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import valuuttalaskuri.common.CurrencyConverterService;
import valuuttalaskuri.common.ExchangeRate;
import valuuttalaskuri.common.HistoricalExchangeRateService;

/**
 *
 * @author jaakko
 */
public class HistoricalExchangeRateDialog {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 500;
    
    private static final String LOADING_TEXT = "Ladataan valuuttakursseja..";
    private static final String ERROR_TEXT = "Valuuttakurssien lataaminen epäonnistui. Tarkista internet-yhteys ja yritä uudestaan.";
    
    private Stage dialog;
    private ComboBox<Timeperiod> timeperiod;
    private LineChart lineChart;
    private VBox loadingPane;
    private Label status;
    private ProgressBar progress;
    private Button retry;
    
    private Task<Map<LocalDate, BigDecimal>> task;
    
    private HistoricalExchangeRateService exchangeRateService;
    
    private Currency fromCurrency;
    private Currency toCurrency;
    
    public HistoricalExchangeRateDialog(HistoricalExchangeRateService service, Currency from, Currency to) {
        this.exchangeRateService = service;
        
        this.fromCurrency = from;
        this.toCurrency = to;
        
        dialog = new Stage();
        dialog.initStyle(StageStyle.UTILITY);
        dialog.setResizable(false);
        dialog.initModality(Modality.NONE);
        dialog.setMaxHeight(HEIGHT);
        dialog.setMinHeight(HEIGHT);
        dialog.setMaxWidth(WIDTH);
        dialog.setMinWidth(WIDTH);
        
        dialog.setTitle(from.getCurrencyCode()+"/"+to.getCurrencyCode());
        
        VBox vbox = new VBox();
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(10, 10, 10, 10));
        
        timeperiod = new ComboBox<>(FXCollections.observableArrayList(Timeperiod.values()).sorted((t1, t2) -> Integer.compare(t1.days, t2.days)));
        timeperiod.getSelectionModel().selectFirst();
        timeperiod.getSelectionModel().selectedItemProperty().addListener(c -> loadRates());
        
        vbox.getChildren().add(timeperiod);
        
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        
        lineChart = new LineChart(xAxis, yAxis);
        lineChart.prefWidthProperty().bind(vbox.widthProperty());
        lineChart.prefHeightProperty().bind(vbox.heightProperty());
        lineChart.managedProperty().bind(lineChart.visibleProperty());
        
        lineChart.setVisible(false);
        
        vbox.getChildren().add(lineChart);
        
        loadingPane = new VBox();
        loadingPane.setAlignment(Pos.CENTER);
        loadingPane.prefWidthProperty().bind(vbox.widthProperty());
        loadingPane.prefHeightProperty().bind(vbox.heightProperty());
        loadingPane.managedProperty().bind(loadingPane.visibleProperty());
        
        status = new Label();
        status.setText(LOADING_TEXT);
        
        loadingPane.getChildren().add(status);
        
        progress = new ProgressBar();
        progress.setProgress(-1);
        progress.managedProperty().bind(progress.visibleProperty());
        
        loadingPane.getChildren().add(progress);
        
        retry = new Button("Yritä uudestaan");
        retry.managedProperty().bind(retry.visibleProperty());
        retry.setVisible(false);
        
        vbox.getChildren().add(loadingPane);
        
        /*label = new Label(UPDATE_TEXT);
        label.setWrapText(true);
        vbox.getChildren().add(label);
        
        progress = new ProgressBar();
        progress.setMinWidth(200);
        progress.setMaxWidth(200);
        progress.setProgress(-1);
        progress.managedProperty().bind(progress.visibleProperty());
        
        vbox.getChildren().add(progress);
        
        retry = new Button("Yritä uudestaan");
        retry.setVisible(false);
        retry.managedProperty().bind(retry.visibleProperty());
        retry.setOnAction(e -> update());
        
        vbox.getChildren().add(retry);*/
        
        dialog.setScene(new Scene(vbox, WIDTH, HEIGHT));
        dialog.show();
        
        loadRates();
    }
    
    private void loadRates() {
        if (task != null) {
            task.cancel(true);
        }
        
        status.setText(LOADING_TEXT);
        lineChart.setVisible(false);
        loadingPane.setVisible(true);
        progress.setVisible(true);
        retry.setVisible(false);
        
        task = new Task<Map<LocalDate, BigDecimal>>() {
            @Override
            protected Map<LocalDate, BigDecimal> call() throws Exception {
                return exchangeRateService.getRatesForCurrencies(fromCurrency, toCurrency, timeperiod.getSelectionModel().getSelectedItem().days);
            }
            
            @Override
            protected void succeeded() {
                try {
                    Map<LocalDate, BigDecimal> result = get();
                    
                    XYChart.Series series = new XYChart.Series();
                    series.setName(fromCurrency.getCurrencyCode()+"/"+toCurrency.getCurrencyCode());
                    
                    for (Entry<LocalDate, BigDecimal> rate : result.entrySet()) {
                        series.getData().add(new XYChart.Data<>(rate.getKey().toString(), rate.getValue()));
                    }
                    
                    lineChart.getData().setAll(series);
                    
                    lineChart.setVisible(true);
                    loadingPane.setVisible(false);
                } catch (InterruptedException | ExecutionException ex) {
                    //Ei voi tapahtua succeeded() metodissa
                }
            }
            
            @Override
            protected void failed() {
                lineChart.setVisible(false);
                
                status.setText(ERROR_TEXT);
                loadingPane.setVisible(true);
                progress.setVisible(false);
                retry.setVisible(true);
            }
        };
        new Thread(task).start();
    }
    
    private enum Timeperiod {
        /*TEN_YEARS("10v", 3650), FIVE_YEARS("5v", 1825),*/ ONE_YEAR("1v", 365), SIX_MONTHS("6kk", 186), THREE_MONTHS("3kk", 93), ONE_MONTH("1kk", 31);
        
        String title;
        int days;

        Timeperiod(String title, int days) {
            this.title = title;
            this.days = days;
        }
        
        @Override
        public String toString() {
            return title;
        }
    }
}
