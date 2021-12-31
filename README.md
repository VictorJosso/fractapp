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

Une fois le projet compilé, se place à la racine et exécuter : `./fractapp`
Sans option cette commande affichera l'aide nécessaire à l'utilisation du programme.
Pour la version graphique : `./fractapp -i` ou `./fractapp --interactif`.
Pour la version en ligne de commande : 
    Pour un ensemble de Julia : `./fractapp -j -options`
    Pour l'ensemnble de Mandelbrot : `./fractapp -m -options`

## Version de Java

Utiliser la version 17 de Java ou une ultérieure.

## Auteurs :
    JOSSO, Victor
    KURDYK, Louis
    
### Misc.

###### Choix techniques

Pour générer de lourde fractale : implémentation de la classe BigBuffredImage.
Pour suivre l'anvancé du calcul : implémentation d'une barre de progression (impose un petite synchronisation des threads).
Pour l'interface graphique : JavaFx.
Pour la concurrence : ParallelStream (voir la partie ci-dessous).

###### Concurrence (par parallel streams)

Comparaison entre le temps de calcul pour des images de différentes tailles entre une version avec Threads (ici streams parallèles) et sans :
Le temps de calcul pouvant varier vis-à-vis de la fonction considérée dans le cadre d'un ensemble de Julia, on s'intéresse ici à l'ensemble de Mandelbrot.

| Taille (en px) | Temps de calcul (en parallèle) | Temps de calcul sur un thread |
| ------------- |:-------------:|:-------------:|
| 4.000.000      | 17s       | 31s |
| 16.000.000     | 1min20s   |  5min11s  |
| 400.000.000    |  51min7s  | 2h17min10s |

Ce tableau nous montre bien la performance accrue dans le cas de l'utilisation de la programmation concurrente.
Cependant, les deux dernières lignes de ce dernier sont surprenantes puisqu'elles indiquent que pour 400 millions de points l'impact de la programmation parallèle est moindre que pour 16 millions, on pourrait en effet s'attendre au résultat inverse.

Comparaison faite avec un processeur intel i3 7gen pouvant gérer 4 threads.

### Sources

Classe BigBufferedImage : Team Puli Space -> @author Zsolt Pocze, Dimitry Polivaev