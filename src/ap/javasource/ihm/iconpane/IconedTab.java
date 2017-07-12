package ap.javasource.ihm.iconpane;

import ap.javasource.rdp.project.Project;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author Tarek
 */
public class IconedTab extends AnchorPane implements Initializable {

    @FXML
    protected Rectangle selectedRect;
    @FXML
    private Label closeButton;
    @FXML
    private ImageView icon;
    
    @FXML 
    private Label tooltipLabel;

    private static final Image PROJECT_ICON = new Image("/ap/resources/images/iconpane/projectIcon.png");
    private static final Image GMA_ICON = new Image("/ap/resources/images/iconpane/gmaIcon.png");

    private final IconPane parent;
    protected Node content;

    public IconedTab(IconPane parent, Node content, Tooltip t) {
        this.parent = parent;
        this.content = content;
        FXMLLoader fXMLLoader = new FXMLLoader(getClass().getResource("/ap/resources/fxml/iconpane/IconedTab.fxml"));
        fXMLLoader.setController(this);
        fXMLLoader.setRoot(this);

        try {
            fXMLLoader.load();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        tooltipLabel.setTooltip(t);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initHandlers();
        if (content instanceof Project) {
            icon.setImage(PROJECT_ICON);
        }else{
            icon.setImage(GMA_ICON);
        }
    }

    public Node getContent() {
        return content;
    }

    private void initHandlers() {
        setOnMouseClicked(e -> {
            if (parent.tabs.contains(this)) {
                parent.selectionModel.select(this);
            }
        });

        closeButton.setOnMouseClicked(e -> {
            parent.closeTab(this);
        });
    }
}
