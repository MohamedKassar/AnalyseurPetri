package ap.test.editeur;

//package gaspp.test.editeur;
//
//import gaspp.javasource.rdp.analyses.AnalyseurReseau;
//import gaspp.javasource.rdp.graphiques.arc.Arc;
//import gaspp.javasource.rdp.graphiques.arc.ArcInhibiteur;
//import gaspp.javasource.rdp.graphiques.arc.ArcPost;
//import gaspp.javasource.rdp.graphiques.arc.ArcPreOrdinaire;
//import gaspp.javasource.rdp.project.Editeur;
//import gaspp.javasource.rdp.graphiques.place.Place;
//import gaspp.javasource.rdp.graphiques.transition.Transition;
//import javafx.application.Application;
//import javafx.stage.Stage;
//
///**
// *
// * @author Tarek
// */
//public class GMATest extends Application {
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//
//    @Override
//    public void start(Stage primaryStage) throws Exception {
//        Editeur editeur = new Editeur();
//        AnalyseurReseau analyseurReseau = editeur.getAnlyseur();
//        double deb, fin, constr;
//
//        exemple_Complique_19M(editeur);
//
//        deb = System.currentTimeMillis();
//        analyseurReseau.construireGMA();
//        fin = System.currentTimeMillis();
//        constr = (fin - deb);
//        deb = System.currentTimeMillis();
//        System.err.println(analyseurReseau.gma);
//        fin = System.currentTimeMillis();
//
//        System.err.println("temp de construction : " + constr + "ms");
//        System.err.println("temp d'affichage : " + (fin - deb) + "ms");
//        System.err.println("nombre d'arcs : "+analyseurReseau.gma.arc);
//        System.err.println("nombre de marquages : "+analyseurReseau.getIdMarquage());
//        System.err.println("Graph Borne : " + analyseurReseau.gma.borne);
//
////        p1.setMarquage(2);
////        deb = System.currentTimeMillis();
////        analyseurReseau.construire();
////        fin = System.currentTimeMillis();
////        System.err.println("temp de construction : " + (fin - deb) + "ms");
////        deb = System.currentTimeMillis();
////        System.err.println(analyseurReseau.gma);
////        fin = System.currentTimeMillis();
////        System.err.println("temp d'affichage : " + (fin - deb) + "ms");
//        primaryStage.show();
//        primaryStage.close();
//    }
//
//    private void exemple_leRetour(Editeur editeur) {
//        Place p0 = new Place(0, 0, editeur);
//        Place p1 = new Place(0, 0, editeur);
//        Place p2 = new Place(0, 0, editeur);
////        Place p3 = new Place(0, 0, editeur);
//
//        Transition t0 = new Transition(0, 0, editeur);
//        Transition t1 = new Transition(0, 0, editeur);
//        Transition t2 = new Transition(0, 0, editeur);
//
//        Arc a0 = new ArcPreOrdinaire(p0, t1, editeur);
//        Arc a1 = new ArcPreOrdinaire(p2, t1, editeur);
//        Arc a2 = new ArcPreOrdinaire(p1, t0, editeur);
//
//        Arc a4 = new ArcPost(t0, p0, editeur);
//        Arc a5 = new ArcPost(t0, p2, editeur);
//        Arc a6 = new ArcPost(t1, p1, editeur);
//        Arc a7 = new ArcPost(t2, p1, editeur);
//
//        Arc a8 = new ArcInhibiteur(p0, t2, editeur);
//        Arc a9 = new ArcInhibiteur(p1, t2, editeur);
//        Arc a10 = new ArcInhibiteur(p2, t2, editeur);
//
//        a8.setPoid(2);
//        a10.setPoid(2);
//        a2.setPoid(2);
////        a0.setPoid(2);
//
//        p1.setMarquage(3);
//    }
//
//    private void exemple_Cour(Editeur editeur) {
//        Place p0 = new Place(0, 0, editeur);
//        Place p1 = new Place(0, 0, editeur);
//        Place p2 = new Place(0, 0, editeur);
//        Place p3 = new Place(0, 0, editeur);
//
//        Transition t0 = new Transition(0, 0, editeur);
//        Transition t1 = new Transition(0, 0, editeur);
//        Transition t2 = new Transition(0, 0, editeur);
//
//        Arc a0 = new ArcPreOrdinaire(p0, t0, editeur);
//        Arc a1 = new ArcPreOrdinaire(p1, t1, editeur);
//        Arc a2 = new ArcPreOrdinaire(p2, t2, editeur);
//        Arc a3 = new ArcPreOrdinaire(p3, t2, editeur);
//
//        Arc a4 = new ArcPost(t0, p2, editeur);
//        Arc a5 = new ArcPost(t1, p3, editeur);
//        Arc a6 = new ArcPost(t2, p0, editeur);
//        Arc a7 = new ArcPost(t2, p1, editeur);
//
////        Arc a8 = new ArcInhibiteur(p0, t2, editeur);
////        Arc a9 = new ArcInhibiteur(p1, t2, editeur);
////        Arc a10 = new ArcInhibiteur(p2, t2, editeur);
////        a8.setPoid(2);
////        a10.setPoid(2);
////        a2.setPoid(2);
////        a0.setPoid(2);
//        p0.setMarquage(1);
//        p1.setMarquage(1);
//    }
//
//    private void exemple_DoubleBornitude(Editeur editeur) {
//        Place p0 = new Place(0, 0, editeur);
//        Place p1 = new Place(0, 0, editeur);
//        Place p2 = new Place(0, 0, editeur);
//        Place p3 = new Place(0, 0, editeur);
//
//        Transition t0 = new Transition(0, 0, editeur);
//        Transition t1 = new Transition(0, 0, editeur);
////        Transition t2 = new Transition(0, 0, editeur);
//
//        Arc a0 = new ArcPreOrdinaire(p1, t0, editeur);
//        Arc a1 = new ArcPreOrdinaire(p3, t0, editeur);
////        Arc a2 = new ArcPreOrdinaire(p0, t2, editeur);
//
//        Arc a4 = new ArcPost(t0, p0, editeur);
//        Arc a5 = new ArcPost(t0, p2, editeur);
//        Arc a6 = new ArcPost(t1, p1, editeur);
//        Arc a7 = new ArcPost(t1, p3, editeur);
//
//        Arc a8 = new ArcInhibiteur(p0, t1, editeur);
//        Arc a9 = new ArcInhibiteur(p2, t1, editeur);
//        Arc a10 = new ArcInhibiteur(p3, t1, editeur);
//
//        a8.setPoid(2);
//        a9.setPoid(2);
////        a4.setPoid(2);
////        a0.setPoid(2);
//
//        p1.setMarquage(1);
//        p3.setMarquage(1);
//    }
//
//    private void exemple_BornitudeSimple(Editeur editeur) {
//        Place p0 = new Place(0, 0, editeur);
//        Place p1 = new Place(0, 0, editeur);
////        Place p2 = new Place(0, 0, editeur);
////        Place p3 = new Place(0, 0, editeur);
//
//        Transition t0 = new Transition(0, 0, editeur);
//        Transition t1 = new Transition(0, 0, editeur);
//        Transition t2 = new Transition(0, 0, editeur);
//
//        Arc a0 = new ArcPreOrdinaire(p1, t0, editeur);
//        Arc a1 = new ArcPreOrdinaire(p1, t1, editeur);
//        Arc a2 = new ArcPreOrdinaire(p0, t2, editeur);
//
//        Arc a4 = new ArcPost(t0, p0, editeur);
////        Arc a5 = new ArcPost(t0, p2, editeur);
//        Arc a6 = new ArcPost(t1, p0, editeur);
//        Arc a7 = new ArcPost(t2, p1, editeur);
//
////        Arc a8 = new ArcInhibiteur(p0, t2, editeur);
////        Arc a9 = new ArcInhibiteur(p1, t2, editeur);
////        Arc a10 = new ArcInhibiteur(p2, t2, editeur);
////        a8.setPoid(2);
////        a10.setPoid(2);
//        a4.setPoid(2);
////        a0.setPoid(2);
//
//        p1.setMarquage(2);
//    }
//
//    private void exemple_M1supM2(Editeur editeur) {
//        Place p0 = new Place(0, 0, editeur);
//        Place p1 = new Place(0, 0, editeur);
//        Place p2 = new Place(0, 0, editeur);
////        Place p3 = new Place(0, 0, editeur);
//
//        Transition t0 = new Transition(0, 0, editeur);
//        Transition t1 = new Transition(0, 0, editeur);
//        Transition t2 = new Transition(0, 0, editeur);
//
//        Arc a0 = new ArcPreOrdinaire(p0, t2, editeur);
//        Arc a1 = new ArcPreOrdinaire(p1, t0, editeur);
//        Arc a2 = new ArcPreOrdinaire(p1, t1, editeur);
////        Arc a3 = new ArcPreOrdinaire(p3, t2, editeur);
//        Arc a4 = new ArcPost(t0, p0, editeur);
//        Arc a5 = new ArcPost(t1, p0, editeur);
//        Arc a6 = new ArcPost(t1, p2, editeur);
//        Arc a7 = new ArcPost(t2, p0, editeur);
//
//        Arc a8 = new ArcInhibiteur(p1, t2, editeur);
////        Arc a9 = new ArcInhibiteur(p1, t2, editeur);
////        Arc a10 = new ArcInhibiteur(p2, t2, editeur);
//
////        a8.setPoid(2);
////        a10.setPoid(2);
//        a5.setPoid(2);
//        a0.setPoid(2);
//        p1.setMarquage(1);
//    }
//
//    private void exemple_Complique_19M(Editeur editeur) {
//        Place p1 = new Place(0, 0, editeur);
//        Place p2 = new Place(0, 0, editeur);
//        Place p3 = new Place(0, 0, editeur);
////        Place p3 = new Place(0, 0, editeur);
//
//        Transition t1 = new Transition(0, 0, editeur);
//        Transition t2 = new Transition(0, 0, editeur);
//        Transition t3 = new Transition(0, 0, editeur);
//        Transition t4 = new Transition(0, 0, editeur);
//
//        Arc a0 = new ArcPreOrdinaire(p1, t1, editeur);
//        Arc a1 = new ArcPreOrdinaire(p1, t4, editeur);
//        Arc a2 = new ArcPreOrdinaire(p2, t2, editeur);
//        Arc a3 = new ArcPreOrdinaire(p3, t3, editeur);
//        Arc a33 = new ArcPreOrdinaire(p3, t4, editeur);
//
//        Arc a4 = new ArcPost(t1, p2, editeur);
//        Arc a5 = new ArcPost(t2, p3, editeur);
//        Arc a6 = new ArcPost(t3, p2, editeur);
//        Arc a7 = new ArcPost(t4, p1, editeur);
//
////        Arc a8 = new ArcInhibiteur(p1, t2, editeur);
////        Arc a9 = new ArcInhibiteur(p1, t2, editeur);
////        Arc a10 = new ArcInhibiteur(p2, t2, editeur);
//
////        a8.setPoid(2);
////        a10.setPoid(2);
////        a5.setPoid(2);
////        a0.setPoid(2);
//        p1.setMarquage(3);
//        p2.setMarquage(0);
//        p3.setMarquage(0);
//    }
//}
