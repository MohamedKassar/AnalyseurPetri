/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ap.javasource.rdp.graphiques.arc;

import javafx.beans.property.DoubleProperty;
import javafx.scene.shape.Line;

/**
 *
 * @author Tarek
 */
public class LinkedLine extends Line {

    private LinkedLine svt = null;

    private DoubleProperty x, y;

    private LinkedAnchor anchor = null;

    public LinkedLine() {
        setStrokeWidth(2);
    }
    

    public void setAnchor(LinkedAnchor anchor) {
        this.anchor = anchor;
    }

    public LinkedAnchor getAnchor() {
        return anchor;
    }

    public LinkedLine getSvt() {
        return svt;
    }

    public void setSvt(LinkedLine svt) {
        this.svt = svt;
    }

    public DoubleProperty getX() {
        return x;
    }

    public DoubleProperty getY() {
        return y;
    }

    public void setXY(DoubleProperty x, DoubleProperty y) {
        endXProperty().bind(x);
        endYProperty().bind(y);
        this.x = x;
        this.y = y;
    }

}
