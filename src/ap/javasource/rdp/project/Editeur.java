package ap.javasource.rdp.project;

import static ap.javasource.rdp.project.EditeurReseau.DEFAULT_CURSOR;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.Slider;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;
import javafx.scene.text.Font;

/**
 *
 * @author Tarek
 */
public class Editeur extends AnchorPane {

    private final ImageView fixView = new ImageView(new Image("/ap/resources/images/editeur/fix.png"));
    private final ImageView rightView = new ImageView(new Image("/ap/resources/images/editeur/expandRight.png"));
    private final ImageView bottomView = new ImageView(new Image("/ap/resources/images/editeur/expandBottom.png"));

    private final Label fixLabel = new Label();
    private final Label rightLabel = new Label();
    private final Label bottomLabel = new Label();

    private final AnchorPane pane = new AnchorPane();
    protected final SimpleDoubleProperty scale = new SimpleDoubleProperty();
    private final ScrollPane scroll = new ScrollPane(pane);
    private final Slider zoomSlider = new Slider(40, 200, 100);

    public Editeur() {
        getStylesheets().add(getClass().getResource("/ap/resources/css/costumstyle/CostumStyle.css").toExternalForm());
        pane.setCursor(EditeurReseau.DEFAULT_CURSOR);
        scroll.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
        scroll.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
        pane.minHeightProperty().bind(scroll.heightProperty().subtract(5));
        pane.minWidthProperty().bind(scroll.widthProperty().subtract(5));
        pane.setPrefSize(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE);
        pane.setStyle("-fx-background-image: url(/ap/resources/images/editeur/fond.png)");
        scroll.setPannable(true);
        AnchorPane.setBottomAnchor(scroll, 0.0);
        AnchorPane.setLeftAnchor(scroll, 0.0);
        AnchorPane.setRightAnchor(scroll, 0.0);
        AnchorPane.setTopAnchor(scroll, 0.0);
        getChildren().add(scroll);

        scale.bind(zoomSlider.valueProperty().divide(100));
        zoomSlider.setShowTickLabels(true);
        zoomSlider.setShowTickMarks(true);
        zoomSlider.setMajorTickUnit(20);
        zoomSlider.setMinorTickCount(2);
        zoomSlider.setSnapToTicks(false);
        zoomSlider.setOrientation(Orientation.VERTICAL);
        AnchorPane.setBottomAnchor(zoomSlider, 20.0);
        AnchorPane.setLeftAnchor(zoomSlider, 20.0);
        getChildren().add(zoomSlider);

        initTooltips();
        initExpandButtons();
    }

    private void initExpandButtons() {

        fixView.setFitHeight(10);
        fixView.setFitWidth(10);

        bottomView.setFitHeight(15);
        bottomView.setFitWidth(15);

        rightView.setFitHeight(15);
        rightView.setFitWidth(15);

        fixLabel.setGraphic(fixView);
        rightLabel.setGraphic(rightView);
        bottomLabel.setGraphic(bottomView);

        fixLabel.setAlignment(Pos.CENTER);
        rightLabel.setAlignment(Pos.CENTER);
        bottomLabel.setAlignment(Pos.CENTER);

        fixLabel.setCursor(DEFAULT_CURSOR);
        rightLabel.setCursor(DEFAULT_CURSOR);
        bottomLabel.setCursor(DEFAULT_CURSOR);

        fixLabel.getStyleClass().add("costumButton");
        rightLabel.getStyleClass().add("costumButton");
        bottomLabel.getStyleClass().add("costumButton");

        fixLabel.setPrefSize(20, 20);
        rightLabel.setPrefSize(20, 20);
        bottomLabel.setPrefSize(20, 20);

        AnchorPane.setRightAnchor(fixLabel, 36.0);
        AnchorPane.setBottomAnchor(fixLabel, 36.0);

        AnchorPane.setRightAnchor(rightLabel, 15.0);
        AnchorPane.setBottomAnchor(rightLabel, 35.0);

        AnchorPane.setRightAnchor(bottomLabel, 35.0);
        AnchorPane.setBottomAnchor(bottomLabel, 15.0);

        getChildren().addAll(fixLabel, rightLabel, bottomLabel);
        rightLabel.setOnMouseClicked(e -> {
            pane.setPrefWidth(pane.getWidth() + 50);
        });

        bottomLabel.setOnMouseClicked(e -> {
            pane.setPrefHeight(pane.getHeight() + 50);
        });

        fixLabel.setOnMouseClicked(e -> {
            pane.setPrefHeight(USE_COMPUTED_SIZE);
            pane.setPrefWidth(USE_COMPUTED_SIZE);
        });
    }

    public ObservableList<Node> getPaneChildren() {
        return pane.getChildren();

    }

    public Rectangle2D childrenArea() {
        if (!pane.getChildren().isEmpty()) {

            double xMin, yMin, xMax, yMax;
            Bounds temp = pane.getChildren().get(0).getLayoutBounds();
            xMin = temp.getMinX();
            yMin = temp.getMinY();
            xMax = 0;
            yMax = 0;
            for (Node n : pane.getChildren()) {
                temp = n.getLayoutBounds();
                if (temp.getMinX() < xMin) {
                    xMin = temp.getMinX();
                }
                if (temp.getMinY() < yMin) {
                    yMin = temp.getMinY();
                }
                if (temp.getMaxX() > xMax) {
                    xMax = temp.getMaxX();
                }
                if (temp.getMaxY() > yMax) {
                    yMax = temp.getMaxY();
                }
            }
            double width = xMax - xMin + 60, height = yMax - yMin + 60;

            if (xMin > 30) {
                xMin -= 30;
            } else {
                xMin = 0;
            }
            if (yMin > 30) {
                yMin -= 30;
            } else {
                yMin = 0;
            }
            return new Rectangle2D(xMin, yMin, width, height);
        }
        return null;
    }

    public AnchorPane getPane() {
        return pane;
    }

    public SimpleDoubleProperty scaleProprety() {
        return scale;
    }

    public void setScale(double scale) {
        zoomSlider.setValue(scale * 100);
    }

    private void initTooltips() {
        Font font = new Font("Consolas", 12);

        Tooltip tooltip = new Tooltip("Zoom");
        tooltip.setFont(font);
        zoomSlider.setTooltip(tooltip);
        
        tooltip = new Tooltip("Taille automatique");
        tooltip.setFont(font);
        fixLabel.setTooltip(tooltip);
        
        tooltip = new Tooltip("Étendre");
        tooltip.setFont(font);
        rightLabel.setTooltip(tooltip);

        bottomLabel.setTooltip(tooltip);
    }
}
