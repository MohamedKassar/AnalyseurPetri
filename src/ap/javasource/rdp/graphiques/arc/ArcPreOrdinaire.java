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
public class ArcPreOrdinaire extends ArcPre {

    public ArcPreOrdinaire(Place place, Transition transition, Editeur editeurPere, ProjectController projectController) {
        super(place, transition, editeurPere, projectController);
        initMotif();
    }

    @Override
    protected final void initMotif() {
        motif = new Polygon(0.0, 5.0, -5.0, -5.0, 5.0, -5.0);
        motifPositioner = (ObservableValue observable, Object oldValue, Object newValue) -> {

            double angle = Math.atan2(line.getEndY() - line.getStartY(), line.getEndX() - line.getStartX()) * 180 / 3.14;
            motif.setRotate(angle - 90);
            double lon = Math.sqrt(((line.getEndY() - line.getStartY()) * (line.getEndY() - line.getStartY())) + ((line.getEndX() - line.getStartX()) * (line.getEndX() - line.getStartX())));

//            double a2 = (line.getEndY()-line.getStartY())/(line.getEndX() - line.getStartX());
//            double b2 = line.getEndY()-(a2*line.getEndX());
//            double a1 = (transition.getBoundsInParent().getMinY()-2-transition.getBoundsInParent().getMinY()-2)/(transition.getBoundsInParent().getMaxX()-transition.getBoundsInParent().getMinX());
//            double b1 = transition.getBoundsInParent().getMinY()-2-(a1*transition.getBoundsInParent().getMinX());
//            
//            p.setLayoutX((b2-b1)/(a1-a2));
//            p.setLayoutY((a1*((b2-b1)/(a1-a2))+b1));
            motif.setLayoutX(line.getEndX() - ((Transition.LARGEUR.doubleValue() / 10 + 5) * (line.getEndX() - line.getStartX())) / lon);
            motif.setLayoutY(line.getEndY() - ((Transition.LARGEUR.doubleValue() / 10 + 5) * (line.getEndY() - line.getStartY())) / lon);

        };
        line.setMotifPositioner(motifPositioner);
        motifPositioner.changed(null, 0, 0);
        getChildren().add(motif);
        motif.setStyle("-fx-fill: black; -fx-stroke: black;");
    }

    @Override
    public boolean isfranchissable() {
        return place.getMarquageAnalyse() >= getPoid();
    }

    @Override
    public void tirer() {
        place.decrementerMarquageAnalysePar(getPoid());
    }

    @Override
    public void supprimer() {
        super.supprimer();
        projectController.getProject().getMatrixPane().removeArcPreOrdinaire(this);
    }

    @Override
    public ArcData getData() {
        return new ArcData(place.getObjId(), transition.getObjId(), poid.get(), ArcData.ARC_PRE, line.getAnchorsPoints());
    }

    public ArcPreOrdinaire(Place place, Transition transition, Editeur editeurPere, ProjectController projectController, ArcData data) {
        this(place, transition, editeurPere, projectController);
        poid.set(data.getPoid());
        line.edit(data.getPoints());
    }
}
