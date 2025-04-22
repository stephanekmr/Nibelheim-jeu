package Controler;

import java.awt.event.*;

//Classe permettant de gérer les entrées clavier du joueur.
//Elle permet de récupérer les touches appuyées par le joueur.
//Elle permet également de gérer les événements liés aux touches.
//La touche Z permet d'avancer vers le haut de l'écran. La touche D permet d'aller vers la droite. 
//La touche S permet de reculer. La touche Q permet d'aller vers la droite.

public class Inputs extends KeyAdapter {

    public boolean up = false;
    public boolean down = false;
    public boolean left = false;
    public boolean right = false;

    // 4 fonctions keyPressed qui vont mettre up, down, left ou right à true si la
    // touche correspondante est appuyée.
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_Z) {
            up = true;
            System.out.println("Go up ...");
        }
        if (e.getKeyCode() == KeyEvent.VK_S) {
            down = true;
            // System.out.println("Go down ...");
        }
        if (e.getKeyCode() == KeyEvent.VK_Q) {
            left = true;
            // System.out.println("Go left ...");
        }
        if (e.getKeyCode() == KeyEvent.VK_D) {
            right = true;
            // System.out.println("Go right ...");
        }
    }

    // Pareil mais avec release qui va mettre à faux
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_Z) {
            up = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_S) {
            down = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_Q) {
            left = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_D) {
            right = false;
        }
    }

    // Méthode pour reinitialiser les touches
    public void resetKeys() {
        up = false;
        down = false;
        left = false;
        right = false;
    }
}
