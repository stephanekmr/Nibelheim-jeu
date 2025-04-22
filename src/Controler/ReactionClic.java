
package Controler;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Timer;
import java.util.TimerTask;
import Model.Tir;

// Classe pour gérer les clics de souris et y réagir
public class ReactionClic implements MouseListener {
    public boolean isPressed = false;
    public static final int DELAY = 100; // Intervalle de temps entre chaque tir (en millisecondes)

    // Timer pour ajouter des tirs à intervalles réguliers
    private Timer timer;
    private TimerTask task;
    Tir tir;

    // Constructeur pour initialiser les attributs
    public ReactionClic(Tir t) {
        this.tir = t;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // Ne rien faire ici pour éviter les tirs multiples (tirs gérés dans mousePressed)
    }

    // On ajoute un nouveau tir à la liste à chaque clic ou maintien du clic
    @Override
    public void mousePressed(MouseEvent e) {
        isPressed = true;

        // Démarrer un Timer pour ajouter des tirs à intervalles réguliers
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                if (isPressed) {
                    tir.addTir();
                }
            }
        };
        timer.scheduleAtFixedRate(task, 0, DELAY);
    }

    // Gerer le relachement du clic pour arrèter de tirer
    @Override
    public void mouseReleased(MouseEvent e) {
        isPressed = false;
        if (timer != null) {
            timer.cancel();
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // Rien à faire ici pour le moment
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // Rien à faire ici pour le moment
    }
}