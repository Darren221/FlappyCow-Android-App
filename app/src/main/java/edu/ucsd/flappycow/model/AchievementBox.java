/**
 * Saves achievements and score in shared preferences.
 * You should use a SQLite DB instead, but I'm too lazy to chance it now.
 *
 * @author Lars Harmsen
 * Copyright (c) <2014> <Lars Harmsen - Quchen>
 */

package edu.ucsd.flappycow.model;

import android.app.Activity;
import android.content.SharedPreferences;

import edu.ucsd.flappycow.GameActivity;
import edu.ucsd.flappycow.R;
import edu.ucsd.flappycow.util.EarningsUpdate;
import edu.ucsd.flappycow.util.IObserver;

public class AchievementBox implements IObserver<EarningsUpdate> {
    /**
     * Points needed for a gold medal
     */
    public static final int GOLD_POINTS = 100;

    /**
     * Points needed for a silver medal
     */
    public static final int SILVER_POINTS = 50;

    /**
     * Points needed for a bronze medal
     */
    public static final int BRONZE_POINTS = 10;

    public static final String SAVE_NAME = "achievements";

    public static final String KEY_COINS = "coin_key";
    public static final String KEY_POINTS = "points";
    public static final String ACHIEVEMENT_KEY_50_COINS = "achievement_survive_5_minutes";
    public static final String ACHIEVEMENT_KEY_TOASTIFICATION = "achievement_toastification";
    public static final String ACHIEVEMENT_KEY_BRONZE = "achievement_bronze";
    public static final String ACHIEVEMENT_KEY_SILVER = "achievement_silver";
    public static final String ACHIEVEMENT_KEY_GOLD = "achievement_gold";

    public int points;

    public int coins;
    public boolean achievement_50_coins;
    public boolean achievement_toastification;
    public boolean achievement_bronze;
    public boolean achievement_silver;
    public boolean achievement_gold;

    GameActivity activity;

    public AchievementBox(GameActivity activity) {
        this.activity = activity;
        points = 0;
        SharedPreferences saves = activity.getSharedPreferences(SAVE_NAME, 0);
        this.coins = saves.getInt(KEY_COINS, 0);
    }

    /**
     * Stores the score and achievements locally.
     * <p>
     * The accomplishments will be saved local via SharedPreferences.
     * This makes it very easy to cheat.
     * <p>
     * todo: is activity the right thing to pass in here?
     *
     * @param activity activity that is needed for shared preferences
     */
    public void save(Activity activity) {
        SharedPreferences saves = activity.getSharedPreferences(SAVE_NAME, 0);
        SharedPreferences.Editor editor = saves.edit();

        if (points > saves.getInt(KEY_POINTS, 0)) {
            editor.putInt(KEY_POINTS, points);
        }
        if (achievement_50_coins) {
            editor.putBoolean(ACHIEVEMENT_KEY_50_COINS, true);
        }
        if (achievement_toastification) {
            editor.putBoolean(ACHIEVEMENT_KEY_TOASTIFICATION, true);
        }
        if (achievement_bronze) {
            editor.putBoolean(ACHIEVEMENT_KEY_BRONZE, true);
        }
        if (achievement_silver) {
            editor.putBoolean(ACHIEVEMENT_KEY_SILVER, true);
        }
        if (achievement_gold) {
            editor.putBoolean(ACHIEVEMENT_KEY_GOLD, true);
        }

        editor.apply();
    }

    /**
     * reads the local stored data
     *
     * @param activity activity that is needed for shared preferences
     * @return local stored score and achievements
     */
    public static AchievementBox load(GameActivity activity) {
        AchievementBox box = new AchievementBox(activity);
        SharedPreferences saves = activity.getSharedPreferences(SAVE_NAME, 0);

        box.points = saves.getInt(KEY_POINTS, 0);
        box.coins = saves.getInt(KEY_COINS, 0);
        box.achievement_50_coins = saves.getBoolean(ACHIEVEMENT_KEY_50_COINS, false);
        box.achievement_toastification = saves.getBoolean(ACHIEVEMENT_KEY_TOASTIFICATION, false);
        box.achievement_bronze = saves.getBoolean(ACHIEVEMENT_KEY_BRONZE, false);
        box.achievement_silver = saves.getBoolean(ACHIEVEMENT_KEY_SILVER, false);
        box.achievement_gold = saves.getBoolean(ACHIEVEMENT_KEY_GOLD, false);

        return box;
    }

    public void saveCoins() {
        SharedPreferences coin_save = activity.getSharedPreferences(SAVE_NAME, 0);
        SharedPreferences.Editor editor = coin_save.edit();
        editor.putInt(KEY_COINS, this.coins);
        editor.apply();
    }

    public void loadCoins() {
        SharedPreferences saves = activity.getSharedPreferences(SAVE_NAME, 0);
        this.coins = saves.getInt(KEY_COINS, 0);
    }

    @Override
    public void onUpdate(EarningsUpdate update) {
        if (update.pointsChange != 0) {
            this.points += update.pointsChange;
            if (update.pointsChange > 0) {
                if (this.points >= BRONZE_POINTS) {
                    if (!achievement_bronze) {
                        achievement_bronze = true;
                        activity.sendHandlerMessage(R.string.toast_achievement_bronze);
                    }
                    if (this.points >= SILVER_POINTS) {
                        if (!achievement_silver) {
                            achievement_silver = true;
                            activity.sendHandlerMessage(R.string.toast_achievement_silver);
                        }
                        if (this.points >= GOLD_POINTS) {
                            if (!achievement_gold) {
                                achievement_gold = true;
                                activity.sendHandlerMessage(R.string.toast_achievement_gold);
                            }
                        }
                    }
                }
            }
        }
        if (update.coinsChange != 0) {
            this.coins += update.coinsChange;
            if (update.coinsChange > 0) {
                if (coins >= 50 && !achievement_50_coins) {
                    achievement_50_coins = true;
                    activity.sendHandlerMessage(R.string.toast_achievement_50_coins);
                }
            }
        }
    }
}