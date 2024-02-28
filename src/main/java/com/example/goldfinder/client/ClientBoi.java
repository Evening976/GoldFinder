package com.example.goldfinder.client;

import java.io.IOException;

public class ClientBoi extends IClient {

  public ClientBoi() {
    super();
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
