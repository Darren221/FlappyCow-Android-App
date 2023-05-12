/**
 * The template for every game object
 *
 * @author Lars Harmsen
 * Copyright (c) <2014> <Lars Harmsen - Quchen>
 */

package edu.ucsd.flappycow.model.sprites;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public abstract class Sprite {

    /** The bitmaps that holds the frames that should be drawn */
    protected Bitmap bitmap;

    /** Height and width of one frame of the bitmap */
    public int height;
    protected int width;

    /** x and y coordinates on the canvas */
    protected int x, y;

    /** Horizontal and vertical speed of the sprite */
    protected float speedX, speedY;

    /** The source frame of the bitmap that should be drawn */
    protected Rect src;

    /** The destination area that the frame should be drawn to */
    protected Rect dst;

    /** Coordinates of the frame in the spritesheet */
    protected byte col;
    public byte row;

    /** Number of columns the sprite has */
    protected byte colNr = 1;

    /** How long a frame should be displayed */
    protected short frameTime;

    /**
     * Counter for the frames
     * Cycling through the columns
     */
    protected short frameTimeCounter;

    /** The context */
    // protected GamePresenter gamePresenter;

    public Sprite() {
        frameTime = 1;
        src = new Rect();
        dst = new Rect();
    }

    /**
     * Draws the frame of the bitmap specified by col and row
     * at the position given by x and y
     * @param canvas Canvas that should be drawn on
     */

    public void draw(Canvas canvas) {
        src.set(col * width, row * height, (col + 1) * width, (row + 1) * height);
        dst.set(x, y, x + width, y + height);
        canvas.drawBitmap(bitmap, src, dst, null);
    }

    /**
     * Modifies the x and y coordinates according to the speedX and speedY value
     */
    public void move() {
        // changeToNextFrame();
        // Its more efficient if only the classes that need this implement it in their move method.
        x += speedX;
        y += speedY;
    }

    /**
     * Changes the frame by cycling through the columns.
     */
    public void changeToNextFrame() {
        this.frameTimeCounter++;
        if (this.frameTimeCounter >= this.frameTime) {
            this.col = (byte) ((this.col + 1) % this.colNr);
            this.frameTimeCounter = 0;
        }
    }

    /**
     * Checks whether the point specified by the x and y coordinates is touching the sprite.
     * @param x
     * @param y
     * @return
     */
    public boolean isTouching(int x, int y) {
        return (x > this.x && x < this.x + width
            && y > this.y && y < this.y + height);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public float getSpeedX() {
        return speedX;
    }

    public void setSpeedX(float speedX) {
        this.speedX = speedX;
    }

    public float getSpeedY() {
        return speedY;
    }

    public void setSpeedY(float speedY) {
        this.speedY = speedY;
    }

    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }

}
