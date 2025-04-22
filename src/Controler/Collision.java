package Controler;

import Model.Character;
import Model.Ennemis;
import Model.Tir;

// Gérer toutes les collisions
public class Collision extends Thread {

    // Constante pour le délai de rafraichissement
    public static final int DELAY = 25;

    // Instances de classe utiles
    private Character c;
    private Tir t;
    private Ennemis e;

    // Constructeur pour initialiser les instances de classe
    public Collision(Character c, Tir t, Ennemis e) {
        this.c = c;
        this.t = t;
        this.e = e;
    }

    // Methode pour gérer les collisions
    @Override
    public void run() {
        while (true) {
            // Gérer les collisions entre les tirs et les araignées

            // Détecter si le joueur touche un bonus
            if (c != null) {
                c.checkBonusProche();
            } else {
                System.out.println("Le Character est null.");
            }

            // Détecter les collisions des ennemis
            if (t != null && e != null) {
                Ennemis.allCollisions(c, t);
            } else {
                System.out.println("Les objets Tir ou Ennemis sont null.");
            }

            try {
                Thread.sleep(DELAY);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
