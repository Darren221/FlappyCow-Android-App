package edu.ucsd.flappycow;

import android.content.SharedPreferences;
import android.graphics.Bitmap;

public interface ActivityPresenter {
    // for model
    void increasePoints();
    void decreasePoints();
    void increaseCoin();
    void decreaseCoin();
    void decreaseCoins(int num);
    void saveCoins();
    void gameOver();
    void revive();

    void hideGameOverDialog();
    void playSound(Object... varargs);
    int getCoins();
    int getPoints();
    String getString_(int id);
    Bitmap getBitmap(Object... varargs);
    int getPosition(Object... varargs);
    int loadSound(Object... varargs);

    // for view
    int getNumRevive();
    void saveAccomplishment();
    void finishActivity();
    SharedPreferences getSharedPreferences_(String str);
    boolean achieveBronze();
    boolean achieveSilver();
    boolean achieveGold();
    void setMedal(int id);
}
