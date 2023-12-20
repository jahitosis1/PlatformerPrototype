package game;

import entity.*;
import javafx.animation.AnimationTimer;
import javafx.animation.Interpolator;
import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class GameTimer extends AnimationTimer {

    private final HashMap<KeyCode, Boolean> keys = new HashMap<>();
    private final ArrayList<Node> platforms = new ArrayList<>();
    private final ArrayList<Node> walls = new ArrayList<>();
    private final ArrayList<Node> traps = new ArrayList<>();
    private final ArrayList<Node> npcs = new ArrayList<>();
    private final ArrayList<Node> coins = new ArrayList<>();
    private final ArrayList<Enemy> enemies = new ArrayList<>();
    private final String[] levelData;
    private final Scene mainMenu;
    private final Stage primaryStage;
    private final Pane gameRoot;
    private final Pane uiRoot;
    private int player_coins;
    private int enemies_killed;
    private int score_int;
    private int levelWidth;
    private int levelHeight;
    private int canJump = 2;
    private int jumpPower = 25;
    private double gameTimer;
    private double fallSpeed = 0.75;
    private Player player;
    private Point2D playerVelocity = new Point2D(0, 0);
    private Label healthPoints;
    private Label time;
    private Label coin_count;
    private Label score;
    private Label coins_collected;
    private Pane pauseUI;
    private Pane gameOverUI;
    private Pane stageClearUI;
    private boolean isPaused = false;
    private boolean doneOnce = false;
    private boolean pressedOnce = false;
    private boolean killBuff = false;
    private boolean invertedGravity = false;

    Rectangle matte;

    public GameTimer(Pane gameRoot, Pane uiRoot, Scene gameScene, Stage primaryStage, Scene previousScene, String[] levelData) {
        this.mainMenu = previousScene;
        this.primaryStage = primaryStage;
        this.gameRoot = gameRoot;
        this.uiRoot = uiRoot;
        this.levelData = levelData;
        initContent(gameRoot, levelData);
        initViewport();
        initUI(uiRoot);
        initPauseUI(uiRoot);
        initClearUI();
        initGameOverUI();
        isPaused = false;

        gameScene.setOnKeyPressed(event -> {
            if (event.getCode() != KeyCode.W) {
                keys.put(event.getCode(), true);
            }else{
                if (!pressedOnce) {
                    jumpPlayer();
                    pressedOnce = true;
                }
            }
        });
        gameScene.setOnKeyReleased(event -> {
            if (event.getCode() != KeyCode.W) {
                keys.put(event.getCode(), false);
            }else{
                pressedOnce = false;
            }
        });
        // super ();
    }

    @Override
    public void handle(long currentTime) {
        update();
    }

    private void update() {
        if (!isPaused) {
            if (playerVelocity.getY() < 10) playerVelocity = playerVelocity.add(0, 0.3);
            if (isPressed(KeyCode.A) && player.getTranslateX() >= 5) movePlayerX(-3);
            if (isPressed(KeyCode.D) && player.getTranslateX() + 40 <= levelWidth - 5) movePlayerX(3);
            movePlayerY((int) playerVelocity.getY());
            if (!isPressed(KeyCode.W) && !isPressed(KeyCode.A) && !isPressed(KeyCode.S) && !isPressed(KeyCode.D))
                player.stopMoveAnimation();
            moveEnemy();
            collideCoin();
            collideNPC();
            collideEnemy();
            collideTrap();
            updateUI();
            if (player.player_hp <= 0) gameOver(uiRoot);
            if (isCleared()) stageClear(uiRoot);
        }
    }

    public void clearAll() {
        platforms.clear();
        walls.clear();
        traps.clear();
        npcs.clear();
        coins.clear();
        enemies.clear();
        player_coins = 0;
        enemies_killed = 0;
        score_int = 0;
    }

    private void initContent(Pane gameRoot, String[] levelData) {
        clearAll();
        gameRoot.setPrefSize(1920 * 20, 1080*2);
        Image bg = new Image("images/City3.png", 0, 1080 * 2, false, true);

        ImagePattern tile;
        Node platform;
        // create Background
        BackgroundImage backgroundimage = new BackgroundImage(bg,
                BackgroundRepeat.REPEAT,
                BackgroundRepeat.REPEAT,
                BackgroundPosition.DEFAULT,
                new BackgroundSize(2000, 1080*2, false, false, false, false));
        Background background = new Background(backgroundimage);
        levelWidth = levelData[0].length() * 120;
        levelHeight = levelData.length * 120;
        gameRoot.setBackground(background);

        player = createPlayer(130, levelHeight - 550, gameRoot);
        player.player_hp = 10;

        for (int i = 0; i < levelData.length; i++) {
            String line = levelData[i];
            for (int j = 0; j < line.length(); j++) {
                switch (line.charAt(j)) {
                    case '1':
                        tile = new ImagePattern(new Image("images/Crate.png"));
                        platform = createMap(j * 120, i * 120, tile, gameRoot);
                        platforms.add(platform);
                        break;
                    case '2':
                        tile = new ImagePattern(new Image("images/StreetTile1.png"));
                        platform = createMap(j * 120, i * 120, tile, gameRoot);

                        platforms.add(platform);
                        break;
                    case '3':
                        Node spike = createMap(j * 120, i * 120, new ImagePattern(new Image("images/spikes.png")), gameRoot);
                        traps.add(spike);
                        break;
                    case '4':
                        Node wall = createMap(j * 120, i * 120, new ImagePattern(new Image("images/grassCenter.png")), gameRoot);
                        walls.add(wall);
                        break;
                    case '5':
                        NPC npc = createNPC(j * 120, i * 120, gameRoot);
                        npcs.add(npc);
                        break;
                    case '6':
                        Coin coin = createCoin(j * 120, i * 120, gameRoot);
                        coins.add(coin);
                        break;
                    case '7':
                        BasicEnemy enemy = createBasicEnemy(j * 120, i * 120, gameRoot);
                        enemies.add(enemy);
                        break;
                    case '8':
                        FlyingEnemy enemyE = createEliteEnemy(j * 120, i * 120, gameRoot);
                        enemies.add(enemyE);
                        break;
                    default:
                        break;
                }
            }
        }

    }

    private void initUI(Pane uiRoot) {
        uiRoot.setPrefSize(1920, 1080);
        Button pause = new Button("pause");
        pause.setLayoutX(1800);
        pause.setLayoutY(50);

        healthPoints = new Label();
        time = new Label();
        coin_count = new Label();

        ImageView hp_indicator = new ImageView("images/UI Elements/hudHeart.png");
        ImageView coin_indicator = new ImageView("images/UI Elements/Coin_0.png");

        hp_indicator.setFitWidth(100);
        hp_indicator.setFitHeight(100);
        coin_indicator.setFitWidth(100);
        coin_indicator.setFitHeight(100);

        hp_indicator.setPreserveRatio(true);
        coin_indicator.setPreserveRatio(true);

        healthPoints.setLayoutX(50);
        healthPoints.setLayoutY(50);
        healthPoints.setText("  " + player.player_hp);
        healthPoints.setFont(new Font("ArcadeClassic", 64));
        healthPoints.setStyle("-fx-text-fill: white;");
        healthPoints.setGraphic(hp_indicator);

        coin_count.setLayoutX(50);
        coin_count.setLayoutY(175);
        coin_count.setText("  " + player_coins);
        coin_count.setFont(new Font("ArcadeClassic", 64));
        coin_count.setStyle("-fx-text-fill: white;");
        coin_count.setGraphic(coin_indicator);

        time.setLayoutX((double) 1920 / 2 - 50);
        time.setLayoutY(50);
        time.setText("Time: " + player_coins);
        time.setFont(new Font("ArcadeClassic", 64));
        time.setStyle("-fx-text-fill: white;");

        pause.setOnMouseClicked(e -> pauseGame(uiRoot));

        uiRoot.getChildren().addAll(pause, healthPoints, time, coin_count);
        startGameTimer();
    }

    private void initPauseUI(Pane uiRoot) {

        pauseUI = new Pane();
        pauseUI.setPrefSize(500, 400);
        pauseUI.setLayoutX(710);
        pauseUI.setLayoutY(340);

        VBox layout = new VBox();
        layout.setAlignment(Pos.CENTER);
        layout.setPrefSize(500, 400);
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
        quit.setOnMouseClicked(event -> {
            Platform.exit();
            System.exit(0);
        });

        layout.getChildren().addAll(resume, menu, quit);
        pauseUI.getChildren().addAll(bg, layout);

    }

    private void initClearUI() {

        stageClearUI = new Pane();
        stageClearUI.setPrefSize(500, 400);
        stageClearUI.setLayoutX(710);
        stageClearUI.setLayoutY(340);

        HBox layout = new HBox();
        layout.setAlignment(Pos.CENTER);
        layout.setPrefSize(500, 400);
        layout.setSpacing(20);

        VBox layout2 = new VBox();
        layout2.setAlignment(Pos.CENTER);
        layout2.setPrefSize(500, 400);
        layout2.setSpacing(20);

        VBox layout3 = new VBox();
        layout3.setAlignment(Pos.CENTER);
        layout3.setPrefSize(500, 400);
        layout3.setSpacing(20);

        HBox layout4 = new HBox();
        layout4.setAlignment(Pos.CENTER);
        layout4.setPrefSize(500, 400);
        layout4.setSpacing(20);

        score = new Label();
        coins_collected = new Label();

        score.setFont(new Font("ArcadeClassic", 34));
//        score.setStyle("-fx-text-fill: white;");
        coins_collected.setFont(new Font("ArcadeClassic", 34));
//        coins_collected.setStyle("-fx-text-fill: white;");

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

        next_level.setOnAction(event -> nextLevel());
        menu.setOnAction(event -> backToMenu());
        quit.setOnAction(event -> {
            Platform.exit();
            System.exit(0);
        });

        Label cleared = new Label();
        cleared.setText("Stage CLear!");
        cleared.setFont(new Font("ArcadeClassic", 64));
//        cleared.setStyle("-fx-text-fill: white;");
        cleared.setTranslateY(50);

        layout4.getChildren().addAll(score, coins_collected);
        layout3.getChildren().addAll(cleared, layout4);
        layout.getChildren().addAll(next_level, menu, quit);
        layout2.getChildren().addAll(layout3, layout);
        stageClearUI.getChildren().addAll(bg, layout2);

    }

    private void initGameOverUI() {

        gameOverUI = new Pane();
        gameOverUI.setPrefSize(500, 400);
        gameOverUI.setLayoutX(710);
        gameOverUI.setLayoutY(340);

        HBox layout = new HBox();
        layout.setAlignment(Pos.CENTER);
        layout.setPrefSize(500, 400);
        layout.setSpacing(20);

        VBox layout2 = new VBox();
        layout2.setAlignment(Pos.CENTER);
        layout2.setPrefSize(500, 400);
        layout2.setSpacing(20);

        HBox layout3 = new HBox();
        layout3.setAlignment(Pos.CENTER);
        layout3.setPrefSize(500, 400);
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
        menu.setOnAction(event -> backToMenu());
        quit.setOnAction(event -> {
            Platform.exit();
            System.exit(0);
        });

        Label over = new Label();
        over.setText("You Died!");
        over.setFont(new Font("ArcadeClassic", 64));
//        over.setStyle("-fx-text-fill: white;");

        layout3.getChildren().addAll(over);
        layout.getChildren().addAll(menu, quit);
        layout2.getChildren().addAll(layout3, layout);
        gameOverUI.getChildren().addAll(bg, layout2);

    }
    private void initViewport(){
        player.translateXProperty().addListener((obs, old, newValue) -> {
            int offset = newValue.intValue();
            if (offset > GameStage.WINDOW_WIDTH / 2 && offset < levelWidth - GameStage.WINDOW_WIDTH / 2) {
                gameRoot.setLayoutX(-(offset - (double) GameStage.WINDOW_WIDTH / 2));
            }
        });

        player.translateYProperty().addListener((obs, old, newValue) -> {
            int offset = newValue.intValue();
            int windowHeight = 1080; // The height of the window
            if (offset > windowHeight * 3 / 4 && offset < levelHeight - windowHeight / 2) {
                gameRoot.setLayoutY(-(offset - (double) windowHeight / 2));
            }
        });
    }

    private void updateUI() {
        healthPoints.setText("  " + player.player_hp);
        coin_count.setText("  " + player_coins);
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
        score_int = ((100/(int) gameTimer) + player_coins - (10 - player.player_hp) + enemies_killed) * 100;
        score.setText("score: " + score_int);
        coins_collected.setText("Coins: " + player_coins);

        isPaused = true;
    }

    private void startGameTimer() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            int count = 0;

            @Override
            public void run() {
                if (!isPaused) {
                    count++;
                    gameTimer = (double) count / 100;
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
                        if (player.getTranslateX() + 80 == platform.getTranslateX()) return;
                    } else {
                        if (player.getTranslateX() == platform.getTranslateX() + 120) return;
                    }
                }
            }
            for (Node wall : walls) {
                if (player.getTranslateX() + 80 >= wall.getTranslateX()) {
                    player.setTranslateX(player.getTranslateX() - 1);
                    if (!doneOnce){
                        fallSpeed *= 0.1;
                        doneOnce = true;
                        canJump = 1;
                    }
                }else if (player.getTranslateX() + 80 <= wall.getTranslateX() - 10){
                    fallSpeed = 0.75;
                    doneOnce = false;
                }
            }
            player.setScaleX((double) value / Math.abs(value));
            player.setTranslateX(player.getTranslateX() + (movingRight ? 1 : -1));
            player.startMoveAnimation();
        }
    }

    private void movePlayerY(int value) {
        boolean movingDown = value > 0;
        if (invertedGravity) movingDown = !movingDown;

        for (int i = 0; i < Math.abs(value); i++) {
            for (Node platform : platforms) {
                if (player.getBoundsInParent().intersects(platform.getBoundsInParent())) {
                    if (movingDown) {
                        if (player.getTranslateY() + 80 >= platform.getTranslateY()
                                && player.getTranslateY() + 80 < platform.getTranslateY() + 100
                                && (player.getTranslateX() + 80 != platform.getTranslateX()
                                && player.getTranslateX() != platform.getTranslateX() + 120)) {
                            player.setTranslateY(player.getTranslateY() - 1);
                            canJump = 2;
                            player.stopJumpAnimation();
                            return;
                        }
                    } else {
                        if (player.getTranslateY() <= platform.getTranslateY() + 120) return;
                    }
                }
            }
            player.setTranslateY(player.getTranslateY() + (movingDown ? fallSpeed * 0.5 : -fallSpeed));
        }
    }

    private void jumpPlayer() {
        if (canJump > 0) {
            playerVelocity = playerVelocity.add(0, -jumpPower);
            canJump--;
            player.startJumpAnimation();
        }
    }

    public void moveEnemy() {
        for (Enemy enemy : enemies) {
            enemy.move();

            for (Node platform : platforms) {
                if (enemy.getBoundsInParent().intersects(platform.getBoundsInParent())) {
                    if (enemy.getTranslateY() + 100 == platform.getTranslateY()) {
                        enemy.setTranslateY(enemy.getTranslateY() - 1);
                        return;
                    }
                    if (enemy.getTranslateX() + 100 == platform.getTranslateX()) return;
                }
            }
            if (enemy instanceof BasicEnemy) enemy.setTranslateY(enemy.getTranslateY() + 1);
        }
    }

    private void collideCoin() {
        if (coins.isEmpty()) return;
        for (Node coin : coins) {
            if (player.getBoundsInParent().intersects(coin.getBoundsInParent())) {
                player_coins++;
                coins.remove(coin);
                gameRoot.getChildren().remove(coin);
            }
        }
    }
    private void collideNPC() {
        if (npcs.isEmpty()) return;
        for (Node npc : npcs) {
            if (player.getBoundsInParent().intersects(npc.getBoundsInParent())) {
                jumpPower = 35;
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    int count = 0;

                    @Override
                    public void run() {
                        if (!isPaused) {
                            count++;
                            if (count > 3) {
                                jumpPower = 25;
                                this.cancel();
                            }
                        }
                    }
                }, 0, 1000); // Start the timer immediately and update every 1000 milliseconds (1 second)
            }
        }
    }

    private void collideEnemy() {
        for (Enemy enemy : enemies) {
            if (player.getBoundsInParent().intersects(enemy.getBoundsInParent())) {
                if (enemy instanceof FlyingEnemy) {
                    player.damagePlayer(5);
                    if (!killBuff) continue;
                }
                if (player.getTranslateY() + 80 > enemy.getTranslateY() + 10) player.damagePlayer(3);
                enemies.remove(enemy);
                gameRoot.getChildren().remove(enemy);
            }
            double distance = Math.sqrt(Math.pow(player.getTranslateX() - enemy.getTranslateX(), 2) + Math.pow(player.getTranslateY() - enemy.getTranslateY(), 2));

            if (distance <= 100) {
                if (player.getTranslateX() > enemy.getTranslateX()) {
                    player.setTranslateX(player.getTranslateX() + 1);
                } else {
                    player.setTranslateX(player.getTranslateX() - 1);
                }
            }

        }
    }
    private void collideTrap(){
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
                player.damagePlayer(1);
            }
        }
    }

    private Node createMap(int x, int y, ImagePattern fill, Pane gameRoot) {
        Rectangle entity = new Rectangle(120, 120);
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

    private BasicEnemy createBasicEnemy(int x, int y, Pane gameRoot) {
        BasicEnemy entity = new BasicEnemy(x, y);

        gameRoot.getChildren().add(entity);
        return entity;
    }

    private FlyingEnemy createEliteEnemy(int x, int y, Pane gameRoot) {
        FlyingEnemy entity = new FlyingEnemy(x, y, player);

        gameRoot.getChildren().add(entity);
        return entity;
    }

    private void backToMenu() {
        this.stop();
        new StageMenu(primaryStage);
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
        // go to next level after clear
        if (levelData == LevelData.LEVEL3) {
            rickRoll();
        }
        if (levelData == LevelData.LEVEL1) {
            this.stop();
            GameStage theStage = new GameStage(primaryStage, mainMenu, LevelData.LEVEL2);
            theStage.setStage();
        }
        if (levelData == LevelData.LEVEL2) {
            this.stop();
            GameStage theStage = new GameStage(primaryStage, mainMenu, LevelData.LEVEL3);
            theStage.setStage();
        }
        if (levelData == LevelData.BONUS_LEVEL) {
            this.stop();
            primaryStage.setScene(mainMenu);
            primaryStage.setFullScreen(true);
            primaryStage.setResizable(false);
        }
    }
    private void rickRoll(){
        System.out.println("rickRoll");
        Media rickRollVid = new Media(new File("images/RickRoll.mp4").toURI().toString());
        MediaPlayer rickRoll = new MediaPlayer(rickRollVid);
        MediaView mediaView = new MediaView(rickRoll);
        mediaView.setFitWidth(1920);
        mediaView.setFitHeight(1080);

        uiRoot.getChildren().add(mediaView);

        PauseTransition pause = new PauseTransition(Duration.seconds(5));
        pause.setOnFinished(event -> {
            rickRoll.stop();
            uiRoot.getChildren().remove(mediaView);
            this.stop();
        });
        pause.play();

        rickRoll.play();
    }

    private boolean isPressed(KeyCode key) {
        return keys.getOrDefault(key, false);
    }

}
