package main;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class GameStage {

  public static final int WINDOW_WIDTH = 1920;
  public static final int WINDOW_HEIGHT = 1080;
  private Group root;
  private Pane gameRoot;
  private Pane uiRoot;
  private Scene scene;
  private Stage stage;
  private GameTimer gameTimer;

  public GameStage() {
    this.root = new Group();
    this.gameRoot = new Pane();
    this.uiRoot = new Pane();
    this.scene = new Scene(root, GameStage.WINDOW_WIDTH, GameStage.WINDOW_HEIGHT, Color.BLACK);
    this.gameTimer = new GameTimer(gameRoot, scene);
  }

  public void setStage(Stage stage) {
    this.stage = stage;

    // set stage elements here
    this.root.getChildren().add(gameRoot);

    // set window
    this.stage.setTitle("Platformer Prototype");
    this.stage.setScene(this.scene);
    this.stage.setMinWidth(WINDOW_WIDTH);
    this.stage.setMinHeight(WINDOW_HEIGHT);
    this.stage.setFullScreen(true);
    this.stage.setResizable(false);

    this.gameTimer.start();
    this.stage.show();
  }
}
