/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package valuuttalaskuri.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author jaakko
 */
public class CurrencyConverterGui extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        
        VBox content = new VBox();
        
        Scene scene = new Scene(content, 250, 200);
        
        stage.setScene(scene);
        stage.setTitle("Valuuttalaskuri");
        stage.show();
    }
}

