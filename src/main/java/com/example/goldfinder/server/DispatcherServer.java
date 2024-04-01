package com.example.goldfinder.server;

import com.example.goldfinder.server.commands.dispatcherserver.DispatcherServerCommand;
import com.example.goldfinder.server.commands.dispatcherserver.DispatcherServerCommandParser;
import com.example.utils.Logger;
import javafx.util.Pair;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
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

    TreeMap<Integer, List<String>> scores = new TreeMap<>();

    public DispatcherServer(int port) throws IOException {
        super(port);
        gameServers = new ArrayList<>(SERVER_COUNT);
        executor = Executors.newFixedThreadPool(SERVER_COUNT);
        for (int i = 0; i < SERVER_COUNT; i++) {
            gameServers.add(new GameServer(0, GAME_COUNT));
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
                    key.cancel();
                    key.channel().close();
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
            System.out.println("Received message from " + senderAddress + " : " + msg);
            key.attach("TCP");
            handleCommands(key, msg, senderAddress);
        }
    }

    @Override
    protected void handleUDPRead(SelectionKey key) throws IOException {
        Pair<InetSocketAddress, String> messageandIp = receiveUDPMessage(key);
        String msg = messageandIp.getValue();
        InetSocketAddress senderAddress = messageandIp.getKey();
        key.attach("UDP");
        if (!msg.isEmpty()) {
            handleCommands(key, msg, senderAddress);
        }
    }

    private void initGameServers(){
        for (GameServer gameServer : gameServers) {
            executor.execute(() -> {
                try {
                    gameServer.startServer();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private void handleCommands(SelectionKey key, String msg, InetSocketAddress senderAddress) {
        DispatcherServerCommand currentCommand = DispatcherServerCommandParser.parseCommand(msg);
        if (currentCommand != null) {
            String response = currentCommand.run(key.channel(),key, gameServers, senderAddress, msg.split(" "));
            System.out.println("Sending to " + senderAddress + " : " + response);
            sendMessage(key.channel(), response, senderAddress);
        }
    }

    private TreeMap<Integer, ArrayList<String>> getScores(){
        TreeMap<Integer, ArrayList<String>> biggesMapEver = new TreeMap<>();
        for(GameServer gameServer : gameServers){
            biggesMapEver.putAll(gameServer.getScores());
        }
        /*soit le dispatcher poll les gameServer et met a jour les tableaux des scores de tous les serveurs
         mais on a plein de copies du même tableau (- de complexité en temps, + de complexité en espace)
         soit on met a jour la liste des scores a chaque fois qu'on ajoute un score dans un gameServer
         et poll le dispatcher pour renvoyer la liste des scores a chaque fois qu'on demande les scores
         ( + de complexité en temps (+ de complexité pour les serveurs de jeux aussi), - de complexité en espace)
         la 1ère solution est plus facile a mettre en place
        */
        return null;
    }

    public static void main(String[] args) {
        DispatcherServer server;
        try {
            server = new DispatcherServer(serverPort);
            System.out.println("dispatcher server should be listening on port " + serverPort);
            server.startServer();
            //ScoreManager.SaveLeaderboards();
        } catch (IOException e) {
            System.out.println("Error creating server " + e.getMessage());
        }
    }
}
