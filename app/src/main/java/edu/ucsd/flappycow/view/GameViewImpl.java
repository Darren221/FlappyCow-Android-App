/**
 * GameView
 * Probably the most important class for the game
 *
 * @author Lars Harmsen
 * Copyright (c) <2014> <Lars Harmsen - Quchen>
 */

package edu.ucsd.flappycow.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import edu.ucsd.flappycow.GamePresenter;
import edu.ucsd.flappycow.R;
import edu.ucsd.flappycow.util.ButtonHandler;
import edu.ucsd.flappycow.util.ButtonModel;
import edu.ucsd.flappycow.util.ButtonView;

public class GameViewImpl extends SurfaceView implements GameView, ButtonModel {
    /** The surfaceholder needed for the canvas drawing */
    private SurfaceHolder holder;

    private MusicPlayer musicPlayer;
    private boolean tutorialIsShown = true;

    private GamePresenter gamePresenter;

    private ButtonView buttonView;

    public GameViewImpl(Context context, GamePresenter gamePresenter) {
        super(context);
        this.gamePresenter = gamePresenter;
        setFocusable(true);
        buttonView = new ButtonHandler(this);
        musicPlayer = new MusicPlayer(context);
        holder = getHolder();
    }

    public View getView() {
        return this;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.performClick();
        if (event.getAction() == MotionEvent.ACTION_DOWN && !gamePresenter.playerIsDead()) {
            if (tutorialIsShown) {
                startTimer();
            }
            tutorialIsShown = false;
            if (gamePresenter.pauseButtonIsTouching((int) event.getX(), (int) event.getY())) {
                buttonView.onPress();
            } else {
                gamePresenter.playerOnTap(this.getHeight());
            }
        }
        return true;
    }

    private Canvas getCanvas() {
        Canvas canvas;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            canvas = holder.lockHardwareCanvas();
        } else {
            canvas = holder.lockCanvas();
        }

        return canvas;
    }

    /**
     * Draw Tutorial
     */
    public void showTutorial() {
        while (!holder.getSurface().isValid()) {
            /*wait*/
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Canvas canvas = getCanvas();
        drawCanvas(canvas, true);
        gamePresenter.showTutorial(this.getWidth(), this.getHeight(), canvas);
        holder.unlockCanvasAndPost(canvas);
    }

    public void drawOnce() {
        (new Thread(new Runnable() {
            @Override
            public void run() {
                if (tutorialIsShown) {
                    showTutorial();
                } else {
                    draw();
                }
            }
        })).start();
    }

    /**
     * Draws all gameobjects on the surface
     */
    public void draw() {
        while (!holder.getSurface().isValid()) {
            /*wait*/
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Canvas canvas = getCanvas();

        drawCanvas(canvas, true);

        holder.unlockCanvasAndPost(canvas);
    }

    /**
     * Draws everything normal,
     * except the player will only be drawn, when the parameter is true
     *
     * @param drawPlayer
     */
    private void drawCanvas(Canvas canvas, boolean drawPlayer) {
        gamePresenter.drawCanvas(canvas, drawPlayer);

        // Score Text
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(getScoreTextMetrics());
        canvas.drawText(gamePresenter.getString(R.string.onscreen_score_text) + " " + gamePresenter.getPoints()
                + " / " + gamePresenter.getString(R.string.onscreen_coin_text) + " " + gamePresenter.getCoins(),
            0, getScoreTextMetrics(), paint);
    }

    /**
     * Update sprite movements
     */

    /**
     * Changes the player to Nyan Cat
     */
    public void notifyMusicPlayer(boolean data) {
        musicPlayer.onUpdate(data);
    }

    /**
     * Sets the player into startposition
     * Removes obstacles.
     * Let's the character blink a few times.
     */
    public void setupRevive(long UPDATE_INTERVAL) {
        for (int i = 0; i < 6; ++i) {
            while (!holder.getSurface().isValid()) {/*wait*/}
            Canvas canvas = getCanvas();
            drawCanvas(canvas, i % 2 == 0);
            holder.unlockCanvasAndPost(canvas);
            // sleep
            try {
                Thread.sleep(UPDATE_INTERVAL * 6);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void resumeMusicPlayer() {
        musicPlayer.onResume();
    }

    public void startMusicPlayer() {
        musicPlayer.start();
    }

    public void pauseMusicPlayer() {
        musicPlayer.onPause();
    }

    public void startTimer() {
        gamePresenter.startTimer();
    }

    public void stopTimer() {
        gamePresenter.stopTimer();
    }

    /**
     * A value for the position and size of the onScreen score Text
     */
    public int getScoreTextMetrics() {
        return (int) (this.getHeight() / 21.0f);
        /*/ game.getResources().getDisplayMetrics().density)*/
    }
}
