package ap.javasource.rdp.gmapane;

import ap.javasource.rdp.analyses.Marquage;
import ap.javasource.rdp.gmapane.marquage.GraphicMarquage;
import ap.javasource.rdp.gmapane.marquage.MarquageArc;
import ap.javasource.rdp.project.Editeur;
import java.util.ArrayList;
import java.util.TreeMap;
import javafx.geometry.Point2D;

/**
 *
 * @author Tarek
 */
public class GMAPane extends Editeur {

//    private static final double margin = 70;
//    private double x = margin, y = margin;

    private GraphicMarquage graphicMarquageTemp;
//    private boolean add = false;
    private final TreeMap<Marquage, GraphicMarquage> marquages = new TreeMap<>((Marquage o1, Marquage o2) -> {
        {
            if (o1.getId() == -1 || o2.getId() == -1) {
                if (o1.equals(o2)) {
                    return 0;
                }
                return 1;
            } else {
                if (o1.getId() == o2.getId()) {
                    return 0;
                } else if (o1.getId() > o2.getId()) {
                    return 1;
                }
                return -1;
            }
        }
    });

    public void addMarquage(Marquage marquage) {
        graphicMarquageTemp = new GraphicMarquage(marquage, scale);
        getPaneChildren().add(graphicMarquageTemp);
        marquages.put(marquage, graphicMarquageTemp);
//        x += graphicMarquageTemp.getWidth() + margin;
//        add = true;
    }

    public void lineBreak() {
//        if (add) {
//            y += margin * 2;
//            x = margin;
//            add = false;
//        }
    }

    public void clear() {
//        x = margin;
//        y = margin;
        getPaneChildren().clear();
        marquages.clear();
//        scale.set(1);
    }

    public void link(Marquage source, Marquage destination, String taux) {
        GraphicMarquage temp1, temp2;
        if (!graphicMarquageTemp.equalTo(destination)) {
            temp2 = marquages.get(destination);
        } else {
            temp2 = graphicMarquageTemp;
        }
        temp1 = marquages.get(source);

        getPaneChildren().add(0, new MarquageArc(temp1, temp2, taux, scale));
    }

    @Override
    public String toString() {
//        return x +", "+y;
        return "";
    }

    public void relocateAll() {
        int nbr = marquages.keySet().size();
        int i = 0;
        ArrayList<Point2D> coords = getCoords(nbr ,(marquages.firstKey().size()*10+10+10)*(nbr/2));
        for (Marquage marquage : marquages.keySet()) {
            marquages.get(marquage).deplacer(coords.get(i).getX(), coords.get(i).getY());
            i++;
        }
    }

    private ArrayList<Point2D> getCoords(int nbr, double arc) {
        ArrayList<Point2D> temp = new ArrayList<>();
        double x, y;
        Point2D centre = new Point2D(arc + 70, arc + 70);
        for (double j = -0.3; j < nbr-0.3; j++) {
            
            x = Math.cos((2 * Math.PI) * j / (nbr)) * arc + centre.getX();
            y = Math.sin((2 * Math.PI) * j / (nbr)) * arc + centre.getY();
            temp.add(new Point2D(x, y));
        }
        return temp;
    }

    public TreeMap<Marquage, GraphicMarquage> getMarquages() {
        return marquages;
    }
    
    

}
