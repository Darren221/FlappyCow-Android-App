package edu.ucsd.flappycow.model.sprites.nonplayables;

import android.graphics.Bitmap;

public class ObstacleObject extends NonplayableObject {

    /**
     * Static bitmap to reduce memory usage.
     */
    public Bitmap globalBitmap;

    public ObstacleObject(Bitmap bitmap, int viewPlayerX) {
        super(viewPlayerX);
        if (globalBitmap == null) {
            globalBitmap = bitmap;
        }
        this.bitmap = globalBitmap;
        this.width = this.bitmap.getWidth();
        this.height = this.bitmap.getHeight();
    }

    /**
     * Sets the position
     * @param x
     * @param y
     */
    public void init(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
