package edu.ucsd.flappycow.model.sprites.nonplayables;

import edu.ucsd.flappycow.model.sprites.Sprite;
import edu.ucsd.flappycow.util.GamePresenterHandlerData;
import edu.ucsd.flappycow.util.IObserver;
import edu.ucsd.flappycow.util.ISubjectImpl;
import edu.ucsd.flappycow.util.SubjectImpl;

public abstract class NonplayableObject extends Sprite implements NonplayableBehavior {

    int viewPlayerX;

    private boolean calledRegister = false;

    protected ISubjectImpl<GamePresenterHandlerData> gpHandler = new SubjectImpl<>();

    public NonplayableObject(int viewPlayerX) {
        super();
        this.viewPlayerX = viewPlayerX;
    }

    /**
     * Checks whether the sprite is touching this.
     * Seeing the sprites as rectangles.
     * @param sprite sprite
     * @return boolean
     */
    public boolean isColliding(Sprite sprite, int collisionTolerance) {
        return this.x + collisionTolerance < sprite.getX() + sprite.getWidth()
                && this.x + this.width > sprite.getX() + collisionTolerance
                && this.y + collisionTolerance < sprite.getY() + sprite.height
                && this.y + this.height > sprite.getY() + collisionTolerance;
    }

    /**
     * Checks whether the sprite is touching this.
     * With the distance of the 2 centers.
     * @param sprite sprite
     * @return boolean
     */
    public boolean isCollidingRadius(Sprite sprite, float factor) {
        int m1x = this.x + (this.width >> 1);
        int m1y = this.y + (this.height >> 1);
        int m2x = sprite.getX() + (sprite.getWidth() >> 1);
        int m2y = sprite.getY() + (sprite.height >> 1);
        int dx = m1x - m2x;
        int dy = m1y - m2y;
        int d = (int) Math.sqrt(dy * dy + dx * dx);

        return d < (this.width + sprite.getWidth()) * factor
                || d < (this.height + sprite.height) * factor;
    }

    public int onCollision() {return 0;}

    /**
     * Checks whether the play has passed this sprite.
     * @return boolean
     */
    public boolean isPassed() {
        return this.x + this.width < viewPlayerX;
    }

    /**
     * Checks whether this sprite is so far to the left, it's not visible anymore.
     * @return boolean
     */
    public boolean isOutOfRange() {
        return this.x + width < 0;
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
