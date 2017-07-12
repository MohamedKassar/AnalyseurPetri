package ap.javasource.rdp.graphiques.arc;

import ap.javasource.data.rdpdata.ArcData;
import ap.javasource.rdp.project.Editeur;
import ap.javasource.rdp.graphiques.place.Place;
import ap.javasource.rdp.graphiques.transition.Transition;
import ap.javasource.rdp.project.ProjectController;
import javafx.beans.value.ObservableValue;
import javafx.scene.shape.Circle;

/**
 *
 * @author Tarek
 */
public class ArcInhibiteur extends ArcPre {

    public ArcInhibiteur(Place place, Transition transition, Editeur editeurPere, ProjectController projectController) {
        super(place, transition, editeurPere, projectController);
        initMotif();
    }

    @Override
    protected final void initMotif() {
        motif = new Circle(5); // TO DO
        motif.setStyle("-fx-fill: white; -fx-stroke: black;");

        motifPositioner = (ObservableValue observable, Object oldValue, Object newValue) -> {
            double lon = Math.sqrt(((line.getEndY() - line.getStartY()) * (line.getEndY() - line.getStartY())) + ((line.getEndX() - line.getStartX()) * (line.getEndX() - line.getStartX())));
            motif.setLayoutX(line.getEndX() - ((Transition.LARGEUR.doubleValue() / 10 + 5) * (line.getEndX() - line.getStartX())) / lon);
            motif.setLayoutY(line.getEndY() - ((Transition.LARGEUR.doubleValue() / 10 + 5) * (line.getEndY() - line.getStartY())) / lon);
        };

        line.setMotifPositioner(motifPositioner);
        motifPositioner.changed(null, 0, 0);

        getChildren().add(motif);
    }

    @Override
    public boolean isfranchissable() {
        return place.getMarquageAnalyse() < getPoid();
    }

    @Override
    public void tirer() {
    }

    @Override
    public void supprimer() {
        super.supprimer();
        projectController.getProject().getMatrixPane().removeArcInh(this);
    }

    @Override
    public ArcData getData() {
        return new ArcData(place.getObjId(), transition.getObjId(), poid.get(), ArcData.ARC_INH, line.getAnchorsPoints());
    }

    public ArcInhibiteur(Place place, Transition transition, Editeur editeurPere, ProjectController projectController, ArcData data) {
        this(place, transition, editeurPere, projectController);
        poid.set(data.getPoid());
        line.edit(data.getPoints());
    }
}
