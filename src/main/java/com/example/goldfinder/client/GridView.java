package com.example.goldfinder.client;

import com.example.utils.Player;
import com.example.utils.PlayerColor;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;

public class GridView {
    Canvas canvas;
    int columnCount, rowCount;
    boolean[][] goldAt, vWall, hWall;


    public GridView(Canvas canvas, int columnCount, int rowCount) {
        this.canvas = canvas;
        this.columnCount = columnCount;
        this.rowCount = rowCount;
        goldAt = new boolean[columnCount][rowCount];
        vWall = new boolean[columnCount+1][rowCount];
        hWall = new boolean[columnCount][rowCount+1];
    }

    public void repaint(int hParallax, int vParallax){
        canvas.getGraphicsContext2D().clearRect(0,0,canvas.getWidth(),canvas.getHeight());
        for(int column =0; column<columnCount;column++)
            for(int row=0;row<rowCount;row++)
                if(goldAt[column][row]) {
                    canvas.getGraphicsContext2D().setFill(Color.YELLOW);
                    canvas.getGraphicsContext2D().fillOval((column + hParallax) * cellWidth(), (row + vParallax) * cellHeight(), cellWidth(), cellHeight());
                }
        canvas.getGraphicsContext2D().setStroke(Color.WHITE);
        for(int column =0; column<columnCount;column++)
            for(int row=0;row<rowCount;row++){
                    if(vWall[column][row])
                        canvas.getGraphicsContext2D().strokeLine((column + hParallax) * cellWidth(), (row + vParallax) * cellHeight(),(column + hParallax) * cellWidth(), (vParallax + row +1) * cellHeight());
                if(hWall[column][row])
                    canvas.getGraphicsContext2D().strokeLine((column + hParallax) * cellWidth(), (row + vParallax) * cellHeight(),(hParallax + column +1) * cellWidth(), (row + vParallax) * cellHeight());
            }

    }

    public void setGoldAt(int col, int row){
        if(col >= 0 && col < columnCount && row >= 0 && row < rowCount)
            goldAt[col][row] = true;
    }

    public void setVWall(int col, int row){
        if(col >= 0 && col < columnCount+1 && row >= 0 && row < rowCount)
            vWall[col][row] = true;
    }

    public void setHWall(int col, int row){
        if(col >= 0 && col < columnCount && row >= 0 && row < rowCount+1)
            hWall[col][row] = true;
    }

    public void removeGoldAt(int col, int row){
        if(col >= 0 && col < columnCount && row >= 0 && row < rowCount && goldAt[col][row])
            goldAt[col][row] = false;
    }

    private int cellWidth(){
        //System.out.println(canvas.getWidth()/columnCount);
        return (int) (canvas.getWidth()/columnCount); }
    private int cellHeight(){
        //System.out.println(canvas.getHeight()/rowCount);
        return (int) (canvas.getHeight()/rowCount); }

    public void paintPlayer(int column, int row, int playerIndex) {
        canvas.getGraphicsContext2D().setFill(Color.BLUE);
        canvas.getGraphicsContext2D().fillRect(column*cellWidth(),row*cellHeight(),cellWidth(),cellHeight());
    }
}
