package ap.javasource.rdp.graphiques.place;

import ap.javasource.data.rdpdata.PlaceData;
import ap.javasource.rdp.project.Editeur;
import ap.javasource.rdp.graphiques.objetgraphique.ObjetGraphique;
import ap.javasource.rdp.project.ProjectController;
import java.util.Arrays;
import javafx.beans.binding.When;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;

/**
 *
 * @author Tarek
 */
public final class Place extends ObjetGraphique {

    private final SimpleIntegerProperty marquage = new SimpleIntegerProperty(0);
    private int marquageAnalyse;

    public static DoubleProperty RAYON = new SimpleDoubleProperty(25);

    private final Circle cerclePrincipal = new Circle();
    private final Circle[] jetons = new Circle[5];
    private final Label labelMarquage = new Label();

    public Place(double x, double y, Editeur editeurPere, int id, ProjectController projectController) {
        super(id, editeurPere, projectController);
        initGraphique(x, y);
        initListners(cerclePrincipal);
        editeurPere.getPaneChildren().add(this);
        initMarquageAnalyse();
        nameProprety.bind(new When(name.isEqualTo("")).then("P" + id).otherwise(name));
    }

    @Override
    public DoubleProperty XProperty() {
        return cerclePrincipal.centerXProperty();
    }

    @Override
    public DoubleProperty YProperty() {
        return cerclePrincipal.centerYProperty();
    }

    public double getX() {
        return cerclePrincipal.getCenterX();
    }

    public double getY() {
        return cerclePrincipal.getCenterY();
    }

    @Override
    public void deplacer(double x, double y) {
        super.deplacer(x, y);
        super.x.set(x / scale.get());
        super.y.set(y / scale.get());
    }

    private void initGraphique(double x, double y) {
        //initilisation du groupe
        toFront();

        //initialisation du cercle principale
        cerclePrincipal.centerXProperty().bind(super.x.multiply(scale));
        cerclePrincipal.centerYProperty().bind(super.y.multiply(scale));
        cerclePrincipal.radiusProperty().bind(RAYON.multiply(scale));
        cerclePrincipal.setStyle("-fx-fill: white; -fx-stroke: black;");

        deplacer(x, y);
        //initialisation des jetons
        for (int i = 0; i < 5; i++) {
            jetons[i] = new Circle();
            jetons[i].radiusProperty().bind(cerclePrincipal.radiusProperty().divide(7).multiply(scale));
            jetons[i].setStyle("-fx-fill: black");

        }
        jetons[0].centerXProperty().bind(cerclePrincipal.centerXProperty().subtract(cerclePrincipal.radiusProperty().divide(2)));
        jetons[0].centerYProperty().bind(cerclePrincipal.centerYProperty().add(cerclePrincipal.radiusProperty().divide(2)));
        jetons[0].visibleProperty().bind(new When(new SimpleBooleanProperty(false).or(marquage.greaterThan(0)).and(marquage.lessThan(6))).then(true).otherwise(false));

        jetons[1].centerXProperty().bind(cerclePrincipal.centerXProperty().add(cerclePrincipal.radiusProperty().divide(2)));
        jetons[1].centerYProperty().bind(cerclePrincipal.centerYProperty().subtract(cerclePrincipal.radiusProperty().divide(2)));
        jetons[1].visibleProperty().bind(new When(new SimpleBooleanProperty(false).or(marquage.greaterThan(1)).and(marquage.lessThan(6))).then(true).otherwise(false));

        jetons[2].centerXProperty().bind(cerclePrincipal.centerXProperty().subtract(cerclePrincipal.radiusProperty().divide(2)));
        jetons[2].centerYProperty().bind(cerclePrincipal.centerYProperty().subtract(cerclePrincipal.radiusProperty().divide(2)));
        jetons[2].visibleProperty().bind(new When(new SimpleBooleanProperty(false).or(marquage.greaterThan(2)).and(marquage.lessThan(6))).then(true).otherwise(false));

        jetons[3].centerXProperty().bind(cerclePrincipal.centerXProperty().add(cerclePrincipal.radiusProperty().divide(2)));
        jetons[3].centerYProperty().bind(cerclePrincipal.centerYProperty().add(cerclePrincipal.radiusProperty().divide(2)));
        jetons[3].visibleProperty().bind(new When(new SimpleBooleanProperty(false).or(marquage.greaterThan(3)).and(marquage.lessThan(6))).then(true).otherwise(false));

        jetons[4].centerXProperty().bind(cerclePrincipal.centerXProperty());
        jetons[4].centerYProperty().bind(cerclePrincipal.centerYProperty());
        jetons[4].visibleProperty().bind(new When(new SimpleBooleanProperty(false).or(marquage.greaterThan(4)).and(marquage.lessThan(6))).then(true).otherwise(false));

        //initilisation du label
        labelMarquage.styleProperty().bind(new SimpleStringProperty("-fx-font-size : ").concat(RAYON.multiply(scale)));
        labelMarquage.layoutXProperty().bind(cerclePrincipal.centerXProperty().subtract(labelMarquage.widthProperty().multiply(scale).divide(scale.multiply(2))));
        labelMarquage.layoutYProperty().bind(cerclePrincipal.centerYProperty().subtract(labelMarquage.heightProperty().multiply(scale).divide(scale.multiply(2))));
        labelMarquage.textProperty().bind(new SimpleStringProperty("").concat(marquage));
        labelMarquage.visibleProperty().bind(new When(marquage.greaterThan(5)).then(true).otherwise(false));

        labelNom.layoutXProperty().bind(cerclePrincipal.centerXProperty().subtract(scale.multiply(RAYON.get() + 10)));
        labelNom.layoutYProperty().bind(cerclePrincipal.centerYProperty().subtract(scale.multiply(RAYON.get() + 19)));
        labelNom.textProperty().bind(new When(name.isEqualTo("")).then(toString()).otherwise(name));

        //l'ajout des noueds a la place
        getChildren().add(0, cerclePrincipal);
        getChildren().add(1, labelMarquage);
        getChildren().add(1, labelNom);
        getChildren().addAll(1, Arrays.asList(jetons));

    }

    @Override
    public String toString() {
        return "P" + id;
    }

    public void setMarquage(int marquage) {
        projectController.getProject().setModified();
        this.marquage.set(marquage);
        marquageAnalyse = marquage;
    }

    public int getMarquage() {
        return marquage.get();
    }

    public int getMarquageAnalyse() {
        return marquageAnalyse;
    }

    public SimpleIntegerProperty getMarquageProperty() {
        return marquage;
    }

    public void setMarquageAnalyse(int marquage) {
        marquageAnalyse = marquage;
    }

    public void incrementerMarquageAnalysePar(int value) {
        setMarquageAnalyse(getMarquageAnalyse() + value);
    }

    public void decrementerMarquageAnalysePar(int value) {
        setMarquageAnalyse(getMarquageAnalyse() - value);
    }

    //**************************** A VOIR ****************************
    public void initMarquageAnalyse() {
        marquageAnalyse = marquage.get();
    }

    @Override
    public void select() {
//        projectController.selectedObject.set(this);
        cerclePrincipal.setStyle("-fx-fill: rgb(119, 181, 254); -fx-stroke: red;");
    }

    @Override
    public void deSelect() {
//        projectController.selectedObject.set(null);
        cerclePrincipal.setStyle("-fx-fill: white; -fx-stroke: black;");
    }

    @Override
    public void supprimer() {
        super.supprimer();
        projectController.getProject().getAnalyseurReseau().removePlace(this);
    }

    public PlaceData getData() {
        return new PlaceData(id, name.get(), marquage.get(), getX(), getY());
    }

    public Place(Editeur editeurPere, ProjectController projectController, PlaceData data) {
        this(data.getX(), data.getY(), editeurPere, data.getId(), projectController);
        name.set(data.getName());
        setMarquage(data.getMarquage());
    }

}
