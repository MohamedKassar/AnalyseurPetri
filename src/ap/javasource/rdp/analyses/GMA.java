package ap.javasource.rdp.analyses;

import ap.javasource.rdp.gmapane.GMAPane;
import ap.javasource.rdp.graphiques.place.Place;
import ap.javasource.rdp.graphiques.transition.Transition;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import javafx.util.Pair;

/**
 *
 * @author Tarek
 */
public class GMA extends TreeMap<Marquage, Pair<ArrayList<Pair<Marquage, Transition>>, ArrayList<Marquage>>> {

    public boolean borne = true, b;
    private Marquage marquageTemp;
    private final GMAPane gmaPane;
    public long arc = 0;

    public GMA(Marquage m0, GMAPane gmaPane) {
        super((Marquage o1, Marquage o2) -> {
            {
                if (o1.id == -1 || o2.id == -1) {
                    if (o1.equals(o2)) {
                        return 0;
                    }
                    return 1;
                } else {
                    if (o1.id == o2.id) {
                        return 0;
                    } else if (o1.id > o2.id) {
                        return 1;
                    }
                    return -1;
                }
            }
        });
        this.gmaPane = gmaPane;
        m0.setId();
        put(m0, new Pair<>(new ArrayList<Pair<Marquage, Transition>>(), new ArrayList<Marquage>()));
        gmaPane.addMarquage(m0);
        gmaPane.lineBreak();
    }

    public boolean add(Marquage source, Transition transition, Marquage nouveau) {
        arc++;
        marquageTemp = verifier(nouveau);
        if (marquageTemp != nouveau) {
            b = false;
        } else {
            marquageTemp.setId();
            put(marquageTemp, new Pair<>(new ArrayList<Pair<Marquage, Transition>>(), new ArrayList<Marquage>()));
            b = true;
        }
        get(marquageTemp).getValue().add(source);
        get(source).getKey().add(new Pair<>(marquageTemp, transition));
        traiterBornitude(marquageTemp);
        if (b) {
            gmaPane.addMarquage(marquageTemp);
        }
        gmaPane.link(source, marquageTemp, transition.getNameProperty().get()+ ": " + transition.getTaux());
        return b;
    }

    public Marquage verifier(Marquage marquage) {
        Set<Marquage> set = keySet();
        for (Marquage m : set) {
            if (m.equals(marquage)) {
                return m;
            }
        }
        return marquage;
    }

    @Override
    public String toString() {
        String temp = "";
        Set<Marquage> set = keySet();
        for (Marquage m : set) {
            temp += m.toString() + " -> ";
            Pair<ArrayList<Pair<Marquage, Transition>>, ArrayList<Marquage>> pairSourceDest = get(m);
            ArrayList<Pair<Marquage, Transition>> list = pairSourceDest.getKey();
            temp = list.stream().map((p) -> "(" + p.getValue() + " - " + p.getKey() + "), ").reduce(temp, String::concat);
            temp = temp.substring(0, temp.length() - 2) + "\n\t source: " + pairSourceDest.getValue() + "\n\n";
        }
        return temp;
    }

    private void traiterBornitude(Marquage marquage) {
        ArrayList<Marquage> listTemp = new ArrayList<>();
        ArrayList<Place> listPlaces = new ArrayList<>();
        testerSiPlusGrand(marquage, marquage, listTemp, listPlaces);

        if (listPlaces.size() > 0) {
            borne = false;
            marquage.stream().forEach((pair) -> {
                Iterator<Place> i = listPlaces.iterator();
                boolean b = true;
                while (i.hasNext() && b) {
                    Place p = i.next();
                    if (pair.getKey() == p) {
                        pair.getValue().set(-1);
                        b = false;
                    }
                }
            });
        }
    }

    public void testerSiPlusGrand(Marquage marquage, Marquage cour, ArrayList<Marquage> dejaTraiter, ArrayList<Place> places) {
        dejaTraiter.add(cour);
        ArrayList<Marquage> marquages = get(cour).getValue();
        for (Marquage m : marquages) {
            if (marquage.plusGrandQue(m, places)) {
                return;
            }
        }
        marquages.stream().filter((m) -> (!dejaTraiter.contains(m))).forEach((m) -> {
            testerSiPlusGrand(marquage, m, dejaTraiter, places);
        });
    }

    @Override
    public boolean containsKey(Object key) {
        Marquage m = (Marquage) key;
        if (m.id != -1) {
            return true;
        } else {
            for (Marquage marquage : keySet()) {
                if (marquage.equals(key)) {
                    return true;
                }
//                 if (comparator().compare(marquage, m)==0) {
//                    return true;
//                } 
            }
            return false;
        }
    }

}
