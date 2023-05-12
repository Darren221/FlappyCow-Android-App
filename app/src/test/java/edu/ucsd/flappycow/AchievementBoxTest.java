package edu.ucsd.flappycow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import edu.ucsd.flappycow.model.AchievementBox;
import edu.ucsd.flappycow.model.sprites.nonplayables.Coin;
import edu.ucsd.flappycow.util.EarningsUpdate;

@RunWith(RobolectricTestRunner.class)
public class AchievementBoxTest {
    @Test
    public void testAchievementBoxInitialization() {
        ActivityScenario<GameActivity> scenario = ActivityScenario.launch(GameActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.onActivity(activity -> {
            assertEquals(0, activity.accomplishmentBox.coins);
            assertEquals(0, activity.accomplishmentBox.points);
        });
    }

    @Test
    public void testIncreaseCoin() {
        ActivityScenario<GameActivity> scenario = ActivityScenario.launch(GameActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.onActivity(activity -> {
            assertEquals(0, activity.accomplishmentBox.coins);
            activity.increaseCoin();
            assertEquals(1, activity.accomplishmentBox.coins);
        });
    }

    @Test
    public void testDecreaseCoin() {
        ActivityScenario<GameActivity> scenario = ActivityScenario.launch(GameActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.onActivity(activity -> {
            assertEquals(0, activity.accomplishmentBox.coins);
            activity.increaseCoin();
            assertEquals(1, activity.accomplishmentBox.coins);
            activity.decreaseCoin();
            assertEquals(0, activity.accomplishmentBox.coins);
        });
    }

    @Test
    public void testIncreasePoints() {
        ActivityScenario<GameActivity> scenario = ActivityScenario.launch(GameActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.onActivity(activity -> {
            assertEquals(0, activity.accomplishmentBox.points);
            activity.increasePoints();
            assertEquals(1, activity.accomplishmentBox.points);
        });
    }

    @Test
    public void testDecreasePoints() {
        ActivityScenario<GameActivity> scenario = ActivityScenario.launch(GameActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.onActivity(activity -> {
            assertEquals(0, activity.accomplishmentBox.points);
            activity.increasePoints();
            assertEquals(1, activity.accomplishmentBox.points);
            activity.decreasePoints();
            assertEquals(0, activity.accomplishmentBox.points);
        });
    }

    @Test
    public void testOnUpdateCoin() {
        ActivityScenario<GameActivity> scenario = ActivityScenario.launch(GameActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.onActivity(activity -> {
            AchievementBox box = new AchievementBox(activity);
            EarningsUpdate update = new EarningsUpdate();
            update.coinsChange = 2;
            box.onUpdate(update);
            assertEquals(2, box.coins);
        });
    }

    @Test
    public void testOnUpdatePoints() {
        ActivityScenario<GameActivity> scenario = ActivityScenario.launch(GameActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.onActivity(activity -> {
            AchievementBox box = new AchievementBox(activity);
            EarningsUpdate update = new EarningsUpdate();
            update.pointsChange = 2;
            box.onUpdate(update);
            assertEquals(2, box.points);
        });
    }

    @Test
    public void testOnUpdatePointsAchievements() {
        ActivityScenario<GameActivity> scenario = ActivityScenario.launch(GameActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.onActivity(activity -> {
            EarningsUpdate update = new EarningsUpdate();
            update.pointsChange = 11;
            activity.accomplishmentBox.onUpdate(update);
            assertTrue(activity.accomplishmentBox.achievement_bronze);
            update.pointsChange = 40;
            activity.accomplishmentBox.onUpdate(update);
            assertTrue(activity.accomplishmentBox.achievement_silver);
            update.pointsChange = 50;
            activity.accomplishmentBox.onUpdate(update);
            assertTrue(activity.accomplishmentBox.achievement_gold);
        });
    }

    @Test
    public void testOnUpdateCoinsAchievements() {
        ActivityScenario<GameActivity> scenario = ActivityScenario.launch(GameActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.onActivity(activity -> {
            EarningsUpdate update = new EarningsUpdate();
            update.coinsChange = 50;
            activity.accomplishmentBox.onUpdate(update);
            assertTrue(activity.accomplishmentBox.achievement_50_coins);
        });
    }

    @Test
    public void testCollisionCoin() {
        ActivityScenario<GameActivity> scenario = ActivityScenario.launch(GameActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.onActivity(activity -> {
            // GIVEN
            Game game = new Game(activity, activity);
            Coin coin = new Coin(game.getBitmapFromView(R.drawable.coin), 0, game.getWidth(), game.getSpeedX(), game.getPlayerX());
            coin.register(game);

            // WHEN
            game.setPlayerX(10);
            game.setPlayerY(10);
            coin.setX(10);
            coin.setY(10);
            coin.onCollision();

            // THEN
//            assertTrue(coin.isColliding(activity.view.getPlayer()));
            assertEquals(1, activity.accomplishmentBox.coins);
        });
    }
}
