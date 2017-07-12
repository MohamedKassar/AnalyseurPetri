/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ap.javasource.rdp.gmadisplay;

import ap.javasource.data.gmadata.GMAData;
import ap.javasource.ihm.couleur.ColorController;
import ap.javasource.ihm.couleur.ColorStyle;
import ap.javasource.ihm.couleur.Colorable;
import ap.javasource.rdp.gmapane.marquage.GraphicMarquage;
import ap.javasource.rdp.gmapane.marquage.MarquageArc;
import ap.javasource.rdp.project.Editeur;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 *
 * @author Tarek
 */
public class GmaDisplayPane extends AnchorPane implements Initializable, Colorable {

    @FXML
    private BorderPane gmaPaneContainer;

    @FXML
    private HBox checkMarkHBox;

    @FXML
    private VBox etatAccVBox;

    @FXML
    private VBox etatPuitVBox;

    @FXML
    private Button checkMarkButton;

    @FXML
    private Label checkMarkLabel;
    @FXML
    private Label blocageLabel;
    @FXML
    private Label vivaciteLabel;
    @FXML
    private Label pseudoLabel;
    @FXML
    private Label quasiLabel;
    @FXML
    private Label conservationLabel;
    @FXML
    private Label reinitLabel;
    @FXML
    private Label etatAccLabel;
    @FXML
    private Label etatPuitLabel;

    private final EventHandler<KeyEvent> intFilter = e -> {
        if (!e.getCharacter().isEmpty()) {
            if (!Character.isDigit(e.getCharacter().charAt(0))) {
                e.consume();
            }
        }
    };

    private final Editeur editeur = new Editeur();
    private final GMAData data;
    boolean etatAcc = true, etatPuit = true;
    
    private final String name;

    public GmaDisplayPane(GMAData data, String filePath) {
        this.data = data;
        this.name = "- "+data.getName()+" ("+filePath+")";
        FXMLLoader fXMLLoader = new FXMLLoader(getClass().getResource("/ap/resources/fxml/gmadisplay/GmaDisplayPane.fxml"));
        fXMLLoader.setController(this);
        fXMLLoader.setRoot(this);
        try {
            fXMLLoader.load();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ColorController.addColorable(this);

        gmaPaneContainer.setCenter(editeur);
        String[] places = data.getPlaces().replaceAll(" ", "").split(",");
        ArrayList<TextField> fields = new ArrayList<>();
        for (String s : places) {
            TextField temp = new TextField();
            temp.setMaxSize(40, Double.MAX_VALUE);
            temp.setPromptText(s);
            temp.setOnKeyTyped(intFilter);
            HBox.setHgrow(temp, Priority.ALWAYS);
            fields.add(temp);
        }
        checkMarkHBox.getChildren().addAll(fields);
        checkMarkHBox.setDisable(false);
        SimpleBooleanProperty boolTemp = new SimpleBooleanProperty(true);
        BooleanBinding bindTemp;
        for (TextField field : fields) {
            bindTemp = field.textProperty().isNotEqualTo("").and(boolTemp);
            boolTemp = new SimpleBooleanProperty();
            boolTemp.bind(bindTemp);
        }
        checkMarkButton.disableProperty().bind(boolTemp.not());

        ArrayList<Label> labels = new ArrayList<>();
        Collections.addAll(labels, blocageLabel, vivaciteLabel, pseudoLabel, quasiLabel, conservationLabel, reinitLabel);
        if (data.getProprietes() != null) {
            data.getProprietes().stream().forEach(p -> {
                switch (p.getName()) {
                    case "Blocage":
                        initLabel(blocageLabel, p.getEtat(), "Le réseau est bloqué.", "Le réseau est sans blocage.");
                        labels.remove(blocageLabel);
                        break;
                    case "Vivacite":
                        initLabel(vivaciteLabel, p.getEtat(), "Le réseau est vivant.", "Le réseau n'est pas vivant.");
                        labels.remove(vivaciteLabel);
                        break;
                    case "Pseudo_vivacite":
                        initLabel(pseudoLabel, p.getEtat(), "Le réseau est pseudo_vivant.", "Le réseau n'est pas pseudo_vivant.");
                        labels.remove(pseudoLabel);
                        break;
                    case "Quasi_vivacite":
                        initLabel(quasiLabel, p.getEtat(), "Le réseau est quasi_vivant.", "Le réseau n'est pas quasi_vivant.");
                        labels.remove(quasiLabel);
                        break;
                    case "Conservation":
                        initLabel(conservationLabel, p.getEtat(), "Le réseau est conservatif.", "Le réseau n'est pas conservatif.");
                        labels.remove(conservationLabel);
                        break;
                    case "Reinitialisation":
                        initLabel(reinitLabel, p.getEtat(), "Le réseau est réinitialisable.", "Le réseau n'est pas réinitialisable.");
                        labels.remove(reinitLabel);
                        break;
                    case "Etats_Accueils":
                        etatAccLabel.setText(p.getEtat());
                        etatAcc = false;
                        break;
                    case "Etats_Puits":
                        etatPuitLabel.setText(p.getEtat());
                        etatPuit = false;
                        break;
                }
            });
        }
        labels.stream().forEach(l -> {
            l.getParent().getParent().setVisible(false);
            l.getParent().getParent().setManaged(false);
        });

        if (etatAcc) {
            etatAccVBox.setVisible(false);
            etatAccVBox.setManaged(false);
        }
        if (etatPuit) {
            etatPuitVBox.setVisible(false);
            etatPuitVBox.setManaged(false);
        }

        HashMap<String, GraphicMarquage> graphicMarquages = new HashMap<>();
        data.getMarquages().stream().forEach(m -> {
            GraphicMarquage temp = new GraphicMarquage(m.getX(), m.getY(), editeur.scaleProprety(), " " + m.getId() + ": " + m.getInfo().replace("[", "").replace("]", "") + " ");
            graphicMarquages.put(m.getId(), temp);
            editeur.getPaneChildren().add(temp);
        });
        data.getMarquages().stream().forEach(m -> {
            if (m.getSucc() != null) {
                m.getSucc().stream().forEach(s -> {
                    editeur.getPaneChildren().add(0, new MarquageArc(graphicMarquages.get(m.getId()), graphicMarquages.get(s.getMarquage()), s.getTransition(), editeur.scaleProprety()));
                });
            }
        });

        checkMarkButton.setOnMouseClicked(e -> {
            String temp = "";
            temp = fields.stream().map((f) -> Integer.parseInt(f.getText()) + ", ").reduce(temp, String::concat);
            temp = temp.substring(0, temp.length() - 2);
            boolean accessible = false;
            GraphicMarquage graphicTemp;
            String marquageName ="";
            for (String id : graphicMarquages.keySet()) {
                graphicTemp =graphicMarquages.get(id);
                marquageName =graphicTemp.getText().replace(id, ""); 
                if (marquageName.substring(3, marquageName.length()-1).equals(temp)) {
                    marquageName = id;
                    accessible = true;
                    break;
                }
            }
            if (accessible) {
                checkMarkLabel.setText("Le Marquage est accessible.\n(" + marquageName + ")");
                checkMarkLabel.setStyle("-fx-background-color:green;-fx-background-radius:5;");
            } else {
                checkMarkLabel.setText("Le Marquage n'est pas accessible.");
                checkMarkLabel.setStyle("-fx-background-color:red;-fx-background-radius:5;");
            }
        });
    }

    private void initLabel(Label label, String etat, String textTrue, String textFalse) {
        if (etat.equals("oui")) {
            if (label != blocageLabel) {
                label.setStyle("-fx-background-color:green;-fx-background-radius:5;");
            } else {
                label.setStyle("-fx-background-color:red;-fx-background-radius:5;");
            }
            label.setText(textTrue);
        } else {
            if (label != blocageLabel) {
                label.setStyle("-fx-background-color:red;-fx-background-radius:5;");
            } else {
                label.setStyle("-fx-background-color:green;-fx-background-radius:5;");
            }
            label.setText(textFalse);
        }
    }

    @Override
    public void setColor(ColorStyle fenetreCouleur) {
        fenetreCouleur.coloring(this);
    }
}
