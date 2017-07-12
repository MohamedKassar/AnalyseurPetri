package ap.javasource.rdp.project;

import ap.javasource.rdp.graphiques.Selectionable.Selectionable;
import ap.javasource.rdp.graphiques.arc.Arc;
import ap.javasource.rdp.graphiques.place.Place;
import ap.javasource.rdp.graphiques.transition.Transition;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.Cursor;
import javafx.scene.layout.VBox;

/**
 *
 * @author Tarek
 */
public class ProjectController {

    private final Project project;

    public final SimpleBooleanProperty none = new SimpleBooleanProperty(true);
    public final SimpleBooleanProperty newArc = new SimpleBooleanProperty(false);
    public final SimpleBooleanProperty newArcInh = new SimpleBooleanProperty(false);
    public final SimpleBooleanProperty delete = new SimpleBooleanProperty(false);
    public final SimpleBooleanProperty rotateTransitionRight = new SimpleBooleanProperty(false);
    public final SimpleBooleanProperty rotateTransitionLeft = new SimpleBooleanProperty(false);
    private SimpleBooleanProperty actionTemp = none;


    public final SimpleObjectProperty<Selectionable> selectedObject = new SimpleObjectProperty<>(null);

    private VBox paletteTemp;

    public ProjectController(Project project) {
        this.project = project;
        initPropertyies();
    }

    public Project getProject() {
        return project;
    }


    public boolean isSelected(Selectionable object) {
        return object == selectedObject.get();
    }

    private void initPropertyies() {
        initActions();
        initSelected();
    }

    private void initActions() {
        newArc.addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (newValue) {
                actionTemp.set(false);
                actionTemp = newArc;
                project.editeurReseau.getEditeur().getPane().setCursor(EditeurReseau.ARROW_CURSOR);
                selectedObject.set(null);
            }
        });
        newArcInh.addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (newValue) {
                actionTemp.set(false);
                actionTemp = newArcInh;
                project.editeurReseau.getEditeur().getPane().setCursor(EditeurReseau.ARROW_INH_CURSOR);
                selectedObject.set(null);
            }
        });
        delete.addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (newValue) {
                actionTemp.set(false);
                actionTemp = delete;
                project.editeurReseau.getEditeur().getPane().setCursor(EditeurReseau.DELETE_CURSOR);
                selectedObject.set(null);
            }
        });
        rotateTransitionRight.addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (newValue) {
                actionTemp.set(false);
                actionTemp = rotateTransitionRight;
                project.editeurReseau.getEditeur().getPane().setCursor(EditeurReseau.ROTATE_RIGHT_CURSOR);
                selectedObject.set(null);
            }
        });
        rotateTransitionLeft.addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (newValue) {
                actionTemp.set(false);
                actionTemp = rotateTransitionLeft;
                project.editeurReseau.getEditeur().getPane().setCursor(EditeurReseau.ROTATE_LEFT_CURSOR);
                selectedObject.set(null);
            }
        });
        none.addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (newValue) {
                actionTemp.set(false);
                actionTemp = none;
                project.editeurReseau.getEditeur().getPane().setCursor(EditeurReseau.DEFAULT_CURSOR);
            }
        });
    }

    private void initSelected() {
        selectedObject.addListener((ObservableValue<? extends Selectionable> observable, Selectionable oldValue, Selectionable newValue) -> {
            if (oldValue != null) {
                oldValue.deSelect();
                if (paletteTemp != null) {
                    paletteTemp.setVisible(false);
                }
            }
            if (newValue != null) {
                newValue.select();
                if (none.get()) {
                    if (newValue instanceof Place) {
                        paletteTemp = project.editeurReseau.setPlacePaletteAt((Place) newValue);
                    } else if (newValue instanceof Arc) {
                        paletteTemp = project.editeurReseau.setArcPaletteAt((Arc) newValue);
                    } else if (newValue instanceof Transition) {
                        paletteTemp = project.editeurReseau.setTransitionPaletteAt((Transition) newValue);
                    }
                }
            }
        });
    }
}
