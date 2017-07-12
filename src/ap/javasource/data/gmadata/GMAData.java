package ap.javasource.data.gmadata;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import ap.javasource.rdp.analyses.AnalyseurReseau;
import ap.javasource.rdp.analyses.Marquage;
import ap.javasource.rdp.gmapane.marquage.GraphicMarquage;
import java.util.ArrayList;
import java.util.TreeMap;

/**
 *
 * @author Tarek
 */
@XStreamAlias("Graphe_accessibilite")
public class GMAData {

    @XStreamAlias("nom")
    @XStreamAsAttribute
    private final String name;
    
    @XStreamAsAttribute
    private String places = "";

    private final ArrayList<Propriete> proprietes = new ArrayList<>();
    private final ArrayList<NoeudData> marquages = new ArrayList<>();

    public GMAData(AnalyseurReseau analyseur, TreeMap<Marquage, GraphicMarquage> marquages, String name) {
        this.name = name;
        analyseur.getPlaces().stream().forEach(place -> {
            places += ", " + place.getNameProperty().get();
        });
        places = places.substring(2, places.length());
        if (analyseur.isPseudoBloc() != null) {
            proprietes.add(new Propriete("Blocage", !analyseur.isPseudoBloc() ? "oui" : "non"));
        }
        if (analyseur.isVivant() != null) {
            proprietes.add(new Propriete("Vivacite", analyseur.isVivant() ? "oui" : "non"));
        }
        if (analyseur.isPseudoBloc() != null) {
            proprietes.add(new Propriete("Pseudo_vivacite", analyseur.isPseudoBloc() ? "oui" : "non"));
        }
        if (analyseur.isQuasi() != null) {
            proprietes.add(new Propriete("Quasi_vivacite", analyseur.isQuasi() ? "oui" : "non"));
        }
        if (analyseur.isConservatif() != null) {
            proprietes.add(new Propriete("Conservation", analyseur.isConservatif() ? "oui" : "non"));
        }
        if (analyseur.isReinit() != null) {
            proprietes.add(new Propriete("Reinitialisation", analyseur.isReinit() ? "oui" : "non"));
        }
        if (analyseur.getEtatAcc() != null) {
            proprietes.add(new Propriete("Etats_Accueils", analyseur.getEtatAcc().startsWith("I") ? "aucun" : analyseur.getEtatAcc()));
        }
        if (analyseur.getEtatPuit() != null) {
            proprietes.add(new Propriete("Etats_Puits", analyseur.getEtatPuit().startsWith("I") ? "aucun" : analyseur.getEtatPuit()));
        }

        analyseur.getGma().keySet().stream().forEach(marquage -> {
            this.marquages.add(new NoeudData(marquage.getId() + "", marquage.toString(), analyseur.getGma().get(marquage).getKey(), marquages.get(marquage)));
        });
    }

    public ArrayList<NoeudData> getMarquages() {
        return marquages;
    }

    public ArrayList<Propriete> getProprietes() {
        return proprietes;
    }

    public String getPlaces() {
        return places;
    }

    public String getName() {
        return name;
    }

    @XStreamAlias("Propriete")
    public static class Propriete {

        @XStreamAlias("nom")
        @XStreamAsAttribute
        private final String name;
        @XStreamAsAttribute
        private final String etat;

        public Propriete(String name, String etat) {
            this.name = name;
            this.etat = etat;
        }

        public String getName() {
            return name;
        }

        public String getEtat() {
            return etat;
        }

    }
}
