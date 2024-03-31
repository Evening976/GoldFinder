package com.example.goldfinder.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class DispatcherServer extends GameServer {

    public DispatcherServer(int port) throws IOException {
        super(port);
        List<GameServer> gameServers = new ArrayList<>(SERVER_COUNT);
        ExecutorService executor = Executors.newFixedThreadPool(SERVER_COUNT);
        for (int i = 0; i < SERVER_COUNT; i++) {
            gameServers.add(new GameServer(DispatcherServer.serverPort + i, GAME_COUNT));
        }
    }

    public static final int SERVER_COUNT = 4;
    public static final int GAME_COUNT = 10;

    public static final int ROW_COUNT = 4;
    public static final int COLUMN_COUNT = 4;
    final static int serverPort = 1234;
    // mettre une propriete pour recuperer l'ip d'un serveur dans le bon type de connexion
    //ajouter une commande redirect au client
    // changer le démarrage du client pour qu'il sache se rediriger vers un serveur de jeu après s'être connecté au dispatcher
    //mettre un executor avec un compte de thread pour les serveurs
    //mettre un selector pour gérer les connexions entrantes
    //on met un tableau de game server avec une propriete dans les games server pour savoir combien de parties sont dispos
    //dès qu'il y'a une connexion entrante on parcourt tout nos serveurs pour savoir lequel a le bon type de game de disponible
    //on envoie le joueur sur ce serveur

    public static void main(String[] args) {
        DispatcherServer server;

        try {
            server = new DispatcherServer(serverPort);
            System.out.println("dispatcher server should be listening on port " + serverPort);
            server.startServer();
        } catch (IOException e) {
            System.out.println("Error creating server " + e.getMessage());
        }
    }
}
