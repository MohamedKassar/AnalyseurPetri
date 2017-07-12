package ap.javasource.ihm.fenetreprincipale;

import ap.javasource.ihm.couleur.ColorController;
import ap.javasource.ihm.couleur.Colorable;
import ap.javasource.ihm.couleur.ColorStyle;
import ap.javasource.ihm.fenetreprincipale.toolbar.ToolBar;
import ap.javasource.ihm.iconpane.IconPane;
import ap.javasource.ihm.iconpane.IconedTab;
import ap.javasource.rdp.project.Project;
import ap.main.newI;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.binding.When;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.BoundingBox;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

/**
 *
 * @author Tarek
 */
public final class FenetrePrincipale extends VBox implements Initializable, Colorable {

    @FXML
    private HBox titleBar;

    @FXML
    private Label labelTitre;

    @FXML
    private AnchorPane contentArea;

    @FXML
    private HBox movePanel;

    @FXML
    private AnchorPane conteneur;

    @FXML
    private Label boutonPleinEcran;
    @FXML
    private ImageView pleinEcranIcon;

    @FXML
    private Label boutonIconiser;

    @FXML
    private Label boutonAgrandiMini;
    @FXML
    private ImageView agrandiMiniIcon;

    @FXML
    private Label boutonFermer;

    @FXML
    private Label boutonTool;
    @FXML
    private ImageView toolIcon;

    @FXML
    private BorderPane toolBar;
    @FXML
    private Region toolPane;

    private static final Image pleinEcran1 = new Image("/ap/resources/images/fenetreprincipale/pleinEcran1.png");
    private static final Image pleinEcran = new Image("/ap/resources/images/fenetreprincipale/pleinEcran.png");
    private static final Image minimize = new Image("/ap/resources/images/fenetreprincipale/minimize.png");
    private static final Image maximize = new Image("/ap/resources/images/fenetreprincipale/maximize.png");

    private static final Image ICON_32 = new Image("/ap/resources/images/fenetreprincipale/icon32.png");
    private static final Image ICON_64 = new Image("/ap/resources/images/fenetreprincipale/icon64.png");
    private static final Image ICON_128 = new Image("/ap/resources/images/fenetreprincipale/icon128.png");
    private static final Image ICON_256 = new Image("/ap/resources/images/fenetreprincipale/icon256.png");

    public static final Image[] ICONS = new Image[]{ICON_256, ICON_32, ICON_64, ICON_128};

    public static final Image SAVE_ICON = new Image("/ap/resources/images/icons/saveIcon.png");
    public static final Image DAMAGED_ICON = new Image("/ap/resources/images/icons/damagedIcon.png");
    public static final Image NEW_ICON = new Image("/ap/resources/images/icons/newIcon.png");

    private final RotateTransition transitionBoutonTool = new RotateTransition(Duration.millis(200));
    private final TranslateTransition transitionToolBar = new TranslateTransition(Duration.millis(200));
    private final ScaleTransition transitionFenetre = new ScaleTransition(Duration.millis(70));

    private double startX = 0;
    private double startY = 0;
    private boolean resize, move, maxi;
    private final BooleanProperty maximized = new SimpleBooleanProperty(false);
    private BoundingBox savedBounds;

    private Stage stage;

    public FenetrePrincipale() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ap/resources/fxml/fenetreprincipale/FenetrePrincipale.fxml"));
        fxmlLoader.setController(this);
        fxmlLoader.setRoot(this);
        try {
            fxmlLoader.load();
        } catch (IOException ex) {
        }
    }

    public FenetrePrincipale(Stage stage) {
        this();
        initialiserFenetre(stage);
    }

    private void initialiserListners() {
        setOnMouseMoved((MouseEvent event) -> {
            if (!maximized.get() && !stage.isFullScreen()) {
                if (event.getX() > stage.getWidth() - 10
                        && event.getX() < stage.getWidth() + 10
                        && event.getY() > stage.getHeight() - 10
                        && event.getY() < stage.getHeight() + 10) {
                    setCursor(Cursor.SE_RESIZE);
                } else {
                    setCursor(Cursor.DEFAULT);
                }
                move = false;
            }
        });

        setOnMousePressed((MouseEvent event) -> {
            resize = event.getX() > stage.getWidth() - 10
                    && event.getX() < stage.getWidth() + 10
                    && event.getY() > stage.getHeight() - 10
                    && event.getY() < stage.getHeight() + 10;
            event.consume();
        });

        setOnMouseDragged((MouseEvent event) -> {
            if (resize) {
                if (event.getX() > stage.getMinWidth()) {
                    stage.setWidth(event.getX());
                } else {
                    stage.setWidth(stage.getMinWidth());
                }
                if (event.getY() > stage.getMinHeight()) {
                    stage.setHeight(event.getY());
                } else {
                    stage.setHeight(stage.getMinHeight());
                }

            }
            event.consume();
        });

        movePanel.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() > 1 && !stage.isFullScreen()) {
                maximizeOrRestore();
            }
        });

        movePanel.setOnMousePressed((MouseEvent event) -> {
            startX = event.getSceneX();
            startY = event.getSceneY();
            move = true;
            if (!maximized.get()) {
                savedBounds = new BoundingBox(stage.getX(), stage.getY(), stage.getWidth(), stage.getHeight());
            }
        });

        movePanel.setOnMouseDragged((MouseEvent event) -> {
            if (!stage.isFullScreen()) {
                if (maximized.get()) {
                    startX = startX / stage.getWidth();
                    maximizeOrRestore();
                    startX = startX * stage.getWidth();
                } else {
                    if (move) {
                        stage.setX(event.getScreenX() - startX);
                        stage.setY(event.getScreenY() - startY);
                        maxi = false;
                        if (event.getScreenY() < 10) {
                            maxi = true;
                        }
                    }
                }
            }
        });

        movePanel.setOnMouseReleased(event -> {
            if (maxi) {
                maximizeOrRestore();
            }
        });
        stage.fullScreenProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (newValue) {
                boutonIconiser.setVisible(false);
                boutonAgrandiMini.setVisible(false);

                pleinEcranIcon.setImage(pleinEcran1);
            } else {
                transitionFenetre.play();

                boutonIconiser.setVisible(true);
                boutonAgrandiMini.setVisible(true);

                pleinEcranIcon.setImage(pleinEcran);
            }
        });

        maximized.addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {

            if (newValue) {
                agrandiMiniIcon.setImage(minimize);
            } else {
                transitionFenetre.play();
                agrandiMiniIcon.setImage(maximize);
            }
        });

        stage.iconifiedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            transitionFenetre.play();
        });
    }
    


    private void initialiserStage() {

        Scene scene = new Scene(this, 1280, 700);
        scene.setFill(null);
        stage.getIcons().addAll(ICONS);
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setMinHeight(700);
        stage.setMinWidth(900);
        stage.titleProperty().bind(new SimpleStringProperty("Analuseur Petri ").concat(titleProprety()));
        stage.show();

        stage.setOnCloseRequest((WindowEvent event) -> {
            ArrayList<Node> list = new ArrayList<>(getContent().getTabs());
            list.stream().forEach(node -> {
                if (node instanceof IconedTab) {
                    getContent().closeTab((IconedTab) node);
                }
            });
        });
    }

    public final void close() {
        stage.fireEvent(
                new WindowEvent(
                        stage,
                        WindowEvent.WINDOW_CLOSE_REQUEST
                )
        );
        stage.close();
    }

    public void iconise(){
         stage.setIconified(false);
        stage.requestFocus();
    }
    
    private void initialiserBoutons() {
        boutonIconiser.managedProperty().bind(boutonIconiser.visibleProperty());
        boutonAgrandiMini.managedProperty().bind(boutonAgrandiMini.visibleProperty());

        boutonPleinEcran.setOnMouseClicked((event) -> {
            stage.setFullScreen(!stage.isFullScreen());
        });

        boutonIconiser.setOnMouseClicked((event) -> {
            stage.setIconified(!stage.isIconified());
        });

        boutonAgrandiMini.setOnMouseClicked(event -> {
            maximizeOrRestore();
        });

        boutonFermer.setOnMouseClicked(event -> {
//            AnalyseurReseau.stopAllRunnable();
            close();
        });

        boutonTool.setOnMouseClicked(event -> {
            transitionBoutonTool.stop();
            transitionToolBar.stop();
            if (transitionBoutonTool.getFromAngle() == 0) {
                transitionBoutonTool.setFromAngle(90);
                transitionBoutonTool.setToAngle(0);
                transitionToolBar.setFromX(265);
                transitionToolBar.setToX(0);
                ((ToolBar) toolBar.getCenter()).setOpen(false);

            } else {
                transitionBoutonTool.setFromAngle(0);
                transitionBoutonTool.setToAngle(90);
                transitionToolBar.setFromX(0);
                transitionToolBar.setToX(265);
                toolBar.setVisible(true);
                ((ToolBar) toolBar.getCenter()).setOpen(true);
            }
            transitionBoutonTool.play();
            transitionToolBar.play();
            toolPane.setVisible(!toolPane.isVisible());
        });

        toolPane.setOnMouseClicked(boutonTool.getOnMouseClicked());
    }

    public void maximizeOrRestore() {
        if (maximized.get()) {
            stage.setX(savedBounds.getMinX());
            stage.setY(savedBounds.getMinY());
            stage.setWidth(savedBounds.getWidth());
            stage.setHeight(savedBounds.getHeight());
            maximized.set(false);
        } else {
            ObservableList<Screen> screensForRectangle = Screen.getScreensForRectangle(stage.getX(), stage.getY(), stage.getWidth(), stage.getHeight());
            Screen screen = screensForRectangle.get(0);
            Rectangle2D visualBounds = screen.getVisualBounds();
            if (!maxi) {
                savedBounds = new BoundingBox(stage.getX(), stage.getY(), stage.getWidth(), stage.getHeight());
            }
            stage.setX(visualBounds.getMinX());
            stage.setY(visualBounds.getMinY());
            stage.setWidth(visualBounds.getWidth());
            stage.setHeight(visualBounds.getHeight());
            maximized.set(true);
            maxi = false;
        }
    }

    private void initialiserTransitions() {
        transitionToolBar.setNode(toolBar);
        transitionToolBar.setOnFinished((ActionEvent event) -> {
            if (transitionToolBar.getToX() == 0) {
                toolBar.setVisible(false);
            }
        });
        transitionBoutonTool.setNode(toolIcon);
        transitionFenetre.setNode(this);
        transitionFenetre.setToX(0.96);
        transitionFenetre.setToY(0.96);
        transitionFenetre.setCycleCount(2);
        transitionFenetre.setAutoReverse(true);
    }

    @Override
    public void setColor(ColorStyle style) {
        style.opaqueColoring(titleBar);
        style.coloring(contentArea);
        style.opaqueColoring(toolBar);
    }

    public void setContent(Node node) {
        conteneur.getChildren().clear();
        AnchorPane.setTopAnchor(node, 0.0);
        AnchorPane.setBottomAnchor(node, 0.0);
        AnchorPane.setRightAnchor(node, 0.0);
        AnchorPane.setLeftAnchor(node, 0.0);
        conteneur.getChildren().add(node);
    }

    public IconPane getContent() {
        if (conteneur.getChildren().size() == 0) {
            return null;
        } else {
            return (IconPane) conteneur.getChildren().get(0);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initialiserTransitions();
        ColorController.addColorable(this);
    }

    public Project getCurrentProject() {
        return getContent().getCurrentProject();
    }

    public void setToolBar(Node node) {
        toolBar.setCenter(node);
    }

    public StringProperty titleProprety() {
        return labelTitre.textProperty();
    }

    public void initialiserFenetre(final Stage stage) {

        this.stage = stage;

        initialiserListners();
        initialiserBoutons();
        initialiserStage();
        initTooltip();

    }

    private void initTooltip() {
        Font paletteFont = new Font("Consolas", 12);

        Tooltip tooltip = new Tooltip("Menu principale");
        tooltip.setFont(paletteFont);
        boutonTool.setTooltip(tooltip);

        tooltip = new Tooltip("Quitter l'application");
        tooltip.setFont(paletteFont);
        boutonFermer.setTooltip(tooltip);

        tooltip = new Tooltip("Réduire");
        tooltip.setFont(paletteFont);
        boutonIconiser.setTooltip(tooltip);

        tooltip = new Tooltip();
        tooltip.setFont(paletteFont);
        tooltip.textProperty().bind(new When(maximized).then("Niveau inf.").otherwise("Agrandir"));
        boutonAgrandiMini.setTooltip(tooltip);

        tooltip = new Tooltip();
        tooltip.setFont(paletteFont);
        tooltip.textProperty().bind(new When(stage.fullScreenProperty()).then("Quitter mode plein ecran").otherwise("Mode plein ecran"));

        boutonPleinEcran.setTooltip(tooltip);
    }

}
