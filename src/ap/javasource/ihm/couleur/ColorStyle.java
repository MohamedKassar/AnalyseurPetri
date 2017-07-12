package ap.javasource.ihm.couleur;

import java.util.Vector;
import javafx.scene.Node;

/**
 *
 * @author Tarek
 */
public final class ColorStyle {

    private static final double OPACITY = 0.9;

    public static final ColorStyle COULEUR_BLEU = ColorStyle.rgba(51, 153, 255, OPACITY);
    public static final ColorStyle COULEUR_VERT = ColorStyle.rgba(51, 153, 138, OPACITY);
    public static final ColorStyle COULEUR_SAUMON = ColorStyle.rgba(250, 128, 114, OPACITY);
    public static final ColorStyle COULEUR_ROUGE = ColorStyle.rgba(255, 0, 0, OPACITY);
    public static final ColorStyle COULEUR_ORANGE = ColorStyle.rgba(255, 69, 0, OPACITY);
    public static final ColorStyle COULEUR_VIOLET = ColorStyle.rgba(219, 112, 147, OPACITY);
    public static final ColorStyle COULEUR_GRIS = ColorStyle.rgba(128, 128, 128, OPACITY);
    public static final ColorStyle COULEUR_BEIGE = ColorStyle.rgba(245, 245, 220, OPACITY);
    public static final ColorStyle COULEUR_CARMOISI = ColorStyle.rgba(220, 20, 60, OPACITY);

    public static Vector<ColorStyle> COLORS;

    protected String opaqueColor = "-fx-background-color: ";
    protected String normalColor = "-fx-background-color: ";

    protected ColorStyle(String color, String colorOpaque) {
        if (COLORS == null) {
            COLORS = new Vector<>();
        }
        opaqueColor += colorOpaque;
        normalColor += color;
        COLORS.add(this);
    }
    
    public void opaqueColoring(Node node){
        node.setStyle(opaqueColor);
    }
    
    public void coloring(Node node){
        node.setStyle(normalColor);
    }

    private static ColorStyle rgba(int r, int g, int b, double a) {

        if (r < 0) {
            r = 0;
        } else if (r > 255) {
            r = 255;
        }

        if (g < 0) {
            g = 0;
        } else if (g > 255) {
            g = 255;
        }

        if (b < 0) {
            b = 0;
        } else if (b > 255) {
            b = 255;
        }

        if (a < 0) {
            a = 0;
        } else if (a > 1) {
            a = 1;
        }
        return new ColorStyle("rgb(" + r + "," + g + "," + b +");", "rgba(" + r + "," + g + "," + b + "," + a + ");");
    }

}
