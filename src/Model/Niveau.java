package Model;

// cette classe représente un niveau du jeu
// elle contient des informations sur les ennemis, les obstacles, etc.
public class Niveau {

    private int niveau; // numéro du niveau
    private int nombreEnnemis; // nombre d'ennemis dans le niveau
    private int nombreObstacles; // nombre d'obstacles dans le niveau
    private String image_arriere_plan; // chemin vers l'image de fond du niveau
    private String musique; // chemin vers la musique du niveau
    private int nombreVague; // Nombre de vagues du niveau

    //private boolean isFinished = false; // indique si le niveau est terminé ou non

    // Constructeur de la classe Niveau
    public Niveau(int niveau, int nombreEnnemis, int nombreObstacles, int nombreVague, String image_arriere_plan, String musique) {
        this.niveau = niveau;
        this.nombreEnnemis = nombreEnnemis;
        this.nombreObstacles = nombreObstacles;
        this.nombreVague = nombreVague;
        this.image_arriere_plan = image_arriere_plan;
        this.musique = musique;
    }

    // Getters et Setters pour chaque attribut
    public int getNiveau() {
        return niveau;
    }
    public void setNiveau(int niveau) {
        this.niveau = niveau;
    }
    public int getNombreEnnemis() {
        return nombreEnnemis;
    }
    public void setNombreEnnemis(int nombreEnnemis) {
        this.nombreEnnemis = nombreEnnemis;
    }
    public int getNombreObstacles() {
        return nombreObstacles;
    }
    public void setNombreObstacles(int nombreObstacles) {
        this.nombreObstacles = nombreObstacles;
    }
    public String getImage_arriere_plan() {
        return image_arriere_plan;
    }
    public void setImage_arriere_plan(String image_arriere_plan) {
        this.image_arriere_plan = image_arriere_plan;
    }
    public String getMusique() {
        return musique;
    }
    public void setMusique(String musique) {
        this.musique = musique;
    }
    public int getNombreVague() {
        return nombreVague;
    }
    public void setNombreVague(int nbreVague) {
        this.nombreVague = nbreVague;
    }

    
}
