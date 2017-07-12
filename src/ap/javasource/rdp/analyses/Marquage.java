package ap.javasource.rdp.analyses;

import ap.javasource.rdp.graphiques.place.Place;
import java.util.ArrayList;
import java.util.Iterator;
import javafx.util.Pair;

/**
 *
 * @author Tarek
 */
public class Marquage extends ArrayList<Pair<Place, CostumInteger>> {

    protected int id = -1;
    private final AnalyseurReseau analyseur;

    public Marquage(AnalyseurReseau analyseur) {
        this.analyseur = analyseur;
        analyseur.places.stream().forEach((place) -> {
            add(new Pair<>(place, new CostumInteger(place.getMarquageAnalyse())));
        });
    }

    protected void setId() {
        id = analyseur.getIdMarquage();
    }

    public int getId() {
        return id;
    }

    public void setMarquageReseau() {
        stream().forEach((pair) -> {
            pair.getKey().setMarquageAnalyse(pair.getValue().get());
        });
    }

    public boolean plusGrandQue(Marquage marquage, ArrayList<Place> places) {
        Iterator<Pair<Place, CostumInteger>> i = iterator();
        Iterator<Pair<Place, CostumInteger>> i1 = marquage.iterator();
        Pair<Place, CostumInteger> pair;
        Pair<Place, CostumInteger> pair1;
        while (i.hasNext()) {
            pair = i.next();
            pair1 = i1.next();
            if (pair.getValue().get() > pair1.getValue().get()) {
                places.add(pair.getKey());
            } else {
                if (pair.getValue().get() < pair1.getValue().get()) {
                    places.clear();
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public String toString() {
        String temp = "[";
        temp = this.stream().map((pair) -> pair.getValue().get() + ", ").reduce(temp, String::concat);
        temp = temp.substring(0, temp.length() - 2) + "]";
        return temp;
    }

    public int getnombreJetons() {
        int temp =0;
        temp = this.stream().map((pair) -> pair.getValue().get()).reduce(temp, Integer::sum);
        return temp;
    }
    
    public boolean compareTo(ArrayList<Integer> marquage){
        boolean temp =true;
        for(int i=0; i<size() && temp; i++){
            if(!marquage.get(i).equals(get(i).getValue().get())){
                temp=false;
            }
        }
        return temp;
    }

}
