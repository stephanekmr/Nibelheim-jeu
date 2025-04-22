package Model;

// Classe representant un bonus de combo
public class ComboBonus {
    // Attributs de la classe
    public int prix; // Prix du combo
    public int type; // Type de combo

    // Constantes pour les prix des combos
    public static final int PRIX_COMBO_1 = 15; // Prix du combo 1
    public static final int PRIX_COMBO_2 = 30; // Prix du combo 2
    public static final int PRIX_COMBO_3 = 50; // Prix du combo 3

    // Constructeur de la classe
    public ComboBonus(int type) {
        this.type = type; // Initialisation du type

        // Initialisation du prix en fonction du type
        switch (type) {
            case 1:
                this.prix = PRIX_COMBO_1; // Prix pour le type 1
                break;
            case 2:
                this.prix = PRIX_COMBO_2; // Prix pour le type 2
                break;
            case 3:
                this.prix = PRIX_COMBO_3; // Prix pour le type 3
                break;
            default:
                this.prix = 0; // Prix par défaut si le type n'est pas valide
        }
    }

    // Getteurs des attributs
    public int getPrix() {
        return prix; // Retourne le prix
    }

    public int getType() {
        return type; // Retourne le type
    }

}
