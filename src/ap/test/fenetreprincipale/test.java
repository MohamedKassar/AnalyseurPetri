package ap.test.fenetreprincipale;

import com.sun.javafx.application.LauncherImpl;
import javafx.application.Application;
import javafx.stage.Stage;
import ap.javasource.ihm.fenetreprincipale.FenetrePrincipale;
import ap.javasource.ihm.fenetreprincipale.toolbar.ToolBar;
import ap.javasource.ihm.home.Home;
import ap.javasource.ihm.iconpane.IconPane;
import ap.preloader.AP_Preloader;

/**
 *
 * @author Tarek
 */
public class test extends Application {

    FenetrePrincipale fenetrePrincipale;
    AnchorTest anchorTest;
    IconPane iconPane;
    ToolBar toolbar;
    long deb;
    long fin;

    @Override
    public void init() {
//        System.out.println("MyApplication#init (doing some heavy lifting), thread: " + Thread.currentThread().getName());
        deb = System.currentTimeMillis();
        fenetrePrincipale = new FenetrePrincipale();
        toolbar = new ToolBar(fenetrePrincipale);
        iconPane = new IconPane(fenetrePrincipale, new Home(toolbar));
        fenetrePrincipale.setToolBar(toolbar);
        fenetrePrincipale.setContent(iconPane);

        // Perform some heavy lifting (i.e. database start, check for application updates, etc. )
        fin = System.currentTimeMillis();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        System.err.println(fin - deb);

        deb = System.currentTimeMillis();

        fenetrePrincipale.initialiserFenetre(primaryStage);
        fin = System.currentTimeMillis();
        System.err.println(fin - deb);
    }

    public static void main(String[] args) {
        LauncherImpl.launchApplication(test.class, AP_Preloader.class, args);
    }
}
