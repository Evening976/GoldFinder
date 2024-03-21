package com.example.goldfinder.client;

import com.example.goldfinder.client.commands.CommandState;
import com.example.goldfinder.client.commands.IClientCommand;
import com.example.utils.ClientCommandParser;
import com.example.utils.ConnectionMode;
import com.example.utils.Logger;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

public class ClientBoi extends IClient {
  private boolean isPlaying = false;
  //ArrayDeque<IClientCommand> commandStack = new ArrayDeque<>();
  public ClientBoi(ConnectionMode mode) {
    super(mode);
      try{
      connect();
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Override
  protected void handleClient() throws IOException {
    String msg = (mode == ConnectionMode.TCP ? receiveMessage(tcpSocket) : receiveMessage(udpSocket));
    if (msg != null) {
      Logger.printYellow("Received message from server: " + msg);
      IClientCommand currentCommand = ClientCommandParser.parseCommand(msg);
        if (currentCommand != null) {
          currentCommand.run(this, msg);
        }
    }
  }

  public void sendMessage(String msg){
      if (mode == ConnectionMode.TCP) {
        sendMessage(tcpSocket, Wbuffer, msg);
        isPlaying = true;
      } else
        sendMessage(udpSocket, Wbuffer, msg);
  }

  public boolean isPlaying(){
    return isPlaying;
  }

  public void setPlaying(boolean b) {
    isPlaying = b;
  }
}
