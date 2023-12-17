package game;

import java.io.PrintStream;
import java.net.StandardSocketOptions;
import java.util.AbstractCollection;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

import entities.Player;
import javafx.animation.AnimationTimer;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.Node;
import javafx.util.Duration;

public class GameTimer extends AnimationTimer {

    private HashMap<KeyCode, Boolean> keys = new HashMap<KeyCode, Boolean>();
    private ArrayList<Node> platforms = new ArrayList<Node>();
    private ArrayList<Node> walls = new ArrayList<Node>();
    private ArrayList<Node> traps = new ArrayList<Node>();

    private Player player;
    private int player_hp;
    private int player_coins;
    private Point2D playerVelocity = new Point2D(0, 0);
    private boolean canJump = true;
    private ImagePattern tile;
    private Node platform;

    private int levelWidth;
    private int levelHeight;
    private Label healthPoints;
    private Label timer;
    private Label coins;

    public GameTimer(Pane gameRoot, Pane uiRoot, Scene gameScene) {
        initContent(gameRoot);
        initUI(uiRoot);

        gameScene.setOnKeyPressed(event -> keys.put(event.getCode(), true));
        gameScene.setOnKeyReleased(event -> keys.put(event.getCode(), false));
        // super ();
    }

    @Override
    public void handle(long currentTime) {
        update();
    }

    private void update() {
        if (isPressed(KeyCode.W) && player.getTranslateY() >= 5) jumpPlayer();
        if (playerVelocity.getY() < 10) playerVelocity = playerVelocity.add(0, 0.3);
        if (isPressed(KeyCode.A) && player.getTranslateX() >= 5) movePlayerX(-3);
        if (isPressed(KeyCode.D) && player.getTranslateX() + 40 <= levelWidth - 5) movePlayerX(3);
        movePlayerY((int) playerVelocity.getY());
        if (!isPressed(KeyCode.W) && !isPressed(KeyCode.A) && !isPressed(KeyCode.S) && !isPressed(KeyCode.D))
            player.stopMoveAnimation();
        updateUI();
    }

    private void initContent(Pane gameRoot) {
        gameRoot.setPrefSize(1920 * 20, 1080);
        Image bg = new Image("images/City3.png");
<<<<<<< Updated upstream
=======
        ImagePattern tile;
        Node platform;
>>>>>>> Stashed changes
        // create Background
        BackgroundImage backgroundimage = new BackgroundImage(bg,
                BackgroundRepeat.REPEAT,
                BackgroundRepeat.REPEAT,
                BackgroundPosition.DEFAULT,
                new BackgroundSize(2000, 1080.0, false, false, false, false));
        Background background = new Background(backgroundimage);
        levelWidth = LevelData.LEVEL2[0].length() * 60;
//        levelHeight = LevelData.LEVEL2.length * 60;
        gameRoot.setBackground(background);

        for (int i = 0; i < LevelData.LEVEL2.length; i++) {
            String line = LevelData.LEVEL2[i];
            for (int j = 0; j < line.length(); j++) {
                switch (line.charAt(j)) {
                    case '0':
                        break;
                    case '1':
                        tile = new ImagePattern(new Image("images/Crate.png"));
                        platform = createEntity(j * 60, i * 60, 60, 60, tile, gameRoot);
                        platforms.add(platform);
                        break;
                    case '2':
                        tile = new ImagePattern(new Image("images/StreetTile1.png"));
                        platform = createEntity(j * 60, i * 60, 60, 60, tile, gameRoot);
                        platforms.add(platform);
                        break;
                    case '3':
                        Node spike = createEntity(j * 60, i * 60, 60, 60, new ImagePattern(new Image("images/spikes.png")), gameRoot);
                        traps.add(spike);
                        break;
                    default:
                        break;
                }
            }
        }

        player = createPlayer(60, 900, gameRoot);
        player.translateXProperty().addListener((obs, old, newValue) -> {
            int offset = newValue.intValue();
            if (offset > 640 && offset < levelWidth - 640) gameRoot.setLayoutX(-(offset - 640));
        });
//        player.translateYProperty().addListener((obs, old, newValue) -> {
//            int offset = newValue.intValue();
//            if (offset > 800 && offset < levelHeight - 800) {
//                gameRoot.setLayoutY(-(offset - 540));
//            } else if (offset >= levelHeight - 640) {
//                gameRoot.setLayoutY(-(levelHeight - 540 - 540));
//            }
//        });
//    rotatePlayer();

        player_hp = 10;
        player_coins = 0;
    }

    private void initUI(Pane uiRoot) {
        Button pause = new Button("pause");
        pause.setLayoutX(1800);
        pause.setLayoutY(50);

        healthPoints = new Label();
        timer = new Label();
        coins = new Label();

        healthPoints.setLayoutX(50);
        healthPoints.setLayoutY(50);
        healthPoints.setText("HP: " + player_hp);

        coins.setLayoutX(50);
        coins.setLayoutY(75);
        coins.setText("Coins: " + player_coins);

        timer.setLayoutX(1920 / 2);
        timer.setLayoutY(50);
        timer.setText("Time: " + player_coins);

        pause.setOnMouseClicked(e -> pauseGame());

        uiRoot.getChildren().addAll(pause, healthPoints, timer, coins);
    }

    private void updateUI(){
        healthPoints.setText("HP: " + player_hp);
        coins.setText("Coins: " + player_coins);
        timer.setText("Time: " + player_coins);
    }

    private void pauseGame() {
        System.out.println("Game is paused!");
    }

    private void rotatePlayer() {
        RotateTransition rotate = new RotateTransition();
        rotate.setNode(player);
        rotate.setByAngle(360);
        rotate.setDuration(Duration.millis(1000));
        rotate.setCycleCount(RotateTransition.INDEFINITE);
        rotate.setInterpolator(Interpolator.LINEAR);
        rotate.play();
    }

    private void movePlayerX(int value) {
        boolean movingRight = value > 0;

        for (int i = 0; i < Math.abs(value); i++) {
            for (Node platform : platforms) {
                if (player.getBoundsInParent().intersects(platform.getBoundsInParent())) {
                    if (movingRight) {
                        if (player.getTranslateX() + 40 == platform.getTranslateX()) return;
                    } else {
                        if (player.getTranslateX() == platform.getTranslateX() + 60) return;
                    }
                }
            }
            player.setScaleX((double) value / Math.abs(value));
            player.setTranslateX(player.getTranslateX() + (movingRight ? 1 : -1));
            player.startMoveAnimation();
        }
    }

    private void movePlayerY(int value) {
        boolean movingDown = value > 0;

        for (int i = 0; i < Math.abs(value); i++) {
            for (Node platform : platforms) {
                if (player.getBoundsInParent().intersects(platform.getBoundsInParent())) {
                    if (movingDown) {
                        if (player.getTranslateY() + 40 == platform.getTranslateY()) {
                            player.setTranslateY(player.getTranslateY() - 1);
                            canJump = true;
                            player.stopJumpAnimation();
                            return;
                        }
                    } else {
                        if (player.getTranslateY() == platform.getTranslateY() + 60) return;
                    }
                }
            }
            player.setTranslateY(player.getTranslateY() + (movingDown ? 1 : -1));
        }
    }

    private void jumpPlayer() {
        if (canJump) {
            playerVelocity = playerVelocity.add(0, -25);
            canJump = false;
            player.startJumpAnimation();
        }
    }

    private Node createEntity(int x, int y, int width, int height, ImagePattern fill, Pane gameRoot) {
        Rectangle entity = new Rectangle(width, height);
        entity.setTranslateX(x);
        entity.setTranslateY(y);
        entity.setFill(fill);

        gameRoot.getChildren().add(entity);
        return entity;
    }

    private Player createPlayer(int x, int y, Pane gameRoot) {
        Player entity = new Player(x, y);

        gameRoot.getChildren().add(entity);
        return entity;
    }
<<<<<<< Updated upstream
=======
    private NPC createNPC(int x, int y, Pane gameRoot) {
        NPC entity = new NPC(x, y);

        gameRoot.getChildren().add(entity);
        return entity;
    }
    private Coin createCoin(int x, int y, Pane gameRoot) {
        Coin entity = new Coin(x, y);

        gameRoot.getChildren().add(entity);
        return entity;
    }
    private BasicEnemy createEnemy(int x, int y, Pane gameRoot) {
        BasicEnemy entity = new BasicEnemy(x, y);

        gameRoot.getChildren().add(entity);
        return entity;
    }

    private void backToMenu() {
        this.stop();
        primaryStage.setScene(mainMenu);
        primaryStage.setFullScreen(false);
        primaryStage.setResizable(false);
    }

    private boolean isCleared() {
        if (levelData == LevelData.LEVEL1) {
            return gameTimer >= 10;
        }
        if (levelData == LevelData.LEVEL2) {
            return coins.isEmpty() && enemies.isEmpty();
        }
        if (levelData == LevelData.LEVEL3) {
            return gameTimer >= 10;
        }
        if (levelData == LevelData.BONUS_LEVEL) {
            return gameTimer >= 10;
        }
        return false;
    }

    private void nextLevel() {
        // instead na ganito, gagawa nalang ako ng level picker tas dun babalik
        // kumbaga mauunlock lang ung next levels
        this.stop();
        primaryStage.setScene(mainMenu);
        primaryStage.setFullScreen(false);
        primaryStage.setResizable(false);
    }
>>>>>>> Stashed changes

    private boolean isPressed(KeyCode key) {
        return keys.getOrDefault(key, false);
    }

}
