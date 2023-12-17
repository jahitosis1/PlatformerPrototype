package game;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class GameStage {

  public static final int WINDOW_WIDTH = 1920;
  public static final int WINDOW_HEIGHT = 1080;
  private final Group root;
  private final Pane gameRoot;
  private final Pane uiRoot;
  private final Scene scene;
  private Stage stage;
  private GameTimer gameTimer;

  public GameStage() {
    this.root = new Group();
    this.gameRoot = new Pane();
    this.uiRoot = new Pane();
    this.scene = new Scene(root, GameStage.WINDOW_WIDTH, GameStage.WINDOW_HEIGHT);
    this.gameTimer = new GameTimer(gameRoot, uiRoot, scene);
  }

  public void setStage(Stage stage) {
    this.stage = stage;

    // set stage elements here
    this.root.getChildren().addAll(gameRoot, uiRoot);

    // set window
    this.stage.setTitle("Platformer Prototype");
    this.stage.setScene(this.scene);
    this.stage.setMinWidth(WINDOW_WIDTH);
    this.stage.setMinHeight(WINDOW_HEIGHT);

<<<<<<< Updated upstream
    this.stage.setFullScreen(true);
    this.stage.setResizable(false);
=======
    stage.setFullScreen(false);
    stage.setResizable(false);
>>>>>>> Stashed changes

    this.gameTimer.start();
    this.stage.show();
  }
}
