package edu.ucsd.flappycow.model.sprites.nonplayables;

import android.graphics.Bitmap;

import edu.ucsd.flappycow.util.GamePresenterHandlerData;

public class Virus extends PowerUp {
    public static Bitmap globalBitmap;

    public Virus(Bitmap bitmap, int viewWidth, int viewSpeedX, int viewPlayerX) {
        super(viewWidth, viewSpeedX, viewPlayerX);
        if (globalBitmap == null) {
            globalBitmap = bitmap;
        }
        this.bitmap = globalBitmap;
        this.width = this.bitmap.getWidth();
        this.height = this.bitmap.getHeight();
    }

    /**
     * When eaten the player will become infected.
     */
    public int onCollision() {
        GamePresenterHandlerData data = new GamePresenterHandlerData("decreasePoints");
        sendHandlerUpdate(data);
        return 2;
    }
}
