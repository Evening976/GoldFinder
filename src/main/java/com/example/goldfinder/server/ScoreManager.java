package com.example.goldfinder.server;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class ScoreManager {
    public static void addToLeaderboards(TreeMap<Integer, ArrayList<String>> scores, int score, String name) {
        for (int i : scores.keySet()) {
            if (scores.get(i).contains(name)) {
                score += i;
                scores.get(i).remove(name);
                break;
            }
        }
        scores.computeIfAbsent(score, k -> new ArrayList<>()).add(name);
    }

    public static String getLeaderboardsText(TreeMap<Integer, ArrayList<String>> scores, int nScores) {
        StringBuilder scoresString = new StringBuilder().append("Leaderboard:\n");
        System.out.println(scores.toString());

        int count = 0;
        for (int i : scores.keySet()) {
            count++;
            if (scores.lastKey() == i || count == nScores) {
                scoresString.append(scores.get(i)).append(" : ").append(i).append(" gold");
                break;
            }
            scoresString.append(scores.get(i)).append(" : ").append(i).append(" gold\n");
        }
        return scoresString.toString();
    }

    public static TreeMap<Integer, ArrayList<String>> LoadLeaderboards() {
        TreeMap<Integer, ArrayList<String>> scores = new TreeMap<>();
        try {
            File file = new File("leaderboard.txt");
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String[] line = scanner.nextLine().split(" : ");
                int key = Integer.parseInt(line[0]);
                String[] values = line[1].substring(1, line[1].length() - 1).split(", ");
                for (String value : values) {
                    if (scores.containsKey(key)) {
                        scores.get(key).add(value);
                    } else {
                        scores.put(key, new ArrayList<>(List.of(value)));
                    }
                }
            }
            scanner.close();
        } catch (IOException e) {
            return new TreeMap<>();
        }
        return scores;
    }

    public static void SaveLeaderboards(TreeMap<Integer, ArrayList<String>> scores) {
        try {
            FileWriter writer = new FileWriter("leaderboard.txt");
            Set<Map.Entry<Integer, ArrayList<String>>> entrySet = scores.entrySet();
            for (Map.Entry<Integer, ArrayList<String>> entry : entrySet) {
                String score = entry.getKey() + " : " + entry.getValue().toString() + "\n";
                score.replaceAll("\\[", "");
                writer.write(score);
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

