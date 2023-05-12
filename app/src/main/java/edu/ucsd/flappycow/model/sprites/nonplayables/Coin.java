/**
 * A Coin
 *
 * @author Lars Harmsen
 * Copyright (c) <2014> <Lars Harmsen - Quchen>
 */

package edu.ucsd.flappycow.model.sprites.nonplayables;

import android.graphics.Bitmap;

import edu.ucsd.flappycow.util.GamePresenterHandlerData;
import edu.ucsd.flappycow.util.Util;

public class Coin extends PowerUp {
    /**
     * Static bitmap to reduce memory usage.
     */
    public static Bitmap globalBitmap;
    private static int sound = -1;

    public Coin(Bitmap bitmap, int loadSound, int viewWidth, int viewSpeedX, int viewPlayerX) {
        super(viewWidth, viewSpeedX, viewPlayerX);
        if (globalBitmap == null) {
            //globalBitmap = Util.getScaledBitmapAlpha8(gameActivity, R.drawable.coin);
            globalBitmap = bitmap;
        }
        this.bitmap = globalBitmap;
        this.width = this.bitmap.getWidth() / (colNr = 12);
        this.height = this.bitmap.getHeight();
        this.frameTime = 1;
        if (sound == -1) {
            //sound = GameActivity.soundPool.load(gameActivity, R.raw.coin, 1);
            sound = loadSound;
        }
    }

    /**
     * When eaten the player will turn into nyan cat.
     */
    public int onCollision() {
        playSound();
        GamePresenterHandlerData data = new GamePresenterHandlerData("increaseCoin");
        sendHandlerUpdate(data);
        return 0;

    }

    private void playSound() {
        GamePresenterHandlerData data = new GamePresenterHandlerData("playSound", sound, Util.volume, Util.volume, 0, 0, 1.f);
        sendHandlerUpdate(data);
    }

    @Override
    public void move() {
        changeToNextFrame();
        super.move();
    }
}
