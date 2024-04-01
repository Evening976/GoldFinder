package com.example.goldfinder.server;

import java.io.Serializable;
import java.util.Random;

public class Grid implements Serializable {
    boolean[][] hWall, vWall, gold;
    int columnCount, rowCount;
    private final Random random;
    private int goldCount;

    public Grid(int columnCount, int rowCount, Random random) {
        this.columnCount = columnCount;
        this.rowCount = rowCount;
        this.random = random;
        this.goldCount = 0;

        RandomMaze randomMaze = new RandomMaze(columnCount,rowCount,.1, random);
        randomMaze.generate();
        hWall = randomMaze.hWall;
        vWall = randomMaze.vWall;

        gold = new boolean [columnCount][rowCount];
        generateGold(3);
    }


    public boolean leftWall(int column, int row){
        if (column==0) return true;
        return vWall[column][row];
    }

    public boolean rightWall(int column, int row){
        if (column==columnCount-1) return true;
        return vWall[column+1][row];
    }

    public boolean upWall(int column, int row){
        if (row==0) return true;
        return hWall[column][row];
    }

    public boolean downWall(int column, int row){
        if (row==rowCount-1) return true;
        return hWall[column][row+1];
    }

    public boolean hasGold(int column, int row){
        return gold[column][row];
    }
    private void generateGold(double v) {
        for(int column=0; column<columnCount; column++) {
            for (int row = 0; row < rowCount; row++) {
                gold[column][row] = (random.nextInt(10) < v);
                if(gold[column][row]) goldCount++;
            }
        }
    }

    public void removeGold(int column, int row){
        if(gold[column][row]) goldCount--;
        gold[column][row] = false;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public int getRowCount() {
        return rowCount;
    }

    public int getGoldCount(){return goldCount;}
}
