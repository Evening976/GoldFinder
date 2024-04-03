package com.example.goldfinder.client;


import com.example.utils.ConnectionMode;
import com.example.utils.GameType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AppBot {

    ExecutorService executor;

    List<Bot> bots;

    public AppBot(int botCount, ConnectionMode connectionMode, GameType gameType){
        executor = Executors.newFixedThreadPool(botCount);
        bots = new ArrayList<>();
        for (int i = 0; i < botCount; i++) {
            bots.add(new Bot(connectionMode, gameType));
        }

    }
    private void initBot(){
        for (Bot bot : bots) {
            executor.execute(() -> {
                try {
                    bot.startBot();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public static void main(String[] args) {
        AppBot appBot = new AppBot(3, ConnectionMode.UDP, GameType.COPS_AND_ROBBERS);
        appBot.initBot();
    }


}
