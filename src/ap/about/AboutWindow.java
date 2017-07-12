package ap.about;

import ap.javasource.ihm.fenetreprincipale.FenetrePrincipale;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Tarek
 */
public class AboutWindow extends Stage {

    private AnchorPane Content;

    public AboutWindow() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ap/resources/fxml/about/AboutWindow.fxml"));
        fxmlLoader.setController(this);
        try {
            Content = fxmlLoader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Scene scene = new Scene(Content, 563, 465);
        setTitle("À propos");
        setScene(scene);
        setMinHeight(505);
        setMinWidth(578);
        setMaxHeight(505);
        setMaxWidth(578);
        initModality(Modality.APPLICATION_MODAL);
        initStyle(StageStyle.UTILITY);
        getIcons().addAll(FenetrePrincipale.ICONS);
    }
}
