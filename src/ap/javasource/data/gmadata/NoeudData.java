package ap.javasource.data.gmadata;

import com.thoughtworks.xstream.annotations.*;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import ap.javasource.rdp.analyses.Marquage;
import ap.javasource.rdp.gmapane.marquage.GraphicMarquage;
import ap.javasource.rdp.graphiques.transition.Transition;
import java.util.ArrayList;
import javafx.util.Pair;

/**
 *
 * @author Tarek
 */
@XStreamAlias("Marquage")
public class NoeudData {

    @XStreamAsAttribute
    private final String id;
    @XStreamAsAttribute
    private final String info;

    @XStreamAsAttribute
    private final int x;
    @XStreamAsAttribute
    private final int y;

    @XStreamImplicit
    private final ArrayList<Successeur> succ = new ArrayList<>();

    public NoeudData(String id, String info, ArrayList<Pair<Marquage, Transition>> list, GraphicMarquage graphic) {
        this.id = "M" + id;
        this.info = info;
        x = (int) graphic.XProperty().get();
        y = (int) graphic.YProperty().get();
        list.stream().forEach((Pair<Marquage, Transition> pair) -> {
            succ.add(new Successeur("M" + pair.getKey().getId(), "T" + pair.getValue().getObjId() + ": " + pair.getValue().getTaux()));
        });
    }

    public String getInfo() {
        return info;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getId() {
        return id;
    }

    public ArrayList<Successeur> getSucc() {
        return succ;
    }

}
