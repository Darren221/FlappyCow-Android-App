package edu.ucsd.flappycow.model.sprites.playables;

public interface PlayableBehavior {
    boolean isTouchingEdge(int viewHeight);

    boolean isTouchingGround(int viewHeight);

    boolean isTouchingSky();

}
