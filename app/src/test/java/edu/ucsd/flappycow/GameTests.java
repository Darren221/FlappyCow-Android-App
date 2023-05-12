package edu.ucsd.flappycow;

import static org.junit.Assert.assertEquals;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import edu.ucsd.flappycow.util.GamePresenterHandlerData;

@RunWith(RobolectricTestRunner.class)
public class GameTests {

    @Test
    public void onUpdateIncreaseCoin() {
        ActivityScenario<GameActivity> scenario = ActivityScenario.launch(GameActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.onActivity(activity -> {
            // GIVEN
            Game game = new Game(activity, activity);

            assertEquals(0, activity.accomplishmentBox.coins);

            GamePresenterHandlerData data = new GamePresenterHandlerData("increaseCoin");
            game.onUpdate(data);

            // THEN
            assertEquals(1, activity.accomplishmentBox.coins);
        });
    }

    @Test
    public void onUpdateIncreasePoints() {
        ActivityScenario<GameActivity> scenario = ActivityScenario.launch(GameActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.onActivity(activity -> {
            // GIVEN
            Game game = new Game(activity, activity);

            assertEquals(0, activity.accomplishmentBox.points);

            GamePresenterHandlerData data = new GamePresenterHandlerData("increasePoints");
            game.onUpdate(data);

            // THEN
            assertEquals(1, activity.accomplishmentBox.points);
        });
    }

    @Test
    public void onUpdateDecreasePoints() {
        ActivityScenario<GameActivity> scenario = ActivityScenario.launch(GameActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.onActivity(activity -> {
            // GIVEN
            Game game = new Game(activity, activity);

            assertEquals(0, activity.accomplishmentBox.points);

            GamePresenterHandlerData data = new GamePresenterHandlerData("decreasePoints");
            game.onUpdate(data);

            // THEN
            assertEquals(-1, activity.accomplishmentBox.points);
        });
    }



}
