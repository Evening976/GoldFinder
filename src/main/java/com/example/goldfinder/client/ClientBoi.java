package com.example.goldfinder.client;

import com.example.utils.ConnectionMode;
import com.example.utils.Logger;

import java.io.IOException;

public class ClientBoi extends IClient {
  final ConnectionMode mode;
  private boolean isPlaying = false;
  public ClientBoi(ConnectionMode mode) {
    super(mode);
      this.mode = mode;
      try{
      connect();
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Override
  protected void handleClient() throws IOException, InterruptedException {
    for(int i = 0; i < 10; i++){
      if(mode == ConnectionMode.TCP) {
        sendMessage(tcpSocket, Wbuffer, "GAME_JOIN Ryo");
        isPlaying = true;
      }
      else
        sendMessage(udpSocket, Wbuffer, "Hello from client");

      Logger.printDebug("Sent message to server");
      Thread.sleep(1000);
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
