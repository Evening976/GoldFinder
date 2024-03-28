package com.example.goldfinder.server;

import com.example.goldfinder.server.commands.IServerCommand;
import com.example.utils.commandParsers.ServerCommandParser;
import com.example.utils.games.GameMap;
import com.example.utils.games.gdGame;
import com.example.utils.Logger;
import com.example.utils.players.AbstractPlayer;
import com.example.utils.players.CopsPlayer;
import com.example.utils.players.GFPlayer;
import javafx.util.Pair;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

public class GameServer extends IServer {
    private final Map<InetSocketAddress, AbstractPlayer> attachedPlayers = new HashMap<>();
    private final GameMap games;
    int playerID = 0;
    String GAMEMODE = "GOLD_FINDER";
    private static final int MAX_PLAYERS = 4;

    public GameServer(int port) throws IOException {
        super(port);
        games = new GameMap(MAX_PLAYERS, 10);
    }

    public void startServer() throws IOException {
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
                    GFPlayer p = (GFPlayer) key.attachment();
                    if (p.getGameID() != null) games.getByID(p.getGameID()).removePlayer(p);
                    key.channel().close();
                    key.cancel();
                    Logger.printYellow("Connection with client has been closed : " + e.getMessage());
                }
            }
        }
    }

    public GameMap getGames() {
        return games;
    }

    private void handleAccept(SelectionKey key) throws IOException {
        SocketChannel client = serverSocketChannel.accept();
        client.configureBlocking(false);
        client.register(selector, SelectionKey.OP_READ);
    }


    private void handleTCPRead(SelectionKey key) throws IOException {
        InetSocketAddress senderAddress = (InetSocketAddress) ((SocketChannel) key.channel()).getRemoteAddress();
        if (!attachedPlayers.containsKey(senderAddress)) {
            if (Objects.equals(GAMEMODE, "GOLD_FINDER"))
                attachedPlayers.put(senderAddress, new GFPlayer(key.channel(), "Player" + playerID++, senderAddress, 0, 0));
            else
                attachedPlayers.put(senderAddress, new CopsPlayer(key.channel(), "Player" + playerID++, senderAddress, 0, 0));
        }

        if (key.attachment() == null) {
            if (Objects.equals(GAMEMODE, "GOLD_FINDER"))
                key.attach(new GFPlayer(key.channel(), "GFPlayer" + playerID++, senderAddress, 0, 0));
            else
                key.attach(new CopsPlayer(key.channel(), "CopsPlayer" + playerID++, senderAddress, 0, 0));
        }

        String msg = receiveTCPMessage((SocketChannel) key.channel());
        if (!msg.isEmpty()) {
            SelectionKey k = handleCommands(key, msg);
            attachedPlayers.put(senderAddress, (AbstractPlayer) k.attachment());
        }
    }

    private void handleUDPRead(SelectionKey key) throws IOException {
        Pair<InetSocketAddress, String> messageandIp = receiveUDPMessage(key);
        String msg = messageandIp.getValue();
        InetSocketAddress senderAddress = messageandIp.getKey();

        if (!attachedPlayers.containsKey(senderAddress)) {
            if (Objects.equals(GAMEMODE, "GOLD_FINDER"))
                attachedPlayers.put(senderAddress, new GFPlayer(key.channel(), "Player" + playerID++, senderAddress, 0, 0));
            else
                attachedPlayers.put(senderAddress, new CopsPlayer(key.channel(), "Player" + playerID++, senderAddress, 0, 0));
        }
        key.attach(attachedPlayers.get(senderAddress));

        if (!msg.isEmpty()) {
            SelectionKey k = handleCommands(key, msg);
            attachedPlayers.put(senderAddress, (AbstractPlayer) k.attachment());
        }
    }

    private SelectionKey handleCommands(SelectionKey key, String msg) {
        AbstractPlayer player = (AbstractPlayer) key.attachment();
        gdGame g = null;

        if (player.getGameID() != null)
            g = games.getByID(player.getGameID());

        IServerCommand currentCommand = ServerCommandParser.parseCommand(msg);
        if (currentCommand != null) {
            if (g == null)
                sendMessage(key.channel(), currentCommand.run(key.channel(), this, player, null, msg.split(" ")), player.getAddress());
            else
                sendMessage(key.channel(), currentCommand.run(key.channel(), this, player, games.getByID(player.getGameID()), msg.split(" ")), player.getAddress());

            player = currentCommand.getPlayer();
            games.setGame(player.getGameID(), currentCommand.getGame());

            System.out.println("Game server data : " + player + " game : " + games.getByID(player.getGameID()));
        }
        key.attach(player);
        return key;
    }
}