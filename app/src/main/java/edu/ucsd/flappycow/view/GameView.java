package edu.ucsd.flappycow.view;

import android.view.View;

public interface GameView {
    View getView();
    void drawOnce();
    int getWidth();
    int getHeight();
    void draw();
    void notifyMusicPlayer(boolean bool);

    void setupRevive(long UPDATE_INTERVAL);

    void resumeMusicPlayer();
    void startMusicPlayer();
    void pauseMusicPlayer();
}
