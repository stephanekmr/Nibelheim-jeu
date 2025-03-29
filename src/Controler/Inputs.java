package Controler;

import java.awt.event.*;

//Classe permettant de gérer les entrées clavier du joueur.
//Elle permet de récupérer les touches appuyées par le joueur.
//Elle permet également de gérer les événements liés aux touches.
//La touche Z permet d'avancer vers le haut de l'écran. La touche D permet d'aller vers la droite. 
//La touche S permet de reculer. La touche Q permet d'aller vers la droite.

public class Inputs extends KeyAdapter {

    boolean up = false;
    boolean down = false;
    boolean left = false;
    boolean right = false;

    // 4 fonctions keyPressed qui vont mettre up, down, left ou right à true si la
    // touche correspondante est appuyée.
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_Z) {
            up = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_S) {
            down = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_Q) {
            left = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_D) {
            right = true;
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
