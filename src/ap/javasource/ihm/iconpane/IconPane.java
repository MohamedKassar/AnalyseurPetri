package ap.javasource.ihm.iconpane;

import ap.javasource.ihm.couleur.ColorController;
import ap.javasource.ihm.couleur.Colorable;
import ap.javasource.ihm.couleur.ColorStyle;
import ap.javasource.ihm.fenetreprincipale.FenetrePrincipale;
import ap.javasource.ihm.home.Home;
import ap.javasource.rdp.gmadisplay.GmaDisplayPane;
import ap.javasource.rdp.project.Project;
import ap.test.fenetreprincipale.AnchorTest;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

/**
 *
 * @author Tarek
 */
public class IconPane extends VBox implements Initializable, Colorable {

    @FXML
    private ScrollPane iconsScrollPane;

    @FXML
    private Pane homeButton;

    @FXML
    private Pane addTabButton;

    @FXML
    private HBox iconsContainer;

    @FXML
    private ImageView rightButton;

    @FXML
    private ImageView leftButton;

    @FXML
    private AnchorPane iconContent;

    @FXML
    private Label tooltipAdd;

    @FXML
    private Label tooltipHome;

    private final Node homeContent;

    protected LinkedList<Node> tabs = new LinkedList<>();

    private final FenetrePrincipale fenetrePrincipale;

    protected SingleSelectionModel<Node> selectionModel = new SingleSelectionModel<Node>() {

        @Override
        protected Node getModelItem(int index) {
            return tabs.get(index);
        }

        @Override
        protected int getItemCount() {
            return tabs.size();
        }
    };

    public IconPane(FenetrePrincipale fenetrePrincipale, Node node) {
        this.fenetrePrincipale = fenetrePrincipale;
        homeContent = node;
        FXMLLoader fXMLLoader = new FXMLLoader(getClass().getResource("/ap/resources/fxml/iconpane/IconPane.fxml"));
        fXMLLoader.setController(this);
        fXMLLoader.setRoot(this);

        try {
            fXMLLoader.load();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void setColor(ColorStyle style) {
//        style.coloring(iconContainer);
//        style.coloring(scrollIcon);
    }

    public void setContent(Node node) {
        iconContent.getChildren().clear();
        AnchorPane.setTopAnchor(node, 0.0);
        AnchorPane.setBottomAnchor(node, 0.0);
        AnchorPane.setRightAnchor(node, 0.0);
        AnchorPane.setLeftAnchor(node, 0.0);
        iconContent.getChildren().add(node);
        if (node instanceof Project) {
            fenetrePrincipale.titleProprety().unbind();
            fenetrePrincipale.titleProprety().bind(((Project) node).titleProperty());
        } else if (node instanceof GmaDisplayPane) {
            fenetrePrincipale.titleProprety().unbind();
            fenetrePrincipale.titleProprety().set(((GmaDisplayPane) node).getName());
        } else {
            fenetrePrincipale.titleProprety().unbind();
            fenetrePrincipale.titleProprety().set("");
        }
    }

    public SingleSelectionModel<Node> getSelectionModel() {
        return selectionModel;
    }

    public Node getContent() {
        return iconContent.getChildren().get(0);
    }

    public ArrayList<File> getOpenedFiles() {
        ArrayList<File> temp = new ArrayList<>();
        tabs.stream().forEach(n -> {
            if (n instanceof IconedTab) {
                if (((IconedTab) n).getContent() instanceof Project) {
                    temp.add(((Project) ((IconedTab) n).getContent()).getFile());
                }
            }
        });
        return temp;
    }

    public Project getCurrentProject() {
        if (!iconContent.getChildren().isEmpty()) {
            Node temp = iconContent.getChildren().get(0);
            return temp instanceof Project ? (Project) temp : null;
        }
        return null;
    }

    public IconedTab addTab() {
        return addTab(new Project());
    }

    private static final Font font = new Font("Consolas", 12);

    public IconedTab addTab(Project project) {
        Tooltip tooltip = new Tooltip(project.getName());
        tooltip.setFont(font);

        IconedTab iconedTab = new IconedTab(this, project, tooltip);
        tabs.add(iconedTab);
        iconsContainer.getChildren().add(iconedTab);
        return iconedTab;
    }

    public IconedTab addTab(GmaDisplayPane gmaDisplayPane) {
        Tooltip tooltip = new Tooltip(gmaDisplayPane.getName());
        tooltip.setFont(font);
        IconedTab iconedTab = new IconedTab(this, gmaDisplayPane, tooltip);
        tabs.add(iconedTab);
        iconsContainer.getChildren().add(iconedTab);
        return iconedTab;
    }

    public void closeTab(IconedTab tab) {
        if (tab.content instanceof Project) {
            ((Project) tab.content).close();
        }
        tabs.remove(tab);
        iconsContainer.getChildren().remove(tab);
        if (selectionModel.getSelectedItem() == tab) {
            selectionModel.select(homeButton);
        }
    }

    public void closeAll() {
        ArrayList<Node> temp = new ArrayList<>(tabs);
        temp.stream().forEach(n -> {
            if (n instanceof IconedTab) {
                closeTab((IconedTab) n);
            }
        });
    }

    public void closeCurrentProject() {
        if (selectionModel.getSelectedItem() instanceof IconedTab) {
            closeTab((IconedTab) selectionModel.getSelectedItem());
        }
    }

    public LinkedList<Node> getTabs() {
        return tabs;
    }

    private void initSelectionModel() {
        selectionModel.selectedItemProperty().addListener((ObservableValue<? extends Node> observable, Node oldValue, Node newValue) -> {
            if (newValue instanceof IconedTab) {
                IconedTab tab = (IconedTab) newValue;
                setContent(tab.getContent());
                tab.selectedRect.setVisible(true);
                if (oldValue instanceof IconedTab) {
                    IconedTab oldTab = (IconedTab) oldValue;
                    oldTab.selectedRect.setVisible(false);
                }
            } else {
                if (oldValue instanceof IconedTab) {
                    ((IconedTab) oldValue).selectedRect.setVisible(false);
                }
                setContent(homeContent);
            }

        });
    }

    private void initHandlers() {
        homeButton.setOnMouseClicked(e -> {
            selectionModel.select(homeButton);
        });

        addTabButton.setOnMouseClicked(e -> {
            selectionModel.select(addTab());
        });
        rightButton.setOnMouseClicked(e -> {
            System.err.println(iconsScrollPane.getContent().getBoundsInLocal().getWidth() + "  " + iconsScrollPane.getBoundsInLocal().getWidth());
        });
    }

    public void selectProject(IconedTab tab) {
        selectionModel.select(tab);
    }

    public void selectProject(File file) {
        IconedTab temp = null;
        for (Node n : tabs) {
            if (n instanceof IconedTab) {
                if (((IconedTab) n).getContent() instanceof Project) {
                    if (((Project) ((IconedTab) n).getContent()).getFile()!=null && ((Project) ((IconedTab) n).getContent()).getFile().equals(file)) {
                        temp = (IconedTab) n;
                        break;
                    }
                }
            }
        }
        selectProject(temp);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ColorController.addColorable(this);
        tabs.add(homeButton);
        initSelectionModel();
        initHandlers();
        initTooltip();
        selectionModel.select(0);

    }

    private void initTooltip() {
        Font paletteFont = new Font("Consolas", 12);

        Tooltip tooltip = new Tooltip("Nouveau projet");
        tooltip.setFont(paletteFont);
        tooltipAdd.setTooltip(tooltip);

        tooltip = new Tooltip("Page d'accueil");
        tooltip.setFont(paletteFont);
        tooltipHome.setTooltip(tooltip);
    }

}
