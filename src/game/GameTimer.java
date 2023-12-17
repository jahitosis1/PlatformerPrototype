package game;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import entity.*;
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
import javafx.stage.Stage;
import javafx.util.Duration;

public class GameTimer extends AnimationTimer {

    private final HashMap<KeyCode, Boolean> keys = new HashMap<>();
    private final ArrayList<Node> platforms = new ArrayList<>();
    private final ArrayList<Node> walls = new ArrayList<>();
    private final ArrayList<Node> traps = new ArrayList<>();
    private final ArrayList<Node> npcs = new ArrayList<>();
    private final ArrayList<Node> coins = new ArrayList<>();
    private final ArrayList<Enemy> enemies = new ArrayList<>();
    private final String[] levelData;
    private int player_hp;
    private int player_coins;
    private int levelWidth;
    private int levelHeight;
    private double gameTimer;
    private double fallSpeed = 1.0;
    private Player player;
    private Point2D playerVelocity = new Point2D(0, 0);
    private Label healthPoints;
    private Label time;
    private Label coin_count;
    private Pane pauseUI;
    private Pane gameOverUI;
    private Pane stageClearUI;
    private boolean canJump = true;
    private boolean isPaused = false;
    private boolean invulnerable = false;
    private final Scene mainMenu;
    private final Stage primaryStage;
    private final Pane gameRoot;
    private final Pane uiRoot;

    Rectangle matte;

    public GameTimer(Pane gameRoot, Pane uiRoot, Scene gameScene, Stage primaryStage, Scene previousScene, String[] levelData) {
        this.mainMenu = previousScene;
        this.primaryStage = primaryStage;
        this.gameRoot = gameRoot;
        this.uiRoot = uiRoot;
        this.levelData = levelData;
        clearAll();
        initContent(gameRoot, levelData);
        initUI(uiRoot);
        initPauseUI(uiRoot);
        initClearUI(uiRoot);
        initGameOverUI(uiRoot);
        isPaused = false;

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
            moveEnemy();
            collideCoin();
            collideEnemy();
            updateUI();
            if (player_hp <= 0) gameOver(uiRoot);
            if (isCleared()) stageClear(uiRoot);
        }
    }

    public void clearAll(){
        platforms.clear();
        walls.clear();
        traps.clear();
        npcs.clear();
        coins.clear();
        enemies.clear();
        player_coins = 0;
        player_hp = 10;
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
                    case '4':
                        Node wall = createEntity(j * 60, i * 60, 60, 60, new ImagePattern(new Image("images/grassCenter.png")), gameRoot);
                        walls.add(wall);
                        break;
                    case '5':
                        NPC npc = createNPC(j*60, i*60,gameRoot);
                        npcs.add(npc);
                        break;
                    case '6':
                        Coin coin = createCoin(j*60, i*60,gameRoot);
                        coins.add(coin);
                        break;
                    case '7':
                        BasicEnemy enemy = createEnemy(j*60, i*60,gameRoot);
                        enemies.add(enemy);
                        break;
                    default:
                        break;
                }
            }
        }

        player = createPlayer(60, 900, gameRoot);
        player.translateXProperty().addListener((obs, old, newValue) -> {
            int offset = newValue.intValue();
            if (offset > GameStage.WINDOW_WIDTH / 2 && offset < levelWidth - GameStage.WINDOW_WIDTH / 2) {
                gameRoot.setLayoutX(-(offset - -40 - (double) GameStage.WINDOW_WIDTH / 2));
            }
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
        coin_count = new Label();

        healthPoints.setLayoutX(50);
        healthPoints.setLayoutY(50);
        healthPoints.setText("HP: " + player_hp);

        coin_count.setLayoutX(50);
        coin_count.setLayoutY(75);
        coin_count.setText("Coins: " + player_coins);

        time.setLayoutX((double) 1920 / 2);
        time.setLayoutY(50);
        time.setText("Time: " + player_coins);

        pause.setOnMouseClicked(e -> pauseGame(uiRoot));

        uiRoot.getChildren().addAll(pause, healthPoints, time, coin_count);
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
        Button menu = new Button("main menu");
        Button quit = new Button("quit");
        resume.setMinSize(100, 50);
        resume.setMaxSize(100, 50);
        menu.setMinSize(100, 50);
        menu.setMaxSize(100, 50);
        quit.setMinSize(100, 50);
        quit.setMaxSize(100, 50);
        resume.setOnMouseClicked(event -> resumeGame(uiRoot));
        menu.setOnMouseClicked(event -> backToMenu());
        quit.setOnMouseClicked(event -> Platform.exit());

        layout.getChildren().addAll(resume, menu, quit);
        pauseUI.getChildren().addAll(bg, layout);

    }
    private void initClearUI(Pane uiRoot){

        stageClearUI = new Pane();
        stageClearUI.setPrefSize(500, 400);
        stageClearUI.setLayoutX(710);
        stageClearUI.setLayoutY(340);

        HBox layout = new HBox();
        layout.setAlignment(Pos.CENTER);
        layout.setPrefSize(500,400);
        layout.setSpacing(20);
        
        VBox layout2 = new VBox();
        layout2.setAlignment(Pos.CENTER);
        layout2.setPrefSize(500,400);
        layout2.setSpacing(20);

        HBox layout3 = new HBox();
        layout3.setAlignment(Pos.CENTER);
        layout3.setPrefSize(500,400);
        layout3.setSpacing(20);

        matte = new Rectangle(1920, 1080, Color.BLACK);
        matte.setOpacity(0.5);

        Rectangle bg = new Rectangle(500, 400, Color.LIGHTBLUE);
        Button next_level = new Button("next level");
        Button menu = new Button("main menu");
        Button quit = new Button("quit");
        next_level.setMinSize(100, 50);
        next_level.setMaxSize(100, 50);
        menu.setMinSize(100, 50);
        menu.setMaxSize(100, 50);
        quit.setMinSize(100, 50);
        quit.setMaxSize(100, 50);
        next_level.setOnMouseClicked(event -> nextLevel());
        menu.setOnMouseClicked(event -> backToMenu());
        quit.setOnMouseClicked(event -> Platform.exit());

        Label cleared = new Label();
        cleared.setText("Stage CLear!");

        layout3.getChildren().addAll(cleared);
        layout.getChildren().addAll(next_level, menu, quit);
        layout2.getChildren().addAll(layout3, layout);
        stageClearUI.getChildren().addAll(bg, layout2);

    }
    private void initGameOverUI(Pane uiRoot){

        gameOverUI = new Pane();
        gameOverUI.setPrefSize(500, 400);
        gameOverUI.setLayoutX(710);
        gameOverUI.setLayoutY(340);

        HBox layout = new HBox();
        layout.setAlignment(Pos.CENTER);
        layout.setPrefSize(500,400);
        layout.setSpacing(20);

        VBox layout2 = new VBox();
        layout2.setAlignment(Pos.CENTER);
        layout2.setPrefSize(500,400);
        layout2.setSpacing(20);

        HBox layout3 = new HBox();
        layout3.setAlignment(Pos.CENTER);
        layout3.setPrefSize(500,400);
        layout3.setSpacing(20);

        matte = new Rectangle(1920, 1080, Color.BLACK);
        matte.setOpacity(0.5);

        Rectangle bg = new Rectangle(500, 400, Color.LIGHTBLUE);
        Button menu = new Button("main menu");
        Button quit = new Button("quit");
        menu.setMinSize(100, 50);
        menu.setMaxSize(100, 50);
        quit.setMinSize(100, 50);
        quit.setMaxSize(100, 50);
        menu.setOnMouseClicked(event -> backToMenu());
        quit.setOnMouseClicked(event -> Platform.exit());

        Label over = new Label();
        over.setText("You Died!");

        layout3.getChildren().addAll(over);
        layout.getChildren().addAll(menu, quit);
        layout2.getChildren().addAll(layout3, layout);
        gameOverUI.getChildren().addAll(bg, layout2);

    }

    private void updateUI(){
        healthPoints.setText("HP: " + player_hp);
        coin_count.setText("Coins: " + player_coins);
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
    private void gameOver(Pane uiRoot) {
        uiRoot.getChildren().addAll(matte, gameOverUI);
        isPaused = true;
    }
    private void stageClear(Pane uiRoot) {
        if (levelData == LevelData.LEVEL1) StageMenu.levels.put("level2", true);
        if (levelData == LevelData.LEVEL2) StageMenu.levels.put("level3", true);
        uiRoot.getChildren().addAll(matte, stageClearUI);
        isPaused = true;
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
            for (Node wall : walls) {
                if (player.getTranslateX() + 40 == wall.getTranslateX()) {
                    player.setTranslateX(player.getTranslateX() - 1);
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
//            for (Node wall : walls) {
//                if (player.getBoundsInParent().intersects(wall.getBoundsInParent())) {
//                    fallSpeed *= 0.5; // Decrease the speed of fall by 50%
//                    canJump = true;
//                }
//            }
            player.setTranslateY(player.getTranslateY() + (movingDown ? fallSpeed*0.5 : -fallSpeed));
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

    public void moveEnemy() {
        for (Enemy enemy : enemies) {
            enemy.move();
        }
    }

    private void collideCoin() {
        if (coins.isEmpty()) return;
        for (Node coin : coins) {
            if (player.getBoundsInParent().intersects(coin.getBoundsInParent())) {
                player_coins++;
                gameRoot.getChildren().remove(coin);
                coins.remove(coin);
            }
        }
    }

    private void collideEnemy() {
        for (Enemy enemy : enemies) {
            if (player.getBoundsInParent().intersects(enemy.getBoundsInParent())) {
                if (!invulnerable) {
                    damagePlayer();
                    invulnerabilityTimer();
                }
            }
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
        primaryStage.setFullScreen(true);
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
        primaryStage.setFullScreen(true);
        primaryStage.setResizable(false);
    }

    private boolean isPressed(KeyCode key) {
        return keys.getOrDefault(key, false);
    }

}
