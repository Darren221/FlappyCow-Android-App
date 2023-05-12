/*
 * The Game Activity
 *
 * @author Lars Harmsen
 * Copyright (c) <2014> <Lars Harmsen - Quchen>
 */

package edu.ucsd.flappycow;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import edu.ucsd.flappycow.model.AchievementBox;
import edu.ucsd.flappycow.util.EarningsUpdate;
import edu.ucsd.flappycow.util.ISubjectImpl;
import edu.ucsd.flappycow.util.MyToastData;
import edu.ucsd.flappycow.util.SubjectImpl;
import edu.ucsd.flappycow.util.Util;
import edu.ucsd.flappycow.view.GameOverDialog;
import edu.ucsd.flappycow.view.ToastHandler;

public class GameActivity extends Activity implements ActivityPresenter{

    /**
     * Will play things like mooing
     */
    public static SoundPool soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);

    private static final int GAMES_PER_AD = 3;
    /**
     * Counts number of played games
     */
    private static int gameOverCounter = 1;

    /**
     * Time interval (ms) you have to press the backbutton twice in to exit
     */
    private static final long DOUBLE_BACK_TIME = 1000;

    /**
     * Saves the time of the last backbutton press
     */
    private long backPressed;

    /**
     * To do UI things from different threads
     */
    public MyHandler handler;

    /**
     * Hold all accomplishments
     */
    public AchievementBox accomplishmentBox;

    /**
     * This will increase the revive price
     */
    public int numberOfRevive = 1;

    /**
     * The dialog displayed when the game is over
     */
    public GameOverDialog gameOverDialog;

    /**
     * Interstitial ad.
     */
    private InterstitialAd interstitial;

    ISubjectImpl<EarningsUpdate> earningsSubj = new SubjectImpl<>();

    ISubjectImpl<MyToastData> toastSubj = new SubjectImpl<>();

    ActivityModel activityModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityModel = new Game(this, this);
        gameOverDialog = new GameOverDialog(this, this);
        handler = new MyHandler(this);

        accomplishmentBox = new AchievementBox(this);
        earningsSubj.register(accomplishmentBox);

        ToastHandler toastHandler = new ToastHandler();
        toastSubj.register(toastHandler);

        View view = activityModel.getView();
        setContentView(view);
        if (gameOverCounter % GAMES_PER_AD == 0) {
            setupAd();
        }
    }

    /**
     * Initializes the player with the nyan cat song
     * and sets the position to 0.
     */

    /**
     * Pauses the view and the music
     */
     @Override
     protected void onPause() {
         activityModel.onPause();
         super.onPause();
     }

    /**
     * Resumes the view (but waits the view waits for a tap)
     * and starts the music if it should be running.
     * Also checks whether the Google Play Services are available.
     */
     @Override
     protected void onResume() {
         activityModel.drawOnce();
         activityModel.resumeMusicPlayer();
         super.onResume();
     }

    /**
     * Prevent accidental exits by requiring a double press.
     */
    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - backPressed < DOUBLE_BACK_TIME) {
            super.onBackPressed();
        } else {
            backPressed = System.currentTimeMillis();
            MyToastData toastData = new MyToastData(R.string.on_back_press, this);
            toastSubj.notify(toastData);
        }
    }

    /**
     * Sends the handler the command to show the GameOverDialog.
     * Because it needs an UI thread.
     */
    public void gameOver() {
        if (gameOverCounter % GAMES_PER_AD == 0) {
            handler.sendMessage(Message.obtain(handler, MyHandler.SHOW_AD));
        } else {
            handler.sendMessage(Message.obtain(handler, MyHandler.SHOW_GAME_OVER_DIALOG));
        }

    }

    public void increaseCoin() {
        EarningsUpdate update = new EarningsUpdate();
        update.coinsChange = 1;
        earningsSubj.notify(update);
    }

    public void decreaseCoin() {
        EarningsUpdate update = new EarningsUpdate();
        update.coinsChange = -1;
        earningsSubj.notify(update);
    }

    public void decreaseCoins(int num) {
        accomplishmentBox.coins -= num;
    }

    /**
     * What should happen, when an obstacle is passed?
     */
    public void increasePoints() {
        EarningsUpdate update = new EarningsUpdate();
        update.pointsChange = 1;
        earningsSubj.notify(update);
    }

    public void saveCoins() {
        accomplishmentBox.saveCoins();
    }

    public void playSound(Object... varargs) {
        soundPool.play((Integer)varargs[0], (Float)varargs[1], (Float)varargs[2], (Integer)varargs[3], (Integer)varargs[4], (Float)varargs[5]);
    }

    public Bitmap getBitmap(Object... varargs) {
        return Util.getScaledBitmapAlpha8(this, (Integer)varargs[0]);
    }

    public int getPosition(Object... varargs) {
        switch ((String)varargs[0]){
            case "width": return this.getResources().getDisplayMetrics().widthPixels;
            case "height": return this.getResources().getDisplayMetrics().heightPixels;
            default: return 0;
        }
    }

    public int loadSound(Object... varargs) {
        return soundPool.load(this, (Integer)varargs[0], (Integer)varargs[1]);
    }

    public void sendHandlerMessage(int message) {
        handler.sendMessage(Message.obtain(handler, MyHandler.SHOW_TOAST, message, MyHandler.SHOW_TOAST));
    }

    public void decreasePoints() {
        EarningsUpdate update = new EarningsUpdate();
        update.pointsChange = -1;
        earningsSubj.notify(update);
    }

    public void revive() {
        numberOfRevive++;
        activityModel.revive();
    }

    public void hideGameOverDialog() {
        gameOverDialog.hide();
    }

    public int getCoins() {
        return accomplishmentBox.coins;
    }

    public int getPoints() {
        return accomplishmentBox.points;
    }

    public String getString_(int id) {
        return getResources().getString(id);
    }

    public int getNumRevive() {
        return numberOfRevive;
    }

    public void saveAccomplishment() {
        accomplishmentBox.save(this);
    }

    public void finishActivity() {
        finish();
    }

    public SharedPreferences getSharedPreferences_(String str) {
        return getSharedPreferences(str, 0);
    }

    public boolean achieveBronze() {
        return accomplishmentBox.achievement_bronze;
    }

    public boolean achieveSilver() {
        return accomplishmentBox.achievement_silver;
    }

    public boolean achieveGold() {
        return accomplishmentBox.achievement_gold;
    }

    public void setMedal(int id) {
        ((ImageView) findViewById(R.id.medal)).setImageBitmap(Util.getScaledBitmapAlpha8(this, id));
    }

    /**
     * Shows the GameOverDialog when a message with code 0 is received.
     */
    public class MyHandler extends Handler {
        public static final int SHOW_GAME_OVER_DIALOG = 0;
        public static final int SHOW_TOAST = 1;
        public static final int SHOW_AD = 2;

        private final GameActivity gameActivity;

        public MyHandler(GameActivity gameActivity) {
            this.gameActivity = gameActivity;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_GAME_OVER_DIALOG:
                    showGameOverDialog();
                    break;
                case SHOW_TOAST:
                    MyToastData toastData = new MyToastData(msg.arg1, GameActivity.this);
                    GameActivity.this.toastSubj.notify(toastData);
                    break;
                case SHOW_AD:
                    showAdIfAvailable();
                    break;
            }
        }

        private void showAdIfAvailable() {
            if (gameActivity.interstitial == null) {
                showGameOverDialog();
            } else {
                gameActivity.interstitial.show(GameActivity.this);
            }
        }

        private void showGameOverDialog() {
            ++GameActivity.gameOverCounter;
            gameActivity.gameOverDialog.init();
            gameActivity.gameOverDialog.show();
        }
    }

    private void setupAd() {
        MobileAds.initialize(this, initializationStatus -> { /* no-op */ });

        String adUnitId = getResources().getString(R.string.ad_unit_id);

        // Make sure only adds appropriate for children of all ages are displayed.
        Bundle extras = new Bundle();
        extras.putString("max_ad_content_rating", "G");

        AdRequest adRequest = new AdRequest.Builder()
            .addNetworkExtrasBundle(AdMobAdapter.class, extras)
            .build();

        InterstitialAd.load(this, adUnitId, adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                Log.i("Ads", "Ad was loaded.");
                interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        handler.sendMessage(Message.obtain(handler, MyHandler.SHOW_GAME_OVER_DIALOG));
                    }
                });
                GameActivity.this.interstitial = interstitialAd;
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                if (loadAdError.getCode() == AdRequest.ERROR_CODE_NO_FILL) {
                    Log.i("Ads", "No ad was available.");
                } else {
                    Log.i("Ads", "Ad failed to load.");
                }
                Log.d("Ads", loadAdError.toString());
                GameActivity.this.interstitial = null;
            }
        });
    }
}
