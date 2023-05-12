package edu.ucsd.flappycow.view;

import android.content.Context;
import android.media.MediaPlayer;

import edu.ucsd.flappycow.R;
import edu.ucsd.flappycow.util.IObserver;
import edu.ucsd.flappycow.util.Util;

public class MusicPlayer implements IObserver<Boolean> {

    private Boolean musicShouldPlay = false;

    public MediaPlayer mediaPlayer = null;

    public MusicPlayer(Context context) {
        if (mediaPlayer == null) {
            // to avoid unnecessary reinitialisation
            mediaPlayer = MediaPlayer.create(context, R.raw.nyan_cat_theme);
            if (mediaPlayer == null) {
                return;
            }
            mediaPlayer.setLooping(true);
            mediaPlayer.setVolume(Util.volume, Util.volume);
        }
        mediaPlayer.seekTo(0);    // Reset song to position 0
    }

    @Override
    public void onUpdate(Boolean data) {
        this.musicShouldPlay = data;
    }

    public void onPause() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    public void onResume() {
        if (mediaPlayer != null && musicShouldPlay) {
            mediaPlayer.start();
        }
    }

    public void start() {
        mediaPlayer.start();
    }
}
