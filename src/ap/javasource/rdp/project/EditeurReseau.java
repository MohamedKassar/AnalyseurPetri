/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ap.javasource.rdp.project;

import ap.javasource.rdp.graphiques.arc.Arc;
import ap.javasource.rdp.graphiques.place.Place;
import ap.javasource.rdp.graphiques.transition.Transition;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

/**
 *
 * @author Tarek
 */
public class EditeurReseau extends AnchorPane implements Initializable {

    @FXML
    private BorderPane editorContainer;

    @FXML
    private Label placeButton;

    @FXML
    private Label transitionButton;

    @FXML
    private ToggleButton arcButton;

    @FXML
    private ToggleButton arcinhButton;

    @FXML
    private ToggleButton deleteButton;

    private final ToggleGroup toggleGroup = new ToggleGroup();

    @FXML
    private VBox palette;

    @FXML
    private VBox placePalette;
    @FXML
    private TextField nomPlace;
    @FXML
    private TextField marquagePlace;

    @FXML
    private VBox arcPalette;
    @FXML
    private TextField poidArc;

    @FXML
    private VBox transitionPalette;
    @FXML
    private TextField nomTransition;
    @FXML
    private TextField prioriteTransition;
    @FXML
    private TextField tauxTransition;

    @FXML
    private Label transitionToolLabel;
    @FXML
    private HBox transitionToolHBox;
    @FXML
    private AnchorPane transitonAnchor;
    @FXML
    private ToggleButton rotateRightButton;
    @FXML
    private ToggleButton rotateLeftButton;

    private final Project project;

    private final Editeur editeur = new Editeur();

    private final Circle circle = new Circle(15, Color.BLACK);
    private final Rectangle rectangle = new Rectangle(15, 15, Color.BLACK);

    public static final ImageCursor ARROW_CURSOR = new ImageCursor(new Image("/ap/resources/images/editeur/arrowCursor.png"));
    public static final ImageCursor ARROW_INH_CURSOR = new ImageCursor(new Image("/ap/resources/images/editeur/arrowInhCursor.png"));
    public static final ImageCursor DELETE_CURSOR = new ImageCursor(new Image("/ap/resources/images/editeur/deleteCursor.png"));
    public static final ImageCursor ROTATE_RIGHT_CURSOR = new ImageCursor(new Image("/ap/resources/images/editeur/rotateRightCursor.png"));
    public static final ImageCursor ROTATE_LEFT_CURSOR = new ImageCursor(new Image("/ap/resources/images/editeur/rotateLeftCursor.png"));
    public static final ImageCursor DEFAULT_CURSOR = new ImageCursor(new Image("/ap/resources/images/editeur/cursor.png"));
    public static final ImageCursor DENIED_CURSOR = new ImageCursor(new Image("/ap/resources/images/editeur/denied.png"));

    private EventHandler<DragEvent> dragOverPane;
    private EventHandler<DragEvent> dragDroppedPane;

    private EventHandler<KeyEvent> intFilter;
    private EventHandler<KeyEvent> doubleFilter;

    private EventHandler<ActionEvent> textFeildsEnterAction;

    public EditeurReseau(Project project) {
        this.project = project;

        FXMLLoader fXMLLoader = new FXMLLoader(getClass().getResource("/ap/resources/fxml/project/EditeurReseau.fxml"));
        fXMLLoader.setController(this);
        fXMLLoader.setRoot(this);

        try {
            fXMLLoader.load();
        } catch (IOException ex) {

        }
    }

    private void initPalette() {
        Font paletteFont = new Font("Consolas", 12);

        Tooltip tooltip = new Tooltip("Nouvelle place\nGlissez");
        tooltip.setFont(paletteFont);
        placeButton.setTooltip(tooltip);

        tooltip = new Tooltip("Nouvelle transition\nGlissez");
        tooltip.setFont(paletteFont);
        transitionButton.setTooltip(tooltip);

        tooltip = new Tooltip("Créer un arc");
        tooltip.setFont(paletteFont);
        arcButton.setTooltip(tooltip);

        tooltip = new Tooltip("Créer un arc inhibiteur");
        tooltip.setFont(paletteFont);
        arcinhButton.setTooltip(tooltip);

        tooltip = new Tooltip("Supprimer");
        tooltip.setFont(paletteFont);
        deleteButton.setTooltip(tooltip);

        tooltip = new Tooltip("Rotation de 45 degrés");
        tooltip.setFont(paletteFont);
        rotateLeftButton.setTooltip(tooltip);
        rotateRightButton.setTooltip(tooltip);

        palette.setCursor(DEFAULT_CURSOR);

        circle.setVisible(false);
        getChildren().add(circle);
        circle.setMouseTransparent(true);

        rectangle.setVisible(false);
        getChildren().add(rectangle);
        rectangle.setMouseTransparent(true);

        placeButton.setOnDragDetected((MouseEvent event) -> {
            Point2D point = sceneToLocal(event.getSceneX() - 15, event.getSceneY() - 15);
            startDrag("Place", point, circle);
            event.consume();
        });

        transitionButton.setOnDragDetected((MouseEvent event) -> {
            Point2D point = sceneToLocal(event.getSceneX() - 7.5, event.getSceneY() - 7.5);
            startDrag("Transition", point, rectangle);
            event.consume();
        });

        dragOverPane = (DragEvent event) -> {
            event.acceptTransferModes(TransferMode.MOVE);
            Point2D point = sceneToLocal(event.getSceneX(), event.getSceneY());
            if (event.getDragboard().getString().equals("Place")) {
                circle.relocate(point.getX() - 15, point.getY() - 15);
            } else {
                if (event.getDragboard().getString().equals("Transition")) {
                    rectangle.relocate(point.getX() - 7.5, point.getY() - 7.5);
                }
            }
            event.consume();
        };

        dragDroppedPane = (DragEvent event) -> {
            palette.setMouseTransparent(false);
            event.setDropCompleted(true);
            Point2D point = sceneToLocal(event.getSceneX(), event.getSceneY());
            if (event.getDragboard().getString().equals("Place")) {
                project.getAnalyseurReseau().addPlace(new Place(point.getX(), point.getY(), editeur, project.getAnalyseurReseau().getIdPlace(), project.getProjectController()));
                circle.setVisible(false);
            } else {
                if (event.getDragboard().getString().equals("Transition")) {
                    project.getAnalyseurReseau().addTransition(new Transition(point.getX(), point.getY(), editeur, project.getAnalyseurReseau().getIdTransition(), project.getProjectController()));
                    rectangle.setVisible(false);
                }
            }
            event.consume();
            editeur.getPane().setOnDragOver(null);
            editeur.getPane().setOnDragDropped(null);
        };

        transitionToolLabel.setOnMouseClicked(e -> {
            if (transitionToolLabel.getText().equals("<")) {
                transitionToolHBox.setVisible(true);
                transitionToolLabel.setText(">");
            } else {
                transitionToolHBox.setVisible(false);
                transitionToolLabel.setText("<");
            }
        });

        transitonAnchor.setOnMouseExited(e -> {
            transitionToolHBox.setVisible(false);
            transitionToolLabel.setText("<");
        });

        toggleGroup.getToggles().addAll(arcButton, arcinhButton, deleteButton, rotateLeftButton, rotateRightButton);

        arcButton.setOnMouseClicked((MouseEvent event) -> {
            if (arcButton.isSelected()) {
                project.getProjectController().newArc.set(true);
            } else {
                project.getProjectController().none.set(true);
            }
        });

        arcinhButton.setOnMouseClicked((MouseEvent event) -> {
            if (arcinhButton.isSelected()) {
                project.getProjectController().newArcInh.set(true);
            } else {
                project.getProjectController().none.set(true);
            }
        });

        deleteButton.setOnMouseClicked((MouseEvent event) -> {
            if (deleteButton.isSelected()) {
                project.getProjectController().delete.set(true);
            } else {
                project.getProjectController().none.set(true);
            }
        });

        rotateLeftButton.setOnMouseClicked((MouseEvent event) -> {
            if (rotateLeftButton.isSelected()) {
                project.getProjectController().rotateTransitionLeft.set(true);
            } else {
                project.getProjectController().none.set(true);
            }
        });

        rotateRightButton.setOnMouseClicked((MouseEvent event) -> {
            if (rotateRightButton.isSelected()) {
                project.getProjectController().rotateTransitionRight.set(true);
            } else {
                project.getProjectController().none.set(true);
            }
        });

        project.getProjectController().none.addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (newValue) {
                toggleGroup.selectToggle(null);
            }
        });

        editeur.getPane().setOnMouseClicked(event -> {
            project.getProjectController().none.set(true);
            project.getProjectController().selectedObject.set(null);
            event.consume();
        });

        editeur.setOnMouseEntered(e -> {
            circle.setVisible(false);
            rectangle.setVisible(false);
            palette.setMouseTransparent(false);
        });
    }

    private void startDrag(String type, Point2D point, Node node) {
        ClipboardContent clipboardContent = new ClipboardContent();
        clipboardContent.putString(type);
        editeur.getPane().setOnDragOver(dragOverPane);
        editeur.getPane().setOnDragDropped(dragDroppedPane);
        palette.setMouseTransparent(true);
        node.relocate(point.getX(), point.getY());
        node.setVisible(true);
        startDragAndDrop(TransferMode.MOVE).setContent(clipboardContent);
    }

    public Editeur getEditeur() {
        return editeur;
    }

    protected VBox setPlacePaletteAt(Place place) {
        placePalette.setVisible(true);
        nomPlace.setText(place.getName());
        nomPlace.requestFocus();
        nomPlace.setOnKeyReleased(e -> {
            place.setName(nomPlace.getText());
        });
        marquagePlace.setText(place.getMarquage() + "");
        marquagePlace.setOnKeyReleased(e -> {
            if (!e.getCharacter().isEmpty()) {
                if (!Character.isDigit(e.getCharacter().charAt(0))) {
                    if (marquagePlace.getText().equals("")) {
                        place.setMarquage(0);
                    } else {
                        try {
                            place.setMarquage(Integer.parseInt(marquagePlace.getText()));
                        } catch (NumberFormatException ex) {
                            marquagePlace.setText(place.getMarquage() + "");
                        }
                    }
                }
            }
        });
        return placePalette;
    }

    protected VBox setArcPaletteAt(Arc arc) {
        arcPalette.setVisible(true);
        poidArc.setText(arc.getPoid() + "");
        poidArc.requestFocus();

        poidArc.setOnKeyReleased(e -> {
            if (!e.getCharacter().isEmpty()) {
                if (!Character.isDigit(e.getCharacter().charAt(0))) {
                    if (poidArc.getText().equals("")) {
                        arc.setPoid(1);
                    } else {
                        try {
                            if (Integer.parseInt(poidArc.getText()) == 0) {
                                arc.setPoid(1);
                                poidArc.setText("1");
                            } else {
                                arc.setPoid(Integer.parseInt(poidArc.getText()));
                            }
                        } catch (NumberFormatException ex) {
                            poidArc.setText(arc.getPoid() + "");
                        }
                    }
                }
            }
        });
        return arcPalette;
    }

    protected VBox setTransitionPaletteAt(Transition transition) {

        transitionPalette.setVisible(true);
        nomTransition.setText(transition.getName());
        prioriteTransition.setText(transition.getPriorite() + "");
        tauxTransition.setText(transition.getTaux() + "");
        nomTransition.requestFocus();

        nomTransition.setOnKeyReleased(e -> {
            transition.setName(nomTransition.getText());
        });

        prioriteTransition.setOnKeyReleased(e -> {
            if (!e.getCharacter().isEmpty()) {
                if (!Character.isDigit(e.getCharacter().charAt(0))) {
                    if (prioriteTransition.getText().equals("")) {
                        transition.setPriorite(1);
                    } else {
                        try {
                            transition.setPriorite(Integer.parseInt(prioriteTransition.getText()));
                        } catch (NumberFormatException ex) {
                            prioriteTransition.setText(transition.getPriorite() + "");
                        }
                    }
                }
            }
        });

        tauxTransition.setOnKeyReleased(e -> {
            if (!e.getCharacter().isEmpty()) {
                if (!Character.isDigit(e.getCharacter().charAt(0)) && !e.getCharacter().equals(".")) {
                    if (tauxTransition.getText().equals("")) {
                        transition.setTaux(1);
                    } else {
                        try {
                            if (Double.parseDouble(tauxTransition.getText()) > 1) {
                                transition.setTaux(1);
                                tauxTransition.setText("1");
                            } else {
                                transition.setTaux(Double.parseDouble(tauxTransition.getText()));
                            }
                        } catch (NumberFormatException ex) {
                            tauxTransition.setText(transition.getTaux() + "");
                        }
                    }
                }
            }
        });
        return transitionPalette;
    }

    private void initTextFeilds() {
        intFilter = e -> {
            if (!e.getCharacter().isEmpty()) {
                if (!Character.isDigit(e.getCharacter().charAt(0))) {
                    e.consume();
                }
            }
        };
        doubleFilter = e -> {
            if (!e.getCharacter().isEmpty()) {
                if (!Character.isDigit(e.getCharacter().charAt(0)) && !e.getCharacter().equals(".")) {
                    e.consume();
                }
            }
        };
        textFeildsEnterAction = e -> {
            project.getProjectController().selectedObject.set(null);
        };

        nomPlace.setOnAction(textFeildsEnterAction);

        marquagePlace.setOnAction(textFeildsEnterAction);
        marquagePlace.setOnKeyTyped(intFilter);

        poidArc.setOnAction(textFeildsEnterAction);
        poidArc.setOnKeyTyped(intFilter);

        nomTransition.setOnAction(textFeildsEnterAction);

        prioriteTransition.setOnAction(textFeildsEnterAction);
        prioriteTransition.setOnKeyTyped(intFilter);

        tauxTransition.setOnAction(textFeildsEnterAction);
        tauxTransition.setOnKeyTyped(doubleFilter);

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        editorContainer.setCenter(editeur);
        initPalette();
        initTextFeilds();

    }

}
