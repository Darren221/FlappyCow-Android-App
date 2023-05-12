/**
 * The pauseButton
 *
 * @author Lars Harmsen
 * Copyright (c) <2014> <Lars Harmsen - Quchen>
 */

package edu.ucsd.flappycow.model.sprites;

import android.graphics.Bitmap;

public class DrawablePauseButton extends Sprite {

    /**
     * Static bitmap to reduce memory usage.
     */
    public static Bitmap globalBitmap;
    public DrawablePauseButton(Bitmap bitmap) {
        super();
        if (globalBitmap == null) {
            this.bitmap = bitmap;
        }
        this.width = this.bitmap.getWidth();
        this.height = this.bitmap.getHeight();
        this.y = 0;
    }
}