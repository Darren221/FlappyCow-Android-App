/**
 * An obstacle: spider + logHead
 *
 * @author Lars Harmsen
 * Copyright (c) <2014> <Lars Harmsen - Quchen>
 */

package edu.ucsd.flappycow.model.sprites.nonplayables;

import android.graphics.Canvas;

import edu.ucsd.flappycow.model.sprites.Sprite;
import edu.ucsd.flappycow.util.GamePresenterData;
import edu.ucsd.flappycow.util.GamePresenterHandlerData;
import edu.ucsd.flappycow.util.Util;

public class CombinationObstacle extends NonplayableObject {
    private ObstacleObject topObstacle;
    private ObstacleObject bottomObstacle;

    private static int collideSound = -1;
    public static int passSound = -1;

    private int viewSpeedX;

    /** Necessary so the onPass method is just called once */
    public boolean isAlreadyPassed = false;

    public CombinationObstacle(GamePresenterData data, ObstacleObject top, ObstacleObject bot, int viewSpeedX, int viewPlayerX) {
        super(viewPlayerX);
        topObstacle = top;
        bottomObstacle = bot;

        if (collideSound == -1) {
            collideSound = data.collideSound;
        }
        if (passSound == -1) {
            passSound = data.passSound;
        }
        this.viewSpeedX = viewSpeedX;
        initPos(data.height, data.width);
    }

    /**
     * Creates a spider and a wooden log at the right of the screen.
     * With a certain gap between them.
     * The vertical position is in a certain area random.
     */
    private void initPos(int gameHeight, int gameWidth) {
        int gab = gameHeight / 4 - this.viewSpeedX;
        if (gab < gameHeight / 5) {
            gab = gameHeight / 5;
        }
        int random = (int) (Math.random() * gameHeight * 2 / 5);
        int y1 = (gameHeight / 10) + random - topObstacle.height;
        int y2 = (gameHeight / 10) + random + gab;

        topObstacle.init(gameWidth, y1);
        bottomObstacle.init(gameWidth, y2);
    }

    /**
     * Draws spider and log.
     */
    @Override
    public void draw(Canvas canvas) {
        topObstacle.draw(canvas);
        bottomObstacle.draw(canvas);
    }

    /**
     * Checks whether both, spider and log, are out of range.
     */
    @Override
    public boolean isOutOfRange() {
        return topObstacle.isOutOfRange() && bottomObstacle.isOutOfRange();
    }

    /**
     * Checks whether the spider or the log is colliding with the sprite.
     */
    @Override
    public boolean isColliding(Sprite sprite, int collisionTolerance) {
        return topObstacle.isColliding(sprite, collisionTolerance) || bottomObstacle.isColliding(sprite, collisionTolerance);
    }

    /**
     * Moves both, spider and log.
     */
    @Override
    public void move() {
        topObstacle.move();
        bottomObstacle.move();
    }

    /**
     * Sets the speed of the spider and the log.
     */
    @Override
    public void setSpeedX(float speedX) {
        topObstacle.setSpeedX(speedX);
        bottomObstacle.setSpeedX(speedX);
    }

    /**
     * Checks whether the spider and the log are passed.
     */
    @Override
    public boolean isPassed() {
        return topObstacle.isPassed() && bottomObstacle.isPassed();
    }

    public static final int SOUND_VOLUME_DIVIDER = 3;

    /**
     * Will call obstaclePassed of the game, if this is the first pass of this obstacle.
     */
    public boolean onPass() {
        if (!isAlreadyPassed) {
            isAlreadyPassed = true;
            return true;
        }
        return false;
    }

    public int getPassSound() {
        return passSound;
    }

    public int getSoundVolumeDivider() {
        return SOUND_VOLUME_DIVIDER;
    }

    @Override
    public int onCollision() {
        GamePresenterHandlerData data = new GamePresenterHandlerData("playSound", collideSound, Util.volume / SOUND_VOLUME_DIVIDER, Util.volume / SOUND_VOLUME_DIVIDER, 0, 0, 1.f);
        gpHandler.notify(data);
        return 0;
    }

}
