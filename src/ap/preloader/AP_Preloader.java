/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ap.preloader;

import static ap.javasource.ihm.fenetreprincipale.FenetrePrincipale.ICONS;
import java.io.IOException;
import javafx.application.Preloader;
import javafx.application.Preloader.ProgressNotification;
import javafx.application.Preloader.StateChangeNotification;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Simple Preloader Using the ProgressBar Control
 *
 * @author Tarek
 */
public class AP_Preloader extends Preloader {

    @FXML
    ProgressBar progressBar;
    Stage stage;

    private Scene createPreloaderScene() {
        progressBar = new ProgressBar();
        BorderPane p = new BorderPane();
        FXMLLoader fXMLLoader = new FXMLLoader(getClass().getResource("/ap/resources/fxml/preloader/PreLoader.fxml"));
        fXMLLoader.setController(this);
        try {
            p.setCenter(fXMLLoader.load());
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return new Scene(p, 550, 388);
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        stage.setResizable(false);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.getIcons().addAll(ICONS);
        stage.setScene(createPreloaderScene());
        progressBar.setProgress(-1);
        stage.show();
    }

    @Override
    public void handleStateChangeNotification(StateChangeNotification scn) {
        if (scn.getType() == StateChangeNotification.Type.BEFORE_START) {
            stage.hide();
        }
    }

    @Override
    public void handleApplicationNotification(PreloaderNotification pn) {
        progressBar.setProgress(((ProgressNotification) pn).getProgress());
    }

}
