package ap.javasource.rdp.matrix;

import ap.javasource.rdp.graphiques.arc.ArcInhibiteur;
import ap.javasource.rdp.graphiques.arc.ArcPost;
import ap.javasource.rdp.graphiques.arc.ArcPreOrdinaire;
import ap.javasource.rdp.graphiques.place.Place;
import ap.javasource.rdp.graphiques.transition.Transition;
import ap.javasource.rdp.project.ProjectController;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.TreeMap;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javax.imageio.ImageIO;

/**
 *
 * @author Tarek & Hadjer
 */
public class MatrixPane extends SplitPane implements Initializable {

    @FXML
    private TableView<MatrixRow> postTable;
    @FXML
    private TableView<MatrixRow> preTable;
    @FXML
    private TableView<MatrixRow> incidenceTable;
    @FXML
    private TableView<MatrixRow> inhTable;

    private final ProjectController projectController;
    private final TreeMap<Integer, TableColumn> postColumns = new TreeMap<>();
    private final TreeMap<Integer, TableColumn> preColumns = new TreeMap<>();
    private final TreeMap<Integer, TableColumn> incidenceColumns = new TreeMap<>();
    private final TreeMap<Integer, TableColumn> inhColumns = new TreeMap<>();

    private Matrix postData;
    private Matrix preData;
    private Matrix incidenceData;
    private Matrix inhData;

    public MatrixPane(ProjectController projectController) {
        this.projectController = projectController;

        FXMLLoader fXMLLoader = new FXMLLoader(getClass().getResource("/ap/resources/fxml/matrix/MatrixPane.fxml"));
        fXMLLoader.setController(this);
        fXMLLoader.setRoot(this);
        try {
            fXMLLoader.load();
        } catch (IOException ex) {
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        postData = new Matrix(projectController.getProject().getAnalyseurReseau());
        preData = new Matrix(projectController.getProject().getAnalyseurReseau());
        incidenceData = new Matrix(projectController.getProject().getAnalyseurReseau());
        inhData = new Matrix(projectController.getProject().getAnalyseurReseau());

        postTable.setItems(postData.getData());
        preTable.setItems(preData.getData());
        incidenceTable.setItems(incidenceData.getData());
        inhTable.setItems(inhData.getData());

        TableColumn<MatrixRow, String> placeHader = new TableColumn<>("");
        placeHader.setCellValueFactory((TableColumn.CellDataFeatures<MatrixRow, String> param) -> param.getValue().getPlace().getNameProperty());

        postTable.getColumns().add(placeHader);
        placeHader.setPrefWidth(35);

        placeHader = new TableColumn<>("");
        placeHader.setCellValueFactory((TableColumn.CellDataFeatures<MatrixRow, String> param) -> param.getValue().getPlace().getNameProperty());
        preTable.getColumns().add(placeHader);
        placeHader.setPrefWidth(35);

        placeHader = new TableColumn<>("");
        placeHader.setCellValueFactory((TableColumn.CellDataFeatures<MatrixRow, String> param) -> param.getValue().getPlace().getNameProperty());
        incidenceTable.getColumns().add(placeHader);
        placeHader.setPrefWidth(35);

        placeHader = new TableColumn<>("");
        placeHader.setCellValueFactory((TableColumn.CellDataFeatures<MatrixRow, String> param) -> param.getValue().getPlace().getNameProperty());
        inhTable.getColumns().add(placeHader);
        placeHader.setPrefWidth(35);

    }

    public void addPlace(Place place) {
        postData.addPlace(place);
        preData.addPlace(place);
        incidenceData.addPlace(place);
        inhData.addPlace(place);

    }

    public void removePlace(Place place) {
        postData.removePlace(place);
        preData.removePlace(place);
        incidenceData.removePlace(place);
        inhData.removePlace(place);
    }

    public void addTransition(Transition transition) {
        postData.addTransition(transition);
        preData.addTransition(transition);
        incidenceData.addTransition(transition);
        inhData.addTransition(transition);
        TableColumn<MatrixRow, Integer> temp = new TableColumn<>();
        temp.textProperty().bind(transition.getNameProperty());
        temp.setCellValueFactory((TableColumn.CellDataFeatures<MatrixRow, Integer> param) -> param.getValue().getProperty(transition).asObject());

        preColumns.put(transition.getObjId(), temp);
        preTable.getColumns().add(temp);

        temp = new TableColumn<>();
        temp.textProperty().bind(transition.getNameProperty());
        temp.setCellValueFactory((TableColumn.CellDataFeatures<MatrixRow, Integer> param) -> param.getValue().getProperty(transition).asObject());

        postColumns.put(transition.getObjId(), temp);
        postTable.getColumns().add(temp);

        temp = new TableColumn<>();
        temp.textProperty().bind(transition.getNameProperty());
        temp.setCellValueFactory((TableColumn.CellDataFeatures<MatrixRow, Integer> param) -> param.getValue().getProperty(transition).asObject());

        inhColumns.put(transition.getObjId(), temp);
        inhTable.getColumns().add(temp);

        temp = new TableColumn<>();
        temp.textProperty().bind(transition.getNameProperty());
        temp.setCellValueFactory((TableColumn.CellDataFeatures<MatrixRow, Integer> param) -> {
            param.getValue().getProperty(transition).bind(
                    postData.getCellValueProprety(param.getValue().getPlace(), transition)
                    .subtract(preData.getCellValueProprety(param.getValue().getPlace(), transition)));
            return param.getValue().getProperty(transition).asObject();
        });

        incidenceColumns.put(transition.getObjId(), temp);
        incidenceTable.getColumns().add(temp);

    }

    public void removeTransition(Transition transition) {
        postData.removeTransition(transition);
        preData.removeTransition(transition);
        incidenceData.removeTransition(transition);
        inhData.removeTransition(transition);

        postTable.getColumns().remove(postColumns.remove(transition.getObjId()));
        preTable.getColumns().remove(preColumns.remove(transition.getObjId()));
        incidenceTable.getColumns().remove(incidenceColumns.remove(transition.getObjId()));
        inhTable.getColumns().remove(inhColumns.remove(transition.getObjId()));
    }

    public void addArcPreOrdinaire(ArcPreOrdinaire arc) {
        SimpleIntegerProperty temp = preData.getCellValueProprety(arc);
        temp.bind(arc.getPoidProperty());
    }

    public void addArcPost(ArcPost arc) {
        SimpleIntegerProperty temp = postData.getCellValueProprety(arc);
        temp.bind(arc.getPoidProperty());
    }

    public void addArcInh(ArcInhibiteur arc) {
        inhData.getCellValueProprety(arc).bind(arc.getPoidProperty());
    }

    public void removeArcPreOrdinaire(ArcPreOrdinaire arc) {
        SimpleIntegerProperty temp = preData.getCellValueProprety(arc);
        temp.unbind();
        temp.set(0);
    }

    public void removeArcPost(ArcPost arc) {
        SimpleIntegerProperty temp = postData.getCellValueProprety(arc);
        temp.unbind();
        temp.set(0);
    }

    public void removeArcInh(ArcInhibiteur arc) {
        SimpleIntegerProperty temp = inhData.getCellValueProprety(arc);
        temp.unbind();
        temp.set(0);
    }

    public boolean saveMatrixsAsPicture(String path) {
        DateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmss");

        try {
            ImageIO.write(SwingFXUtils.fromFXImage(preTable.snapshot(null, null), null), "png", new File(path + projectController.getProject().getName().concat("_" + "matrice pre").concat("_" + dateFormat.format(new Date())).concat(".png")));
        } catch (Exception s) {
            return false;
        }
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(postTable.snapshot(null, null), null), "png", new File(path + projectController.getProject().getName().concat("_" + "matrice post").concat("_" + dateFormat.format(new Date())).concat(".png")));
        } catch (Exception s) {
            return false;
        }
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(incidenceTable.snapshot(null, null), null), "png", new File(path + projectController.getProject().getName().concat("_" + "matrice d'incidence").concat("_" + dateFormat.format(new Date())).concat(".png")));
        } catch (Exception s) {
            return false;
        }
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(inhTable.snapshot(null, null), null), "png", new File(path + projectController.getProject().getName().concat("_" + "matrice d'inhibition").concat("_" + dateFormat.format(new Date())).concat(".png")));
        } catch (Exception s) {
            return false;
        }
        return true;
    }
}
