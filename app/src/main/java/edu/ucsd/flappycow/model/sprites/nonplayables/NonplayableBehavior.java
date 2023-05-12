package edu.ucsd.flappycow.model.sprites.nonplayables;

import edu.ucsd.flappycow.model.sprites.Sprite;

public interface NonplayableBehavior {
    boolean isColliding(Sprite sprite, int collisionTolerance);

    boolean isCollidingRadius(Sprite sprite, float factor);

    int onCollision();

    boolean isPassed();
}
