package ap.javasource.data.rdpdata;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import ap.javasource.rdp.analyses.AnalyseurReseau;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Tarek
 */
@XStreamAlias("RDPSP")
public class ProjectData implements Serializable {

    @XStreamAsAttribute
    private final String id;

    @XStreamAlias("nom")
    @XStreamAsAttribute
    private final String name;

    @XStreamAlias("id_place")
    @XStreamAsAttribute
    private final int placeId;

    @XStreamAlias("id_transition")
    @XStreamAsAttribute
    private final int transitionId;


    private ArrayList<PlaceData> places = new ArrayList<>();
    private ArrayList<TransitionData> transitions = new ArrayList<>();
    private ArrayList<ArcData> arcs = new ArrayList<>();

    public ProjectData(String name, String id, AnalyseurReseau analyseur) {
        this.name = name;
        this.id = id;
        this.placeId = analyseur.getCurrentIdPlace();
        this.transitionId = analyseur.getCurrentIdTransition();

        analyseur.getPlaces().stream().forEach(place -> {
            places.add(place.getData());
        });
        analyseur.getTransitions().stream().forEach(transition -> {
            transitions.add(transition.getData());
        });
        analyseur.getArcs().stream().forEach(arc -> {
            arcs.add(arc.getData());
        });
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPlaceId() {
        return placeId;
    }

    public int getTransitionId() {
        return transitionId;
    }

    public ArrayList<PlaceData> getPlaces() {
        return places;
    }

    public ArrayList<TransitionData> getTransitions() {
        return transitions;
    }

    public ArrayList<ArcData> getArcs() {
        return arcs;
    }

}
