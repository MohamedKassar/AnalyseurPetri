/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ap.javasource.ihm.home;

import ap.javasource.ihm.fenetreprincipale.toolbar.ToolBar;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author Tarek
 */
public class Home extends VBox implements Initializable {

    private final ToolBar toolBar;
    
    public Home(ToolBar toolBar) {
        this.toolBar=toolBar;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ap/resources/fxml/home/Home.fxml"));
        fxmlLoader.setController(this);
        fxmlLoader.setRoot(this);
        try {
            fxmlLoader.load();
        } catch (IOException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    
    
    @FXML
    private void newProject(ActionEvent event) {
        toolBar.newProject();
    }
    
    @FXML
    private void openProject(ActionEvent event) {
        toolBar.openProject();
    }
    
    @FXML
    private void openGMA(ActionEvent event) {
        toolBar.openGMA();
    }
    
}
