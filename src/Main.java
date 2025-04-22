import java.awt.Point;

import javax.swing.*;
import View.*;
import Controler.*;
import Model.*;
import Model.Character;

public class Main {
    public static void main(String[] args) throws Exception {

        // Créer une fenêtre pour afficher le jeu
        JFrame f = new JFrame("Nibelheim");
        // Fermer la fenêtre lorsqu'on clique sur la croix
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Instances de classe utiles et Theads
        Inputs inputs = new Inputs();
        Bonus b = new Bonus();
        Obstacles o = new Obstacles();
        Ennemis e = new Ennemis();
        Character c = new Character(b, inputs, o);
        Tir t = new Tir(c, o);
        LevelManager lm = new LevelManager(c, e, b, o);
        Affichage a = new Affichage(c, t, b, inputs, o, lm);
        ReactionClic m = new ReactionClic(t);
        Avancer_tir avancer_tir = new Avancer_tir(t);
        Redessine r = new Redessine(a);
        Collision col = new Collision(c, t, e);

        // Ajouter un MouseListener pour gérer les clics de souris
        a.addMouseListener(m);

        // Démarrer nos différentes Threads
        col.start(); // Démarrer le thread de collision
        avancer_tir.start(); // Démarrer le thread de tir
        r.start(); // Démarrer le thread de redessin
        c.start(); // Démarrer le thread du personnage
        lm.start(); // Démarrer le gestionnaire de niveaux

        // Ajouter un KeyListener pour gérer les entrées clavier
        f.addKeyListener(inputs);

        // Ajouter la vue à la fenêtre et afficher la fenetre
        f.add(a);
        f.pack();
        f.setVisible(true);
    }
}
