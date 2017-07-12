package ap.javasource.rdp.graphiques.arc;

import ap.javasource.data.rdpdata.ArcData;
import ap.javasource.rdp.project.Editeur;
import ap.javasource.rdp.graphiques.place.Place;
import ap.javasource.rdp.graphiques.transition.Transition;
import ap.javasource.rdp.project.ProjectController;
import javafx.beans.value.ObservableValue;
import javafx.scene.shape.Polygon;

/**
 *
 * @author Tarek
 */
public class ArcPost extends Arc {

    public ArcPost(Transition transition, Place place, Editeur editeurPere, ProjectController projectController) {
        super(transition, place, editeurPere, projectController);
        initMotif();
        line.setPropreties(transition.XProperty(), transition.YProperty(), place.XProperty(), place.YProperty());
        transition.addPost(this);
        place.addPost(this);
    }

    @Override
    protected final void initMotif() {
        motif = new Polygon(0.0, 5.0, -5.0, -5.0, 5.0, -5.0);
        motifPositioner = (ObservableValue observable, Object oldValue, Object newValue) -> {
            double angle = Math.atan2(line.getEndY() - line.getStartY(), line.getEndX() - line.getStartX()) * 180 / 3.14;
            motif.setRotate(angle - 90);
            double lon = Math.sqrt(((line.getEndY() - line.getStartY()) * (line.getEndY() - line.getStartY())) + ((line.getEndX() - line.getStartX()) * (line.getEndX() - line.getStartX())));
            motif.setLayoutX(line.getEndX() - ((scale.get() * Place.RAYON.doubleValue() + 4) * (line.getEndX() - line.getStartX())) / lon);
            motif.setLayoutY(line.getEndY() - ((scale.get() * Place.RAYON.doubleValue() + 4) * (line.getEndY() - line.getStartY())) / lon);
        };
        line.setMotifPositioner(motifPositioner);
        motifPositioner.changed(null, 0, 0);

        getChildren().add(motif);
        motif.setStyle("-fx-fill: black; -fx-stroke: black;");
    }

    @Override
    public void tirer() {
        place.incrementerMarquageAnalysePar(getPoid());
    }

    @Override
    public void supprimer() {
        super.supprimer();
        projectController.getProject().getMatrixPane().removeArcPost(this);
    }

    @Override
    public ArcData getData() {
        return new ArcData(place.getObjId(), transition.getObjId(), poid.get(), ArcData.ARC_POST, line.getAnchorsPoints());
    }

    public ArcPost(Transition transition, Place place, Editeur editeurPere, ProjectController projectController, ArcData data) {
        this(transition, place, editeurPere, projectController);
        poid.set(data.getPoid());
        line.edit(data.getPoints());
    }
}
