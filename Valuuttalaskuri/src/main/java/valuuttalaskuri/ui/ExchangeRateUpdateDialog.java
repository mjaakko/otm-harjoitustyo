/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package valuuttalaskuri.ui;

import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import valuuttalaskuri.common.CurrencyConverterService;

/**
 *
 * @author jaakko
 */
public class ExchangeRateUpdateDialog {
    private static final String UPDATE_TEXT = "Päivitetään valuuttakursseja...";
    private static final String ERROR_TEXT = "Valuuttakurssien lataaminen epäonnistui. Tarkista internet-yhteys ja yritä uudestaan.";
    
    private Stage dialog;
    
    private CurrencyConverterService currencyConverterService;
    
    private OnUpdated onUpdatedListener;
    
    private Label label;
    private ProgressBar progress;
    private Button retry;
    
    private boolean updated = false;
    
    public ExchangeRateUpdateDialog(CurrencyConverterService currencyConverterService, OnUpdated onUpdatedListener) {
        this.currencyConverterService = currencyConverterService;
        
        this.onUpdatedListener = onUpdatedListener;
        
        dialog = new Stage();
        dialog.initStyle(StageStyle.UTILITY);
        dialog.setResizable(false);
        dialog.initModality(Modality.APPLICATION_MODAL);
        
        dialog.setTitle("Valuuttalaskuri");
        
        VBox vbox = new VBox();
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(10, 10, 10, 10));
        
        label = new Label(UPDATE_TEXT);
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
        
        vbox.getChildren().add(retry);
        
        dialog.setScene(new Scene(vbox, 220, 120));
        //Signaloi jos valuuttakursseja ei ole päivitetty
        dialog.setOnCloseRequest(e -> {
            if (!updated) onUpdatedListener.onUpdated(false);
        });
        dialog.show();
        update();
    }
    
    public void update() {
        retry.setVisible(false);
        progress.setVisible(true);
        label.setText(UPDATE_TEXT);
        
        Task<Void> updateTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                long start = System.currentTimeMillis();
                
                currencyConverterService.update();
                                
                long delta = System.currentTimeMillis() - start;
                
                try {
                    //Odota vähintään 3s, jotta dialogi ei "välähdä"
                    Thread.sleep(Math.max(0, 3000-delta));
                } catch (InterruptedException e) {}
                 
                return null;
            }
            
            @Override
            protected void failed() {
                retry.setVisible(true);
                progress.setVisible(false);
                label.setText(ERROR_TEXT);
            }
            
            @Override
            protected void succeeded() {
                updated = true;
                onUpdatedListener.onUpdated(true);
                dialog.close();
            }
            
        };
        new Thread(updateTask).start();
    }
    
    public interface OnUpdated {
        void onUpdated(boolean updated);
    }
}
