# Goldfinder 


Projet réseau 2023-2024/L3 info/AMU. Jeu multijoueur de collecte de trésors dans un labyrinthe avec une architecture 
client-serveur 


- Binôme 1 : *LEFEVRE  Quentin* 

- Binôme 2 : *MESKINI DRISS*


Commandes client : 
- `SURROUNDING` : Renvoie les cases adjacentes à la position du joueur
- `GAME_JOIN <pseudo>` : Rejoindre une partie
- `LEADER <n>` : Renvoie les n meilleurs scores
- `<dir>` : Déplacement du joueur (N, S, E, W)

Commandes serveurs de jeu :
- `GAME_START` : Lance une partie
- `GAME_END` : Arrête une partie
- `VALID_MOVE <objet>` : Valide un déplacement du joueur et renvoie l'objet présent sur la case
- `SCORE:<pseudo>:<score>` : Envoie le score d'un joueur
- `<dir>:<objet>` : Envoie la présence d'un joueur (ou non) sur une case, sert aussi à répondre à un `SURROUNDING`

Commandes serveur de gestion :
- `REDIRECT <ip>` : Redirige le client vers un serveur de jeu


Taches à réaliser :
- [x] Création du serveur de jeu
- [x] Création du serveur de gestion
- [x] Création du client

Sous-taches :
- [x] Jeu
- [ ] Bot
- [ ] Leaderboard
- [x] Gestion de parties multiples
- [x] TCP et UDP
- [x] Montée en charge


