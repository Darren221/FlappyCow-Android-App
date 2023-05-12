/**
 * The cow that is controlled by the player
 *
 * @author Lars Harmsen
 * Copyright (c) <2014> <Lars Harmsen - Quchen>
 */

package edu.ucsd.flappycow.model.sprites.playables;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import edu.ucsd.flappycow.model.sprites.Accessory;
import edu.ucsd.flappycow.util.GamePresenterData;
import edu.ucsd.flappycow.util.GamePresenterHandlerData;
import edu.ucsd.flappycow.util.Util;

public class Cow extends PlayableCharacter {

    private static final int POINTS_TO_SIR = 23;
    private static final int POINTS_TO_COOL = 35;

    /** Static bitmap to reduce memory usage. */
    public static Bitmap globalBitmap;

    /** The moo sound */
    private static int sound = -1;

    /** sunglasses, hats and stuff */
    private Accessory accessory;

    public Cow(Bitmap bitmap, GamePresenterData data, Accessory accessory1) {
        super();
        if (globalBitmap == null) {
            globalBitmap = bitmap;
            //globalBitmap = Util.getScaledBitmapAlpha8(gameActivity, R.drawable.cow);
        }
        this.bitmap = globalBitmap;
        this.width = this.bitmap.getWidth() / (colNr = 8);    // The image has 8 frames in a row
        this.height = this.bitmap.getHeight() / 4;            // and 4 in a column
        this.frameTime = 3;        // the frame will change every 3 runs
        this.y = data.height;    // Startposition in in the middle of the screen

        if (sound == -1) {
            sound = data.sound;
            //sound = GameActivity.soundPool.load(gameActivity, R.raw.cow, 1);
        }

        this.accessory = accessory1;
    }

    private void playSound() {
        GamePresenterHandlerData data = new GamePresenterHandlerData("playSound", sound, Util.volume, Util.volume, 0, 0, 1.0f);
        sendHandlerUpdate(data);
        // GameActivity.soundPool.play(sound, Util.volume, Util.volume, 0, 0, 1);
    }

    @Override
    public void onTap(int viewHeight) {
        super.onTap(viewHeight);
        playSound();
    }

    /**
     * Calls super.move
     * and manages the frames. (flattering cape)
     */

    public void move(int viewWidth, int viewHeight) {
        changeToNextFrame();
        super.move(viewWidth, viewHeight);

        // manage frames
        if (row != 3) {
            // not dead
            if (speedY > getTabSpeed(viewHeight) / 3 && speedY < getMaxSpeed(viewHeight) * 1 / 3) {
                row = 0;
            } else if (speedY > 0) {
                row = 1;
            } else {
                row = 2;
            }
        }

        if (this.accessory != null) {
            this.accessory.moveTo(this.x, this.y);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (this.accessory != null && !isDead) {
            this.accessory.draw(canvas);
        }
    }

    /**
     * Calls super.dead
     * And changes the frame to a dead cow -.-
     */
    @Override
    public void dead(int viewHeight) {
        this.row = 3;
        this.frameTime = 3;
        super.dead(viewHeight);
    }

    @Override
    public void revive(Bitmap bitmap) {
        super.revive(bitmap);
        this.accessory.setBitmap(bitmap);
    }

    @Override
    public void upgradeBitmap(Bitmap bitmap_sir, Bitmap bitmap_cool, int points) {
        super.upgradeBitmap(bitmap_sir, bitmap_cool, points);
        if (points == POINTS_TO_SIR) {
            //this.accessory.setBitmap(Util.getScaledBitmapAlpha8(gameActivity, R.drawable.accessory_sir));
            this.accessory.setBitmap(bitmap_sir);
        } else if (points == POINTS_TO_COOL) {
            //this.accessory.setBitmap(Util.getScaledBitmapAlpha8(gameActivity, R.drawable.accessory_sunglasses));
            this.accessory.setBitmap(bitmap_cool);
        }
    }

    @Override
    public void wearMask(Bitmap bitmap) {
        super.wearMask(bitmap);
        //this.accessory.setBitmap(Util.getScaledBitmapAlpha8(gameActivity, R.drawable.mask));
        this.accessory.setBitmap(bitmap);
    }
}
