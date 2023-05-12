/**
 * A yummy toast
 *
 * @author Lars Harmsen
 * Copyright (c) <2014> <Lars Harmsen - Quchen>
 */

package edu.ucsd.flappycow.model.sprites.nonplayables;

import android.graphics.Bitmap;

public class Toast extends PowerUp {

    /**
     * Static bitmap to reduce memory usage.
     */
    public static Bitmap globalBitmap;

    public static final int POINTS_TO_TOAST = 42;

    public Toast(Bitmap bitmap, int viewWidth, int viewSpeedX, int viewPlayerX) {
        super(viewWidth, viewSpeedX, viewPlayerX);
        if (globalBitmap == null) {
            globalBitmap = bitmap;
        }
        this.bitmap = globalBitmap;
        this.width = this.bitmap.getWidth();
        this.height = this.bitmap.getHeight();
    }

    /**
     * When eaten the player will turn into nyan cat.
     */
    public int onCollision() {
        return 1;
        //view.changeToNyanCat();
    }


}
