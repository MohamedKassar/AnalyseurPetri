package ap.main;

import ap.javasource.ihm.fenetreprincipale.FenetrePrincipale;
import ap.javasource.rdp.project.Project;
import static ap.main.Main.arg;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Tarek
 */
public class newI implements Runnable {

    private SimpleObjectProperty<FenetrePrincipale> fenetrePrincipale = new SimpleObjectProperty<>();

    public void setFenetrePrincipale(FenetrePrincipale fenetrePrincipale) {
        this.fenetrePrincipale.set(fenetrePrincipale);
    }

    private final ServerSocket s;

    public newI(ServerSocket s) {
        this.s = s;
    }
    boolean alive = true, arested = false;
    Socket ss;
    Scanner c;
    DataInputStream d;

    public void tuer() {
        alive = false;
    }

    public boolean isAlive() {
        return !arested;
    }

    @Override
    public void run() {
        try {
            String msg = "";
            while (alive) {
                d = new DataInputStream(s.accept().getInputStream());
                msg = d.readUTF();
                if (fenetrePrincipale.get() != null && msg.endsWith("apsp") || msg.endsWith("xml")) {
                    try {
                        ArrayList<File> temp = fenetrePrincipale.get().getContent().getOpenedFiles();
                        Project project = Project.loadProject(temp, new File(msg));
                        Platform.runLater(() -> {
                            if (project != null) {
                                fenetrePrincipale.get().getContent().selectProject(
                                        fenetrePrincipale.get().getContent().addTab(project));
                            } else {
                                if (!temp.isEmpty()) {
                                    fenetrePrincipale.get().getContent().selectProject(temp.get(0));
                                }
                            }
                        });
                    } catch (Project.DamagedFile ex) {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Fichier endommagé ou non valide", ButtonType.OK);
                        ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons().add(FenetrePrincipale.DAMAGED_ICON);
                        alert.setHeaderText("Erreur de lecture du fichier!");
                        alert.showAndWait();
                    }
                }
                Platform.runLater(() -> {
                    fenetrePrincipale.get().iconise();
                });
            }
            arested = true;
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

}
