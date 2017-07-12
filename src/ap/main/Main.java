package ap.main;

import ap.test.fenetreprincipale.*;
import com.sun.javafx.application.LauncherImpl;
import javafx.application.Application;
import javafx.stage.Stage;
import ap.javasource.ihm.fenetreprincipale.FenetrePrincipale;
import ap.javasource.ihm.fenetreprincipale.toolbar.ToolBar;
import ap.javasource.ihm.home.Home;
import ap.javasource.ihm.iconpane.IconPane;
import ap.javasource.rdp.project.Project;
import ap.preloader.AP_Preloader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 *
 * @author Tarek
 */
public class Main extends Application {

    static newI newInstanceObsrver;
    static String arg = null;
    FenetrePrincipale fenetrePrincipale;
    AnchorTest anchorTest;
    IconPane iconPane;
    ToolBar toolbar;
    long deb;
    long fin;

    @Override
    public void init() {
        deb = System.currentTimeMillis();
        fenetrePrincipale = new FenetrePrincipale();
        toolbar = new ToolBar(fenetrePrincipale);
        iconPane = new IconPane(fenetrePrincipale, new Home(toolbar));
        fenetrePrincipale.setToolBar(toolbar);
        fenetrePrincipale.setContent(iconPane);
        newInstanceObsrver.setFenetrePrincipale(fenetrePrincipale);
        if (arg != null) {
            try {
                fenetrePrincipale.getContent().selectProject(
                        fenetrePrincipale.getContent().addTab(Project.loadProject(new File(arg))));
            } catch (Project.DamagedFile ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Fichier endommagé ou non valide", ButtonType.OK);
                ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons().add(FenetrePrincipale.DAMAGED_ICON);
                alert.setHeaderText("Erreur de lecture du fichier!");
                alert.show();
            }
        }

        fin = System.currentTimeMillis();
        double time = fin - deb;
            while(time< 5000) {
                
            try {
                Thread.sleep(1);
            } catch (InterruptedException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
            time++;
        }
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
        ServerSocket ss;
        Socket s;
        try {
            ss = new ServerSocket(12000);
            System.err.println("premiere execution");
            newInstanceObsrver = new newI(ss);
            Thread t = new Thread(newInstanceObsrver);
            t.start();
            if (args.length > 0) {
                arg = args[0];
            }
            LauncherImpl.launchApplication(Main.class, AP_Preloader.class, args);
            newInstanceObsrver.tuer();
            s = new Socket("127.0.0.1", 12000);
            DataOutputStream dou = new DataOutputStream(s.getOutputStream());
            dou.writeUTF("close");
            s.close();
        } catch (IOException ex) {
            try {
                s = new Socket("127.0.0.1", 12000);
                DataOutputStream dou = new DataOutputStream(s.getOutputStream());
                if (args.length > 0) {
                    dou.writeUTF(args[0]);
                } else {
                    dou.writeUTF("deja instancie");
                }
                s.close();
            } catch (IOException ex1) {
                ex1.printStackTrace();
            }
            System.exit(1);
        }
    }
}
