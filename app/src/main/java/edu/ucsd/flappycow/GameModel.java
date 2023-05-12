package edu.ucsd.flappycow;

import android.graphics.Canvas;

public interface GameModel {
    int getPlayerX();
    void setPlayerX(int x);
    void setPlayerY(int y);
    boolean playerIsNyanCat();
    boolean playerIsDead();
    void playerDead(int viewHeight);
    void playerOnTap(int height);
    void playerMove(int width, int height);
    boolean playerIsTouchingEdge(int viewHeight);
    boolean playerIsTouchingGround(int viewHeight);
    void playerUpdateBitmap(int points);
    boolean pauseButtonIsTouching(int x, int y);
    void setPauseButtonX(int viewWidth);
    void drawTutorial(int viewWidth, int viewHeight, Canvas canvas);
    void showTutorial(int viewWidth, int viewHeight, Canvas canvas);
    void drawCanvas(Canvas canvas, boolean drawPlayer);
    void checkPasses(int points, int viewWidth, int speedX);
    void createPowerUp(int points, int viewWidth, int speedX);
    void checkOutOfRange();
    boolean checkObstacleCollision();
    void checkPowerUpCollision();
    void createObstacle(int speedX);
    void move(int viewWidth, int viewHeight, int speedX);
    void changeToNyanCat();
    void changeToSick();
    void setupRevive(int viewHeight, int viewWidth);
}
