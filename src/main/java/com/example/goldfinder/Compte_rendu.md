# GOLD FINDER Equipe P1

## LEFEVRE Quentin & MESKINI Driss 

### Tâches realisées:

[x] Tâche 1: Parties mono-joueur  
[x] Tâche 2: Parties multi-joueur  
[x] Tâche 3: Bot  
[x] Tâche 4: Leader-board  
[x] Tâche 5: Gestion de parties multiples  
[x] Tâche 6: Multi-servers  
[x] Tâche 7: Montée en charge  
[x] Tâche 8: Cops vs Robbers  
[x] Tâche 9: TCP et UDP  
[x] Tâche 10: améliorations : 
  - Ajout du mode massivement multi-joueur pour GoldFinder
  - Ajout du mode massivement multi-joueur pour Cops and Robbers
  - Ajout de Parallax


### Organisation du code:

Nous avons mis un point d'honneur sur l'extensibilié, la modularité ainsi que la clareté du code en essyant de respecter au maximum les principes SOLID.

#### Pool de Server: 

Nous avons mis en place un dispatcher utilise une pool de thread qui contient l'ensemble des "GameSever" qui permet de redigirer le client vers un server disponible.

#### Server:

Un server qui sera ici un "GameServer" permet de gérer les connexions aux parties. 

#### Parties:

Il existe différents modes de jeu tels que le GoldFinder classique à 4 joueurs par défaut, GoldFinder solo, le mode Cops and Robbers à 4 joueurs ainsi que le GoldFinder Massive.

- GoldFinder Classique:  
    Le GoldFinder classique se joue à 4 joueurs sur une grille de 16x16. Ces mêmes joueurs sont placés alléatoirement sur la grille au lancement de la partie.
    Lorsqu'un jouer ramasse une pièce, son score augmente de 1. Ce score sera stocké à la fin de la partie dans le fichier leaderboard.txt. La partie se termine lorsque toutes les pièces sont collectées et que toutes les cases sont visitées.

- GoldFinder Solo:  
    Le GoldFinder Solo se lance à 1 joueur et possède les mêmes règles que le GoldFinder Classique

- Cops and Robbers:  
    Le Mode de jeu Cops and Robbers se joue à 4 joueurs. Il sont réparti dans chaque équipe au lancement de la partie. Le but des Robbers est de collecter toutes les pièces sans se faire attraper par les Cops. Ces derniers ont pour but de capturer la totalité des Robbers.  Chaque joueur possède un boolean isCop qui permet donc de définir s'il est Cop ou non. Les joueurs sont placés aléatoirement sur la grille. Lorsque deux joueurs alliés se rencontrent ils sont affichés en bleu. A l'inverse s'ils sont ennemis, le joueur de l'équipe adverse est affiché en rouge. Lorsque deux joueurs adverses se dirigent vers la même case, le Robber est capturé et disparaît de la vue des autres joueurs. Le joueur capturé garde son client ouvert. Il ne peut plus se déplacer, cependant les cases autour de lui sont toujours mise à jour ce qui implique qu'il peut voir s'il un autre joueur passe sur une de ces cases adjacentes. Les Cops peuvent voir les pièces mais pas les récupérer.

- GoldFinder Massive:
    Le GoldFinder Massive est une version du GoldFinder classique ou le nombre de joueur passe à 32 et la grille augmente sensiblement en taille (64x64). Les règles restent les mêmes que le mode de jeu classique.

- Cops and Robbers Massive:
    Le Cops and Robbers Massive est une version du Cops and Robbers ou le nombre de joueur passe à 32 et la grille augmente sensiblement en taille (64x64). Les règles restent les mêmes que le mode de jeu Cops and Robbers.

#### Client: 

- Pour lancer le client, il faut démarrer AppClient qui possède une instance de la classe "Client". Ce dernier a deux modes de connection. Le mode TCP et UDP. Il peut sélectionner le mode voulu direcement depuis l'application sur le menu dépliant, avant de se connecter. Lorsque l'on appuie sur le bouton Connect, le client essaie de se connecter avec le server 10 fois. Une fois connecté, le bouton "Connect" devient "Play". Le mode de jeu peut être selectionné à l'aide d'un menu dépliant (par défaut : GoldFinder Classique). La vue du joueur s'adapte en fonction du mode de jeu afin d'obtenir une grille adaptée. Lorsque le client est placé sur la grille, il envoie une commande "SURROUNDING" au server qui lui renvoie le contenu des cases adjacentes. Le Client possède un controller qui permet de récupérer la saisie des touches. On envoie ensuite une demande au server pour authoriser le déplacement. Le client envoie un "SURROUNDING" afin d'actualisé la carte. La mise à jour de la carte donne l'illusion que le client se déplace cependant nous avons mis en place un système de parallax qui déplace la grille. Le joueur reste donc toujours au centre de l'écran.

#### Bot: 
- Le bot est, vis-à-vis du serveur, vu comme un joueur normal. il utilise également 
e même client et est donc compatible entre TCP et UDP. Nous avons décidé d'aller légèrement au-dessus du 
ode de déplacement complétement aléatoire qui nous a été proposé sur le sujet du projet en permettant au bot de se déplacer seulement sur des cases où le déplacement est possible, cela évite que lorsqu'une seule direction est possible qu'il ne bloque sur les 3 autres modulo l'aléatoire. Les déplacements se font en utilisant une instance spéciale de la classe `Executor` qui permet de définir un délai à l'execution du tâche (avec un nombre de tâche à éxécuter définie ou non).  

- Au delà du bot au singuler, nous av#ns créé une classe AppBot qui permet de lancer une quantitée à l'avance de bots son rôle est simplement de créer une pool de threads contenant chacun un bot. 
- Nous avons également décider d'ajouter un léger délai (10ms) a la connexion de chaque bot pour éviter de surcharger le serveur, puisque nous n'avons pas trouvé réaliste le fait que à la un même temps, 1500 connexions soient à gérer par le serveur de dispatch


### Difficultés rencontrées et solutions:

- Synchronizer la grille du client avec celle du server:  
En effet le client n'a pas connaissance de ses propres coordonnées durant la partie. Seul le server connait les coordonnées de l'entiereté des joueurs. Par défaut, le joueur apparaît aux coordonnées x = 10 et y = 10 sur la vue client. Le server quant à lui, place le joueur aléatoirement sur sa grille, les coordonnées ne sont donc pas synchronizées ce qui à occasionné des bugs sur la grille du client. Par exemple nous avions de murs qui apparaîssaient ou disparaîssaient. Afin de résoudre ce problème, nous avons imaginé un système de parallax qui permet donc de déplacer la grille et non le joueur. 

- S'occuper des joueurs capturés dans Cops and Robbers:
Dans le mode de jeu Cops and Robbers lorsque les joueurs sont placé dans une équipe, les Robbers sont placé dans une HashMap. Les clés sont joueurs et les valeurs, leurs status est définis par un STRING "FREE". Lorsque deux joueurs adverses se rencontrent, le Cop capture le Robber, le status du Robber est remplacé par "CAUGHT". Son client est passé en mode "Spectateur" c'est à dire qu'il ne peut plus se déplacer, cependant les cases adjacentes continues de se mettre à jour. Ce même joueur n'apparaîtra plus la vision des autres joueurs.



#### Améliorations:
- Ajout du mode massivement multi-joueur pour GoldFinder
- Ajout du mode massivement multi-joueur pour Cops and Robbers
- Ajout de Parallax
- Couleur spécifiques à chaque joueur
- Grille redimensionnable
- Caméra qui suit le joueur
- Possibilitée de se déconnecter
- Mode spectateur pour les voleurs (Dans cops and robbers)

#### Tests:
- Fonctionnement du serveur :
  - En charge
Dispatcher  - Nombres de connexions importantes (100 connexions par secondes)érents modes de jeux en même temps
  F nctionnement du client : 


  - Test de la bonne redirection des clients- Fonctionnement du server de jeu :
  - Test de tous les modes de jeux (et les différents rôles si possible)
  - Test de la montée en charge   
  - Test du classement
  - Connexion TCP et UDP
  - Test de tous les modes de jeux et les différents rôles si possible

- Fonctionnement du bot :
  - Test dans tous les modes de jeux (et les différents rôles si possible)
  - Test de la montée en charge 
  
  
![](/image.png)