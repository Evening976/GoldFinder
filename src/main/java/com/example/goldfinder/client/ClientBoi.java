package com.example.goldfinder.client;

import com.example.utils.ConnectionMode;

import java.io.IOException;

public class ClientBoi extends IClient {

  public ClientBoi() {
    super(ConnectionMode.UDP);
    try{
      connect();
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Override
  protected void handleClient() throws IOException, InterruptedException {
  }

}
