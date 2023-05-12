/*
 * Main Activity / Splashscreen with buttons.
 *
 * @author Lars Harmsen
 * Copyright (c) <2014> <Lars Harmsen - Quchen>
 */

package edu.ucsd.flappycow;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;

import edu.ucsd.flappycow.util.Util;
import edu.ucsd.flappycow.view.StartScreenView;

public class MainActivity extends Activity {

    /**
     * Name of the SharedPreference that saves the medals
     */
    public static final String MEDAL_SAVE = "medal_save";

    /**
     * Key that saves the medal
     */
    public static final String MEDAL_KEY = "medal_key";

    private StartScreenView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = new StartScreenView(this);
        setContentView(view);
        setSocket();
    }

    public void muteToggle() {
        if (Util.volume != 0) {
            Util.volume = 0;
            view.setSpeaker(false);
        } else {
            Util.volume = Util.DEFAULT_VOLUME;
            view.setSpeaker(true);
        }
        view.invalidate();
    }

    /**
     * Fills the socket with the medals that have already been collected.
     */
    private void setSocket() {
        SharedPreferences saves = this.getSharedPreferences(MEDAL_SAVE, 0);
        view.setSocket(saves.getInt(MEDAL_KEY, 0));
        view.invalidate();
    }

    /**
     * Updates the socket for the medals.
     */
    @Override
    protected void onResume() {
        super.onResume();
        setSocket();
    }
}
