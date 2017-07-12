/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ap.test.fenetreprincipale;

import ap.javasource.ihm.couleur.ColorController;
import ap.javasource.ihm.couleur.ColorStyle;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

/**
 *
 * @author Tarek
 */
public class AnchorTest extends StackPane {

    public AnchorTest() {
        Button button = new Button("Couleur Suivante");

        getChildren().add(button);
        button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            int i=0;
            @Override
            public void handle(MouseEvent event) {
                ColorController.setColor(ColorStyle.COLORS.elementAt(i));
                i++;
                if(i>=ColorStyle.COLORS.size()){
                    i=0;
                }
            }
        });
//        setStyle("-fx-background-color:white");
    }

}
