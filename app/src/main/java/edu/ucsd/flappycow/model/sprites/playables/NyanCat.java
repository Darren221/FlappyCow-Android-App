/**
 * Nyan Cat character
 *
 * @author Lars Harmsen
 * Copyright (c) <2014> <Lars Harmsen - Quchen>
 * <p>
 * Nyan Cat was drawn by Christopher Torres and momo momo remixed the music by daniwell
 */

package edu.ucsd.flappycow.model.sprites.playables;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import edu.ucsd.flappycow.model.sprites.DrawableRainbow;

public class NyanCat extends PlayableCharacter {

    /** Static bitmap to reduce memory usage */
    public static Bitmap globalBitmap;

    /** The rainbow tail behind the cat */
    private DrawableRainbow drawableRainbow;

    public NyanCat(Bitmap bitmap, int y, DrawableRainbow drawableRainbow1) {
        super();
        if (globalBitmap == null) {
            globalBitmap = bitmap;
        }
        this.bitmap = globalBitmap;
        this.width = this.bitmap.getWidth();
        this.height = this.bitmap.getHeight() / 2;
        this.y = y;

        this.drawableRainbow = drawableRainbow1;
    }

    /**
     * Moves itself via super.move
     * and moves the rainbow and manages its frames
     */

    public void move(int viewWidth, int viewHeight) {
        super.move(viewWidth, viewHeight);

        if (drawableRainbow != null) {
            manageRainbowMovement(viewHeight);
        }
    }

    private void manageRainbowMovement(int viewHeight) {
        drawableRainbow.setY(this.y);        // nyan cat and rainbow bitmap have the same height
        drawableRainbow.setX(this.x - drawableRainbow.getWidth());
        changeToNextFrame();
        drawableRainbow.move();


        // manage frames of the rainbow
        if (speedY > getTabSpeed(viewHeight) / 3 && speedY < getMaxSpeed(viewHeight) * 1 / 3) {
            drawableRainbow.row = 0;
        } else if (speedY > 0) {
            drawableRainbow.row = 1;
        } else {
            drawableRainbow.row = 2;
        }
    }

    /**
     * Draws itself via super.draw
     * and the rainbow.
     */
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (drawableRainbow != null && !isDead) {
            drawableRainbow.draw(canvas);
        }
    }

    /**
     * Calls super.dead,
     * removes the rainbow tail
     * and set the bitmapframe to a dead cat -.-
     */
    @Override
    public void dead(int viewHeight) {
        super.dead(viewHeight);
        this.row = 1;

        // Maybe an explosion
    }

    public void revive(Bitmap bitmap, int viewHeight) {
        super.revive(bitmap);
        manageRainbowMovement(viewHeight);
    }

}
