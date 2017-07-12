package ap.javasource.rdp.graphiques.arc;

import ap.javasource.rdp.project.Editeur;
import ap.javasource.rdp.graphiques.place.Place;
import ap.javasource.rdp.graphiques.transition.Transition;
import ap.javasource.rdp.project.ProjectController;

/**
 *
 * @author Tarek
 */
public abstract class ArcPre extends Arc {

    public ArcPre(Place place, Transition transition, Editeur editeurPere, ProjectController projectController) {
        super(transition, place, editeurPere, projectController);
        line.setPropreties(place.XProperty(), place.YProperty(), transition.XProperty(), transition.YProperty());
        transition.addPre(this);
        place.addPre(this);
    }

    public abstract boolean isfranchissable();
}
