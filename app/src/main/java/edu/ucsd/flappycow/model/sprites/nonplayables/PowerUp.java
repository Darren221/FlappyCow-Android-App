/**
 * The abstract spriteclass for power-ups
 *
 * @author Lars Harmsen
 * Copyright (c) <2014> <Lars Harmsen - Quchen>
 */

package edu.ucsd.flappycow.model.sprites.nonplayables;

import edu.ucsd.flappycow.util.GamePresenterHandlerData;
import edu.ucsd.flappycow.util.IObserver;
import edu.ucsd.flappycow.util.ISubjectImpl;
import edu.ucsd.flappycow.util.SubjectImpl;

public abstract class PowerUp extends NonplayableObject {

    protected ISubjectImpl<GamePresenterHandlerData> gpHandler = new SubjectImpl<>();

    private boolean calledRegister = false;

    private int viewWidth;
    private int viewSpeedX;
    public PowerUp(int viewWidth, int viewSpeedX, int viewPlayerX) {
        super(viewPlayerX);
        this.viewSpeedX = viewSpeedX;
        this.viewWidth = viewWidth;
        init();
    }

    /**
     * Sets this sprite above the visible screen.
     * At x = 4/5 of the screen.
     * Uses the speed of the GameView to let the power-up fall slowly down.
     */
    private void init() {
        this.x = viewWidth * 4 / 5;
        this.y = 0 - this.height;
        this.speedX = -viewSpeedX;
        this.speedY = (int) (viewSpeedX * (Math.random() + 0.5));
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
