package edu.ucsd.flappycow.util;

import edu.ucsd.flappycow.GameActivity;

public class MyToastData {
    public int arg;
    public GameActivity gameActivity;

    public MyToastData(int arg, GameActivity gameActivity) {
        this.arg = arg;
        this.gameActivity = gameActivity;
    }
}