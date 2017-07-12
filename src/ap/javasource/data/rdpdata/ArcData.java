package ap.javasource.data.rdpdata;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import java.io.Serializable;
import java.util.ArrayList;
import javafx.geometry.Point2D;

/**
 *
 * @author Tarek
 */
@XStreamAlias("Arc")
public class ArcData implements Serializable {

    public final static ArcType ARC_INH = ArcType.Arc_Inhibiteurs;
    public final static ArcType ARC_PRE = ArcType.Place_Transition;
    public final static ArcType ARC_POST = ArcType.Transition_Place;

    @XStreamAlias("id_place")
    @XStreamAsAttribute
    private final int placeId;

    @XStreamAlias("id_transition")
    @XStreamAsAttribute
    private final int transitionID;

    @XStreamAsAttribute
    private final int poid;

    @XStreamAsAttribute
    private final ArcType type;

    @XStreamImplicit
    private final ArrayList<Point> points = new ArrayList<>();

    public ArcData(int placeId, int transitionID, int poid, ArcType type, ArrayList<Point2D> points) {
        this.placeId = placeId;
        this.transitionID = transitionID;
        this.poid = poid;
        this.type = type;
        points.stream().forEach(p -> {
            this.points.add(new Point(p.getX(), p.getY()));
        });
    }

    public int getPlaceId() {
        return placeId;
    }

    public int getTransitionID() {
        return transitionID;
    }

    public int getPoid() {
        return poid;
    }

    public ArcType getType() {
        return type;
    }

    public ArrayList<Point> getPoints() {
        return points;
    }

    public enum ArcType {

        Transition_Place, Place_Transition, Arc_Inhibiteurs;
    }

    @XStreamAlias("point")
    public static class Point implements Serializable {

        @XStreamAsAttribute
        final double x;

        @XStreamAsAttribute
        final double y;

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

    }
}
