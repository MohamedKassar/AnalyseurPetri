/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ap.javasource.ihm.fenetreprincipale.toolbar;

import ap.about.AboutWindow;
import ap.javasource.ihm.fenetreprincipale.FenetrePrincipale;
import ap.javasource.rdp.gmadisplay.GmaDisplayPane;
import ap.javasource.rdp.project.Project;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Accordion;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Tarek
 */
public class ToolBar extends VBox implements Initializable {

    @FXML
    private Accordion accordion;
    @FXML
    private TitledPane project;
    @FXML
    private Button saveButton;
    @FXML
    private Button nouveauProjetButton;
    @FXML
    private Button ouvrirButton;
    @FXML
    private Button ouvrirGMAButton;
    @FXML
    private Button fermerProjectButton;
    @FXML
    private Button fermerToutProjetButton;
    @FXML
    private Button savePicNetButton;
    @FXML
    private Button savePicMatButton;
    @FXML
    private Button savePicGMAButton;
    @FXML
    private Button saveProjectButton;
    @FXML
    private Button saveXmlNetButton;
    @FXML
    private Button saveXmlGmaButton;
    @FXML
    private Button aboutButton;
    @FXML
    private Button exitButton;

    private SimpleBooleanProperty open = new SimpleBooleanProperty(false);

    private final FenetrePrincipale fenetrePrincipale;

    private AboutWindow aboutWindow;

    public ToolBar(FenetrePrincipale fenetrePrincipale) {
        FXMLLoader fXMLLoader = new FXMLLoader(getClass().getResource("/ap/resources/fxml/toolbar/ToolBar.fxml"));
        fXMLLoader.setController(this);
        fXMLLoader.setRoot(this);

        try {
            fXMLLoader.load();
            Platform.runLater(() -> {
                aboutWindow = new AboutWindow();
            });
        } catch (IOException ex) {

        }
        this.fenetrePrincipale = fenetrePrincipale;
    }

    public void openProject() {
        try {
            ArrayList<File> temp = fenetrePrincipale.getContent().getOpenedFiles();
            Project project = Project.loadProject(temp);
            if (project != null) {
                fenetrePrincipale.getContent().selectProject(
                        fenetrePrincipale.getContent().addTab(project));
            } else {
                if (!temp.isEmpty()) {
                    fenetrePrincipale.getContent().selectProject(temp.get(0));
                }
            }
        } catch (Project.DamagedFile ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Fichier endommagé ou non valide", ButtonType.OK);
            ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons().add(FenetrePrincipale.DAMAGED_ICON);
            alert.setHeaderText("Erreur de lecture du fichier!");
            alert.showAndWait();
//                switch (ex.getMessage()) {
//                    case "apsp":
//                        //TODO
//                        System.err.println("apsp");
//                        break;
//                    case "xml":
//                        //TODO
//                        System.err.println("xml");
//                        break;
//                }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void openGMA() {
        try {
            GmaDisplayPane gmaPane = Project.loadGMA();
            if (gmaPane != null) {
                fenetrePrincipale.getContent().selectProject(
                        fenetrePrincipale.getContent().addTab(gmaPane));
            }
        } catch (Project.DamagedFile ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Fichier endommagé ou non valide", ButtonType.OK);
            ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons().add(FenetrePrincipale.DAMAGED_ICON);
            alert.setHeaderText("Erreur de lecture du fichier!");
            alert.showAndWait();
//                switch (ex.getMessage()) {
//                    case "xml":
//                        System.err.println("xml");
//                        break;
//                }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void newProject() {
        fenetrePrincipale.getContent().getSelectionModel().select(
                fenetrePrincipale.getContent().addTab());
    }

    private void initButtons() {
        saveButton.setOnMouseClicked(e -> {
            if (fenetrePrincipale.getCurrentProject().saveProject()) {
                saveButton.setDisable(true);
            }
        });

        savePicNetButton.setOnMouseClicked(e -> {
            try {
                if (fenetrePrincipale.getCurrentProject().saveNetAsPicture()) {
                    savePicNetButton.setDisable(true);
                }
            } catch (Project.OverSizePicture ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Enregistrement impossible", ButtonType.OK);
                ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons().add(FenetrePrincipale.DAMAGED_ICON);
                alert.setHeaderText("L'image est trop grande!");
                alert.showAndWait();
            }

        });

        savePicGMAButton.setOnMouseClicked(e -> {
            try {
                if (fenetrePrincipale.getCurrentProject().saveGMAAsPicture()) {
                    savePicGMAButton.setDisable(true);
                }
            } catch (Project.OverSizePicture ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Enregistrement impossible", ButtonType.OK);
                ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons().add(FenetrePrincipale.DAMAGED_ICON);
                alert.setHeaderText("L'image est trop grande!");
                alert.showAndWait();
            }

        });

        savePicMatButton.setOnMouseClicked(e -> {
            if (fenetrePrincipale.getCurrentProject().saveMatrixsAsPictures()) {
                savePicMatButton.setDisable(true);
            }
        });

        saveProjectButton.setOnMouseClicked(e -> {
            if (fenetrePrincipale.getCurrentProject().saveProjectAs()) {
                saveProjectButton.setDisable(true);
            }
        });

        saveXmlNetButton.setOnMouseClicked(e -> {
            if (fenetrePrincipale.getCurrentProject().saveProjectAsXML()) {
                saveXmlNetButton.setDisable(true);
            }
        });

        saveXmlGmaButton.setOnMouseClicked(e -> {
            if (fenetrePrincipale.getCurrentProject().saveGMAAsXML()) {
                saveXmlGmaButton.setDisable(true);
            }
        });

        ouvrirButton.setOnMouseClicked(e -> {
            openProject();
        });

        ouvrirGMAButton.setOnMouseClicked(e -> {
            openGMA();
        });

        fermerToutProjetButton.setOnMouseClicked(e -> {
            fenetrePrincipale.getContent().closeAll();
            fermerToutProjetButton.setDisable(true);
        });

        fermerProjectButton.setOnMouseClicked(e -> {
            fenetrePrincipale.getContent().closeCurrentProject();
            fermerProjectButton.setDisable(true);
            if (fenetrePrincipale.getContent().getTabs().size() == 1) {
                fermerToutProjetButton.setDisable(true);
            }
        });

        nouveauProjetButton.setOnMouseClicked(e -> {
            newProject();
        });

        exitButton.setOnMouseClicked(e -> {
            fenetrePrincipale.close();
        });

        aboutButton.setOnMouseClicked(e -> {
            Platform.runLater(() -> {
                aboutWindow.showAndWait();
            });
        });
    }

    public void setOpen(boolean b) {
        open.set(b);
    }

    private void initPropretis() {
        open.addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (newValue) {
                if (fenetrePrincipale.getContent().getTabs().size() > 1) {
                    fermerToutProjetButton.setDisable(false);
                } else {
                    fermerToutProjetButton.setDisable(true);
                }
                if (!fenetrePrincipale.getContent().getSelectionModel().isSelected(0)) {
                    fermerProjectButton.setDisable(false);

                } else {
                    fermerProjectButton.setDisable(true);
                }
                if (accordion.getExpandedPane() == null) {
                    accordion.setExpandedPane(project);
                }
                if (fenetrePrincipale.getCurrentProject() != null) {

                    if (fenetrePrincipale.getCurrentProject().getAnalyseurReseau().getGma() != null) {
                        saveXmlGmaButton.setDisable(false);
                    } else {
                        saveXmlGmaButton.setDisable(true);
                    }

                    if (fenetrePrincipale.getCurrentProject().getAnalyseurReseau().getTransitions().isEmpty() || fenetrePrincipale.getCurrentProject().getAnalyseurReseau().getPlaces().isEmpty()) {
                        savePicNetButton.setDisable(true);
                        savePicMatButton.setDisable(true);
                    } else {
                        savePicNetButton.setDisable(false);
                        savePicMatButton.setDisable(false);
                    }

                    if (fenetrePrincipale.getCurrentProject().getAnalysesPane().getGmaPane().getPaneChildren().size() < 1) {
                        savePicGMAButton.setDisable(true);
                    } else {
                        savePicGMAButton.setDisable(false);
                    }

                    if (fenetrePrincipale.getCurrentProject().isModified()) {
                        saveButton.setDisable(false);
                    }
                    saveProjectButton.setDisable(false);
                    saveXmlNetButton.setDisable(false);
                } else {
                    savePicNetButton.setDisable(true);
                    savePicMatButton.setDisable(true);
                    savePicGMAButton.setDisable(true);
                    saveButton.setDisable(true);
                    saveProjectButton.setDisable(true);
                    saveXmlNetButton.setDisable(true);
                    saveXmlGmaButton.setDisable(true);
                }
            }
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initButtons();
        initPropretis();
    }
}
