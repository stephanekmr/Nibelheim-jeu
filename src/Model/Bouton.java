package Model;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;

// classe represenant la logique de chaque bouton
public class Bouton extends JButton {
    // Attributs nécessaires pour le bouton :
    String name ; // Nom du bouton
    int x; // position x du bouton
    int y; // position y du bouton
    int width; // largeur du bouton
    int height; // hauteur du bouton
    Color couleur; // Couleur du bouton
    Color couleur_texte; // Couleur du texte
    int tailleTexte; // Taille du texte
    Color couleur_survol; // Couleur du bouton quand on survole
    String image; // image du bouton (facultatif)

    // Constructeur de la classe Bouton
    public Bouton(String name, int x, int y, int width, int height, Color couleur, Color couleur_texte, Color couleur_survol, String image, int tailleTexte) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.couleur = couleur;
        this.couleur_texte = couleur_texte;
        this.couleur_survol = couleur_survol;
        this.image = image;

        // Initialisation du bouton
        setFont(new Font("Arial", Font.BOLD, tailleTexte));
        setBounds(x, y, width, height);
        setBackground(couleur);
        setForeground(couleur_texte);
        setText(name);
        setBorderPainted(false);
        setFocusPainted(false);

        // Ajouter des bordures épaisses au bouton
        setBorder(javax.swing.BorderFactory.createLineBorder(couleur, 2)); // Bordure de 2 pixels de la couleur du bouton

        // Charger l'icone du bouton s'il en a une
        ImageIcon OriginalstartIcon = new ImageIcon(image); // Image originale pour l'icone du bouton
        Image resizedImage = OriginalstartIcon.getImage().getScaledInstance(40, 40, Image.SCALE_DEFAULT); // Redimensionner l'image
        ImageIcon icone = new ImageIcon(resizedImage); // Créer un ImageIcon avec l'image redimensionnée
        setIcon(icone); // Associer l'icône au bouton

        // Ajouter l'effet de survol du bouton
        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                setBackground(couleur_survol); // Couleur de fond au survol
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                setBackground(couleur); // Revenir à la couleur initiale
            }
        });
    }
}
