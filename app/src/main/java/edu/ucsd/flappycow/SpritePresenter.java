package edu.ucsd.flappycow;

import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.flappycow.model.sprites.Accessory;
import edu.ucsd.flappycow.model.sprites.DrawablePauseButton;
import edu.ucsd.flappycow.model.sprites.DrawableRainbow;
import edu.ucsd.flappycow.model.sprites.DrawableTutorial;
import edu.ucsd.flappycow.model.sprites.GroundLayer;
import edu.ucsd.flappycow.model.sprites.nonplayables.Coin;
import edu.ucsd.flappycow.model.sprites.nonplayables.CombinationObstacle;
import edu.ucsd.flappycow.model.sprites.nonplayables.ObstacleObject;
import edu.ucsd.flappycow.model.sprites.nonplayables.PowerUp;
import edu.ucsd.flappycow.model.sprites.nonplayables.Toast;
import edu.ucsd.flappycow.model.sprites.nonplayables.Virus;
import edu.ucsd.flappycow.model.sprites.playables.Cow;
import edu.ucsd.flappycow.model.sprites.playables.NyanCat;
import edu.ucsd.flappycow.model.sprites.playables.PlayableCharacter;
import edu.ucsd.flappycow.util.GamePresenterData;
import edu.ucsd.flappycow.util.Util;

public class SpritePresenter implements GameModel {
    private PlayableCharacter player;
    private GroundLayer background;
    private GroundLayer frontground;
    private List<CombinationObstacle> obstacles = new ArrayList<>();
    private List<PowerUp> powerUps = new ArrayList<>();

    private DrawablePauseButton drawablePauseButton;

    private DrawableTutorial drawableTutorial;

    private GamePresenter gamePresenter;

    public SpritePresenter(GamePresenter gamePresenter) {
        this.gamePresenter = gamePresenter;
        GamePresenterData playerData = new GamePresenterData();
        playerData.height = gamePresenter.getPositionFromView("height") / 2;
        playerData.sound = gamePresenter.loadSoundFromView(R.raw.cow, 1);
        player = new Cow(gamePresenter.getBitmapFromView(R.drawable.cow), playerData, new Accessory());
        player.register(gamePresenter);
        background = new GroundLayer(gamePresenter.getBitmapFromView(R.drawable.bg));
        frontground = new GroundLayer(gamePresenter.getBitmapFromView(R.drawable.fg));
        drawablePauseButton = new DrawablePauseButton(gamePresenter.getBitmapFromView(R.drawable.pause_button));
        drawableTutorial = new DrawableTutorial(gamePresenter.getBitmapFromView(R.drawable.tutorial));
    }

    public int getPlayerX() {
        return player.getX();
    }

    public void setPlayerX(int x) {
        player.setX(x);
    }

    public void setPlayerY(int y) {
        player.setY(y);
    }

    public boolean playerIsNyanCat() {
        return player.getClass() == NyanCat.class;
    }

    public boolean playerIsDead() {
        return this.player.isDead();
    }

    public boolean pauseButtonIsTouching(int x, int y) {
        return drawablePauseButton.isTouching(x, y);
    }

    public void playerOnTap(int height) {
        this.player.onTap(height);
    }

    public void playerMove(int width, int height) {
        player.move(width, height);
    }

    public void setPauseButtonX(int viewWidth) {
        drawablePauseButton.setX(viewWidth - drawablePauseButton.getWidth());
    }

    public void drawTutorial(int viewWidth, int viewHeight, Canvas canvas) {
        drawableTutorial.setX(viewWidth / 2 - drawableTutorial.getWidth() / 2);
        drawableTutorial.setY(viewHeight / 2 - drawableTutorial.getHeight() / 2);
        drawableTutorial.draw(canvas);
    }

    public void showTutorial(int viewWidth, int viewHeight, Canvas canvas) {
        playerMove(viewWidth, viewHeight);
        setPauseButtonX(viewWidth);
        drawTutorial(viewWidth, viewHeight, canvas);
    }

    public void drawCanvas(Canvas canvas, boolean drawPlayer) {
        background.draw(canvas);
        for (CombinationObstacle r : obstacles) {
            r.draw(canvas);
        }
        for (PowerUp p : powerUps) {
            p.draw(canvas);
        }
        if (drawPlayer) {
            player.draw(canvas);
        }
        frontground.draw(canvas);
        drawablePauseButton.draw(canvas);
    }

    public void playerDead(int viewHeight) {
        player.dead(viewHeight);
    }

    public boolean playerIsTouchingGround(int viewHeight) {
        return player.isTouchingGround(viewHeight);
    }

    public void checkPasses(int points, int viewWidth, int speedX) {
        for (CombinationObstacle o : obstacles) {
            if (o.isPassed()) {
                if (!o.isAlreadyPassed) {    // probably not needed
                    if(o.onPass()) {
                        gamePresenter.Handler("increasePoints");
                        gamePresenter.Handler("playSound", o.getPassSound(), Util.volume / o.getSoundVolumeDivider(), Util.volume / o.getSoundVolumeDivider(), 0, 0, 1.f);
                    }
                    createPowerUp(points, viewWidth, speedX);
                }
            }
        }
    }

    public void createPowerUp(int points, int viewWidth, int speedX) {
        // Toast
        if (points >= Toast.POINTS_TO_TOAST /*&& powerUps.size() < 1*/ && !(player instanceof NyanCat)) {
            // If no powerUp is present and you have more than / equal 42 points
            if (points == Toast.POINTS_TO_TOAST) {    // First time 100 % chance
                Toast toast = new Toast(gamePresenter.getBitmapFromView(R.drawable.toast), viewWidth, speedX, player.getX());
                toast.register(gamePresenter);
                powerUps.add(toast);
            } else if (Math.random() * 100 < 33) {    // 33% chance
                Toast toast = new Toast(gamePresenter.getBitmapFromView(R.drawable.toast), viewWidth, speedX, player.getX());
                toast.register(gamePresenter);
                powerUps.add(toast);
            }
        }

        if ((powerUps.size() < 1) && (Math.random() * 100 < 20)) {
            // If no powerUp is present and 20% chance
            Coin coin = new Coin(gamePresenter.getBitmapFromView(R.drawable.coin), gamePresenter.loadSoundFromView(R.raw.coin, 1), viewWidth, speedX, player.getX());
            coin.register(gamePresenter);
            powerUps.add(coin);
        }

        if ((powerUps.size() < 1) && (Math.random() * 100 < 10)) {
            // If no powerUp is present and 10% chance (if also no coin)
            Virus virus = new Virus(gamePresenter.getBitmapFromView(R.drawable.virus), viewWidth, speedX, player.getX());
            virus.register(gamePresenter);
            powerUps.add(virus);
        }
    }

    public void checkOutOfRange() {
        for (int i = 0; i < obstacles.size(); i++) {
            if (this.obstacles.get(i).isOutOfRange()) {
                this.obstacles.remove(i);
                i--;
            }
        }
        for (int i = 0; i < powerUps.size(); i++) {
            if (this.powerUps.get(i).isOutOfRange()) {
                this.powerUps.remove(i);
                i--;
            }
        }
    }

    public boolean checkObstacleCollision() {
        for (CombinationObstacle o : obstacles) {
            if (o.isColliding(player, getCollisionTolerance())) {
                o.onCollision();
                return true;
            }
        }
        return false;
    }

    public int getCollisionTolerance() {
        // 25 @ 720x1280 px
        return gamePresenter.getPositionFromView("height") / 50;
    }

    public void checkPowerUpCollision() {
        for (int i = 0; i < powerUps.size(); i++) {
            if (this.powerUps.get(i).isColliding(player, getCollisionTolerance())) {
                int output = this.powerUps.get(i).onCollision();
                if (output == 1) {
                    this.changeToNyanCat();
                } else if (output == 2) {
                    this.changeToSick();
                }
                this.powerUps.remove(i);
                i--;
            }
        }
    }

    public boolean playerIsTouchingEdge(int viewHeight) {
        return player.isTouchingEdge(viewHeight);
    }

    public void createObstacle(int speedX) {
        if (obstacles.size() < 1) {
            ObstacleObject spider = new ObstacleObject(gamePresenter.getBitmapFromView(R.drawable.spider_full), player.getX());
            ObstacleObject log = new ObstacleObject(gamePresenter.getBitmapFromView(R.drawable.log_full), player.getX());
            GamePresenterData combObstData = new GamePresenterData();
            combObstData.height = gamePresenter.getPositionFromView("height");
            combObstData.width = gamePresenter.getPositionFromView("width");
            combObstData.collideSound = gamePresenter.loadSoundFromView(R.raw.crash, 1);
            combObstData.passSound = gamePresenter.loadSoundFromView(R.raw.pass, 1);
            CombinationObstacle combinationObstacle = new CombinationObstacle(combObstData, spider, log, speedX, player.getX());
            combinationObstacle.register(gamePresenter);
            obstacles.add(combinationObstacle);
        }
    }

    public void move(int viewWidth, int viewHeight, int speedX) {
        for (CombinationObstacle o : obstacles) {
            o.setSpeedX(-speedX);
            o.move();
        }
        for (PowerUp p : powerUps) {
            p.move();
        }

        background.setSpeedX(-speedX / 2);
        background.move();

        frontground.setSpeedX(-speedX * 4 / 3);
        frontground.move();

        setPauseButtonX(viewWidth);

        playerMove(viewWidth, viewHeight);
    }

    public void changeToNyanCat() {
        PlayableCharacter tmp = player;
        player = new NyanCat(gamePresenter.getBitmapFromView(R.drawable.nyan_cat), gamePresenter.getPositionFromView("height") / 2, new DrawableRainbow(gamePresenter.getBitmapFromView(R.drawable.rainbow)));
        player.setX(tmp.getX());
        player.setY(tmp.getY());
        player.setSpeedX(tmp.getSpeedX());
        player.setSpeedY(tmp.getSpeedY());
    }

    public void changeToSick() {
        player.wearMask(gamePresenter.getBitmapFromView(R.drawable.mask));
    }

    public void setupRevive(int viewHeight, int viewWidth) {
        player.setY(viewHeight / 2 - player.getWidth() / 2);
        player.setX(viewWidth / 6);
        obstacles.clear();
        powerUps.clear();
        player.revive(gamePresenter.getBitmapFromView(R.drawable.accessory_scumbag));
    }

    public void playerUpdateBitmap(int points) {
        player.upgradeBitmap(gamePresenter.getBitmapFromView(R.drawable.accessory_sir), gamePresenter.getBitmapFromView(R.drawable.accessory_sunglasses), points);
    }

}