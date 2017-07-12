/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ap.javasource.data.rdpdata;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import java.io.Serializable;

/**
 *
 * @author Tarek
 */
@XStreamAlias("Transition")
public class TransitionData implements Serializable {

    @XStreamAsAttribute
    private final int id;

    @XStreamAlias("nom")
    @XStreamAsAttribute
    private final String name;

    @XStreamAsAttribute
    private final int priorite;

    @XStreamAsAttribute
    private final double taux;

    @XStreamAsAttribute
    private final double x;

    @XStreamAsAttribute
    private final double y;

    @XStreamAsAttribute
    private final double rotation;

    public TransitionData(int id, String name, int priorite, double taux, double x, double y, double rotation) {
        this.id = id;
        this.name = name;
        this.priorite = priorite;
        this.taux = taux;
        this.x = x;
        this.y = y;
        this.rotation = rotation;
    }

    public int getId() {
        return id;
    }

    public double getRotation() {
        return rotation;
    }
    
    public String getName() {
        return name;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getPriorite() {
        return priorite;
    }

    public double getTaux() {
        return taux;
    }

}
