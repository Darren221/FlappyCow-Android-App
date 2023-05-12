package edu.ucsd.flappycow;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

import edu.ucsd.flappycow.util.GamePresenterHandlerData;
import edu.ucsd.flappycow.util.IObserver;
import edu.ucsd.flappycow.view.GameView;
import edu.ucsd.flappycow.view.GameViewImpl;

public class Game implements GamePresenter, ActivityModel, IObserver<GamePresenterHandlerData> {
    /** Milliseconds for game timer tick */
    public static final long UPDATE_INTERVAL = 50;        // = 20 FPS

    volatile private boolean paused = false;

    private Timer timer = new Timer();

    private TimerTask timerTask;

    private GameView gameView;

    private GameModel gameModel;

    private ActivityPresenter activityPresenter;

    public Game(Context context, ActivityPresenter activityPresenter) {
        this.activityPresenter = activityPresenter;
        this.gameView = new GameViewImpl(context, this);
        this.gameModel = new SpritePresenter(this);
    }

    public int getPlayerX() {
        return gameModel.getPlayerX();
    }

    public void setPlayerX(int x) {
        gameModel.setPlayerX(x);
    }

    public void setPlayerY(int y) {
        gameModel.setPlayerY(y);
    }

    public boolean playerIsNyanCat() {
        return gameModel.playerIsNyanCat();
    }

    public boolean playerIsDead() {
        return gameModel.playerIsDead();
    }

    @Override
    public void playerOnTap(int viewHeight) {
        gameModel.playerOnTap(viewHeight);
    }

    public boolean pauseButtonIsTouching(int x, int y) {
        return gameModel.pauseButtonIsTouching(x, y);
    }

    public void startTimer() {
        setUpTimerTask();
        timer = new Timer();
        timer.schedule(timerTask, UPDATE_INTERVAL, UPDATE_INTERVAL);
        Log.d("Timer", "Timer Started in Game.java");
    }

    public void stopTimer() {
        Log.d("Timer", "Timer Stopped in Game.java");
        if (timer != null) {
            timer.cancel();
            timer.purge();
            Log.d("Timer", "Timer Cancelled and Purged in Game.java");
        }
        if (timerTask != null) {
            timerTask.cancel();
            Log.d("Timer", "Timer Task Cancelled in Game.java");
        }
    }

    public void setUpTimerTask() {
        stopTimer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                Game.this.run();
            }
        };
    }

    /**
     * content of the timertask
     */
    public void run() {
        checkPasses();
        checkOutOfRange();
        checkCollision();
        createObstacle();
        move();
        gameView.draw();
    }

    /**
     * Draw Tutorial
     */
    public void showTutorial(int viewWidth, int viewHeight, Canvas canvas) {
        gameModel.showTutorial(viewWidth, viewHeight, canvas);
    }

    public void pause() {
        stopTimer();
        paused = true;
    }

    /**
     * Draws everything normal,
     * except the player will only be drawn, when the parameter is true
     *
     * @param drawPlayer
     */
    public void drawCanvas(Canvas canvas, boolean drawPlayer) {
        gameModel.drawCanvas(canvas, drawPlayer);
    }

    /**
     * Let the player fall to the ground
     */
    private void playerDeadFall() {
        gameModel.playerDead(gameView.getHeight());
        do {
            gameModel.playerMove(gameView.getWidth(), gameView.getHeight());
            gameView.draw();
            // sleep
            try {
                Thread.sleep(UPDATE_INTERVAL / 4);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (!gameModel.playerIsTouchingGround(gameView.getHeight()));
    }

    /**
     * Checks whether an obstacle is passed.
     */
    private void checkPasses() {
        gameModel.checkPasses(getPoints(), gameView.getWidth(), getSpeedX());
    }

    /**
     * Checks whether the obstacles or powerUps are out of range and deletes them
     */
    private void checkOutOfRange() {
        gameModel.checkOutOfRange();
    }

    /**
     * Checks collisions and performs the action
     */
    private void checkCollision() {
        if (gameModel.checkObstacleCollision()) {
            gameOver();
        }
        gameModel.checkPowerUpCollision();
        if (gameModel.playerIsTouchingEdge(gameView.getHeight()));
    }

    /**
     * if no obstacle is present a new one is created
     */
    private void createObstacle() {
        gameModel.createObstacle(getSpeedX());
    }

    /**
     * Update sprite movements
     */
    private void move() {
        gameModel.move(gameView.getWidth(), gameView.getHeight(), getSpeedX());
    }

    /**
     * Changes the player to Nyan Cat
     */
    public void changeToNyanCat() {
        gameModel.changeToNyanCat();
        gameView.notifyMusicPlayer(true);
        gameView.startMusicPlayer();
    }

    public void changeToSick() {
        gameModel.changeToSick();
    }

    /**
     * return the speed of the obstacles/cow
     */
    public int getSpeedX() {
        // 16 @ 720x1280 px
        int speedDefault = gameView.getWidth() / 45;

        // 1,2 every 4 points @ 720x1280 px
        int speedIncrease = (int) (gameView.getWidth() / 600f * (getPoints() / 4));

        int speed = speedDefault + speedIncrease;

        return Math.min(speed, 2 * speedDefault);
    }

    /**
     * Let's the player fall down dead, makes sure the runcycle stops
     * and invokes the next method for the dialog and stuff.
     */
    public void gameOver() {
        pause();
        playerDeadFall();
        activityPresenter.gameOver();
    }

    public void revive() {
        // This needs to run another thread, so the dialog can close.
        new Thread(this::setupRevive).start();
    }

    /**
     * Sets the player into startposition
     * Removes obstacles.
     * Let's the character blink a few times.
     */
    private void setupRevive() {
        activityPresenter.hideGameOverDialog();
        gameModel.setupRevive(gameView.getWidth(), gameView.getHeight());
        gameView.setupRevive(UPDATE_INTERVAL);
        paused = false;
        startTimer();
    }

    /**
     * A value for the position and size of the onScreen score Text
     */
    public int getScoreTextMetrics() {
        return (int) (gameView.getHeight() / 21.0f);
        /*/ game.getResources().getDisplayMetrics().density)*/
    }

    public void resumeMusicPlayer() {
        gameView.resumeMusicPlayer();
    }

    public View getView() {
        return gameView.getView();
    }

    public void drawOnce() {
        gameView.drawOnce();
    }

    public void onPause() {
        pause();
        gameView.pauseMusicPlayer();
    }

    private void increasePoints() {
        activityPresenter.increasePoints();
        gameModel.playerUpdateBitmap(getPoints());
    }

    private void increaseCoin() {
        activityPresenter.increaseCoin();
    }

    private void decreasePoints() {
        activityPresenter.decreasePoints();
    }

    public void Handler(String Demand, Object... varargs) {
        switch (Demand) {
            case "increaseCoin": increaseCoin();
                break;
            case "increasePoints": increasePoints();
                break;
            case "decreasePoints": decreasePoints();
                break;
            case "playSound": activityPresenter.playSound(varargs);
                break;
//            case "loadSound": soundPool.load(this, R.raw.coin, 1);
            default:
                break;
        }

    }

    @Override
    public Bitmap getBitmapFromView(Object... varargs) {
        return activityPresenter.getBitmap(varargs);
    }

    @Override
    public int getPositionFromView(Object... varargs) {
        return activityPresenter.getPosition(varargs);
    }

    @Override
    public int loadSoundFromView(Object... varargs) {
        return activityPresenter.loadSound(varargs);
    }

    public int getPoints() {
        return activityPresenter.getPoints();
    }

    public int getCoins() {
        return activityPresenter.getCoins();
    }

    public String getString(int id) {
        return activityPresenter.getString_(id);
    }

    public int getWidth() {
        return gameView.getWidth();
    }

    public int getHeight() {
        return gameView.getHeight();
    }

    @Override
    public void onUpdate(GamePresenterHandlerData data) {
        Handler(data.Demand, data.varargs);
    }
}
