package com.example.goldfinder.client;

import com.example.utils.ConnectionMode;
import com.example.utils.Logger;

import java.io.IOException;

public class ClientBoi extends IClient {
  final ConnectionMode mode;
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
      if(mode == ConnectionMode.TCP)
        sendMessage(tcpSocket, Wbuffer, "Hello from client");
      else
        sendMessage(udpSocket, Wbuffer, "Hello from client");

      Logger.printDebug("Sent message to server");
      Thread.sleep(1000);
    }
  }

}
