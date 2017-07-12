package ap.javasource.rdp.gmapane.marquage;

import ap.javasource.rdp.analyses.Marquage;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;

/**
 *
 * @author Tarek
 */
public class GraphicMarquage extends Group {

    private final Marquage marquage;
    private final Label label = new Label();
    private double width;

    protected EventHandler<DragEvent> listnerDragOver;
    protected EventHandler<DragEvent> listnerDragDropped;

    private final SimpleDoubleProperty x = new SimpleDoubleProperty(), y = new SimpleDoubleProperty();
    private final DoubleProperty scale;
    private String text;

    public GraphicMarquage(Marquage marquage, double x, double y, DoubleProperty scale) {
        this.marquage = marquage;
        this.scale = scale;
        initGraphics(x, y);
        initListners();
    }

    public GraphicMarquage(double x, double y, DoubleProperty scale, String text) {
        this.marquage = null;
        this.scale = scale;
        this.text = text;
        initGraphics(x, y);
        initListners();

    }

    public GraphicMarquage(Marquage marquage, DoubleProperty scale) {
        this(marquage, 0, 0, scale);
    }

    private void initGraphics(double x, double y) {
        label.styleProperty().bind(new SimpleStringProperty("-fx-background-color: white; -fx-border-color: black; -fx-font: ").concat(scale.multiply(16)).concat(" arial;"));
        // init Label
        String temp;
        if (marquage != null) {
            temp = " M" + marquage.getId() + ": ";
            temp = marquage.stream().map((pair) -> {
                return pair.getValue().get() + ", ";
            }).reduce(temp, String::concat);
            temp = temp.substring(0, temp.length() - 2);
            temp += " ";
        } else {
            temp = text;
        }
        label.setText(temp);
        width = temp.length() * 5;
        getChildren().add(label);
        label.layoutXProperty().bind(this.x.multiply(scale));
        label.layoutYProperty().bind(this.y.multiply(scale));
        deplacer(x, y);
    }

    private void initListners() {
        setOnDragDetected((MouseEvent event) -> {
            getParent().setOnDragOver(listnerDragOver);
            getParent().setOnDragDropped(listnerDragDropped);
            ClipboardContent content = new ClipboardContent();
            content.putString("GMA");
            startDragAndDrop(TransferMode.ANY).setContent(content);
            event.consume();
        });

        listnerDragOver = (DragEvent event) -> {
            event.acceptTransferModes(TransferMode.MOVE);
            Point2D p = sceneToLocal(event.getSceneX(), event.getSceneY());
            forcer(p.getX(), p.getY());
        };

        listnerDragDropped = (DragEvent event) -> {
            event.setDropCompleted(true);
            getParent().setOnDragOver(null);
            getParent().setOnDragDropped(null);
            event.consume();
        };
    }

    private void forcer(double x, double y) {
        this.x.set((x - label.getWidth() / 2) / scale.get());
        this.y.set((y - label.getHeight() / 2) / scale.get());
    }

    public void deplacer(double x, double y) {
//        label.setLayoutX(x - label.getWidth() / 2);
//        label.setLayoutY(y - label.getHeight() / 2);
        this.x.set((x - label.getWidth() / 2));
        this.y.set((y - label.getHeight() / 2));
    }

    public double getWidth() {
        return width;
    }

    public DoubleProperty XProperty() {
        SimpleDoubleProperty s = new SimpleDoubleProperty();
        s.bind(label.layoutXProperty().add(label.widthProperty().divide(2)));
        return s;
    }

    public DoubleProperty YProperty() {
        SimpleDoubleProperty s = new SimpleDoubleProperty();
        s.bind(label.layoutYProperty().add(label.heightProperty().divide(2)));
        return s;
    }

    public boolean equalTo(Marquage marquage) {
        return marquage.equals(this.marquage);
    }

    public String getText() {
        return text;
    }
    
}
