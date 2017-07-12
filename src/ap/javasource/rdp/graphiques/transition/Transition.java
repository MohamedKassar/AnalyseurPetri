package ap.javasource.rdp.graphiques.transition;

import ap.javasource.data.rdpdata.TransitionData;
import ap.javasource.rdp.graphiques.arc.ArcInhibiteur;
import ap.javasource.rdp.graphiques.arc.ArcPost;
import ap.javasource.rdp.graphiques.arc.ArcPre;
import ap.javasource.rdp.graphiques.arc.ArcPreOrdinaire;
import ap.javasource.rdp.project.Editeur;
import ap.javasource.rdp.graphiques.objetgraphique.ObjetGraphique;
import ap.javasource.rdp.graphiques.place.Place;
import ap.javasource.rdp.project.ProjectController;
import java.util.Iterator;
import javafx.beans.binding.When;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author Tarek
 */
public final class Transition extends ObjetGraphique {

    private final SimpleIntegerProperty priorite = new SimpleIntegerProperty(1);
    private final SimpleDoubleProperty taux = new SimpleDoubleProperty(1);

    private final Rectangle rectangle = new Rectangle();
    public static DoubleProperty LARGEUR = new SimpleDoubleProperty();

    public Transition(double x, double y, Editeur editeurPere, int id, ProjectController projectController) {
        super(id, editeurPere, projectController);
        initGraphique(x, y);
        initListners(rectangle);
        editeurPere.getPaneChildren().add(this);
        nameProprety.bind(new When(name.isEqualTo("")).then("T" + id).otherwise(name));

    }

    @Override
    public void deplacer(double x, double y) {
        super.deplacer(x, y);
        super.x.set((x - rectangle.getWidth() / 2) / scale.get());
        super.y.set((y - rectangle.getHeight() / 2) / scale.get());
    }

    private void initGraphique(double x, double y) {
        getChildren().add(rectangle);
        rectangle.xProperty().bind(super.x.multiply(scale));
        rectangle.yProperty().bind(super.y.multiply(scale));

        LARGEUR.bind(Place.RAYON.multiply(2));

        rectangle.widthProperty().bind(LARGEUR.multiply(scale));
        rectangle.heightProperty().bind(LARGEUR.divide(10).multiply(scale));

        deplacer(x, y);

        labelNom.layoutXProperty().bind(rectangle.xProperty().subtract(scale.multiply(60)));
        labelNom.layoutYProperty().bind(rectangle.yProperty().subtract(scale.multiply(20)));
        labelNom.textProperty().bind(new When(name.isEqualTo("")).then(toString()).otherwise(name).concat(", ").concat(priorite).concat(", ").concat(taux));

        getChildren().add(1, labelNom);

        rectangle.setStrokeWidth(2);
        rectangle.setStyle("-fx-fill: white; -fx-stroke: black;");

    }

    @Override
    public DoubleProperty XProperty() {
        SimpleDoubleProperty s = new SimpleDoubleProperty();
        s.bind(rectangle.xProperty().add(rectangle.widthProperty().divide(2)));
        return s;
    }

    @Override
    public DoubleProperty YProperty() {
        SimpleDoubleProperty s = new SimpleDoubleProperty();
        s.bind(rectangle.yProperty().add(rectangle.heightProperty().divide(2)));
        return s;
    }

    public void rotateRight() {
        rectangle.setRotate(rectangle.getRotate() + 45);
    }

    public void rotateLeft() {
        rectangle.setRotate(rectangle.getRotate() - 45);
    }

    @Override
    public String toString() {
        return "T" + id;
    }

    public boolean isFranchissable() {
        if (pre.isEmpty() && post.isEmpty()) {
            return false;
        }
        boolean temp = true;
        Iterator<ArcPre> i = pre.iterator();
        while (i.hasNext() && temp) {
            temp = i.next().isfranchissable();
        }
        return temp;
    }

    public void tirer() {
        pre.stream().forEach((arc) -> {
            arc.tirer();
        });
        post.stream().forEach((arc) -> {
            arc.tirer();
        });
    }

    public boolean alreadyLinkedInh(Place place) {
        for (ArcPre arc : pre) {
            if (arc instanceof ArcInhibiteur && arc.getPlace() == place) {
                return true;
            }
        }

        return false;
    }

    public void setPriorite(int priorite) {
        projectController.getProject().setModified();
        this.priorite.set(priorite);
    }

    public int getPriorite() {
        return priorite.get();
    }

    public void setTaux(double taux) {
        projectController.getProject().setModified();
        this.taux.set(taux);
    }

    public double getTaux() {
        return taux.get();
    }

    @Override
    public void supprimer() {
        super.supprimer();
        projectController.getProject().getAnalyseurReseau().removeTransition(this);
    }

    public boolean alreadyLinkedPost(Place place) {
        for (ArcPost arc : post) {
            if (arc.getPlace() == place) {
                return true;
            }
        }
        return false;
    }

    public boolean alreadyLinkedPre(Place place) {
        for (ArcPre arc : pre) {
            if (arc instanceof ArcPreOrdinaire && arc.getPlace() == place) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void select() {
//        projectController.selectedObject.set(this);
        rectangle.setStyle("-fx-fill: rgb(119, 181, 254); -fx-stroke: red;");
    }

    @Override
    public void deSelect() {
//        projectController.selectedObject.set(null);
        rectangle.setStyle("-fx-fill: white; -fx-stroke: black;");
    }

    public TransitionData getData() {
        return new TransitionData(id, name.get(), getPriorite(), getTaux(), XProperty().get(), YProperty().get(), rectangle.getRotate()%360);
    }

    public Transition(Editeur editeurPere, ProjectController projectController, TransitionData data) {
        this(data.getX(), data.getY(), editeurPere, data.getId(), projectController);
        name.set(data.getName());
        setPriorite(data.getPriorite());
        setTaux(data.getTaux());
        rectangle.setRotate(data.getRotation());
    }

}
