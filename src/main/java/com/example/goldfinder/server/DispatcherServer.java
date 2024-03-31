package com.example.goldfinder.server;

import com.example.utils.Logger;
import javafx.util.Pair;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DispatcherServer extends IServer {

    public static final int SERVER_COUNT = 4;
    public static final int GAME_COUNT = 10;
    public static final int ROW_COUNT = 4;
    public static final int COLUMN_COUNT = 4;
    final static int serverPort = 1234;

    ExecutorService executor;
    List<GameServer> gameServers;

    public DispatcherServer(int port) throws IOException {
        super(port);
        gameServers = new ArrayList<>(SERVER_COUNT);
        executor = Executors.newFixedThreadPool(SERVER_COUNT);
        for (int i = 0; i < SERVER_COUNT; i++) {
            gameServers.add(new GameServer(DispatcherServer.serverPort + i, GAME_COUNT));
        }
    }


    @Override
    public void startServer() throws IOException {
        initGameServers();
        while (true) {
            if (selector.select() == 0) {
                continue;
            }
            Iterator<SelectionKey> selectedKeys = selector.selectedKeys().iterator();
            while (selectedKeys.hasNext()) {
                SelectionKey key = selectedKeys.next();
                try {
                    if (key.isAcceptable()) {
                        handleAccept(key);
                    } else if (key.isReadable()) {
                        if (key.channel() instanceof SocketChannel) handleTCPRead(key);
                        else handleUDPRead(key);
                    }
                    selectedKeys.remove();
                } catch (IOException e) {
                    Logger.printYellow("Connection with client has been closed : " + e.getMessage());
                }
            }
        }
    }

    @Override
    protected void handleTCPRead(SelectionKey key) throws IOException {
        InetSocketAddress senderAddress = (InetSocketAddress) ((SocketChannel) key.channel()).getRemoteAddress();
        String msg = receiveTCPMessage((SocketChannel) key.channel());
        if(!msg.isEmpty()){
            handleCommands(key, msg, senderAddress);
        }
    }

    @Override
    protected void handleUDPRead(SelectionKey key) throws IOException {
        Pair<InetSocketAddress, String> messageandIp = receiveUDPMessage(key);
        String msg = messageandIp.getValue();
        InetSocketAddress senderAddress = messageandIp.getKey();
        if (!msg.isEmpty()) {
            handleCommands(key, msg, senderAddress);
        }
    }

    private void initGameServers(){
        for (GameServer gameServer : gameServers) {
            try {
                gameServer.startServer();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleCommands(SelectionKey key, String msg, InetSocketAddress senderAddress) {
        // TODO

    }

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
