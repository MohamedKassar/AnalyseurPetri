package ap.javasource.rdp.graphiques.objetgraphique;

import ap.javasource.rdp.graphiques.Selectionable.Selectionable;
import ap.javasource.rdp.graphiques.arc.Arc;
import ap.javasource.rdp.graphiques.arc.ArcInhibiteur;
import ap.javasource.rdp.graphiques.arc.ArcPost;
import ap.javasource.rdp.graphiques.arc.ArcPre;
import ap.javasource.rdp.graphiques.arc.ArcPreOrdinaire;
import ap.javasource.rdp.graphiques.place.Place;
import static ap.javasource.rdp.graphiques.place.Place.RAYON;
import ap.javasource.rdp.project.Editeur;
import ap.javasource.rdp.graphiques.supprimable.Supprimable;
import ap.javasource.rdp.graphiques.transition.Transition;
import ap.javasource.rdp.project.EditeurReseau;
import ap.javasource.rdp.project.ProjectController;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.binding.When;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;

/**
 *
 * @author Tarek
 */
public abstract class ObjetGraphique extends Group implements Supprimable, Selectionable {

    protected final List<ArcPre> pre = new ArrayList<>();
    protected final List<ArcPost> post = new ArrayList<>();

    protected final Label labelNom = new Label();

    protected EventHandler<DragEvent> listnerDragOver;
    protected EventHandler<DragEvent> listnerDragDropped;
    protected Editeur editeurPere;

    protected ProjectController projectController;

    public void deplacer(double x, double y) {
        projectController.getProject().setModified();
    }

    protected final SimpleDoubleProperty x = new SimpleDoubleProperty();
    protected final SimpleDoubleProperty y = new SimpleDoubleProperty();

    public abstract DoubleProperty XProperty();

    public abstract DoubleProperty YProperty();

    protected int id;
    protected final SimpleStringProperty name = new SimpleStringProperty("");
    protected final SimpleStringProperty nameProprety = new SimpleStringProperty();
    protected final DoubleProperty scale;

    protected ObjetGraphique(int id, Editeur editeurPere, ProjectController projectController) {
        this.editeurPere = editeurPere;
        this.projectController = projectController;
        scale = editeurPere.scaleProprety();
        this.id = id;
        labelNom.styleProperty().bind(new SimpleStringProperty("-fx-font-size : ").concat(RAYON.divide(1.75).multiply(scale)));
        projectController.getProject().setModified();
    }

    protected void initListners(Node node) {
        setOnDragDetected((MouseEvent event) -> {
            if (!projectController.newArc.get() && !projectController.newArcInh.get()) {
                getParent().setOnDragOver(listnerDragOver);
                getParent().setOnDragDropped(listnerDragDropped);
                ClipboardContent content = new ClipboardContent();
                content.putString("RDP");
                startDragAndDrop(TransferMode.ANY).setContent(content);
//            startFullDrag();
                event.consume();
            }
        });

        listnerDragOver = (DragEvent event) -> {
            event.acceptTransferModes(TransferMode.MOVE);
            Point2D p = sceneToLocal(event.getSceneX(), event.getSceneY());
            deplacer(p.getX(), p.getY());
            event.consume();
        };

        listnerDragDropped = (DragEvent event) -> {
            event.setDropCompleted(true);
            getParent().setOnDragOver(null);
            getParent().setOnDragDropped(null);
            event.consume();
        };

        setOnMouseClicked(e -> {
            if (projectController.newArc.get()) {
                if (projectController.selectedObject.get() == null) {
                    projectController.selectedObject.set(this);
                } else {
                    if (this instanceof Place && projectController.selectedObject.get() != null && projectController.selectedObject.get() instanceof Transition) {
                        if (!((Transition) projectController.selectedObject.get()).alreadyLinkedPost((Place) this)) {
                            projectController.getProject().getMatrixPane().addArcPost(
                                    new ArcPost((Transition) projectController.selectedObject.get(), (Place) this, editeurPere, projectController));
                        }
                        projectController.selectedObject.set(null);
                    } else if (this instanceof Transition && projectController.selectedObject.get() != null && projectController.selectedObject.get() instanceof Place) {
                        if (!((Transition) this).alreadyLinkedPre((Place) projectController.selectedObject.get())) {
                            projectController.getProject().getMatrixPane().addArcPreOrdinaire(
                                    new ArcPreOrdinaire((Place) projectController.selectedObject.get(), (Transition) this, editeurPere, projectController));
                        }
                        projectController.selectedObject.set(null);

                    } else if (projectController.selectedObject.get() != null && projectController.selectedObject.get() instanceof Place) {
                        if (projectController.selectedObject.get() == this) {
                            projectController.selectedObject.set(null);
                        } else {
                            projectController.selectedObject.set(this);
                        }
                    } else if (projectController.selectedObject.get() != null && projectController.selectedObject.get() instanceof Transition) {
                        if (projectController.selectedObject.get() == this) {
                            projectController.selectedObject.set(null);
                        } else {
                            projectController.selectedObject.set(this);
                        }
                    }
                }
            } else if (projectController.newArcInh.get()) {
                if (projectController.selectedObject.get() == null || !(projectController.selectedObject.get() instanceof Place)) {
                    if (this instanceof Place) {
                        projectController.selectedObject.set(this);
                    }
                } else {
                    if (projectController.selectedObject.get() != null && projectController.selectedObject.get() instanceof Place) {
                        if (this instanceof Transition) {
                            if (!((Transition) this).alreadyLinkedInh((Place) projectController.selectedObject.get())) {
                                projectController.getProject().getMatrixPane().addArcInh(
                                        new ArcInhibiteur((Place) projectController.selectedObject.get(), (Transition) this, editeurPere, projectController));
                            }
                            projectController.selectedObject.set(null);
                        } else {
                            if (projectController.selectedObject.get() == this) {
                                projectController.selectedObject.set(null);
                            } else {
                                projectController.selectedObject.set(this);
                            }
                        }

                    }
                }
            } else if (projectController.delete.get()) {
                supprimer();
            } else if (projectController.rotateTransitionLeft.get()) {
                if (this instanceof Transition) {
                    ((Transition) this).rotateLeft();
                }
            } else if (projectController.rotateTransitionRight.get()) {
                if (this instanceof Transition) {
                    ((Transition) this).rotateRight();
                }
            } else if (projectController.none.get()) {
                if (projectController.selectedObject.get() == this) {
                    projectController.selectedObject.set(null);
                } else {
                    projectController.selectedObject.set(this);
                }
            }

            e.consume();
        });

        setOnMouseEntered(e -> {
            if (!projectController.isSelected(this)) {
                node.setStyle("-fx-fill: white; -fx-stroke: red;");
                if (this instanceof Place &&(projectController.rotateTransitionLeft.get() || projectController.rotateTransitionRight.get()) ||(this instanceof Transition && projectController.newArcInh.get() && projectController.selectedObject.get()==null)) {
                    setCursor(EditeurReseau.DENIED_CURSOR);
                }
            }

        });
        setOnMouseExited(e -> {
            if (!projectController.isSelected(this)) {
                node.setStyle("-fx-fill: white; -fx-stroke: black;");
                setCursor(null);
            }
        });
    }

    public void addPre(ArcPre arc) {
        pre.add(arc);
    }

    public void addPost(ArcPost arc) {
        post.add(arc);
    }

    public void supprimerArc(Arc arc) {
        if (arc instanceof ArcPre) {
            pre.remove(arc);
        } else {
            post.remove(arc);
        }
    }

    @Override
    public void supprimer() {
        projectController.getProject().setModified();
        editeurPere.getPaneChildren().remove(this);
        Object[] preTemp = pre.toArray();
        Object[] postTemp = post.toArray();
        for (Object arc : preTemp) {
            ((ArcPre) arc).supprimer();
        }
        for (Object arc : postTemp) {
            ((ArcPost) arc).supprimer();
        }
    }

    public int getObjId() {
        return id;
    }

    public StringProperty getNameProperty() {
        return nameProprety;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
        projectController.getProject().setModified();
    }

}
