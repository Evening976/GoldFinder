package com.example.goldfinder.server;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class ScoreManager {
    public static synchronized void addToLeaderboards(TreeMap<Integer, ArrayList<String>> scores, int score, String name) {
        for (int i : scores.keySet()) {
            if (scores.get(i).contains(name)) {
                System.out.println("Removing " + name + " from " + i + " score");
                score += i;
                scores.get(i).remove(name);
                if(scores.get(i).isEmpty()) scores.remove(i);
                break;
            }
        }
        scores.computeIfAbsent(score, k -> new ArrayList<>()).add(name);
    }

    public static synchronized String getLeaderboardsText(TreeMap<Integer, ArrayList<String>> scores, int nScores) {
        StringBuilder scoresString = new StringBuilder();
        int count = 0;
        for (int i : scores.keySet()) {
            for (String name : scores.get(i)) {
                count++;
                if(count > nScores) break;
                String cleanScore = name.replaceAll("\\[", "").replaceAll("]", "");
                if (scores.get(i).size() == i || count == nScores) {
                    System.out.println("Breaking at " + i + " score");
                    scoresString.append("SCORE:").append(i).append(":").append(cleanScore).append("\n");
                    break;
                }
                scoresString.append("SCORE:").append(i).append(":").append(cleanScore).append("\n");
            }
        }
        return scoresString.toString();
    }

    public static synchronized TreeMap<Integer, ArrayList<String>> LoadLeaderboards() {
        TreeMap<Integer, ArrayList<String>> scores = new TreeMap<>(Comparator.reverseOrder());
        try {
            File file = new File("leaderboard.txt");
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String[] line = scanner.nextLine().split(" : ");
                int key = Integer.parseInt(line[0]);
                String[] values = line[1].substring(1, line[1].length() - 1).split(", ");
                for (String value : values) {
                    addToLeaderboards(scores, key, value);
                }
            }
            scanner.close();
        } catch (IOException e) {
            return new TreeMap<>();
        }
        return scores;
    }

    public static synchronized void SaveLeaderboards(TreeMap<Integer, ArrayList<String>> scores) {
        try {
            FileWriter writer = new FileWriter("leaderboard.txt");
            Set<Map.Entry<Integer, ArrayList<String>>> entrySet = scores.entrySet();
            for (Map.Entry<Integer, ArrayList<String>> entry : entrySet) {
                if(entry.getValue().isEmpty()) continue;
                String score = entry.getKey() + " : " + entry.getValue() + "\n";
                score.replaceAll("\\[", "");
                writer.write(score);
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

