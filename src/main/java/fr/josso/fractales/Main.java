package fr.josso.fractales;

import fr.josso.fractales.Graphics.HelloApplication;
import fr.josso.fractales.cmdLine.cmdLineController;
import javafx.application.Application;

public class Main {
    public static void main(String[] args) {


        if (args[0].equals("--graphic")) Application.launch(HelloApplication.class);

        else if (args[0].equals("--help")) {
            System.out.println("--graphic : pour afficher l'interface graphique\n" +
                    "--cmdLine -j minX maxX minY maxY step maxIter radius poly : pour utiliser la version ligne de commande pour un ensemble de Julia\n" +
                    "--cmdLine -m minX maxX minY maxY step maxIter radius : pour utiliser la version ligne de commande pour afficher l'ensemble de Mandelbrot.\n" +
                    "--help -h : pour plus de détails.\n");
            if (args.length > 1 && args[1].equals("-h")) {
                System.out.println("Aide détaillée :\n" +
                        "Entrez votre fonction poly comme suit :\n" +
                        "+-(cn)z^n +- ... +- (c1)z^1 +- (c) où les cn sont écrits sous la forme : a +- bi (les espaces après et avant le symbole d'opération sont importants)\n" +
                        "Attention à mettre exactement un espace après chaque exposant et pas avant.\n" +
                        "Exemple :  - (-1)z^2 - (42 - .25i)z^1 - (3.5 + 8i)z^7 - (3.14 + 1.2i)\n" +
                        "minX, maxX, minY, maxY de type float détermine le rectangle dans lequel la fractale sera dessinée.\n" +
                        "step de type double détermine l'écart entre deux point du plan.\n" +
                        "maxinter de type long détermine le nombre d'application maximal de la fonction sur chaque point.\n" +
                        "radius de type BigInterger détermine à partir de quel module un point est considéré comme divergent.");
            }
        }
        else if (args[0].equals("--cmdLine")) {
            new cmdLineController(args).exec();
        } else {
            System.out.println("Application Java de dessin de fractales, --help pour afficher l'aide.");
        }
    }
}
