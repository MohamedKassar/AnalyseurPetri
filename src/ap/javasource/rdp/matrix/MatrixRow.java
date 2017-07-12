package ap.javasource.rdp.matrix;

import ap.javasource.rdp.analyses.AnalyseurReseau;
import ap.javasource.rdp.graphiques.place.Place;
import ap.javasource.rdp.graphiques.transition.Transition;
import java.util.TreeMap;
import javafx.beans.property.SimpleIntegerProperty;

/**
 *
 * @author Tarek
 */
public class MatrixRow {

    private final TreeMap<Integer, SimpleIntegerProperty> list = new TreeMap<>();
    private final Place place;


    public MatrixRow(Place place, AnalyseurReseau analyseurReseau) {
        this.place = place;
        analyseurReseau.getTransitions().stream().forEach(transition -> {
            list.put(transition.getObjId(), new SimpleIntegerProperty(0));
        });
    }

    protected void addTransition(Transition transition) {
        list.put(transition.getObjId(), new SimpleIntegerProperty(0));
    }

    protected void removeTransition(Transition transition) {
        list.remove(transition.getObjId());
    }

    public Place getPlace() {
        return place;
    }

    public SimpleIntegerProperty getProperty(Transition transition) {
        return list.get(transition.getObjId());
    }
}
