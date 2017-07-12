package ap.javasource.rdp.analyses;

import ap.javasource.rdp.gmapane.GMAPane;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 *
 * @author Tarek
 */
public class AnalysesPane extends SplitPane implements Initializable {

    private final AnalyseurReseau analyseurReseau;
    private final GMAPane gmaPane = new GMAPane();

    @FXML
    private BorderPane gmaPaneContainer;

    @FXML
    private HBox checkMarkHBox;

    @FXML
    private Button checkMarkButton;
    @FXML
    private Button blocageButton;
    @FXML
    private Button gmaButton;
    @FXML
    private Button vivaciteButton;
    @FXML
    private Button pseudoButton;
    @FXML
    private Button quasiButton;
    @FXML
    private Button conservationButton;
    @FXML
    private Button reinitButton;
    @FXML
    private Button etatAccButton;
    @FXML
    private Button etatPuitButton;

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

    private final Button[] buttons;
    private final Label[] labels;

    public AnalysesPane(AnalyseurReseau analyseurReseau) {
        this.analyseurReseau = analyseurReseau;
        FXMLLoader fXMLLoader = new FXMLLoader(getClass().getResource("/ap/resources/fxml/analyses/AnalysesPane.fxml"));
        fXMLLoader.setController(this);
        fXMLLoader.setRoot(this);
        try {
            fXMLLoader.load();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        buttons = new Button[]{vivaciteButton, pseudoButton, quasiButton, conservationButton, reinitButton, etatAccButton, checkMarkButton, blocageButton, etatPuitButton};
        labels = new Label[]{vivaciteLabel, pseudoLabel, quasiLabel, conservationLabel, reinitLabel, etatAccLabel,checkMarkLabel, blocageLabel, etatPuitLabel};
    }

    private void initButtons() {
        gmaButton.setOnMouseClicked(e -> {
            analyseurReseau.construireGMA(gmaPaneContainer, gmaPane, gmaButton, buttons, labels, checkMarkHBox);
        });

        vivaciteButton.setOnMouseClicked(e -> {
            analyseurReseau.testerVivacité();
        });

        pseudoButton.setOnMouseClicked(e -> {
            analyseurReseau.testerPseudoVivacité();
        });

        quasiButton.setOnMouseClicked(e -> {
            analyseurReseau.testerQuasiVivacité();
        });
        conservationButton.setOnMouseClicked(e -> {
            analyseurReseau.testerConservation();
        });

        reinitButton.setOnMouseClicked(e -> {
            analyseurReseau.testerRéinitialisation();
        });

        etatAccButton.setOnMouseClicked(e -> {
            analyseurReseau.trouverEtatAcc();
        });
        
        checkMarkButton.setOnMouseClicked(e -> {
            analyseurReseau.verifierSiMarquageAcc();
        });

        blocageButton.setOnMouseClicked(e -> {
            analyseurReseau.testerBlocage();
        });
        
        etatPuitButton.setOnMouseClicked(e -> {
            analyseurReseau.trouverEtatPuit();
        });
    }

    public GMAPane getGmaPane() {
        return gmaPane;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initButtons();
    }

}
