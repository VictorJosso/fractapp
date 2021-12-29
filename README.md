# Projet fractales de CPOO

Ce projet correspond à celui demandé pour l'évaluation de CPOO en L3.
Il permet de générer des images de fractales étant des ensembles de Julia ou l'ensemble de Mandelbrot.
Il est possible d'éxécuter le programme via la ligne de commande ou via une interface graphique (voir exécution).

## Installation

Pour intaller le projet :
```git clone git@gaufre.informatique.univ-paris-diderot.fr:Victor.Josso/cpoo_fractales.git```

## Compilation

Un Makefile se trouve à la racine du projet, il faut donc s'y placer et exécuter la commande `make`.

## Exécution

Une fois le projet compilé, se place à la racine et exécuter :
    ```./fractapp```
Sans option cette commande affichera l'aide nécessaire à l'utilisation du programme.

## Version de Java

Utiliser la Java 17 ou ultérieur.

## Auteurs :
    JOSSO, Victor
    KURDYK, Louis
    
### Misc.

Comparaison entre le temps de calcul pour des images de différentes tailles entre une version avec Threads (ici streams parallèles) et sans :
Le temps de calcul pouvant varier vis-à-vis de la fonction considérée dans le cadre d'un ensemble de Julia, on s'intéresse ici à l'ensemble de Mandelbrot.

| Taille (en px) | Temps de calcul (en parallèle) | Temps de calcul sur un thread |
| ------------- |:-------------:|:-------------:|
| 4.000.000      | 17s       | 31s |
| 16.000.000     | 1min20s   |  5min11s  |
| 40.000.000     |  51min7s  | En cours de calcul |


Comparaison faite avec un processeur intel i3 7gen pouvant gérer 4 threads.