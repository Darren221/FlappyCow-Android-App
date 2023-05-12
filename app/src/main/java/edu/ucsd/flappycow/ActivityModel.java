package edu.ucsd.flappycow;

import android.view.View;

public interface ActivityModel {
    View getView();
    void drawOnce();
    void onPause();
    void resumeMusicPlayer();
    void revive();
    void gameOver();
}
