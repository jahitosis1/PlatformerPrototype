package game;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class GameStage {

  public static final int WINDOW_WIDTH = 1920;
  public static final int WINDOW_HEIGHT = 1080;
  private final Group root;
  private final Pane gameRoot;
  private final Pane uiRoot;
  private final Scene scene;
  private final GameTimer gameTimer;
  private final Stage stage;

  public GameStage(Stage stage, Scene previousScene, String[] levelData) {
    this.root = new Group();
    this.gameRoot = new Pane();
    this.uiRoot = new Pane();
    this.scene = new Scene(root, GameStage.WINDOW_WIDTH, GameStage.WINDOW_HEIGHT);
    this.stage = stage;
    this.gameTimer = new GameTimer(gameRoot, uiRoot, scene, stage, previousScene, levelData);
  }

  public void setStage() {

    // set stage elements here
    this.root.getChildren().addAll(gameRoot, uiRoot);

    // set window
    stage.setTitle("Platformer Prototype");
    stage.setScene(this.scene);
    stage.setMinWidth(WINDOW_WIDTH);
    stage.setMinHeight(WINDOW_HEIGHT);

    stage.setFullScreen(false);
    stage.setResizable(false);

    this.gameTimer.start();
  }
}
