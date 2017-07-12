package ap.javasource.data.rdpdata;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import java.io.Serializable;

/**
 *
 * @author Tarek
 */
@XStreamAlias("Place")
public class PlaceData implements Serializable {

    @XStreamAsAttribute
    private final int id;

    @XStreamAlias("nom")
    @XStreamAsAttribute
    private final String name;

    @XStreamAsAttribute
    private final int marquage;

    @XStreamAsAttribute
    private final double x;

    @XStreamAsAttribute
    private final double y;

    public PlaceData(int id, String name, int marquage, double x, double y) {
        this.id = id;
        this.name = name;
        this.marquage = marquage;
        this.x = x;
        this.y = y;
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getMarquage() {
        return marquage;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

}
