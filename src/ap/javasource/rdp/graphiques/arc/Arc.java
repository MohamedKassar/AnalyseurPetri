package ap.javasource.rdp.graphiques.arc;

import ap.javasource.data.rdpdata.ArcData;
import ap.javasource.rdp.graphiques.Selectionable.Selectionable;
import ap.javasource.rdp.project.Editeur;
import ap.javasource.rdp.graphiques.place.Place;
import ap.javasource.rdp.graphiques.supprimable.Supprimable;
import ap.javasource.rdp.graphiques.transition.Transition;
import ap.javasource.rdp.project.ProjectController;
import javafx.beans.binding.When;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

/**
 *
 * @author Tarek
 */
public abstract class Arc extends Group implements Supprimable, Selectionable {

    protected final Place place;
    protected final Transition transition;
    protected final SimpleIntegerProperty poid = new SimpleIntegerProperty(1);

    protected ProjectController projectController;

    private final Editeur editeurPere;
    private final Label poidLabel = new Label();
    protected TLine line;
    protected Shape motif;
    protected ChangeListener motifPositioner;
    protected DoubleProperty scale;

    protected Arc(Transition transition, Place place, Editeur editeurPere, ProjectController projectController) {
        this.projectController = projectController;
        this.place = place;
        this.transition = transition;
        this.editeurPere = editeurPere;
        this.scale = projectController.getProject().getEditeurReseau().getEditeur().scaleProprety();
        line = new TLine(projectController, scale);
        getChildren().add(line);
        editeurPere.getPaneChildren().add(0, this);
        initHandler();
        initLabel();
        projectController.getProject().getAnalyseurReseau().addArc(this);
        projectController.getProject().setModified();
    }

    protected abstract void initMotif();

    @Override
    public void supprimer() {
        projectController.getProject().setModified();
        editeurPere.getPaneChildren().remove(this);
        transition.supprimerArc(this);
        place.supprimerArc(this);
        projectController.getProject().getAnalyseurReseau().removeArc(this);

    }

    public SimpleIntegerProperty getPoidProperty() {
        return poid;
    }

    public int getPoid() {
        return poid.get();
    }

    public void setPoid(int poid) {
        projectController.getProject().setModified();
        this.poid.set(poid);
    }

    public abstract void tirer();

    public Place getPlace() {
        return place;
    }

    public Transition getTransition() {
        return transition;
    }

    private void initHandler() {
        setOnMouseClicked(e -> {
            if (!line.isAdd()) {
                if (projectController.delete.get()) {
                    supprimer();
                } else if (projectController.none.get()) {
                    if (projectController.selectedObject.get() == this) {
                        projectController.selectedObject.set(null);
                    } else {
                        projectController.selectedObject.set(this);
                    }
                }
            }else{
                line.setAdd(false);
            }
            e.consume();
        });

    }

    private void initLabel() {
        getChildren().add(poidLabel);
        poidLabel.styleProperty().bind(new SimpleStringProperty("-fx-font-weight: bold; -fx-font-size: ").concat(scale.multiply(14)));

        poidLabel.layoutXProperty().bind(line.endXProperty().add(line.startXProperty()).divide(2).add(3));
        poidLabel.layoutYProperty().bind(line.endYProperty().add(line.startYProperty()).divide(2).add(3));

        poidLabel.textProperty().bind(poid.asString());

        poidLabel.visibleProperty().bind(new When(poid.isEqualTo(1)).then(false).otherwise(true));
    }

    @Override
    public void select() {
        line.setLinesStyle("-fx-stroke: red;");
        if (motif instanceof Circle) {
            motif.setStyle(" -fx-stroke: red; -fx-fill:white;");
        } else {
            motif.setStyle(" -fx-fill: red;");
        }
    }

    @Override
    public void deSelect() {
        line.setLinesStyle("-fx-stroke: black;");
        if (motif instanceof Circle) {
            motif.setStyle(" -fx-stroke: black; -fx-fill:white;");
        } else {
            motif.setStyle(" -fx-fill: black;");
        }
    }

    public abstract ArcData getData();

}
