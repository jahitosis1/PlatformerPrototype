package game;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import entity.Player;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.Node;
import javafx.util.Duration;

public class GameTimer extends AnimationTimer {

    private final HashMap<KeyCode, Boolean> keys = new HashMap<>();
    private final ArrayList<Node> platforms = new ArrayList<>();
    private final ArrayList<Node> walls = new ArrayList<>();
    private final ArrayList<Node> traps = new ArrayList<>();

    private Player player;
    private int player_hp;
    private int player_coins;
    private Point2D playerVelocity = new Point2D(0, 0);
    private boolean canJump = true;

    private int levelWidth;
    private int levelHeight;
    private Label healthPoints;
    private Label time;
    private double gameTimer;
    private Label coins;
    private Pane pauseUI;
    private boolean isPaused = false;
    private boolean invulnerable = false;

    Rectangle matte;

    public GameTimer(Pane gameRoot, Pane uiRoot, Scene gameScene) {
        initContent(gameRoot, LevelData.LEVEL2);
        initUI(uiRoot);
        initPauseUI(uiRoot);

        gameScene.setOnKeyPressed(event -> keys.put(event.getCode(), true));
        gameScene.setOnKeyReleased(event -> keys.put(event.getCode(), false));
        // super ();
    }

    @Override
    public void handle(long currentTime) {
        update();
    }

    private void update() {
        if (!isPaused) {
            if (isPressed(KeyCode.W) && player.getTranslateY() >= 5) jumpPlayer();
            if (playerVelocity.getY() < 10) playerVelocity = playerVelocity.add(0, 0.3);
            if (isPressed(KeyCode.A) && player.getTranslateX() >= 5) movePlayerX(-2);
            if (isPressed(KeyCode.D) && player.getTranslateX() + 40 <= levelWidth - 5) movePlayerX(2);
            movePlayerY((int) playerVelocity.getY());
            if (!isPressed(KeyCode.W) && !isPressed(KeyCode.A) && !isPressed(KeyCode.S) && !isPressed(KeyCode.D))
                player.stopMoveAnimation();
            updateUI();
            if (player_hp <= 0) Platform.exit();
        }
    }

    private void initContent(Pane gameRoot, String[] levelData) {
        gameRoot.setPrefSize(1920 * 20, 1080);
        Image bg = new Image("images/colored_land.png");
        ImagePattern tile;
        Node platform;
        // create Background
        BackgroundImage backgroundimage = new BackgroundImage(bg,
                BackgroundRepeat.REPEAT,
                BackgroundRepeat.REPEAT,
                BackgroundPosition.DEFAULT,
                new BackgroundSize(2000, 1080.0, false, false, false, false));
        Background background = new Background(backgroundimage);
        levelWidth = levelData[0].length() * 60;
//        levelHeight = levelData.length * 60;
        gameRoot.setBackground(background);

        for (int i = 0; i < levelData.length; i++) {
            String line = levelData[i];
            for (int j = 0; j < line.length(); j++) {
                switch (line.charAt(j)) {
                    case '0':
                        break;
                    case '1':
                        tile = new ImagePattern(new Image("images/grassMid.png"));
                        platform = createEntity(j * 60, i * 60, 60, 60, tile, gameRoot);
                        platforms.add(platform);
                        break;
                    case '2':
                        tile = new ImagePattern(new Image("images/grassCenter.png"));
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
        uiRoot.setPrefSize(1920, 1080);
        Button pause = new Button("pause");
        pause.setLayoutX(1800);
        pause.setLayoutY(50);

        healthPoints = new Label();
        time = new Label();
        coins = new Label();

        healthPoints.setLayoutX(50);
        healthPoints.setLayoutY(50);
        healthPoints.setText("HP: " + player_hp);

        coins.setLayoutX(50);
        coins.setLayoutY(75);
        coins.setText("Coins: " + player_coins);

        time.setLayoutX((double) 1920 / 2);
        time.setLayoutY(50);
        time.setText("Time: " + player_coins);

        pause.setOnMouseClicked(e -> pauseGame(uiRoot));

        uiRoot.getChildren().addAll(pause, healthPoints, time, coins);
        startGameTimer();
    }

    private void initPauseUI(Pane uiRoot){

        pauseUI = new Pane();
        pauseUI.setPrefSize(500, 400);
        pauseUI.setLayoutX(710);
        pauseUI.setLayoutY(340);

        VBox layout = new VBox();
        layout.setAlignment(Pos.CENTER);
        layout.setPrefSize(500,400);
        layout.setSpacing(20);

        matte = new Rectangle(1920, 1080, Color.BLACK);
        matte.setOpacity(0.5);

        Rectangle bg = new Rectangle(500, 400, Color.LIGHTBLUE);
        Button resume = new Button("resume");
        Button quit = new Button("quit");
        resume.setMinSize(100, 50);
        resume.setMaxSize(100, 50);
        quit.setMinSize(100, 50);
        quit.setMaxSize(100, 50);
        resume.setOnMouseClicked(event -> resumeGame(uiRoot));
        quit.setOnMouseClicked(event -> Platform.exit());

        layout.getChildren().addAll(resume, quit);
        pauseUI.getChildren().addAll(bg, layout);

    }

    private void updateUI(){
        healthPoints.setText("HP: " + player_hp);
        coins.setText("Coins: " + player_coins);
        time.setText("Time: " + gameTimer);
    }

    private void pauseGame(Pane uiRoot) {
        uiRoot.getChildren().addAll(matte, pauseUI);
        isPaused = true;
    }

    private void resumeGame(Pane uiRoot) {
        uiRoot.getChildren().removeAll(pauseUI, matte);
        isPaused = false;
    }

    private void startGameTimer(){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            int count = 0;
            @Override
            public void run() {
                if (!isPaused) {
                    count++;
                    gameTimer = (double) count/100;
//                    Platform.runLater(() -> gameTimer = count);
                }
            }
        }, 0, 10); // Start the timer immediately and update every 1000 milliseconds (1 second)
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
            for (Node trap : traps) {
                Bounds trapBounds = trap.getBoundsInParent();
                double trapHalfHeight = trapBounds.getHeight() / 2;

                Bounds bottomTrapBounds = new BoundingBox(
                        trapBounds.getMinX(),
                        trapBounds.getMinY() + trapHalfHeight,
                        trapBounds.getWidth(),
                        trapHalfHeight
                );

                if (player.getBoundsInParent().intersects(bottomTrapBounds)) {
                    if (!invulnerable) {
                        damagePlayer();
                        invulnerabilityTimer();
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
            for (Node trap : traps) {
                if (player.getBoundsInParent().intersects(trap.getBoundsInParent())) {
                    if (!invulnerable) {
                        damagePlayer();
                        invulnerabilityTimer();
                    }
                }
            }
            player.setTranslateY(player.getTranslateY() + (movingDown ? 1 : -1));
        }
    }

    private void invulnerabilityTimer(){
        // Change invulnerable to true
        invulnerable = true;

        // Create a FadeTransition on the player
        FadeTransition fade = new FadeTransition(Duration.seconds(3), player);
        fade.setFromValue(1.0);
        fade.setToValue(0.0);
        fade.setDuration(Duration.millis(250));
        fade.setAutoReverse(true);
        fade.setCycleCount(Transition.INDEFINITE);

        // Start the FadeTransition
        fade.play();

        // Create a PauseTransition with a duration of 3 seconds
        PauseTransition pause = new PauseTransition(Duration.seconds(1));

        // Set a ChangeListener to change invulnerable back to false after the pause
        pause.setOnFinished(event -> {
            invulnerable = false;
            fade.stop();
        });

        // Start the PauseTransition
        pause.play();
    }

    private void jumpPlayer() {
        if (canJump) {
            playerVelocity = playerVelocity.add(0, -23);
            canJump = false;
            player.startJumpAnimation();
        }
    }

    private void damagePlayer(){
        player_hp--;
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

    private boolean isPressed(KeyCode key) {
        return keys.getOrDefault(key, false);
    }

}
