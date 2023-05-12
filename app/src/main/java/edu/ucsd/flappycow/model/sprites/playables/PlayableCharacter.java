/**
 * The SuperClass of every character that is controlled by the player
 *
 * @author Lars Harmsen
 * Copyright (c) <2014> <Lars Harmsen - Quchen>
 */

package edu.ucsd.flappycow.model.sprites.playables;

import android.graphics.Bitmap;

import edu.ucsd.flappycow.model.sprites.GroundLayer;
import edu.ucsd.flappycow.model.sprites.Sprite;
import edu.ucsd.flappycow.util.GamePresenterHandlerData;
import edu.ucsd.flappycow.util.IObserver;
import edu.ucsd.flappycow.util.ISubjectImpl;
import edu.ucsd.flappycow.util.SubjectImpl;

public abstract class PlayableCharacter extends Sprite implements PlayableBehavior {
    protected boolean isDead = false;

    boolean calledRegister = false;

    protected ISubjectImpl<GamePresenterHandlerData> gpHandler = new SubjectImpl<>();

    public PlayableCharacter() {
        super();
        move();
    }

    /**
     * Calls super.move
     * Moves the character to 1/6 of the horizontal screen
     * Manages the speed changes -> Falling
     */
    public void move(int viewWidth, int viewHeight) {
        this.x = viewWidth / 6;

        if (speedY < 0) {
            // The character is moving up
            speedY = speedY * 2 / 3 + getSpeedTimeDecrease(viewHeight) / 2;
        } else {
            // the character is moving down
            this.speedY += getSpeedTimeDecrease(viewHeight);
        }

        if (this.speedY > getMaxSpeed(viewHeight)) {
            // speed limit
            this.speedY = getMaxSpeed(viewHeight);
        }

        super.move();
    }

    /**
     * Checks whether the sprite is touching the ground or the sky.
     * @return
     */
    public boolean isTouchingEdge(int viewHeight) {
        return isTouchingGround(viewHeight) || isTouchingSky();
    }

    /**
     * Checks whether the sprite is touching the ground.
     * @return
     */
    public boolean isTouchingGround(int viewHeight) {
        return this.y + this.height > viewHeight - viewHeight * GroundLayer.FOREGROUND_HEIGHT;
    }

    /**
     * Checks whether the sprite is touching the sky.
     * @return
     */
    public boolean isTouchingSky() {
        return this.y < 0;
    }

    /**
     * A dead character falls slowly to the ground.
     */
    public void dead(int viewHeight) {
        this.isDead = true;
        this.speedY = getMaxSpeed(viewHeight) / 2;
    }

    /**
     * Let the character flap up.
     */
    public void onTap(int viewHeight) {
        this.speedY = getTabSpeed(viewHeight);
        this.y += getPosTabIncrease(viewHeight);
    }

    /**
     * Falling speed limit
     *
     * @return
     */
    protected float getMaxSpeed(int viewHeight) {
        // 25 @ 720x1280 px
        return viewHeight / 51.2f;
    }

    /**
     * Every run cycle the speed towards the ground will increase.
     *
     * @return
     */
    protected float getSpeedTimeDecrease(int viewHeight) {
        // 4 @ 720x1280 px
        return viewHeight / 320;
    }

    /**
     * The character gets this speed when taped.
     *
     * @return
     */
    protected float getTabSpeed(int viewHeight) {
        // -80 @ 720x1280 px
        return -viewHeight / 16f;
    }

    /**
     * The character jumps up the pixel height of this value.
     *
     * @return
     */
    protected int getPosTabIncrease(int viewHeight) {
        // -12 @ 720x1280 px
        return -viewHeight / 100;
    }

    public void revive(Bitmap bitmap) {
        this.isDead = false;
        this.row = 0;
    }

    public void upgradeBitmap(Bitmap bitmap_sir, Bitmap bitmap_cool, int points) {
        // Change bitmap, maybe when a certain amount of point is reached.
    }

    public void wearMask(Bitmap bitmap) {
        // Change bitmap to have a mask.
    }

    public boolean isDead() {
        return this.isDead;
    }

    public void register(IObserver<GamePresenterHandlerData> observer) {
        if (!calledRegister) {
            gpHandler.register(observer);
            calledRegister = true;
        }
    }

    protected void sendHandlerUpdate(GamePresenterHandlerData data) {
        gpHandler.notify(data);
    }
}
