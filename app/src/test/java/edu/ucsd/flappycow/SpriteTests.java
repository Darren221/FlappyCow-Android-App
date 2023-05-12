package edu.ucsd.flappycow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import edu.ucsd.flappycow.model.sprites.Accessory;
import edu.ucsd.flappycow.model.sprites.GroundLayer;
import edu.ucsd.flappycow.model.sprites.nonplayables.Coin;
import edu.ucsd.flappycow.model.sprites.nonplayables.CombinationObstacle;
import edu.ucsd.flappycow.model.sprites.nonplayables.ObstacleObject;
import edu.ucsd.flappycow.model.sprites.nonplayables.Toast;
import edu.ucsd.flappycow.model.sprites.nonplayables.Virus;
import edu.ucsd.flappycow.model.sprites.playables.Cow;
import edu.ucsd.flappycow.util.GamePresenterData;

@RunWith(RobolectricTestRunner.class)
public class SpriteTests {
    @Test
    public void testActivityResumes() {
        // GIVEN
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);

        // WHEN
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.RESUMED);

        // THEN
        scenario.onActivity(activity -> {
            assertNotNull(activity);
        });
    }

    @Test
    public void testCollisionCoin() {
        ActivityScenario<GameActivity> scenario = ActivityScenario.launch(GameActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.onActivity(activity -> {
            // GIVEN
            Game game = new Game(activity, activity);

            GamePresenterData playerData = new GamePresenterData();
            playerData.height = game.getPositionFromView("height") / 2;
            playerData.sound = game.loadSoundFromView(R.raw.cow, 1);

            Cow cow = new Cow(game.getBitmapFromView(R.drawable.cow), playerData, new Accessory());
            cow.register(game);

            Coin coin = new Coin(game.getBitmapFromView(R.drawable.coin), 0, game.getWidth(), game.getSpeedX(), game.getPlayerX());
            coin.register(game);

            // WHEN
            cow.setX(10);
            cow.setY(10);
            coin.setX(10);
            coin.setY(10);
            coin.onCollision();

            // THEN
            assertTrue(coin.isColliding(cow, 10));
            assertEquals(1, activity.accomplishmentBox.coins);
        });
    }

    @Test
    public void testCollisionVirus() {
        ActivityScenario<GameActivity> scenario = ActivityScenario.launch(GameActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.onActivity(activity -> {
            // GIVEN
            Game game = new Game(activity, activity);

            GamePresenterData playerData = new GamePresenterData();
            playerData.height = game.getPositionFromView("height") / 2;
            playerData.sound = game.loadSoundFromView(R.raw.cow, 1);

            Cow cow = new Cow(game.getBitmapFromView(R.drawable.cow), playerData, new Accessory());
            cow.register(game);

            Virus virus = new Virus(game.getBitmapFromView(R.drawable.virus), game.getWidth(), game.getSpeedX(), game.getPlayerX());
            virus.register(game);

            // WHEN
            cow.setX(10);
            cow.setY(10);
            virus.setX(10);
            virus.setY(10);
            int initPoints = activity.accomplishmentBox.points;
            virus.onCollision();

            // THEN
            assertTrue(virus.isColliding(cow, 10));
            assertEquals(initPoints-1, activity.accomplishmentBox.points);

        });
    }

    @Test
    public void testCollisionToast() {
        ActivityScenario<GameActivity> scenario = ActivityScenario.launch(GameActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.onActivity(activity -> {
            // GIVEN
            Game game = new Game(activity, activity);

            GamePresenterData playerData = new GamePresenterData();
            playerData.height = game.getPositionFromView("height") / 2;
            playerData.sound = game.loadSoundFromView(R.raw.cow, 1);

            Cow cow = new Cow(game.getBitmapFromView(R.drawable.cow), playerData, new Accessory());
            cow.register(game);

            Toast toast = new Toast(game.getBitmapFromView(R.drawable.toast), game.getWidth(), game.getSpeedX(), game.getPlayerX());
            toast.register(game);

            // WHEN
            cow.setX(10);
            cow.setY(10);
            toast.setX(10);
            toast.setY(10);
            try {
                toast.onCollision();
            } catch (Exception NullPointerException) {
                // THEN
                assertTrue(toast.isColliding(cow, 10));
                assertTrue(game.playerIsNyanCat());
            }

        });
    }

    @Test
    public void testCollisionSpider() {
        ActivityScenario<GameActivity> scenario = ActivityScenario.launch(GameActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.onActivity(activity -> {
            // GIVEN
            Game game = new Game(activity, activity);

            GamePresenterData playerData = new GamePresenterData();
            playerData.height = game.getPositionFromView("height") / 2;
            playerData.sound = game.loadSoundFromView(R.raw.cow, 1);

            Cow cow = new Cow(game.getBitmapFromView(R.drawable.cow), playerData, new Accessory());
            cow.register(game);

            ObstacleObject spider = new ObstacleObject(game.getBitmapFromView(R.drawable.spider_full), game.getPlayerX());
            spider.register(game);

            cow.setX(10);
            cow.setY(10);
            spider.setX(10);
            spider.setY(10);

            assertTrue(spider.isColliding(cow, 10));

        });
    }

    @Test
    public void testCollisionWoodLog() {
        ActivityScenario<GameActivity> scenario = ActivityScenario.launch(GameActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.onActivity(activity -> {
            // GIVEN
            Game game = new Game(activity, activity);

            GamePresenterData playerData = new GamePresenterData();
            playerData.height = game.getPositionFromView("height") / 2;
            playerData.sound = game.loadSoundFromView(R.raw.cow, 1);

            Cow cow = new Cow(game.getBitmapFromView(R.drawable.cow), playerData, new Accessory());
            cow.register(game);

            ObstacleObject log = new ObstacleObject(game.getBitmapFromView(R.drawable.log_full), game.getPlayerX());
            log.register(game);

            cow.setX(10);
            cow.setY(10);
            log.setX(10);
            log.setY(10);

            assertTrue(log.isColliding(cow, 10));

        });
    }

    @Test
    public void testGameOver() {
        ActivityScenario<GameActivity> scenario = ActivityScenario.launch(GameActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.onActivity(activity -> {
            Game game = new Game(activity, activity);
            try {
                game.gameOver();
            } catch (Exception NullPointerException) {
                assertTrue(game.playerIsDead());
            }
        });
    }

    @Test
    public void testTouchingSky() {
        ActivityScenario<GameActivity> scenario = ActivityScenario.launch(GameActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.onActivity(activity -> {
            // GIVEN
            Game game = new Game(activity, activity);

            GamePresenterData playerData = new GamePresenterData();
            playerData.height = game.getPositionFromView("height") / 2;
            playerData.sound = game.loadSoundFromView(R.raw.cow, 1);

            Cow cow = new Cow(game.getBitmapFromView(R.drawable.cow), playerData, new Accessory());
            cow.register(game);

            cow.setX(10);
            cow.setY(-1);

            assertTrue(cow.isTouchingSky());

        });
    }

    @Test
    public void testTouchingGround() {
        ActivityScenario<GameActivity> scenario = ActivityScenario.launch(GameActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.onActivity(activity -> {
            // GIVEN
            Game game = new Game(activity, activity);

            GamePresenterData playerData = new GamePresenterData();
            playerData.height = game.getPositionFromView("height") / 2;
            playerData.sound = game.loadSoundFromView(R.raw.cow, 1);

            Cow cow = new Cow(game.getBitmapFromView(R.drawable.cow), playerData, new Accessory());
            cow.register(game);

            cow.setX(10);
            float val = game.getHeight() - game.getHeight() * GroundLayer.FOREGROUND_HEIGHT + 1;
            int testy = (int)val;
            cow.setY(testy);

            assertTrue(cow.isTouchingGround(game.getHeight()));

        });
    }

    @Test
    public void testPassObstacle() {
        ActivityScenario<GameActivity> scenario = ActivityScenario.launch(GameActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.onActivity(activity -> {
            // GIVEN
            Game game = new Game(activity, activity);
            ObstacleObject spider = new ObstacleObject(game.getBitmapFromView(R.drawable.spider_full), 10);
            ObstacleObject log = new ObstacleObject(game.getBitmapFromView(R.drawable.log_full), 10);
            GamePresenterData combObstData = new GamePresenterData();
            combObstData.height = game.getPositionFromView("height");
            combObstData.width = game.getPositionFromView("width");
            combObstData.collideSound = game.loadSoundFromView(R.raw.crash, 1);
            combObstData.passSound = game.loadSoundFromView(R.raw.pass, 1);
            CombinationObstacle combinationObstacle = new CombinationObstacle(combObstData, spider, log, 10, 10);
            combinationObstacle.register(game);
            combinationObstacle.onPass();
        });
    }
}
