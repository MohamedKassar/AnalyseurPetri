package ap.javasource.rdp.analyses;

import ap.javasource.rdp.gmapane.GMAPane;
import ap.javasource.rdp.graphiques.arc.Arc;
import ap.javasource.rdp.graphiques.place.Place;
import ap.javasource.rdp.graphiques.transition.Transition;
import ap.javasource.rdp.project.Project;
import ap.javasource.rdp.project.ProjectController;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.util.Pair;

/**
 *
 * @author Tarek
 */
public class AnalyseurReseau {

    protected final List<Place> places = new ArrayList<>();
    protected final List<Transition> transitions = new ArrayList<>();
    private final List<Arc> arcs = new ArrayList<>();
    private GMA gma;

    private final ProjectController projectController;

    private Button[] propButtons = null;
    private Label[] propLabels;
    private HBox checkMarkHBox;
    private final ArrayList<TextField> markFields = new ArrayList<>();

    private final EventHandler<KeyEvent> intFilter = e -> {
        if (!e.getCharacter().isEmpty()) {
            if (!Character.isDigit(e.getCharacter().charAt(0))) {
                e.consume();
            }
        }
    };

    private int idMarquage = -1;
    private int idTransition = -1;
    private int idPlace = -1;

    private Boolean isVivant = null;
    private Boolean isPseudoBloc = null;
    private Boolean isQuasi = null;
    private Boolean isConservatif = null;
    private Boolean isReinit = null;
    private String etatAcc = null;
    private String etatPuit = null;

    private final ConstruireGmaTarget constGmaTarget;
    private final TesterVivaciteTarget testVivaciteTarget;
    private final TesterPseudoVivaciteBlocageTarget testPseudoVivaciteBlocageTarget;
    private final TesterQuasiVivaciteTarget testQuasiVivaciteTarget;
    private final TesterConservationTarget testerConservationTarget;
    private final TesterReinitialisationTarget testerReinitialisationTarget;
    private final TrouverEtatAccTarget trouverEtatAccTarget;
    private final VerfierSiMarquageAccTarget verfierSiMarquageAccTarget;
    private final TrouverEtatPuitTarget trouverEtatPuitTarget;

    public AnalyseurReseau(ProjectController projectController) {
        this.projectController = projectController;

        constGmaTarget = new ConstruireGmaTarget(projectController.getProject());
        testVivaciteTarget = new TesterVivaciteTarget(projectController.getProject());
        testPseudoVivaciteBlocageTarget = new TesterPseudoVivaciteBlocageTarget(projectController.getProject());
        testQuasiVivaciteTarget = new TesterQuasiVivaciteTarget(projectController.getProject());
        testerConservationTarget = new TesterConservationTarget(projectController.getProject());
        testerReinitialisationTarget = new TesterReinitialisationTarget(projectController.getProject());
        trouverEtatAccTarget = new TrouverEtatAccTarget(projectController.getProject());
        verfierSiMarquageAccTarget = new VerfierSiMarquageAccTarget(projectController.getProject());
        trouverEtatPuitTarget = new TrouverEtatPuitTarget(projectController.getProject());
    }

    public GMA getGma() {
        return gma;
    }

    public void addTransition(Transition transition) {
        transitions.add(transition);
        projectController.getProject().getMatrixPane().addTransition(transition);
    }

    public void addPlace(Place place) {
        places.add(place);
        projectController.getProject().getMatrixPane().addPlace(place);
    }

    public void addArc(Arc arc) {
        arcs.add(arc);
    }

    public void removeArc(Arc arc) {
        arcs.remove(arc);
    }

    public void removeTransition(Transition transition) {
        transitions.remove(transition);
        projectController.getProject().getMatrixPane().removeTransition(transition);

    }

    public void removePlace(Place place) {
        places.remove(place);
        projectController.getProject().getMatrixPane().removePlace(place);
    }

    public Place getPlace(int id) {
        for (Place place : places) {
            if (place.getObjId() == id) {
                return place;
            }
        }
        return null;
    }

    public Transition getTransition(int id) {
        for (Transition transition : transitions) {
            if (transition.getObjId() == id) {
                return transition;
            }
        }
        return null;
    }

    public int getIdMarquage() {
        idMarquage++;
        return idMarquage;
    }

    public int getCurrentIdTransition() {
        return idTransition;
    }

    public int getCurrentIdPlace() {
        return idPlace;
    }

    public void setIdTransition(int idTransition) {
        this.idTransition = idTransition;
    }

    public void setIdPlace(int idPlace) {
        this.idPlace = idPlace;
    }

    public int getIdTransition() {
        idTransition++;
        return idTransition;
    }

    public int getIdPlace() {
        idPlace++;
        return idPlace;
    }

    public List<Transition> getTransitions() {
        return transitions;
    }

    public List<Place> getPlaces() {
        return places;
    }

    public List<Arc> getArcs() {
        return arcs;
    }

    public Boolean isVivant() {
        return isVivant;
    }

    public Boolean isPseudoBloc() {
        return isPseudoBloc;
    }

    public Boolean isQuasi() {
        return isQuasi;
    }

    public Boolean isConservatif() {
        return isConservatif;
    }

    public Boolean isReinit() {
        return isReinit;
    }

    public String getEtatAcc() {
        return etatAcc;
    }

    public String getEtatPuit() {
        return etatPuit;
    }

    private Button getButton(String id) {
        Button temp = null;
        int i = 0;
        while (temp == null && i < propButtons.length) {
            if (propButtons[i].getId().equals(id)) {
                temp = propButtons[i];
            }
            i++;
        }
        return temp;
    }

    private Label getLabel(String id) {
        Label temp = null;
        int i = 0;
        while (temp == null && i < propLabels.length) {
            if (propLabels[i].getId().equals(id)) {
                temp = propLabels[i];
            }
            i++;
        }
        return temp;
    }

    public void construireGMA(BorderPane pane, GMAPane gmaPane, Button button, Button[] propButtons, Label[] propLabels, HBox checkMarkHBox) {
        if (!constGmaTarget.active) {
            if (this.propButtons == null) {
                this.propButtons = propButtons;
                this.propLabels = propLabels;
                this.checkMarkHBox = checkMarkHBox;
                constGmaTarget.set(pane, button, gmaPane);
            }
            new Thread(constGmaTarget).start();
        } else {
            button.setText("Annulation ...");
            button.setDisable(true);
            constGmaTarget.Stop();
        }

    }

    public void verifierSiMarquageAcc() {
        if (!verfierSiMarquageAccTarget.active) {
            new Thread(verfierSiMarquageAccTarget).start();
        } else {
            getButton("checkMarkButton").setDisable(true);
            getButton("checkMarkButton").setText("Annulation ...");
            verfierSiMarquageAccTarget.Stop();
        }
    }

    public void testerVivacité() {
        if (!testVivaciteTarget.active) {
            new Thread(testVivaciteTarget).start();
        } else {
            getButton("vivaciteButton").setDisable(true);
            getButton("vivaciteButton").setText("Annulation ...");
            testVivaciteTarget.Stop();
        }
    }

    public void testerPseudoVivacité() {
        if (!testPseudoVivaciteBlocageTarget.active) {
            testPseudoVivaciteBlocageTarget.setPseudo(true);
            new Thread(testPseudoVivaciteBlocageTarget).start();
        } else {
            getButton("pseudoButton").setDisable(true);
            getButton("pseudoButton").setText("Annulation ...");
            testPseudoVivaciteBlocageTarget.Stop();
        }
    }

    public void testerBlocage() {
        if (!testPseudoVivaciteBlocageTarget.active) {
            testPseudoVivaciteBlocageTarget.setPseudo(false);
            new Thread(testPseudoVivaciteBlocageTarget).start();
        } else {
            getButton("blocageButton").setDisable(true);
            getButton("blocageButton").setText("Annulation ...");
            testPseudoVivaciteBlocageTarget.Stop();
        }
    }

    public void testerQuasiVivacité() {
        if (!testQuasiVivaciteTarget.active) {
            new Thread(testQuasiVivaciteTarget).start();
        } else {
            getButton("quasiButton").setDisable(true);
            getButton("quasiButton").setText("Annulation ...");
            testQuasiVivaciteTarget.Stop();
        }
    }

    public void testerConservation() {
        if (!testerConservationTarget.active) {
            new Thread(testerConservationTarget).start();
        } else {
            getButton("conservationButton").setDisable(true);
            getButton("conservationButton").setText("Annulation ...");
            testerConservationTarget.Stop();
        }
    }

    public void testerRéinitialisation() {
        if (!testerReinitialisationTarget.active) {
            new Thread(testerReinitialisationTarget).start();
        } else {
            getButton("reinitButton").setDisable(true);
            getButton("reinitButton").setText("Annulation ...");
            testerReinitialisationTarget.Stop();
        }
    }

    public void trouverEtatAcc() {
        if (!trouverEtatAccTarget.active) {
            new Thread(trouverEtatAccTarget).start();
        } else {
            getButton("etatAccButton").setDisable(true);
            getButton("etatAccButton").setText("Annulation ...");
            trouverEtatAccTarget.Stop();
        }
    }

    public void trouverEtatPuit() {
        if (!trouverEtatPuitTarget.active) {
            new Thread(trouverEtatPuitTarget).start();
        } else {
            getButton("etatPuitButton").setDisable(true);
            getButton("etatPuitButton").setText("Annulation ...");
            trouverEtatPuitTarget.Stop();
        }
    }

    private class ConstruireGmaTarget extends StopableRunnable {

        private BorderPane pane;
        private Button button;
        private GMAPane gmaPane;

        private final Label constImposibleLabel = new Label("Réseau non borné, graphe d'accessibilité infini, construction impossible.");
        private final Label constAnnuleLabel = new Label("Construction du graphe d'accessibilité annulé.");

        public ConstruireGmaTarget(Project project) {
            super(project);
        }

        {
            constAnnuleLabel.setWrapText(true);
            constImposibleLabel.setWrapText(true);
            constImposibleLabel.setStyle("-fx-font-family:Consolas; -fx-font-size: 14;");
            constAnnuleLabel.setStyle("-fx-font-family:Consolas; -fx-font-size: 14;");
        }

        public void set(BorderPane pane, Button button, GMAPane gmaPane) {
            this.pane = pane;
            this.button = button;
            this.gmaPane = gmaPane;
        }

        private void setDisableButtons(boolean b) {
            for (Button btn : propButtons) {
                if (!btn.getId().equals("checkMarkButton")) {
                    btn.setDisable(b);
                }
            }

        }

        private void clearLabels() {
            for (Label lbl : propLabels) {
                lbl.setStyle("");
                lbl.setText("");
            }
        }

        private void clearProps() {
            isConservatif = null;
            isPseudoBloc = null;
            isQuasi = null;
            isReinit = null;
            isVivant = null;
            etatAcc = null;
            etatPuit = null;
        }

        @Override
        public void run() {
            if (!places.isEmpty()) {
                Platform.runLater(() -> {
                    clearLabels();
                    clearProps();
                    checkMarkHBox.setDisable(true);
                    checkMarkHBox.getChildren().clear();
                    markFields.clear();
                    setDisableButtons(true);
                    button.setDisable(true);
                    pane.setCenter(null);
                    gmaPane.clear();
                    active = true;
                    button.setText("Construction ... Cliquer pour annuler");
                });
                while (!active) {
                }
                button.setDisable(false);

                ArrayList<Transition> transitionFranchissable = new ArrayList<>();
                ArrayList<Marquage> A = new ArrayList<>();
                Marquage marquageTemp, marquage, marquage0;
                Transition transitionTemp;
                int tempPrio;
                Object[] tempTransitions;
                marquage = marquage0 = new Marquage(AnalyseurReseau.this);

                gma = new GMA(marquage, gmaPane);

                A.add(marquage);

                while (!A.isEmpty() && gma.borne && active) {
                    marquage = A.remove(0);
                    marquage.setMarquageReseau();
                    transitions.stream().forEach(transition -> {
                        if (transition.isFranchissable()) {
                            transitionFranchissable.add(transition);
                        }
                    }); //traiter priorite
                    if (!transitionFranchissable.isEmpty()) {
                        tempPrio = transitionFranchissable.get(0).getPriorite();
                        for (Transition transition : transitionFranchissable) {
                            if (tempPrio < transition.getPriorite()) {
                                tempPrio = transition.getPriorite();
                            }
                        }

                        tempTransitions = transitionFranchissable.toArray();
                        for (Object transition : tempTransitions) {
                            if (((Transition) transition).getPriorite() != tempPrio) {
                                transitionFranchissable.remove(transition);
                            }
                        }
                    }
                    while (!transitionFranchissable.isEmpty() && gma.borne && active) {
                        transitionTemp = transitionFranchissable.remove(0);
                        transitionTemp.tirer();
                        marquageTemp = new Marquage(AnalyseurReseau.this);
                        if (gma.add(marquage, transitionTemp, marquageTemp)) {
                            A.add(marquageTemp);
                        }
                        marquage.setMarquageReseau();
                    }
                    gmaPane.lineBreak();
                }
                marquage0.setMarquageReseau();
                idMarquage = -1;
                if (active && gma.borne) {
                    gmaPane.relocateAll();
                } else {
                    gmaPane.clear();
                }
                Platform.runLater(() -> {
                    if (active) {
                        if (gma.borne) {
                            pane.setCenter(gmaPane);
                            button.setText("Réseau borné. Retester la bornitude et construire le graphe d'accessibilité");
                            setDisableButtons(false);
                            places.stream().forEach(place -> {
                                TextField temp = new TextField();
                                temp.setMaxSize(40, Double.MAX_VALUE);
                                temp.promptTextProperty().bind(place.getNameProperty());
                                temp.setOnKeyTyped(intFilter);
                                HBox.setHgrow(temp, Priority.ALWAYS);
                                markFields.add(temp);
                            });
                            checkMarkHBox.getChildren().addAll(markFields);
                            checkMarkHBox.setDisable(false);
                            SimpleBooleanProperty boolTemp = new SimpleBooleanProperty(true);
                            BooleanBinding bindTemp;
                            for (TextField field : markFields) {
                                bindTemp = field.textProperty().isNotEqualTo("").and(boolTemp);
                                boolTemp = new SimpleBooleanProperty();
                                boolTemp.bind(bindTemp);
                            }
                            getButton("checkMarkButton").disableProperty().bind(boolTemp.not());
                        } else {
                            pane.setCenter(constImposibleLabel);
                            button.setText("Réseau non borné. Retester la bornitude et construire le graphe d'accessibilité");
                        }
                    } else {
                        pane.setCenter(constAnnuleLabel);
                        button.setText("Tester la bornitude et Construire le graphe d'accessibilité");
                    }
                    active = false;
                    button.setDisable(false);
                });
            }
        }
    }

    private class VerfierSiMarquageAccTarget extends StopableRunnable {

        private Button button = null;
        private Label label = null;
        private volatile boolean accessible;
        private String marquageName = "";

        public VerfierSiMarquageAccTarget(Project project) {
            super(project);
        }

        private void init() {
            button = getButton("checkMarkButton");
            label = getLabel("checkMarkLabel");
        }

        @Override
        public void run() {
            Platform.runLater(() -> {
                init();
                active = true;
                button.setText("Traitement... Cliquer pour annuler");
            });
            while (!active) {
            }
            accessible = false;
            ArrayList<Integer> temp = new ArrayList<>();
            markFields.stream().forEach(field -> {
                temp.add(Integer.parseInt(field.getText()));
            });

            Set<Marquage> marquages = gma.keySet();
            Iterator<Marquage> i = marquages.iterator();
            Marquage marquageTemp;
            while (i.hasNext() && !accessible && active) {
                marquageTemp = i.next();
                if (marquageTemp.compareTo(temp)) {
                    accessible = true;
                    marquageName = "M" + marquageTemp.id;
                }
            }

            Platform.runLater(() -> {
                button.setText("Verifier");

                if (accessible) {
                    label.setText("Le Marquage est accessible.\n(" + marquageName + ")");
                    label.setStyle("-fx-background-color:green;-fx-background-radius:5;");
                } else {
                    label.setText("Le Marquage n'est pas accessible.");
                    label.setStyle("-fx-background-color:red;-fx-background-radius:5;");
                }
                active = false;
            });
        }
    }

    private class TesterVivaciteTarget extends StopableRunnable {

        private Button button = null;
        private Label label = null;
        private volatile boolean vivant;

        public TesterVivaciteTarget(Project project) {
            super(project);
        }

        private void init() {
            int i = 0;
            while (button == null && i < 5) {
                if (propButtons[i].getId().equals("vivaciteButton")) {
                    button = propButtons[i];
                }
                i++;
            }
            i = 0;
            while (label == null && i < 5) {
                if (propLabels[i].getId().equals("vivaciteLabel")) {
                    label = propLabels[i];
                }
                i++;
            }
        }

        @Override
        public void run() {
            Platform.runLater(() -> {
                init();
                button.setDisable(true);
                active = true;
                button.setText("Traitement... Cliquer pour annuler");
            });
            while (!active) {
            }
            button.setDisable(false);

            vivant = true;
            ArrayList<Marquage> déjaTesté = new ArrayList<>(), A = new ArrayList<>(), A1 = new ArrayList<>();
            ArrayList<Transition> transitionTemp = new ArrayList<>(), presMarquageTemp = new ArrayList<>();
            Iterator<Marquage> marquages = gma.keySet().iterator();
            ArrayList<Pair<Marquage, Transition>> pairPresMarquage;
            Marquage m, m1;
            while (marquages.hasNext() && vivant && active) {
                A.clear();
                A1.clear();
                transitionTemp.addAll(transitions);
                m = marquages.next();
                A.add(m);
                A1.add(m);
                while (!A1.isEmpty() && !transitionTemp.isEmpty() && active) {
                    m1 = A1.remove(0);
                    pairPresMarquage = gma.get(m1).getKey();
                    presMarquageTemp.clear();
                    pairPresMarquage.stream().forEach((pair) -> {
                        presMarquageTemp.add(pair.getValue());
                    });
                    //supp les elmnt de t dans s
                    presMarquageTemp.stream().filter((transition) -> (transitionTemp.contains(transition))).forEach((transition) -> {
                        transitionTemp.remove(transition);
                    });
                    pairPresMarquage.stream().forEach((pair) -> {
                        if (!déjaTesté.contains(pair.getKey())) {
                            if (!A.contains(pair.getKey())) {
                                A1.add(pair.getKey());
                                A.add(pair.getKey());
                            }
                        } else {
                            transitionTemp.clear();
                        }
                    });

                }
                if (!transitionTemp.isEmpty()) {
                    vivant = false;
                } else {
                    déjaTesté.add(m);
                }
            }
            Platform.runLater(() -> {
                if (active) {
                    button.setText("Tester la vivacité");
                    isVivant = active;
                    if (vivant) {
                        label.setText("Le réseau est vivant.");
                        label.setStyle("-fx-background-color:green;-fx-background-radius:5;");
                    } else {
                        label.setText("Le réseau n'est pas vivant.");
                        label.setStyle("-fx-background-color:red;-fx-background-radius:5;");
                    }
                    button.setDisable(true);
                    active = false;
                } else {
                    button.setText("Tester la vivacité");
                    button.setDisable(false);
                }

            });
        }
    }

    private class TesterPseudoVivaciteBlocageTarget extends StopableRunnable {

        private Button button = null;
        private Label label = null;
        private volatile boolean pseudoVivant;
        private boolean pseudoOp;

        public TesterPseudoVivaciteBlocageTarget(Project project) {
            super(project);
        }

        public void setPseudo(boolean pseudoOp) {
            this.pseudoOp = pseudoOp;
        }

        private void init() {
            if (pseudoOp) {
                button = getButton("pseudoButton");
                label = getLabel("pseudoLabel");
            } else {
                button = getButton("blocageButton");
                label = getLabel("blocageLabel");
            }
        }

        @Override
        public void run() {
            Platform.runLater(() -> {
                init();
                button.setDisable(true);
                button.setText("Traitement... Cliquer pour annuler");
                active = true;
            });
            while (!active) {
            }
            button.setDisable(false);

            if (isPseudoBloc == null) {
                pseudoVivant = true;

                Iterator<Marquage> marquages = gma.keySet().iterator();
                ArrayList<Pair<Marquage, Transition>> pairPresMarquage;
                ArrayList<Transition> presMarquageTemp = new ArrayList<>();

                while (marquages.hasNext() && pseudoVivant && active) {
                    pairPresMarquage = gma.get(marquages.next()).getKey();
                    presMarquageTemp.clear();
                    pairPresMarquage.stream().forEach((pair) -> {
                        presMarquageTemp.add(pair.getValue());
                    });
                    if (presMarquageTemp.isEmpty()) {
                        pseudoVivant = false;
                    }
                }
            } else {
                pseudoVivant = isPseudoBloc;
            }

            Platform.runLater(() -> {
                if (pseudoOp) {
                    if (active) {
                        button.setText("Tester la pseudo-vivacité");
                        isPseudoBloc = pseudoVivant;
                        if (pseudoVivant) {
                            label.setText("Le réseau est pseudo-vivant.");
                            label.setStyle("-fx-background-color:green;-fx-background-radius:5;");
                        } else {
                            label.setText("Le réseau n'est pas pseudo-vivant.");
                            label.setStyle("-fx-background-color:red;-fx-background-radius:5;");
                        }
                        button.setDisable(true);
                        active = false;
                    } else {
                        button.setText("Tester la pseudo-vivacité");
                        button.setDisable(false);
                    }
                } else {
                    if (active) {
                        button.setText("Tester si le réseau est bloqué");
                        isPseudoBloc = pseudoVivant;
                        if (pseudoVivant) {
                            label.setText("Le réseau est sans blocage.");
                            label.setStyle("-fx-background-color:green;-fx-background-radius:5;");
                        } else {
                            label.setText("Le réseau est bloqué.");
                            label.setStyle("-fx-background-color:red;-fx-background-radius:5;");
                        }
                        button.setDisable(true);
                        active = false;
                    } else {
                        button.setText("Tester si le réseau est bloqué");
                        button.setDisable(false);
                    }
                }

            });
        }
    }

    private class TesterQuasiVivaciteTarget extends StopableRunnable {

        private Button button = null;
        private Label label = null;
        private volatile boolean quasiVivant;

        public TesterQuasiVivaciteTarget(Project project) {
            super(project);
        }

        private void init() {
            int i = 0;
            while (button == null && i < 5) {
                if (propButtons[i].getId().equals("quasiButton")) {
                    button = propButtons[i];
                }
                i++;
            }
            i = 0;
            while (label == null && i < 5) {
                if (propLabels[i].getId().equals("quasiLabel")) {
                    label = propLabels[i];
                }
                i++;
            }
        }

        @Override
        public void run() {
            Platform.runLater(() -> {
                init();
                button.setDisable(true);
                active = true;
                button.setText("Traitement... Cliquer pour annuler");
            });
            while (!active) {
            }
            button.setDisable(false);

            Iterator<Marquage> marquages = gma.keySet().iterator();
            ArrayList<Pair<Marquage, Transition>> pairPresMarquage;
            ArrayList<Transition> transitionTemp = new ArrayList<>(), presMarquageTemp = new ArrayList<>();
            transitionTemp.addAll(transitions);
            while (marquages.hasNext() && !transitionTemp.isEmpty() && active) {
                pairPresMarquage = gma.get(marquages.next()).getKey();
                pairPresMarquage.stream().forEach((pair) -> {
                    presMarquageTemp.add(pair.getValue());
                });
                //supp les elmnt de t dans s
                presMarquageTemp.stream().filter((transition) -> (transitionTemp.contains(transition))).forEach((transition) -> {
                    transitionTemp.remove(transition);
                });
            }
            quasiVivant = transitionTemp.isEmpty();

            Platform.runLater(() -> {
                if (active) {
                    isQuasi = quasiVivant;
                    button.setText("Tester la quasi-vivacité");
                    if (quasiVivant) {
                        label.setText("Le réseau est quasi-vivant.");
                        label.setStyle("-fx-background-color:green;-fx-background-radius:5;");
                    } else {
                        label.setText("Le réseau n'est pas quasi-vivant.");
                        label.setStyle("-fx-background-color:red;-fx-background-radius:5;");
                    }
                    button.setDisable(true);
                    active = false;
                } else {
                    button.setText("Tester la quasi-vivacité");
                    button.setDisable(false);
                }
            });
        }
    }

    private class TesterConservationTarget extends StopableRunnable {

        private Button button = null;
        private Label label = null;
        private volatile boolean conservatif;

        public TesterConservationTarget(Project project) {
            super(project);
        }

        private void init() {
            int i = 0;
            while (button == null && i < 5) {
                if (propButtons[i].getId().equals("conservationButton")) {
                    button = propButtons[i];
                }
                i++;
            }
            i = 0;
            while (label == null && i < 5) {
                if (propLabels[i].getId().equals("conservationLabel")) {
                    label = propLabels[i];
                }
                i++;
            }
        }

        @Override
        public void run() {
            Platform.runLater(() -> {
                init();
                button.setDisable(true);
                active = true;
                button.setText("Traitement... Cliquer pour annuler");
            });
            while (!active) {
            }
            button.setDisable(false);

            conservatif = true;
            Iterator<Marquage> marquages = gma.keySet().iterator();
            if (marquages.hasNext()) {
                Marquage M0 = marquages.next();
                while (marquages.hasNext() && conservatif && active) {
                    if (M0.getnombreJetons() != marquages.next().getnombreJetons()) {
                        conservatif = false;
                    }
                }
            }

            Platform.runLater(() -> {
                if (active) {
                    button.setText("Tester la conservation");
                    isConservatif = conservatif;
                    if (conservatif) {
                        label.setText("Le réseau est conservatif.");
                        label.setStyle("-fx-background-color:green;-fx-background-radius:5;");
                    } else {
                        label.setText("Le réseau n'est pas conservatif.");
                        label.setStyle("-fx-background-color:red;-fx-background-radius:5;");
                    }
                    button.setDisable(true);
                    active = false;
                } else {
                    button.setText("Tester la conservation");
                    button.setDisable(false);
                }
            });
        }
    }

    private class TesterReinitialisationTarget extends StopableRunnable {

        private Button button = null;
        private Label label = null;
        private volatile boolean reinitialisable;

        public TesterReinitialisationTarget(Project project) {
            super(project);
        }

        private void init() {
            int i = 0;
            while (button == null && i < 5) {
                if (propButtons[i].getId().equals("reinitButton")) {
                    button = propButtons[i];
                }
                i++;
            }
            i = 0;
            while (label == null && i < 5) {
                if (propLabels[i].getId().equals("reinitLabel")) {
                    label = propLabels[i];
                }
                i++;
            }
        }

        @Override
        public void run() {
            Platform.runLater(() -> {
                init();
                button.setDisable(true);
                active = true;
                button.setText("Traitement... Cliquer pour annuler");
            });
            while (!active) {
            }
            button.setDisable(false);

            Iterator<Marquage> marquages = gma.keySet().iterator();
            ArrayList<Marquage> etat_accueil = new ArrayList<>(gma.keySet()), m = new ArrayList<>(), svt = new ArrayList<>();
            ArrayList<Pair<Marquage, Transition>> pairPresMarquage;

            while (marquages.hasNext() && active) {
                m.add(marquages.next());
                svt.clear();

                while (!m.isEmpty() && active) {
                    Marquage m1 = m.remove(0);
                    pairPresMarquage = gma.get(m1).getKey();
                    pairPresMarquage.stream().forEach((pair) -> {
                        if (!svt.contains(pair.getKey())) {
                            m.add(pair.getKey());
                            svt.add(pair.getKey());
                        }
                    });
                }

                Object[] etat = etat_accueil.toArray();
                for (int i = 0; i < etat.length; i++) {
                    if (!svt.contains(etat[i])) {
                        etat_accueil.remove(etat[i]);
                    }
                }
            }

            ArrayList<Marquage> etat_acceuil = new ArrayList<>(etat_accueil);
            marquages = gma.keySet().iterator();
            Marquage M0 = marquages.next();

            reinitialisable = etat_acceuil.contains(M0);

            Platform.runLater(() -> {
                if (active) {
                    isReinit = reinitialisable;
                    button.setText("Tester la réinitialisation");
                    if (reinitialisable) {
                        label.setText("Le réseau est réinitialisable.");
                        label.setStyle("-fx-background-color:green;-fx-background-radius:5;");
                    } else {
                        label.setText("Le réseau n'est pas réinitialisable.");
                        label.setStyle("-fx-background-color:red;-fx-background-radius:5;");
                    }
                    button.setDisable(true);
                    active = false;
                } else {
                    button.setText("Tester la réinitialisation");
                    button.setDisable(false);
                }
            });
        }
    }

    private class TrouverEtatAccTarget extends StopableRunnable {

        private Button button = null;
        private Label label = null;
        private volatile boolean etatAcc;
        private volatile String etatAccS = "";

        public TrouverEtatAccTarget(Project project) {
            super(project);
        }

        private void init() {
            int i = 0;
            while (button == null) {
                if (propButtons[i].getId().equals("etatAccButton")) {
                    button = propButtons[i];
                }
                i++;
            }
            i = 0;
            while (label == null) {
                if (propLabels[i].getId().equals("etatAccLabel")) {
                    label = propLabels[i];
                }
                i++;
            }
        }

        @Override
        public void run() {
            Platform.runLater(() -> {
                init();
                button.setDisable(true);
                active = true;
                button.setText("Traitement... Cliquer pour annuler");
            });
            while (!active) {
            }
            button.setDisable(false);

            Iterator<Marquage> marquages = gma.keySet().iterator();
            ArrayList<Marquage> etat_accueil = new ArrayList<>(gma.keySet()), m = new ArrayList<>(), svt = new ArrayList<>();
            ArrayList<Pair<Marquage, Transition>> pairPresMarquage;

            while (marquages.hasNext()) {
                m.add(marquages.next());
                svt.clear();

                while (!m.isEmpty() && active) {
                    Marquage m1 = m.remove(0);
                    pairPresMarquage = gma.get(m1).getKey();
                    pairPresMarquage.stream().forEach((pair) -> {
                        if (!svt.contains(pair.getKey())) {
                            m.add(pair.getKey());
                            svt.add(pair.getKey());
                        }
                    });
                }

                Object[] etat = etat_accueil.toArray();
                for (int i = 0; i < etat.length && active; i++) {
                    if (!svt.contains(etat[i])) {
                        etat_accueil.remove(etat[i]);
                    }
                }
            }
            etatAcc = !etat_accueil.isEmpty();

            etatAccS = "";
            if (active) {
                if (etatAcc) {
                    etat_accueil.stream().forEach(mrq -> {
                        etatAccS += ", M" + mrq.id + mrq;
                    });
                    etatAccS = etatAccS.substring(2, etatAccS.length());
                } else {
                    etatAccS = "Il n'y a aucun état d'accueil.";
                }
            }

            Platform.runLater(() -> {
                button.setText("Trouver les états d'accueil");
                if (active) {
                    AnalyseurReseau.this.etatAcc = etatAccS;
                    label.setText(etatAccS);
//                    if (!etatAcc) {
//                        label.setStyle("-fx-background-color:red;-fx-background-radius:5;");
//                    }
                    button.setDisable(true);
                    active = false;
                } else {
                    button.setDisable(false);
                }
            });
        }
    }

    private class TrouverEtatPuitTarget extends StopableRunnable {

        private Button button = null;
        private Label label = null;
        private volatile boolean etatPuit;
        private volatile String etatPuitS = "";

        public TrouverEtatPuitTarget(Project project) {
            super(project);
        }

        private void init() {
            button = getButton("etatPuitButton");
            label = getLabel("etatPuitLabel");
        }

        @Override
        public void run() {
            Platform.runLater(() -> {
                init();
                button.setDisable(true);
                active = true;
                button.setText("Traitement... Cliquer pour annuler");
            });
            while (!active) {
            }
            button.setDisable(false);

            Iterator<Marquage> marquages = gma.keySet().iterator();
            ArrayList<Marquage> etat_Puits = new ArrayList<>();
            Marquage marquageTemp;

            while (marquages.hasNext() && active) {
                marquageTemp = marquages.next();
                if (gma.get(marquageTemp).getKey().isEmpty()) {
                    etat_Puits.add(marquageTemp);
                }
            }

            etatPuit = !etat_Puits.isEmpty();

            etatPuitS = "";
            if (active) {
                if (etatPuit) {
                    etat_Puits.stream().forEach(mrq -> {
                        etatPuitS += ", M" + mrq.id + mrq;
                    });
                    etatPuitS = etatPuitS.substring(2, etatPuitS.length());
                } else {
                    etatPuitS = "Il n'y a aucun état puit.";
                }
            }

            Platform.runLater(() -> {
                button.setText("Trouver les états puits");
                if (active) {
                    AnalyseurReseau.this.etatPuit = etatPuitS;
                    label.setText(etatPuitS);
//                    if (!etatAcc) {
//                        label.setStyle("-fx-background-color:red;-fx-background-radius:5;");
//                    }
                    button.setDisable(true);
                    active = false;
                } else {
                    button.setDisable(false);
                }
            });
        }
    }
    public static ArrayList<StopableRunnable> RUNNABLE_LIST = new ArrayList<>();

    public static void stopAllRunnable() {
        RUNNABLE_LIST.stream().forEach(r -> {
            r.Stop();
        });
    }

    public abstract class StopableRunnable implements Runnable {

        public StopableRunnable(Project project) {
            RUNNABLE_LIST.add(this);
            project.RUNNABLE_LIST.add(this);
        }

        protected volatile boolean active = false;

        public void Stop() {
            active = false;
        }

    }

}
