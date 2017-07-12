package ap.javasource.rdp.matrix;

import ap.javasource.rdp.analyses.AnalyseurReseau;
import ap.javasource.rdp.graphiques.arc.Arc;
import ap.javasource.rdp.graphiques.place.Place;
import ap.javasource.rdp.graphiques.transition.Transition;
import java.util.Iterator;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Tarek
 */
public class Matrix {

    private final ObservableList<MatrixRow> data = FXCollections.observableArrayList();
    private final AnalyseurReseau analyseurReseau;

    public Matrix(AnalyseurReseau analyseurReseau) {
        this.analyseurReseau = analyseurReseau;

    }

    public ObservableList<MatrixRow> getData() {
        return data;
    }

    public void addPlace(Place place) {
        data.add(new MatrixRow(place, analyseurReseau));
    }

    public void removePlace(Place place) {
        Iterator i = data.iterator();
        MatrixRow temp = null;
        boolean b = true;
        while (i.hasNext() && b) {
            temp = (MatrixRow) i.next();
            if (temp.getPlace() == place) {
                b = false;
            }
        }
        data.remove(temp);
    }

    public void addTransition(Transition transition) {
        data.stream().forEach(row -> {
            row.addTransition(transition);
        });
    }

    public void removeTransition(Transition transition) {
        data.stream().forEach(row -> {
            row.removeTransition(transition);
        });
    }

    public SimpleIntegerProperty getCellValueProprety(Arc arc) {
        Iterator i = data.iterator();
        MatrixRow temp = null;
        boolean b = true;
        while (i.hasNext() && b) {
            temp = (MatrixRow) i.next();
            if (temp.getPlace() == arc.getPlace()) {
                b = false;
            }
        }
        return temp.getProperty(arc.getTransition());
    }

    public SimpleIntegerProperty getCellValueProprety(Place place, Transition transition) {
        Iterator i = data.iterator();
        MatrixRow temp = null;
        boolean b = true;
        while (i.hasNext() && b) {
            temp = (MatrixRow) i.next();
            if (temp.getPlace() == place) {
                b = false;
            }
        }
        return temp.getProperty(transition);
    }
}
