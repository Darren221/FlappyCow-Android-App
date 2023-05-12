package edu.ucsd.flappycow.model.sprites;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class GroundLayer extends Sprite {

    public Bitmap globalBitmap;

    /**
     * Height of the ground relative to the height of the bitmap
     */
    public static final float FOREGROUND_HEIGHT = (1f * /*45*/ 35) / 720;

    public GroundLayer(Bitmap bitmap) {
        super();
        if (globalBitmap == null) {
            globalBitmap = bitmap;
        }
        this.bitmap = bitmap;
    }

    /**
     * Draws the bitmap to the Canvas.
     * The height of the bitmap will be scaled to the height of the canvas.
     * When the bitmap is scrolled to far to the left, so it won't cover the whole screen,
     * the bitmap will be drawn another time behind the first one.
     */
    @Override
    public void draw(Canvas canvas) {
        double factor = (1.0 * canvas.getHeight()) / bitmap.getHeight();

        if (-x > bitmap.getWidth()) {
            // The first bitmap is completely out of the screen
            x += bitmap.getWidth();
        }

        int endBitmap = Math.min(-x + (int) (canvas.getWidth() / factor), bitmap.getWidth());
        int endCanvas = (int) ((endBitmap + x) * factor) + 1;
        src.set(-x, 0, endBitmap, bitmap.getHeight());
        dst.set(0, 0, endCanvas, canvas.getHeight());
        canvas.drawBitmap(this.bitmap, src, dst, null);

        if (endBitmap == bitmap.getWidth()) {
            // draw second bitmap
            src.set(0, 0, (int) (canvas.getWidth() / factor), bitmap.getHeight());
            dst.set(endCanvas, 0, endCanvas + canvas.getWidth(), canvas.getHeight());
            canvas.drawBitmap(this.bitmap, src, dst, null);
        }
    }
}
