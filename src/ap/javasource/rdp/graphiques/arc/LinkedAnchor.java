/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ap.javasource.rdp.graphiques.arc;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 *
 * @author Tarek
 */
public class LinkedAnchor extends Circle {

    private LinkedLine first, last;
    private SimpleDoubleProperty x = new SimpleDoubleProperty(), y = new SimpleDoubleProperty();
    private final DoubleProperty scale;

    public LinkedAnchor(double x, double y, LinkedLine first, LinkedLine last, DoubleProperty scale) {
        super(x, y, 3, Color.BLACK);
        this.scale = scale;
        this.x.set(x);
        this.y.set(y);
        centerXProperty().bind(this.x.multiply(scale));
        centerYProperty().bind(this.y.multiply(scale));
        this.first = first;
        this.last = last;

        setOnMouseEntered(e -> {
            setRadius(6);
        });
        setOnMouseExited(e -> {
            setRadius(3);
        });
    }

    public void setX(double x) {
        this.x.set(x / scale.get());
    }

    public void setY(double y) {
        this.y.set(y / scale.get());
    }

    public void setFirst(LinkedLine first) {
        this.first = first;
    }

    public void setLast(LinkedLine last) {
        this.last = last;
    }

    public LinkedLine getFirst() {
        return first;
    }

    public LinkedLine getLast() {
        return last;
    }

}
