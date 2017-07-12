package ap.javasource.data.gmadata;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 *
 * @author Tarek
 */
@XStreamAlias("Marquage_successeur")
public class Successeur {

    @XStreamAlias("id")
    @XStreamAsAttribute
    private final String Marquage;
    
    @XStreamAsAttribute
    private final String Transition;

    public Successeur(String Marquage, String Transition) {
        this.Marquage = Marquage;
        this.Transition = Transition;
    }

    public String getMarquage() {
        return Marquage;
    }

    public String getTransition() {
        return Transition;
    }
}
