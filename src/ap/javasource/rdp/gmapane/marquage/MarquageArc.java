package ap.javasource.rdp.gmapane.marquage;

import ap.javasource.rdp.graphiques.arc.TLine;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.shape.Polygon;

/**
 *
 * @author Tarek
 */
public class MarquageArc extends Group {

    private final TLine line;
    private Polygon motif;
    private ChangeListener motifPositioner;
    private final Label tauxLabel = new Label();

    public MarquageArc(GraphicMarquage graphicMarquage1, GraphicMarquage graphicMarquage2, String taux, DoubleProperty scale) {
        this.line = new TLine(scale);
        line.setPropreties(graphicMarquage1.XProperty(), graphicMarquage1.YProperty(), graphicMarquage2.XProperty(), graphicMarquage2.YProperty());
        getChildren().add(line);
        initMotif(graphicMarquage2, scale);
        initLabel(taux, scale);
    }

    private void initLabel(String taux, DoubleProperty scale) {
        getChildren().add(tauxLabel);
        tauxLabel.styleProperty().bind(new SimpleStringProperty("-fx-font-weight: bold; -fx-font-size: ").concat(scale.multiply(14)).concat(" ;"));

        tauxLabel.layoutXProperty().bind(line.endXProperty().add(line.startXProperty()).divide(2).add(3));
        tauxLabel.layoutYProperty().bind(line.endYProperty().add(line.startYProperty()).divide(2).add(3));
        tauxLabel.setText(taux);
    }

    private void initMotif(GraphicMarquage graphicMarquage, DoubleProperty scale) {
        motif = new Polygon(0.0, 5.0, -5.0, -5.0, 5.0, -5.0);
        motif.scaleXProperty().bind(scale);
        motif.scaleYProperty().bind(scale);
        motifPositioner = (ObservableValue observable, Object oldValue, Object newValue) -> {
            double angle = Math.atan2(line.getEndY() - line.getStartY(), line.getEndX() - line.getStartX()) * 180 / 3.14;
            motif.setRotate(angle - 90);
            double lon = Math.sqrt(((line.getEndY() - line.getStartY()) * (line.getEndY() - line.getStartY())) + ((line.getEndX() - line.getStartX()) * (line.getEndX() - line.getStartX())));
            motif.setLayoutX(line.getEndX() - ((graphicMarquage.getWidth() / 10 + 25) * (line.getEndX() - line.getStartX())) / lon);
            motif.setLayoutY(line.getEndY() - ((graphicMarquage.getWidth() / 10 + 25) * (line.getEndY() - line.getStartY())) / lon);
        };
        line.setMotifPositioner(motifPositioner);
        motifPositioner.changed(null, 0, 0);
        getChildren().add(motif);
        motif.setStyle("-fx-fill: black; -fx-stroke: black;");
    }
}
