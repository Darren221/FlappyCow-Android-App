/**
 * The dialog shown when the game is over
 *
 * @author Lars Harmsen
 * Copyright (c) <2014> <Lars Harmsen - Quchen>
 */

package edu.ucsd.flappycow.view;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import edu.ucsd.flappycow.ActivityPresenter;
import edu.ucsd.flappycow.GameActivity;
import edu.ucsd.flappycow.MainActivity;
import edu.ucsd.flappycow.R;

public class GameOverDialog extends Dialog {
    public static final int REVIVE_PRICE = 5;

    /**
     * Name of the SharedPreference that saves the score
     */
    public static final String score_save_name = "score_save";

    /**
     * Key that saves the score
     */
    public static final String best_score_key = "score";

    /**
     * The game that invokes this dialog
     */

    private TextView tvCurrentScoreVal;
    private TextView tvBestScoreVal;

    private ActivityPresenter activityPresenter;

    public GameOverDialog(GameActivity gameActivity, ActivityPresenter activityPresenter) {
        super(gameActivity);
        this.activityPresenter = activityPresenter;
        this.setContentView(R.layout.gameover);
        this.setCancelable(false);

        tvCurrentScoreVal = findViewById(R.id.tv_current_score_value);
        tvBestScoreVal = findViewById(R.id.tv_best_score_value);
    }

    public void init() {
        Button okButton = findViewById(R.id.b_ok);
        okButton.setOnClickListener(view -> {
            saveCoins();
            if (activityPresenter.getNumRevive() <= 1) {
                activityPresenter.saveAccomplishment();
            }

            dismiss();
            activityPresenter.finishActivity();
        });

        Button reviveButton = findViewById(R.id.b_revive);
        reviveButton.setText(activityPresenter.getString_(R.string.revive_button)
            + " " + REVIVE_PRICE * activityPresenter.getNumRevive() + " "
            + activityPresenter.getString_(R.string.coins));
        reviveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                activityPresenter.decreaseCoins(REVIVE_PRICE * activityPresenter.getNumRevive());
                saveCoins();
                activityPresenter.revive();
            }
        });
        reviveButton.setClickable(activityPresenter.getCoins() >= REVIVE_PRICE * activityPresenter.getNumRevive());

        manageScore();
        manageMedals();
    }

    private void manageScore() {
        SharedPreferences saves = activityPresenter.getSharedPreferences_(score_save_name);
        int oldPoints = saves.getInt(best_score_key, 0);
        if (activityPresenter.getPoints() > oldPoints) {
            // Save new highscore
            SharedPreferences.Editor editor = saves.edit();
            editor.putInt(best_score_key, activityPresenter.getPoints());
            tvBestScoreVal.setTextColor(Color.RED);
            editor.apply();
        }
        tvCurrentScoreVal.setText("" + activityPresenter.getPoints());
        tvBestScoreVal.setText("" + oldPoints);
    }

    private void manageMedals() {
        SharedPreferences MEDAL_SAVE = activityPresenter.getSharedPreferences_(MainActivity.MEDAL_SAVE);
        int medal = MEDAL_SAVE.getInt(MainActivity.MEDAL_KEY, 0);

        SharedPreferences.Editor editor = MEDAL_SAVE.edit();

        if (activityPresenter.achieveGold()) {
            activityPresenter.setMedal(R.drawable.gold);
            if (medal < 3) {
                editor.putInt(MainActivity.MEDAL_KEY, 3);
            }
        } else if (activityPresenter.achieveSilver()) {
            activityPresenter.setMedal(R.drawable.silver);
            if (medal < 2) {
                editor.putInt(MainActivity.MEDAL_KEY, 2);
            }
        } else if (activityPresenter.achieveBronze()) {
            activityPresenter.setMedal(R.drawable.bronce);
            if (medal < 1) {
                editor.putInt(MainActivity.MEDAL_KEY, 1);
            }
        } else {
            findViewById(R.id.medal).setVisibility(View.INVISIBLE);
        }
        editor.apply();
    }

    private void saveCoins() {
        activityPresenter.saveCoins();
    }

}
