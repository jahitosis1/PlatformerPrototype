package main;

import java.util.HashMap;
import java.util.ArrayList;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.Node;

public class GameTimer extends AnimationTimer {

  private HashMap<KeyCode, Boolean> keys = new HashMap<KeyCode, Boolean> ();
  private ArrayList<Node> platforms = new ArrayList<Node> ();

  private Scene gameScene;
  private Node player;
  private Point2D playerVelocity = new Point2D (0, 0);
  private boolean canJump = true;

  private int levelWidth;

  public GameTimer (Pane gameRoot, Scene gameScene) {
    initContent(gameRoot);

    // this.gameScene = gameScene;
    gameScene.setOnKeyPressed(event -> keys.put(event.getCode(), true));
    gameScene.setOnKeyReleased(event -> keys.put(event.getCode(), false));
    // super ();
  }

  @Override
  public void handle (long currentTime){
    update ();
  }

  private void update (){
    if (isPressed(KeyCode.SPACE) && player.getTranslateY() >= 5) jumpPlayer();
    if (isPressed(KeyCode.J) && player.getTranslateX() >= 5) movePlayerX(-5);
    if (isPressed(KeyCode.K) && player.getTranslateX() + 40 <= levelWidth - 5) movePlayerX(5);
    if (playerVelocity.getY() < 10) playerVelocity = playerVelocity.add(0,1);
    movePlayerY((int) playerVelocity.getY());
  }

  private void initContent(Pane gameRoot){
    Rectangle bg = new Rectangle(1920, 1080);
    levelWidth = LevelData.LEVEL1[0].length() * 200;

    for (int i = 0; i < LevelData.LEVEL1.length; i++) {
      String line = LevelData.LEVEL1[i];
      for (int j = 0; j < line.length(); j++) {
        switch (line.charAt(j)) {
          case '0':
            break;
          case '1':
            Node platform = createEntity(j*200, i*60, 200, 60, Color.BROWN, gameRoot);
            platforms.add(platform);
            break;
          default:
            break;
        }
      }
    }

    player = createEntity(0, 900, 40, 40, Color.BLUE, gameRoot);
    player.translateXProperty().addListener((obs, old, newValue) -> {
      int offset = newValue.intValue();
      if (offset > 640 && offset < levelWidth - 640) gameRoot.setLayoutX(-(offset-640));
    });
  }

  private void movePlayerX (int value) {
    boolean movingRight = value > 0;

    for (int i = 0; i < Math.abs(value); i++) {
      for (Node platform : platforms) {
        if (player.getBoundsInParent().intersects(platform.getBoundsInParent())) {
          if (movingRight) {
            if (player.getTranslateX() + 40 == platform.getTranslateX()) return;
          }else{
            if (player.getTranslateX() == platform.getTranslateX() + 60) return;
          }
        }
      }
      player.setTranslateX(player.getTranslateX() + (movingRight ? 1 : -1));
    }
  }

  private void movePlayerY (int value) {
    boolean movingDown = value > 0;

    for (int i = 0; i < Math.abs(value); i++) {
      for (Node platform : platforms) {
        if (player.getBoundsInParent().intersects(platform.getBoundsInParent())) {
          if (movingDown) {
            if (player.getTranslateY() + 40 == platform.getTranslateY()){
              player.setTranslateY(player.getTranslateY() - 1);
              canJump = true;
              return;
            }
          }else{
            if (player.getTranslateY() == platform.getTranslateY() + 60) return;
          }
        }
      }
      player.setTranslateY(player.getTranslateY() + (movingDown ? 1 : -1));
    }
  }

  private void jumpPlayer () {
    if (canJump) {
      playerVelocity = playerVelocity.add(0, -30);
      canJump = false;
    }
  }

  private Node createEntity (int x, int y, int width, int height, Color color, Pane gameRoot) {
    Rectangle entity = new Rectangle (width, height);
    entity.setTranslateX(x);
    entity.setTranslateY(y);
    entity.setFill(color);

    gameRoot.getChildren().add(entity);
    return entity;
  }

  private boolean isPressed (KeyCode key){
    return keys.getOrDefault(key, false);
  }

}
