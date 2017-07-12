package ap.javasource.rdp.graphiques.arc;

import ap.javasource.data.rdpdata.ArcData.Point;
import ap.javasource.rdp.project.ProjectController;
import java.util.ArrayList;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;

/**
 *
 * @author TarekRDP
 */
public class TLine extends Group {

    private ProjectController projectController;
    private final LinkedLine lineFirst;
    private final SimpleObjectProperty<LinkedLine> lineLast = new SimpleObjectProperty<>();
    private final ArrayList<LinkedLine> lines = new ArrayList<>();
    private final DoubleProperty scale;
//    private final ArrayList<LinkedAnchor> anchors = new ArrayList<>();

    private boolean add = true;

    public TLine(ProjectController projectController, DoubleProperty scale) {
        this(scale);
        this.projectController = projectController;
        initHandlers();
    }

    public TLine(DoubleProperty scale) {
        this.scale=scale;
        lineFirst = getNewLine();
        lineLast.set(lineFirst);
    }

    private LinkedLine getNewLine() {
        LinkedLine line = new LinkedLine();
        lines.add(line);
        line.setOnMouseClicked(e -> {
            if (e.getClickCount() > 1) {

                e.consume();
                LinkedLine l = getNewLine();
                LinkedAnchor anchor = getNewAnchor(e.getX(), e.getY(), line, l);

                l.startXProperty().bind(anchor.centerXProperty());
                l.startYProperty().bind(anchor.centerYProperty());
                l.setXY(line.getX(), line.getY());

                if (line.getSvt() == null) {
                    lineLast.set(l);
                } else {
                    l.setSvt(line.getSvt());
                    l.setAnchor(line.getAnchor());
                    line.getAnchor().setFirst(l);
                }
                line.setXY(anchor.centerXProperty(), anchor.centerYProperty());
                line.setSvt(l);

                line.setAnchor(anchor);
                if (projectController != null) {
                    projectController.getProject().setModified();
                    if (line.getStyle().contains("red")) {
                        l.setStyle("-fx-stroke: red;");
                    }
                    l.setStrokeWidth(4);
                }
                add = true;
            }
        });
        lines.add(line);
        getChildren().add(0, line);
        return line;
    }

    private LinkedAnchor getNewAnchor(double x, double y, LinkedLine first, LinkedLine last) {
        LinkedAnchor anchor = new LinkedAnchor(x, y, first, last, scale);
        getChildren().add(anchor);
//        anchors.add(anchor);
        anchor.setOnMouseClicked(e -> {
            if (e.getClickCount() > 1) {
                if (projectController != null) {
                    projectController.getProject().setModified();
                }
                anchor.getFirst().setSvt(anchor.getLast().getSvt());
                anchor.getFirst().setXY(anchor.getLast().getX(), anchor.getLast().getY());
                anchor.getFirst().setAnchor(anchor.getLast().getAnchor());
                if (anchor.getLast().getAnchor() != null) {
                    anchor.getLast().getAnchor().setFirst(anchor.getFirst());
                }
                if (lineLast.get() == anchor.getLast()) {
                    lineLast.set(anchor.getFirst());
                }
                lines.remove(anchor.getLast());
//                anchors.remove(anchor);
                getChildren().removeAll(anchor.getLast(), anchor);
                e.consume();
            }
        });

        EventHandler<DragEvent> listnerDragOver = (DragEvent event) -> {
            event.acceptTransferModes(TransferMode.MOVE);
            Point2D p = sceneToLocal(event.getSceneX(), event.getSceneY());
            anchor.setX(p.getX());
            anchor.setY(p.getY());
            event.consume();
        };

        EventHandler<DragEvent> listnerDragDropped = (DragEvent event) -> {
            event.setDropCompleted(true);
            getParent().getParent().setOnDragOver(null);
            getParent().getParent().setOnDragDropped(null);
            event.consume();
        };

        anchor.setOnDragDetected((MouseEvent event) -> {

            getParent().getParent().setOnDragOver(listnerDragOver);
            getParent().getParent().setOnDragDropped(listnerDragDropped);
            ClipboardContent content = new ClipboardContent();
            content.putString("Anchor");
            startDragAndDrop(TransferMode.ANY).setContent(content);
//            startFullDrag();
            event.consume();

        });
        return anchor;
    }

    public boolean isAdd() {
        return add;
    }

    public void setAdd(boolean add) {
        this.add = add;
    }

    public void setPropreties(DoubleProperty startX, DoubleProperty startY, DoubleProperty endX, DoubleProperty endY) {
        lineFirst.startXProperty().bind(startX);
        lineFirst.startYProperty().bind(startY);
        lineFirst.setXY(endX, endY);
    }

    public DoubleProperty startXProperty() {
        return lineFirst.startXProperty();
    }

    public DoubleProperty startYProperty() {
        return lineFirst.startYProperty();
    }

    public DoubleProperty endXProperty() {
        return lineLast.get().endXProperty();
    }

    public DoubleProperty endYProperty() {
        return lineLast.get().endYProperty();
    }

    public double getStartX() {
        return lineLast.get().startXProperty().get();
    }

    public double getStartY() {
        return lineLast.get().startYProperty().get();
    }

    public double getEndX() {
        return lineLast.get().endXProperty().get();
    }

    public double getEndY() {
        return lineLast.get().endYProperty().get();
    }

    public void setMotifPositioner(ChangeListener motifPositioner) {
        lineLast.get().startXProperty().addListener(motifPositioner);
        lineLast.get().startYProperty().addListener(motifPositioner);
        lineLast.get().endXProperty().addListener(motifPositioner);
        lineLast.get().endYProperty().addListener(motifPositioner);
        lineLast.addListener((ObservableValue<? extends LinkedLine> observable, LinkedLine oldValue, LinkedLine newValue) -> {
            newValue.startXProperty().addListener(motifPositioner);
            newValue.startYProperty().addListener(motifPositioner);
            newValue.endXProperty().addListener(motifPositioner);
            newValue.endYProperty().addListener(motifPositioner);
            motifPositioner.changed(null, 0, 0);
        });
    }

    public void setLinesStyle(String style) {
        lines.stream().forEach(l -> {
            l.setStyle(style);
        });
    }

    private void initHandlers() {
        setOnMouseEntered(e -> {
            if (projectController.delete.get() || projectController.none.get()) {
                lines.stream().forEach(l -> {
                    l.setStrokeWidth(4);
                });
            }
            e.consume();
        });

        setOnMouseExited(e -> {
            if (projectController.delete.get() || projectController.none.get()) {
                lines.stream().forEach(l -> {
                    l.setStrokeWidth(2);
                });
            }

            e.consume();
        });
    }

    public ArrayList<Point2D> getAnchorsPoints() {
        LinkedLine tempLine = lineFirst;
        ArrayList<Point2D> temp = new ArrayList<>();
        while (tempLine != null && tempLine.getAnchor() != null) {
            temp.add(new Point2D(tempLine.getAnchor().getCenterX(), tempLine.getAnchor().getCenterY()));
            tempLine = tempLine.getSvt();
        }
        return temp;
    }

    public void edit(ArrayList<Point> points) {
        if (points != null && !points.isEmpty()) {
            LinkedLine lineTemp1, lineTemp2;
            lineTemp1 = lineTemp2 = lineFirst;
            for (Point p : points) {
                lineTemp2 = getNewLine();
                LinkedAnchor anchorTemp = getNewAnchor(p.getX(), p.getY(), lineTemp1, lineTemp2);
                lineTemp2.startXProperty().bind(anchorTemp.centerXProperty());
                lineTemp2.startYProperty().bind(anchorTemp.centerYProperty());
                lineTemp2.setXY(lineTemp1.getX(), lineTemp1.getY());
                lineTemp1.setXY(anchorTemp.centerXProperty(), anchorTemp.centerYProperty());
                lineTemp1.setSvt(lineTemp2);
                lineTemp1.setAnchor(anchorTemp);
                lineTemp1 = lineTemp2;
            }
            lineLast.set(lineTemp2);
        }

    }
}
