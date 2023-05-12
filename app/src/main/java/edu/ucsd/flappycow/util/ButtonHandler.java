package edu.ucsd.flappycow.util;

import android.util.Log;

public class ButtonHandler implements ButtonView {
    public boolean pressed = false;
    ButtonModel buttonModel;
    public ButtonHandler(ButtonModel buttonModel) {
        this.buttonModel = buttonModel;
    }

    public void onPress() {
        if(pressed) {
            Log.d("Timer", "Timer Started in ButtonHandler.java");
            buttonModel.startTimer();
        } else {
            Log.d("Timer", "Timer Stopped in ButtonHandler.java");
            buttonModel.stopTimer();
        }
        pressed = !pressed;
    }
}
