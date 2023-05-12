/**
 * The tutorial that says you should tap
 *
 * @author Lars Harmsen
 * Copyright (c) <2014> <Lars Harmsen - Quchen>
 */

package edu.ucsd.flappycow.model.sprites;

import android.graphics.Bitmap;

public class DrawableTutorial extends Sprite {

    /**
     * Static bitmap to reduce memory usage.
     */
    public static Bitmap globalBitmap;

    public DrawableTutorial(Bitmap bitmap) {
        super();
        if (globalBitmap == null) {
            globalBitmap = bitmap;
        }
        this.bitmap = globalBitmap;
        this.width = this.bitmap.getWidth();
        this.height = this.bitmap.getHeight();
    }

}
