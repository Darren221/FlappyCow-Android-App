package edu.ucsd.flappycow;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import edu.ucsd.flappycow.util.GamePresenterHandlerData;
import edu.ucsd.flappycow.util.IObserver;

public interface GamePresenter extends IObserver<GamePresenterHandlerData> {
    // for view
    int getPlayerX();
    boolean playerIsNyanCat();
    boolean playerIsDead();
    void playerOnTap(int viewHeight);
    void setPlayerX(int x);
    void setPlayerY(int y);
    boolean pauseButtonIsTouching(int x, int y);
    void startTimer();
    void stopTimer();
    void showTutorial(int viewWidth, int viewHeight, Canvas canvas);
    void drawCanvas(Canvas canvas, boolean drawPlayer);
    int getPoints();
    int getCoins();
    String getString(int id);

    // for model
    void Handler(String Request, Object... varargs);
    Bitmap getBitmapFromView(Object... varargs);
    int getPositionFromView(Object... varargs);
    int loadSoundFromView(Object... varargs);

    int getWidth();
    int getHeight();
}
