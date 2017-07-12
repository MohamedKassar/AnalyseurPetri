package ap.javasource.rdp.project;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import ap.javasource.data.gmadata.GMAData;
import ap.javasource.data.gmadata.NoeudData;
import ap.javasource.data.rdpdata.ArcData;
import ap.javasource.data.rdpdata.PlaceData;
import ap.javasource.data.rdpdata.ProjectData;
import ap.javasource.data.rdpdata.TransitionData;
import ap.javasource.rdp.matrix.MatrixPane;
import ap.javasource.ihm.couleur.ColorController;
import ap.javasource.ihm.couleur.ColorStyle;
import ap.javasource.ihm.couleur.Colorable;
import ap.javasource.ihm.fenetreprincipale.FenetrePrincipale;
import ap.javasource.rdp.analyses.AnalysesPane;
import ap.javasource.rdp.analyses.AnalyseurReseau;
import ap.javasource.rdp.gmadisplay.GmaDisplayPane;
import ap.javasource.rdp.graphiques.arc.ArcInhibiteur;
import ap.javasource.rdp.graphiques.arc.ArcPost;
import ap.javasource.rdp.graphiques.arc.ArcPreOrdinaire;
import ap.javasource.rdp.graphiques.place.Place;
import ap.javasource.rdp.graphiques.transition.Transition;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

/**
 *
 * @author Tarek
 */
public class Project extends AnchorPane implements Initializable, Colorable {

    @FXML
    private BorderPane content;

    @FXML
    private ToggleGroup group;

    @FXML
    private ToggleButton editeurButton;
    @FXML
    private ToggleButton matricesButton;
    @FXML
    private ToggleButton gmaButton;

    public ArrayList<AnalyseurReseau.StopableRunnable> RUNNABLE_LIST = new ArrayList<>();

    private final ProjectController projectController = new ProjectController(this);
    protected final EditeurReseau editeurReseau = new EditeurReseau(this);
    private final AnalyseurReseau analyseur = new AnalyseurReseau(projectController);
    private final MatrixPane matrixPane = new MatrixPane(projectController);
    private final AnalysesPane analysesPane = new AnalysesPane(analyseur);

    private final String name;
    private final String id;
    private final SimpleObjectProperty<File> file = new SimpleObjectProperty<>(null);
    private final SimpleStringProperty title = new SimpleStringProperty();
    private SimpleBooleanProperty modified = new SimpleBooleanProperty(false);

    public Project() {
        DateFormat d = new SimpleDateFormat("yyyyMMddHHmmss");
        id = d.format(new Date());
        TextInputDialog alert = new TextInputDialog();
        ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons().add(FenetrePrincipale.NEW_ICON);
        ((Stage) alert.getDialogPane().getScene().getWindow()).setTitle("Nouveau Projet");
        alert.getEditor().setText("Projet" + id);
        alert.setHeaderText("Entrez le nom du projet");
        alert.setContentText("Nom :");
        Optional<String> showAndWait = alert.showAndWait();
        if (showAndWait.isPresent() && !showAndWait.get().replace(" ", "").equals("")) {
            name = showAndWait.get().replaceAll(" +", " ");
        } else {
            name = "Projet" + id;
        }
        FXMLLoader fXMLLoader = new FXMLLoader(getClass().getResource("/ap/resources/fxml/project/Project.fxml"));
        fXMLLoader.setController(this);
        fXMLLoader.setRoot(this);
        try {
            fXMLLoader.load();
        } catch (IOException ex) {
        }

    }

    @Override
    public void setColor(ColorStyle fenetreCouleur) {
        fenetreCouleur.coloring(this);
    }

    private void initButtons() {

        group.selectedToggleProperty().addListener((ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) -> {
            if (newValue == null) {
                group.selectToggle(oldValue);
            }
        });

        editeurButton.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (newValue) {
                content.setCenter(editeurReseau);
            }
        });

        matricesButton.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (newValue) {
                content.setCenter(matrixPane);
            }
        });

        gmaButton.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (newValue) {
                content.setCenter(analysesPane);
            }
        });
        group.selectToggle(editeurButton);
    }

    public ProjectController getProjectController() {
        return projectController;
    }

    public AnalyseurReseau getAnalyseurReseau() {
        return analyseur;
    }

    public MatrixPane getMatrixPane() {
        return matrixPane;
    }

    public EditeurReseau getEditeurReseau() {
        return editeurReseau;
    }

    public boolean saveNetAsPicture() throws OverSizePicture {
        System.gc();
        Rectangle2D pictureArea = editeurReseau.getEditeur().childrenArea();
        if (pictureArea != null) {
            SnapshotParameters snapshotParameters = new SnapshotParameters();
            snapshotParameters.setViewport(pictureArea);
            File file = chooseFile(this, "modèle", "Image PNG", ".png");
            if (file != null) {
                try {
                    ImageIO.write(SwingFXUtils.fromFXImage(editeurReseau.getEditeur().getPane().snapshot(snapshotParameters, null), null), "png", file);
                    System.gc();
                    return true;
                } catch (Exception s) {
                    throw new OverSizePicture("");
                }
            } else {
                return false;
            }
        } else {
            return false;
        }

    }

    public boolean saveGMAAsPicture() throws OverSizePicture {
        System.gc();
        Rectangle2D pictureArea = analysesPane.getGmaPane().childrenArea();
        if (pictureArea != null) {
            SnapshotParameters snapshotParameters = new SnapshotParameters();
            snapshotParameters.setViewport(pictureArea);
            File file = chooseFile(this, "graphe d'accessibilité", "Image PNG", ".png");
            if (file != null) {
                try {
                    ImageIO.write(SwingFXUtils.fromFXImage(analysesPane.getGmaPane().getPane().snapshot(snapshotParameters, null), null), "png", file);
                    System.gc();
                    return true;
                } catch (Exception s) {
                    throw new OverSizePicture("");
                }
            } else {
                return false;
            }
        } else {
            return false;
        }

    }

    public boolean saveMatrixsAsPictures() {
        boolean b = false;
        if (!analyseur.getTransitions().isEmpty() && !analyseur.getPlaces().isEmpty()) {

            Toggle toggle = group.getSelectedToggle();
            matricesButton.setSelected(true);
            String temp = chooseDirectory();
            if (temp != null) {
                b = matrixPane.saveMatrixsAsPicture(temp);
            }
            toggle.setSelected(true);

        }
        return b;
    }

    public boolean saveProjectAs() {
        File file = chooseFile(this, "modèle", "Fichier APSP", ".apsp");
        return saveProjectAs(file);
    }

    public boolean saveProjectAs(File file) {
        if (file != null) {
            try {
                FileOutputStream fos = new FileOutputStream(file);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(getRdpData());
                oos.flush();
                oos.close();
                this.file.set(file);
                setSaveModified();
                return true;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return false;
    }

    public boolean saveProject() {
        if (file.get() == null) {
            return Project.this.saveProjectAs();
        } else {
            if (file.get().getAbsolutePath().endsWith("xml") || file.get().getAbsolutePath().endsWith("XML")) {
                return saveProjectAsXML(file.get());
            } else {
                return saveProjectAs(file.get());
            }
        }
    }

    public boolean saveProjectAsXML() {
        File file = chooseFile(this, "modèle", "Fichier XML", ".xml");
        return saveProjectAsXML(file);
    }

    public boolean saveProjectAsXML(File file) {
        if (file != null) {
            BufferedWriter writer = null;
            try {
                double temp = editeurReseau.getEditeur().scaleProprety().get();
                editeurReseau.getEditeur().setScale(1);

                writer = new BufferedWriter(new FileWriter(file));
                XStream xStream = new XStream(new DomDriver());
                xStream.autodetectAnnotations(true);
                String xml = xStream.toXML(getRdpData());

                editeurReseau.getEditeur().setScale(temp);

                writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + xml);
                writer.flush();
                if (this.file.get() == null) {
                    this.file.set(file);
                }
                setSaveModified();
                return true;
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                try {
                    writer.close();
                } catch (Exception e) {
                }
            }
        }
        return false;
    }

    public boolean saveGMAAsXML() {

        File file = chooseFile(this, "graphe d'accessibilité", "Fichier XML", ".xml");
        if (file != null) {

            BufferedWriter writer = null;
            try {
                double temp = analysesPane.getGmaPane().scaleProprety().get();
                analysesPane.getGmaPane().setScale(1);

                writer = new BufferedWriter(new FileWriter(file));
                XStream xStream = new XStream(new DomDriver());
                xStream.autodetectAnnotations(true);
                String xml = xStream.toXML(getGMAData());

                analysesPane.getGmaPane().setScale(temp);

                writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + xml);
                writer.flush();
                return true;
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                try {
                    writer.close();
                } catch (Exception e) {
                }
            }
        }
        return false;
    }

    public static Project loadProject(File file) throws DamagedFile {
        if (file != null) {
            ProjectData data = null;
            if (file.getName().endsWith("apsp")) {
                FileInputStream fin = null;
                ObjectInputStream ois = null;
                try {
                    fin = new FileInputStream(file);
                    ois = new ObjectInputStream(fin);
                    data = (ProjectData) ois.readObject();
                    Project project = new Project(data);
                    project.file.set(file);
                    return project;
                } catch (IOException | ClassNotFoundException ex) {
                    throw new DamagedFile("apsp");
                } finally {
                    if (ois != null) {
                        try {
                            ois.close();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            } else if(file.getName().endsWith("xml")) {
                XStream xStream = new XStream(new DomDriver());
                xStream.autodetectAnnotations(true);
                xStream.alias("RDPSP", ProjectData.class);
                xStream.alias("Arc", ArcData.class);
                xStream.alias("Place", PlaceData.class);
                xStream.alias("Transition", TransitionData.class);
                try {
                    data = (ProjectData) xStream.fromXML(file);
                    Project project = new Project(data);
                    project.file.set(file);
                    return project;
                } catch (Exception ex) {
                    ex.printStackTrace();
                    throw new DamagedFile("xml");
                }
            }else{
                throw new DamagedFile("xml");
            }
        }
        return null;
    }

    public static Project loadProject(ArrayList<File> temp, File file)throws DamagedFile {
        if (!temp.contains(file)) {
            return loadProject(file);
        }else{
            temp.clear();
            temp.add(file);
            return null;
        }
    }
    public static Project loadProject(ArrayList<File> temp) throws DamagedFile {
        File file = chooseFile(null, "", null, null);
        return loadProject(temp, file);
    }

    public static GmaDisplayPane loadGMA() throws DamagedFile {
        File file = chooseFile(null, "", "Fichier XML", ".xml");
        if (file != null) {
            GMAData data = null;

            XStream xStream = new XStream(new DomDriver());
            xStream.autodetectAnnotations(true);
            xStream.alias("Graphe_accessibilite", GMAData.class);
            xStream.alias("Propriete", GMAData.Propriete.class);
            xStream.alias("Marquage", NoeudData.class);
            try {
                data = (GMAData) xStream.fromXML(file);
                return new GmaDisplayPane(data, file.getAbsolutePath());
            } catch (Exception ex) {
                throw new DamagedFile("xml");
            }
        }
        return null;
    }

    private static File chooseFile(Project project, String type, String extensionName, String extension) {
        FileChooser chooser = new FileChooser();
        File path = new File(System.getProperty("user.home") + "/Documents");
        chooser.setInitialDirectory(path);
        if (extension == null) {
            chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichier APSP", "*" + ".apsp"));
            chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichier XML", "*" + ".xml"));
        } else {
            chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(extensionName, "*" + extension));
        }
        File file;
        if (project != null) {
            DateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmss");
            chooser.setInitialFileName(project.getName().concat("_" + type).concat("_" + dateFormat.format(new Date())).concat(extension));
            file = chooser.showSaveDialog(null);
            if (file == null) {
                return null;
            }
            if (!file.getAbsolutePath().endsWith(extension)) {
                file = new File(file.getAbsolutePath() + extension);
            }
        } else {
            file = chooser.showOpenDialog(null);
        }
        return file;
    }

    private static String chooseDirectory() {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setInitialDirectory(new File(System.getProperty("user.home") + "\\Documents"));
        File temp = chooser.showDialog(null);
        return temp == null ? null : temp.getAbsolutePath() + "\\";
    }

    public String getName() {
        return name;
    }

    public String getIdProject() {
        return id;
    }

    public AnalysesPane getAnalysesPane() {
        return analysesPane;
    }

    public GMAData getGMAData() {
        return new GMAData(analyseur, analysesPane.getGmaPane().getMarquages(), name);
    }

    public ProjectData getRdpData() {
        return new ProjectData(name, id, analyseur);
    }

    public Project(ProjectData data) {
        id = data.getId();
        name = data.getName();

        FXMLLoader fXMLLoader = new FXMLLoader(getClass().getResource("/ap/resources/fxml/project/Project.fxml"));
        fXMLLoader.setController(this);
        fXMLLoader.setRoot(this);
        try {
            fXMLLoader.load();
        } catch (IOException ex) {
        }
        analyseur.setIdPlace(data.getPlaceId());
        analyseur.setIdTransition(data.getTransitionId());
        data.getPlaces().stream().forEach(placeData -> {
            analyseur.addPlace(new Place(editeurReseau.getEditeur(), projectController, placeData));
        });
        data.getTransitions().stream().forEach(transitionData -> {
            analyseur.addTransition(new Transition(editeurReseau.getEditeur(), projectController, transitionData));
        });

        data.getArcs().stream().forEach(arcData -> {
            if (arcData.getType().equals(ArcData.ARC_INH)) {
                matrixPane.addArcInh(new ArcInhibiteur(analyseur.getPlace(arcData.getPlaceId()), analyseur.getTransition(arcData.getTransitionID()), editeurReseau.getEditeur(), projectController, arcData));
            } else if (arcData.getType().equals(ArcData.ARC_POST)) {
                matrixPane.addArcPost(new ArcPost(analyseur.getTransition(arcData.getTransitionID()), analyseur.getPlace(arcData.getPlaceId()), editeurReseau.getEditeur(), projectController, arcData));
            } else if (arcData.getType().equals(ArcData.ARC_PRE)) {
                matrixPane.addArcPreOrdinaire(new ArcPreOrdinaire(analyseur.getPlace(arcData.getPlaceId()), analyseur.getTransition(arcData.getTransitionID()), editeurReseau.getEditeur(), projectController, arcData));
            }
        });
        setSaveModified();
    }

    private void initTooltips() {
        Font paletteFont = new Font("Consolas", 12);

        Tooltip tooltip = new Tooltip("Éditeur graphique du réseau");
        tooltip.setFont(paletteFont);
        editeurButton.setTooltip(tooltip);

        tooltip = new Tooltip("Représentation matricielle");
        tooltip.setFont(paletteFont);
        matricesButton.setTooltip(tooltip);

        tooltip = new Tooltip("Analyse comportementale du réseau");
        tooltip.setFont(paletteFont);
        gmaButton.setTooltip(tooltip);
    }

    public static class DamagedFile extends Exception {

        public DamagedFile(String message) {
            super(message);
        }
    }

    public static class OverSizePicture extends Exception {

        public OverSizePicture(String message) {
            super(message);
        }
    }

    public SimpleStringProperty titleProperty() {
        return title;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initButtons();
        initTooltips();
        ColorController.addColorable(this);

        title.set("- " + name);
        file.addListener((ObservableValue<? extends File> observable, File oldValue, File newValue) -> {
            if (newValue != null) {
                title.set("- " + name + " (" + newValue.getAbsolutePath() + ")");
            }
        });

        modified.addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (newValue) {
                if (file.get() != null) {
                    title.set("- " + name + " *(" + file.get().getAbsolutePath() + ")");
                } else {
                    title.set("- *" + name);
                }
            } else {
                if (file.get() != null) {
                    title.set("- " + name + " (" + file.get().getAbsolutePath() + ")");
                } else {
                    title.set("- " + name);
                }
            }
        });
    }

    public boolean isModified() {
        return modified.get();
    }

    public void setModified() {
        this.modified.set(true);
    }

    public void setSaveModified() {
        this.modified.set(false);
    }

    public File getFile() {
        return file.get();
    }

    public void close() {
        RUNNABLE_LIST.stream().forEach(r -> {
            r.Stop();
        });
        AnalyseurReseau.RUNNABLE_LIST.remove(RUNNABLE_LIST);
        if ((file.get() == null && modified.get()) || modified.get()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Voulez vous enregistrer les modifications apportées?", ButtonType.YES, ButtonType.NO);
            ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons().add(FenetrePrincipale.SAVE_ICON);
            alert.setHeaderText("Projet: (" + name + ") non enregistrer!");
            alert.showAndWait().filter(r -> r == ButtonType.YES).ifPresent(r -> saveProject());
        }
    }
}
