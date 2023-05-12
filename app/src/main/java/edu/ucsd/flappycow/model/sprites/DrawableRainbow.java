/**
 * Rainbow tail for the nyan cat
 *
 * @author Lars Harmsen
 * Copyright (c) <2014> <Lars Harmsen - Quchen>
 */

package edu.ucsd.flappycow.model.sprites;

import android.graphics.Bitmap;

public class DrawableRainbow extends Sprite {

    /**
     * Static bitmap to reduce memory usage.
     */
    public static Bitmap globalBitmap;

    public DrawableRainbow(Bitmap bitmap) {
        super();
        if (globalBitmap == null) {
            globalBitmap = bitmap;
        }
        this.bitmap = globalBitmap;
        this.width = this.bitmap.getWidth() / (colNr = 4);
        this.height = this.bitmap.getHeight() / 3;
    }
}
