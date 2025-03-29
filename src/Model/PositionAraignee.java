package Model;

/*Gere la position des ennemis */
public class PositionAraignee {
    //dimension de l'araignée
    public  final int LARGEUR_A=10;
    public  final int HAUTEUR_A=10;

    //constante pour l'horizon
    public static final int BEFORE= 50;
    public static final int AFTER=2000;
    public static final int HAUTEUR_MAX= 1000;
    
    //centre page
    public static final int X_CENTRE = AFTER/3 -BEFORE;
    public static final int Y_CENTRE = HAUTEUR_MAX/4;

    //vitesse de l'araignée
    public int vitesseA=6;
    
    /*getter et setter pour la vitesse */
    public int getVitesseAraignee() {
        return this.vitesseA;
    }
    public void setVitesseAraignee(int vitesse) {
        this.vitesseA = vitesse;
    }

    
}
