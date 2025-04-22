package Controler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import Model.Araignee;
import Model.Bonus;
import Model.Character;
import Model.Ennemis;
import Model.Fantome;
import Model.Niveau;
import Model.Obstacles;
import Model.Goules;

// Classe qui gère les différents niveaux du jeu
public class LevelManager extends Thread {
    // Liste des niveaux
    private List<Niveau> niveaux;
    private int currentLevelIndex = 0; // Index du niveau actuel, 0 -> niveau 1
    public static final int DELAY = 50; // Délai entre chaque vérification du niveau

    // Constantes pour les pourcentages de chaque type d'ennemi
    public static final int POURCENTAGEFANTOMES = 30; // pourcentage de fantômes dans le niveau
    public static final int POURCENTAGEARAIGNEE = 60; // pourcentage d'araignées dans le niveau
    public static int pourcentageGoules = 10; // pourcentage de goules dans le niveau

    // Instances des classes utiles
    private Character c; // Instance du personnage
    private Ennemis e; // Instance de la gestion des ennemis
    private Bonus b; // Instance de la classe bonus
    private Obstacles o; // Instance de la classe obstacle

    // Timer et TimerTask pour lancer les vagues d'ennemis
    private Timer timer;
    private TimerTask task;

    // Générateur d'entiers aléatoires
    private static final Random rand = new Random();

    // Clip audio pour jouer la musique
    Clip clip = null;

    // Attributs pour l'état du jeu
    public boolean game_start = false; // Indique si le jeu a démarré
    public boolean game_lose = false; // Indique si le joueur a perdu
    public boolean game_running = false; // Indique si une partie est en cours
    public boolean game_won = false; // Indique si le joueur a gagné
    private static boolean showStore = false; // Indique si la boutique doit être affichée

    // Setteur simplifié pour game_running : initialise également game_won et
    // game_lose à false
    public void setGameRunning() {
        this.game_running = true;
        this.game_won = false;
        this.game_lose = false;
    }

    // Getteurs pour les états du jeu
    public boolean getGameWon() {
        return this.game_won;
    }

    public boolean getGameStart() {
        return this.game_start;
    }

    public boolean getGameLose() {
        return this.game_lose;
    }

    public boolean getGame_running() {
        return this.game_running;
    }

    // Getteur pour le niveau actuel
    public Niveau getNiveauActuel() {
        return niveaux.get(currentLevelIndex);
    }

    // Getteur pour showStore
    public boolean getShowStore() {
        return showStore;
    }

    // Méthode pour passer au niveau suivant
    public void goNextStage() {
        game_running = true; // Une partie commence
        showStore = false;
        currentLevelIndex++;
        initialiserEnnemis();
        lancerVaguesEnnemies();
        c.resumeGame(); // Reprendre le jeu pour le joueur
        System.out.println("Niveau suivant : " + (currentLevelIndex + 1));
        jouerSon(niveaux.get(currentLevelIndex).getMusique()); // Jouer la musique du niveau
    }

    // Méthode pour lancer une nouvelle partie
    public void startNewGame() {
        c.restartPlayer(); // Réinitialiser le joueur
        initialiserEnnemis(); // Initialiser les ennemis du niveau 1
        game_running = true; // Le jeu est en cours
        game_start = true; // La partie a démarré
        game_lose = false; // Pas de défaite initialement
        game_won = false; // Pas de victoire initialement
        showStore = false; // Ne pas afficher la boutique
        lancerVaguesEnnemies(); // Lancer les vagues d'ennemis
        jouerSon(niveaux.get(currentLevelIndex).getMusique()); // Jouer la musique du niveau
        System.out.println("Nouvelle partie commencée --------------- !");
    }

    // Méthode pour relancer une partie
    public void relancerGame() {
        c.restartPlayer(); // Réinitialiser le joueur
        initialiserEnnemis(); // Réinitialiser les ennemis
        setGameRunning(); // Démarrer la partie
        lancerVaguesEnnemies(); // Lancer les vagues d'ennemis
        jouerSon(niveaux.get(currentLevelIndex).getMusique()); // Jouer la musique du niveau
        game_running = true;
        System.out.println("-- Nouvelle partie relancée ! ----------");
    }

    // Méthode pour retourner à l'accueil
    public void goToAccueil() {
        game_running = false; // Arrêter le jeu
        showStore = false;
        game_lose = false;
        game_won = false;
        game_start = false;
        stopSon(); // Arrêter la musique du niveau
    }

    // Constructeur de la classe LevelManager
    public LevelManager(Character character, Ennemis enemy, Bonus bonus, Obstacles obs) {
        this.c = character;
        this.e = enemy;
        this.b = bonus;
        this.o = obs;
        niveaux = new ArrayList<>();
        // Ajout du ou des niveaux (les autres niveaux sont commentés, à décommenter
        // selon le besoin)
        niveaux.add(new Niveau(1, 20, 8, 2, "src/Images/bg1.png", "src/Audios/musique1.wav"));
        niveaux.add(new Niveau(2, 60, 10, 3, "src/Images/bg2.png", "src/Audios/musique2.wav"));
        niveaux.add(new Niveau(3, 90, 12, 3, "src/Images/bg3.png", "src/Audios/musique3.wav"));
        niveaux.add(new Niveau(4, 160, 15, 4, "src/Images/bg3.png", "src/Audios/musique4.wav"));
        niveaux.add(new Niveau(5, 280, 20, 5, "src/Images/bg3.png", "src/Audios/musique5.wav"));
        initialiserEnnemis();
    }

    // Méthode qui initialise les ennemis en fonction du niveau actuel
    public void initialiserEnnemis() {
        // Vider la liste des ennemis avant de les réinitialiser
        Ennemis.getListEnnemies().clear();
        // Nombre d'obstacles du niveau actuel
        int nombreObstacles = niveaux.get(currentLevelIndex).getNombreObstacles();
        // Nombre total d'ennemis du niveau actuel
        int nombreEnnemis = niveaux.get(currentLevelIndex).getNombreEnnemis();
        // Calcul du nombre d'ennemis de chaque type
        int nombreFantomes = (int) (nombreEnnemis * POURCENTAGEFANTOMES / 100);
        int nombreAraignees = (int) (nombreEnnemis * POURCENTAGEARAIGNEE / 100);
        int nombreGoules = (int) (nombreEnnemis * pourcentageGoules / 100);

        // Création des fantômes
        for (int i = 0; i < nombreFantomes; i++) {
            new Fantome(c, 7, 1, Ennemis.genererPositionAleatoire(), b);
        }
        // Création des araignées
        for (int i = 0; i < nombreAraignees; i++) {
            new Araignee(c, 5, 1, Ennemis.genererPositionAleatoire(), b);
        }
        // On crée les goules
        for (int i = 0; i < nombreGoules; i++) {
            new Goules(c, rand.nextInt(6 - 3 + 1) + 3, 1, Ennemis.genererPositionAleatoire(), b);
        }
        // Génération des obstacles
        o.genererObstacle(nombreObstacles);
    }

    // Méthode pour arrêter la musique
    public void stopSon() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.close();
        }
    }

    // Méthode statique pour jouer un son (fichier .WAV)
    public void jouerSon(String chemin) {
        try {
            stopSon(); // Arrêter un clip déjà en cours
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File(chemin));
            clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Méthode pour démarrer les vagues d'ennemis
    public void lancerVaguesEnnemies() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                Niveau niveau = niveaux.get(currentLevelIndex);
                List<Ennemis> ennemisNonDeplaces = new ArrayList<>();

                synchronized (e.getListEnnemies()) {
                    for (Ennemis ennemi : e.getListEnnemies()) {
                        if (!ennemi.getIsMoving()) {
                            ennemisNonDeplaces.add(ennemi);
                        }
                    }
                }
                // Calculer le nombre d'ennemis à déplacer en fonction du niveau
                int n = Math.max(1, niveau.getNombreEnnemis() / Math.max(1, niveau.getNombreVague()));
                // Vérifier si le nombre d'ennemis non déplacés est supérieur ou égal à n
                if (ennemisNonDeplaces.size() >= n) {
                    // Démarrer le mouvement de n ennemis aléatoires
                    for (int i = 0; i < n; i++) {
                        int randomIndex = rand.nextInt(ennemisNonDeplaces.size());
                        Ennemis ennemi = ennemisNonDeplaces.get(randomIndex);
                        ennemi.startMouvement();
                        ennemisNonDeplaces.remove(randomIndex);
                    }
                    // System.out.println("Vague activée : " + n + " ennemis démarrés.");
                }
                // Sinon, si tous les ennemis sont déplacés, on arrête la tâche
                else if (ennemisNonDeplaces.isEmpty()) {
                    // System.out.println("Dernière vague !");
                } else {
                    for (Ennemis ennemi : ennemisNonDeplaces) {
                        ennemi.startMouvement(); // Démarrer le mouvement de l'ennemi
                    }
                    System.out.println("Dernière vague activée.");
                    timer.cancel(); // Annuler le timer
                    timer.purge(); // Purger le timer
                }
            }
        };
        // Planifier la tâche pour exécuter une vague toutes les 10 secondes
        timer.schedule(task, 0, 10000);
    }

    // Méthode pour réinitialiser le jeu (par exemple, en cas de défaite)
    public void reinitialiserJeu() {
        // On arrete le mouvement des goules pour limiter les exceptions
        for (Ennemis ennemi : Ennemis.getListEnnemies()) {
            if (ennemi instanceof Goules) {
                ((Goules) ennemi).stopMovement();
            }
        }
        Ennemis.ListEnnemies = new CopyOnWriteArrayList<>(); // Réinitialiser la liste des ennemis
        currentLevelIndex = 0; // Retourner au premier niveau
        stopSon(); // Arrêter la musique
        System.out.println("Jeu réinitialisé  ----------.");
    }

    // Méthode pour vérifier si le niveau est terminé
    public void checkNiveauFini() {
        // Si le joueur n'a plus de vie, la partie est perdue
        if (c.getVie() <= 0) {
            System.out.println("Vous avez perdu !");
            game_running = false;
            game_lose = true;
            reinitialiserJeu();
            return;
        }
        // Si tous les ennemis sont éliminés
        if (e.getListEnnemies().isEmpty()) {
            // Si tous les niveaux ont été terminés
            if (currentLevelIndex >= niveaux.size() - 1) {
                System.out.println("Bravo, vous avez terminé tous les niveaux --------- !");
                game_running = false;
                game_won = true;
                reinitialiserJeu();
                return;
            } else {
                System.out.println("Niveau terminé, affichage de la boutique --------- !");
                c.annulerLesCombos(); // Annuler les combos du joueur
                b.resetBonus(); // Réinitialiser les bonus
                game_running = false; // La partie est arrêtée
                showStore = true; // Afficher la boutique
                stopSon(); // Arrêter la musique
            }
        }
    }

    // Boucle de vérification du niveau
    @Override
    public void run() {
        while (true) {
            if (game_running) {
                checkNiveauFini();
            }
            try {
                Thread.sleep(DELAY);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
