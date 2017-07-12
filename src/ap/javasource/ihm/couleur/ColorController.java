package ap.javasource.ihm.couleur;

import java.util.ArrayList;
/**
 *
 * @author Tarek
 */
public class ColorController {

    private static final ArrayList<Colorable> colorableVector = new ArrayList<>();
    private static  ColorStyle defaultColor = ColorStyle.COULEUR_GRIS;

    public static void addColorable(Colorable colorable){
        colorableVector.add(colorable);
        colorable.setColor(defaultColor);
    }

    public static void setColor(ColorStyle fenetreCouleur){
        defaultColor = fenetreCouleur;
        colorableVector.stream().forEach((c) -> {
            c.setColor(fenetreCouleur);
        });
    }
}
