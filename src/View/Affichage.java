package View;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import Controler.Inputs;
import Controler.LevelManager;
import Model.Tir;
import Model.Obstacles;
import Model.Araignee;
import Model.Bonus;
import Model.Bouton;
import Model.Character;
import Model.ComboBonus;
import Model.Ennemis;
import Model.Fantome;
import Model.Goules;

public class Affichage extends JPanel {

    // Dimensions de la vue
    public static final int X = 1920;
    public static final int Y = 1080;

    // Position et dimension de la barre de vie
    public static final int xBarreVie = 30;
    public static final int yBarreVie = 30;
    public static final int heightBarreVie = 20;
    public static final int arcBarreVie = 10;

    // Créations des boutons du jeu
    private Bouton relancerButton = new Bouton("Nouvelle Partie", X / 2 - 100, Y / 2 - 30, 200, 50,
            new Color(169, 169, 169), Color.WHITE, new Color(70, 130, 180), null, 18);
    private Bouton acceuil = new Bouton("Accueil", X / 2 - 100, Y / 2 + 50, 200, 50,
            new Color(169, 169, 169), Color.WHITE, new Color(70, 130, 180), null, 18);
    private Bouton startGame = new Bouton("Commencer une partie", X / 2 - 200, Y - 500, 300, 55,
            new Color(169, 169, 169), Color.WHITE, new Color(70, 130, 180), "src/Images/start_bouton.png", 18);
    private Bouton nextStageBtn = new Bouton("Etage suivant", X / 2 + 145, Y / 2 + 285, 180, 40,
            new Color(169, 169, 169), Color.WHITE, new Color(70, 130, 180), null, 18);
    private Bouton quitter = new Bouton("Quitter", X - 150, 30, 100, 35,
            new Color(255, 0, 0), Color.WHITE, new Color(255, 0, 0), null, 18);
    private Bouton buyCombo1 = new Bouton("Buy 15p", X / 2 - 225, Y / 2 - 95, 90, 22,
            new Color(96, 96, 96), Color.WHITE, new Color(70, 130, 180), null, 14);
    private Bouton buyCombo2 = new Bouton("Buy 30p", X / 2 - 50, Y / 2 - 95, 90, 22,
            new Color(96, 96, 96), Color.WHITE, new Color(70, 130, 180), null, 14);
    private Bouton buyCombo3 = new Bouton("Buy 50p", X / 2 + 125, Y / 2 - 95, 90, 22,
            new Color(96, 96, 96), Color.WHITE, new Color(70, 130, 180), null, 14);

    // Instances de classes utiles
    private Bonus b;
    private Character c;
    private Tir tir;
    private Inputs i;
    private Obstacles o;
    private LevelManager lm;

    // Images (initialisées dans initImages() pour éviter des NullPointer lors de la
    // création)
    private Image imgObstacle;
    private Image coinImage;
    private Image imgCombo1;
    private Image imgCombo2;
    private Image imgCombo3;

    public Affichage(Character character, Tir t, Bonus bonus, Inputs inputs, Obstacles obs, LevelManager levelManager) {
        // Configuration du panneau
        setLayout(null);
        setPreferredSize(new Dimension(X, Y));

        this.c = character;
        this.tir = t;
        this.b = bonus;
        this.i = inputs;
        this.o = obs;
        this.lm = levelManager;

        // Initialisation des images après avoir initialisé les instances
        initImages();

        // Initialisation des boutons et des listeners
        initButtons();
        initListeners();

        // Rendre invisibles certains boutons par défaut
        hideGameButtons();
    }

    // Initialisation des images de la vue
    private void initImages() {
        // S'assurer que les dimensions nécessaires sont accessibles
        imgObstacle = new ImageIcon("src/Images/caisse.png").getImage()
                .getScaledInstance(Obstacles.WIDTH_O, Obstacles.HEIGHT_O, Image.SCALE_DEFAULT);
        coinImage = new ImageIcon("src/Images/coin.png").getImage()
                .getScaledInstance(Bonus.WIDTH_B, Bonus.HEIGHT_B, Image.SCALE_DEFAULT);
        imgCombo1 = new ImageIcon("src/Images/shoot.png").getImage()
                .getScaledInstance(85, 85, Image.SCALE_DEFAULT);
        imgCombo2 = new ImageIcon("src/Images/speed.png").getImage()
                .getScaledInstance(85, 85, Image.SCALE_DEFAULT);
        imgCombo3 = new ImageIcon("src/Images/sante.png").getImage()
                .getScaledInstance(85, 85, Image.SCALE_DEFAULT);
    }

    // Initialisation des boutons et ajout au panel
    private void initButtons() {
        add(relancerButton);
        add(acceuil);
        add(startGame);
        add(nextStageBtn);
        add(quitter);
        add(buyCombo1);
        add(buyCombo2);
        add(buyCombo3);
    }

    // Initialisation des listeners sur les boutons
    private void initListeners() {
        // Redémarrer la partie
        relancerButton.addActionListener(e -> {
            i.resetKeys();
            c.restartPlayer();
            c.resetInput(i);
            configurerClavier();
            tir.resetTirs();
            b.resetBonus();
            lm.relancerGame(); // relancer le jeu
        });

        // Retour à l'accueil
        acceuil.addActionListener(e -> {
            lm.goToAccueil();
            relancerButton.setVisible(false);
            acceuil.setVisible(false);
            repaint();
        });

        // Quitter le jeu
        quitter.addActionListener(e -> System.exit(0));

        // Lancer une nouvelle partie
        startGame.addActionListener(e -> {
            lm.startNewGame();
            i.resetKeys();
            c.restartPlayer();
            c.resetInput(i);
            configurerClavier();
            tir.resetTirs();
            b.resetBonus();
        });

        // Passage au niveau suivant
        nextStageBtn.addActionListener(e -> {
            lm.goNextStage();
            buyCombo1.setEnabled(true); // Activer le bouton de combo 1
            buyCombo2.setEnabled(true); // Activer le bouton de combo 2
            buyCombo3.setEnabled(true); // Activer le bouton de combo 3
            c.activerLesCombos();
        });

        // Achat des combos
        buyCombo1.addActionListener(e -> {
            boolean isBuy = c.addComboBonus(new ComboBonus(1));
            if (isBuy) {
                buyCombo1.setEnabled(false);
            }
        });
        buyCombo2.addActionListener(e -> {
            boolean isBuy = c.addComboBonus(new ComboBonus(2));
            if (isBuy) {
                buyCombo2.setEnabled(false);
            }
        });
        buyCombo3.addActionListener(e -> {
            boolean isBuy = c.addComboBonus(new ComboBonus(3));
            if (isBuy) {
                buyCombo3.setEnabled(false);
            }
        });
    }

    // Méthode pour configurer le clavier
    private void configurerClavier() {
        this.removeKeyListener(i); // Évite les doublons
        this.addKeyListener(i);
        this.setFocusable(true);
        this.requestFocusInWindow(); // Force le focus clavier
    }

    // Méthode utilitaire pour masquer les boutons de jeu
    private void hideGameButtons() {
        relancerButton.setVisible(false);
        acceuil.setVisible(false);
        startGame.setVisible(false);
        nextStageBtn.setVisible(false);
        quitter.setVisible(false);
        buyCombo1.setVisible(false);
        buyCombo2.setVisible(false);
        buyCombo3.setVisible(false);
    }

    // ------------------------------- Redessiner la vue
    // -------------------------------

    // Redéfinition de paintComponent pour dessiner la vue
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Dessiner le fond d'écran du niveau
        String lien_bg = lm.getNiveauActuel().getImage_arriere_plan();
        Image bg = new ImageIcon(lien_bg).getImage();
        g.drawImage(bg, 0, 0, X, Y, null);

        // Afficher la page d'accueil si le jeu n'a pas démarré
        if (!lm.getGameStart()) {
            drawAcceuil(g);
        } else {
            // Si le jeu est en cours
            if (lm.getGame_running() && !lm.getGameLose() && !lm.getGameWon()) {
                g.drawImage(Character.characterSprite, (int) c.getCurrent_x(), (int) c.getCurrent_y(), null);
                drawTirs(g); // Dessiner les tirs
                drawBonus(g); // Dessiner les bonus
                drawEnnemies(g); // Dessiner les ennemis
                drawObstacle(g); // Dessiner les obstacles
                drawBarreVie(g); // Dessiner la barre de vie
                drawNiveau(g); // Dessiner le niveau actuel
                relancerButton.setVisible(false);
                acceuil.setVisible(false);
                nextStageBtn.setVisible(false);
            }
            // Masquer startGame et quitter quand le jeu est lancé
            startGame.setVisible(false);
            quitter.setVisible(false);
        }
        // Affichage de l'écran de victoire/défaite et de la boutique
        drawGameStop_Won(g);
        drawBoutique(g);
    }

    // -------------------------- Méthodes de dessin spécifiques
    // ----------------------------

    // Méthode pour dessiner la boutique (si applicable)
    public void drawBoutique(Graphics g) {
        if (lm.getShowStore()) {
            // Un grand carré de au centre de la fenetre représente la boutique
            g.setColor(new Color(220, 220, 220, 200)); // Couleur de fond de la boutique
            g.fillRect(X / 2 - 350, Y / 2 - 350, 700, 700); // Fond de la boutique
            g.setColor(Color.BLACK); // Couleur du contour
            g.drawRect(X / 2 - 350, Y / 2 - 350, 700, 700); // Contour de la boutique
            // Contenu de la boutique ici
            g.setFont(new Font("Arial", Font.BOLD, 25)); // Police de la boutique
            g.setColor(Color.BLACK); // Couleur du texte
            g.drawString("Victoire ! Etage " + lm.getNiveauActuel().getNiveau() + " terminé", X / 2 - 120, Y / 2 - 300); // Titre de la boutique
            // Dessiner les icones des combos
            g.drawImage(imgCombo1, X / 2 - 225, Y / 2 - 210, null); // Image du bonus 1
            g.drawImage(imgCombo2, X / 2 - 50, Y / 2 - 210, null); // Image du bonus 2
            g.drawImage(imgCombo3, X / 2 + 125, Y / 2 - 210, null); // Image du bonus 3
            // Afficher le nombre de pièces en bas à gauche
            g.setColor(Color.BLACK); // Couleur du texte
            g.setFont(new Font("Arial", Font.PLAIN, 15)); // Police de texte
            g.drawString("Double tir", X / 2 - 220, Y / 2 - 105); // Texte du bonus 1
            g.drawString("Vitesse x2", X / 2 - 45, Y / 2 - 105); // Texte du bonus 2
            g.drawString("Vie pleine", X / 2 + 130, Y / 2 - 105); // Texte du bonus 3
            // Afficher le nombre de bonus collectés en bas à gauche
            g.setColor(Color.BLACK); // Couleur du texte
            g.setFont(new Font("Arial", Font.PLAIN, 15)); // Police de texte
            g.drawString("Mes pièces : " + c.getNombreBonus(), X / 2 - 320, Y / 2 + 320); // Texte du bonus 3
            // Un cadre au centre avec les astuces
            g.setColor(new Color(230, 230, 230, 200)); // Couleur de fond de la zone d'astuces
            g.fillRect(X / 2 - 250, Y / 2 + 8, 500, 150); // Fond de la zone d'astuces
            g.setColor(Color.BLACK); // Bordure de la zone d'astuces
            g.setFont(new Font("Arial", Font.BOLD, 16));
            g.drawString("Astuces", X / 2 - 250, Y / 2);
            g.drawRect(X / 2 - 250, Y / 2 + 8, 500, 150);
            // Ecrire les astuces
            g.setFont(new Font("Arial", Font.PLAIN, 15));
            g.drawString("•  Double tir vous permet de tirer 2 projectiles en un clic.", X / 2 - 240, Y / 2 + 35);
            g.drawString("•  Vitesse x2 vous permet d'aller 2 fois plus vite.", X / 2 - 240, Y / 2 + 65);
            g.drawString("•  Vie pleine réinitialise vos points de vie à 100%.", X / 2 - 240, Y / 2 + 95);

            // Afficher les boutons de la boutique
            nextStageBtn.setVisible(true);
            buyCombo1.setVisible(true);
            buyCombo2.setVisible(true);
            buyCombo3.setVisible(true);
        } else {
            nextStageBtn.setVisible(false);
            buyCombo1.setVisible(false);
            buyCombo2.setVisible(false);
            buyCombo3.setVisible(false);
        }
    }

    // Méthode pour dessiner les bonus (coins)
    public void drawBonus(Graphics g) {
        for (int i = 0; i < b.getPointBonus().size(); i++) {
            g.drawImage(coinImage, b.getPointBonus().get(i).x, b.getPointBonus().get(i).y, null);
        }
    }

    // Méthode pour dessiner les tirs
    public void drawTirs(Graphics g) {
        for (int i = 0; i < tir.getTirs().size(); i++) {
            int x = tir.getTirs().get(i).getPosition().x;
            int y = tir.getTirs().get(i).getPosition().y;
            g.drawImage(Tir.imageProjectile, x, y, null);
        }
    }

    // Méthode pour dessiner les ennemis
    public void drawEnnemies(Graphics g) {
        // Appel de la méthode statique sans instance
        List<Ennemis> ennemis = Ennemis.getListEnnemies();
        // Boucle affichant tous les ennemis
        for (Ennemis ennemi : ennemis) {
            // Vérification si l'ennemi est un Fantome
            if (ennemi instanceof Fantome) {
                Fantome fantome = (Fantome) ennemi; // Casting en Fantome
                g.drawImage(fantome.img, (int) fantome.getPosition().getX(), (int) fantome.getPosition().getY(), null);
            }
            // Vérifier si l'ennemi est une araignée
            if (ennemi instanceof Araignee) {
                Araignee araignee = (Araignee) ennemi; // Casting en Araignee
                g.drawImage(araignee.img, (int) araignee.getPosition().getX(), (int) araignee.getPosition().getY(), null);
            }
            // Vérifier si l'ennemi est un goule
            if(ennemi instanceof Goules) {
                Goules goule = (Goules) ennemi; // Casting en Goule
                g.drawImage(goule.img, (int) goule.getPosition().getX(), (int)
                goule.getPosition().getY(), null);
                if (goule.projectile != null) {
                    g.setColor(Color.RED);
                    g.fillOval(goule.projectile.getPosition().x,
                    goule.projectile.getPosition().y, 8, 8);
                    g.setColor(Color.BLACK);
                }
            }
        }
    }

    // Méthode pour dessiner les obstacles
    public void drawObstacle(Graphics g) {
        ArrayList<Point> obstacles = o.getObstacles();
        for (Point obstacle : obstacles) {
            g.drawImage(imgObstacle, (int) obstacle.getX(), (int) obstacle.getY(), null);
        }
    }

    // Méthode pour dessiner le niveau actuel
    public void drawNiveau(Graphics g) {
        Color couleur = (lm.getNiveauActuel().getNiveau() < 3) ? Color.BLACK : Color.WHITE;
        g.setColor(couleur);
        g.setFont(new Font("Arial", Font.PLAIN, 14));
        g.drawString("Etage actuel : ", X - 450, 43);
        for (int i = 0; i < 5; i++) {
            int x = X - 355 + (i * 35);
            g.drawRect(x, 30, 20, 20);
            if (i < lm.getNiveauActuel().getNiveau()) {
                g.setColor(new Color(70, 130, 180));
                g.fillRect(x, 30, 20, 20);
                g.setColor(couleur);
            }
            g.setFont(new Font("Arial", Font.PLAIN, 12));
            g.drawString(String.valueOf(i + 1), x + 6, 45);
        }
    }

    // Méthode pour dessiner la page d'accueil
    public void drawAcceuil(Graphics g) {
        Image bg = new ImageIcon("src/Images/acceuil.jpg").getImage();
        g.drawImage(bg, 0, 0, X, Y, null);
        startGame.setVisible(true);
        quitter.setVisible(true);
        g.setFont(new Font("Comic Sans MS", Font.BOLD, 60));
        g.setColor(Color.WHITE);
        g.drawString("Bienvenue sur Nibelhein", X / 2 - 402, Y / 2 - 2);
        g.setColor(Color.BLACK);
        g.drawString("Bienvenue sur Nibelhein", X / 2 - 400, Y / 2);
    }

    // Méthode pour dessiner la barre de vie et afficher les pièces
    public void drawBarreVie(Graphics g) {
        g.setColor(Color.GRAY);
        g.fillRoundRect(xBarreVie, yBarreVie, c.MAXVIE * 40, heightBarreVie, arcBarreVie, arcBarreVie);
        g.setColor(Color.RED);
        g.fillRoundRect(xBarreVie, yBarreVie, c.getVie() * 40, heightBarreVie, arcBarreVie, arcBarreVie);
        g.setColor(Color.BLACK);
        g.drawRoundRect(xBarreVie, yBarreVie, c.MAXVIE * 40, heightBarreVie, arcBarreVie, arcBarreVie);
        g.drawImage(coinImage, X - 135, 25, null);
        g.setColor(new Color(225, 0, 0));
        g.setFont(new Font("Arial", Font.PLAIN, 14));
        g.drawString("Pièces : " + c.getNombreBonus(), X - 100, 43);
    }

    // Méthode pour dessiner l'écran de victoire ou de game over
    public void drawGameStop_Won(Graphics g) {
        if (lm.getGameWon()) {
            relancerButton.setVisible(true);
            acceuil.setVisible(true);
            g.setColor(Color.BLACK);
            g.fillRect(X / 2 - 252, Y / 2 - 252, 504, 504);
            g.setColor(Color.WHITE);
            g.fillRect(X / 2 - 250, Y / 2 - 250, 500, 500);
            g.setColor(Color.GREEN);
            g.setFont(new Font("Arial", Font.BOLD, 35));
            g.drawString("Victoire !!!", X / 2 - 90, Y / 2 - 180);
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("Vous êtes simplement imbattables !", X / 2 - 175, Y / 2 - 120);
        } else if (lm.getGameLose()) {
            relancerButton.setVisible(true);
            acceuil.setVisible(true);
            g.setColor(Color.BLACK);
            g.fillRect(X / 2 - 252, Y / 2 - 252, 504, 504);
            g.setColor(Color.WHITE);
            g.fillRect(X / 2 - 250, Y / 2 - 250, 500, 500);
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 35));
            g.drawString("Game Over", X / 2 - 90, Y / 2 - 180);
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("Nombre de pièces collectées : " + c.getNombreBonus(), X / 2 - 150, Y / 2 - 120);
        } else {
            relancerButton.setVisible(false);
            acceuil.setVisible(false);
        }
    }
}
