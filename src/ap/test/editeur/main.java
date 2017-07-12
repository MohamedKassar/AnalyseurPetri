package ap.test.editeur;

//package gaspp.test.editeur;
//
//import gaspp.javasource.rdp.graphiques.arc.Arc;
//import gaspp.javasource.rdp.graphiques.arc.ArcInhibiteur;
//import gaspp.javasource.rdp.graphiques.arc.ArcPreOrdinaire;
//import gaspp.javasource.rdp.graphiques.arc.ArcPost;
//import gaspp.javasource.rdp.project.Editeur;
//import gaspp.javasource.rdp.graphiques.place.Place;
//import gaspp.javasource.rdp.graphiques.transition.Transition;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.ObjectInputStream;
//import java.io.ObjectOutputStream;
//import javafx.application.Application;
//import javafx.scene.Scene;
//import javafx.scene.control.Button;
//import javafx.scene.layout.BorderPane;
//import javafx.scene.shape.Circle;
//import javafx.scene.shape.Line;
//import javafx.scene.shape.Polygon;
//import javafx.stage.Stage;
//
///**
// *
// * @author Tarek
// */
//public class main extends Application {
//
//    Circle start = new Circle(10), end = new Circle(10);
//    Editeur pane = new Editeur();
//    static Transition t, t1;
//
//    @Override
//    public void start(Stage primaryStage) throws Exception {
//
//        Place p3 = new Place(150, 200, pane);
//        Place p1 = new Place(200, 50, pane);
//        Place p2 = new Place(100, 50, pane);
//        p1.setMarquage(3);
//        p2.setMarquage(8);
//        t = new Transition(150, 100, pane);
//        Arc a1 = new ArcPreOrdinaire(p2, t, pane);
//        Arc a2 = new ArcPost(t, p3, pane);
//        Arc a3 = new ArcInhibiteur(p1, t, pane);
//        
//        a1.setPoid(2);
//        a2.setPoid(3);
//        a3.setPoid(4);
//        Button bo = new Button();
//        bo.setOnMouseClicked(e -> {
//            if (t.isFranchissable()) {
//                System.err.println(p1.getMarquage()+", "+p2.getMarquage()+", "+p3.getMarquage());
//                t.tirer();
//                System.err.println("tirage fait: "+p1.getMarquageAnalyse()+", "+p2.getMarquageAnalyse()+", "+p3.getMarquageAnalyse());
//            } else {
//                System.err.println("tirage pas fait: "+p1.getMarquageAnalyse()+", "+p2.getMarquageAnalyse()+", "+p3.getMarquageAnalyse());
//            }
//        });
//        BorderPane b = new BorderPane(pane);
//        b.setTop(bo);
//        Scene s = new Scene(b, 640, 400);
//        primaryStage.setScene(s);
//        primaryStage.show();
//
//    }
//
//    void test() {
//        Line line = new Line(500, 200, 400, 400);
//        line.setStrokeWidth(2);
//        pane.getPaneChildren().add(line);
//        Polygon p = new Polygon(0, 5.0, -5.0, -5.0, 5.0, -5.0);
//        pane.getPaneChildren().add(p);
//        p.setStyle("-fx-fill: transparent; -fx-stroke: black;");
//        double angle = Math.atan2(line.getEndY() - line.getStartY(), line.getEndX() - line.getStartX()) * 180 / 3.14;
//        p.setRotate(angle - 90);
////        p.setTranslateX(line.getStartX());
////        p.setTranslateY(line.getStartY());
//        double lon = Math.sqrt(((line.getEndY() - line.getStartY()) * (line.getEndY() - line.getStartY())) + ((line.getEndX() - line.getStartX()) * (line.getEndX() - line.getStartX())));
//        System.err.println(lon);
//        p.setTranslateX(line.getEndX() - (Place.RAYON.doubleValue() * (line.getEndX() - line.getStartX())) / lon);
//        p.setTranslateY(line.getEndY() - (Place.RAYON.doubleValue() * (line.getEndY() - line.getStartY())) / lon);
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//
//    private void load() {
//        FileInputStream fin = null;
//        ObjectInputStream ois = null;
//        try {
//            File file = new File("fichier");
//            fin = new FileInputStream(file);
//            ois = new ObjectInputStream(fin);
////            System.err.println(ois.readObject());
//            pane = (Editeur) ois.readObject();
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        } finally {
//            if (ois != null) {
//                try {
//                    ois.close();
//                } catch (IOException ex) {
//                    ex.printStackTrace();
//                }
//            }
//        }
//    }
//
//    private void save(Editeur pane) {
//        try {
//            File file = new File("fichier");
//            FileOutputStream fos = new FileOutputStream(file);
//            ObjectOutputStream oos = new ObjectOutputStream(fos);
//            oos.writeObject(pane);
//            oos.flush();
//            oos.close();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }
//}
