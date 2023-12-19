package game;

import main.Main;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.HashMap;

public class StageMenu extends Menu{

  public StageMenu(Stage primaryStage) {
    super(primaryStage);
  }

  public static final HashMap<String, Boolean> levels = new HashMap<>() {{
    put("level1", true);
    put("level2", false);
    put("level3", false);
  }};

  @Override
  protected void initContent(StackPane layout) {
    HBox buttons = new HBox();
    buttons.setAlignment(Pos.CENTER);
    buttons.setTranslateX(40);
    Pane labelPane = new Pane();
    ImageView label = new ImageView("images/UI Elements/label.png");
    labelPane.getChildren().addAll(label);
    label.setFitWidth(600);
    label.setPreserveRatio(true);
    label.setX(635);
    label.setY(20);

    buttons.setSpacing(65);
    ImageView image2 = new ImageView("images/UI Elements/level1.png");
    ImageView image3 = new ImageView("images/UI Elements/level2.png");
    ImageView image4 = new ImageView("images/UI Elements/level3.png");
    ImageView backImg =  new ImageView("images/UI Elements/UI_Flat_Cross_Large.png");

    Button button = new Button("", image2);
    Button button2 = new Button("", image3);
    Button button3 = new Button("", image4);
    Button button4 = new Button("");
    Button back = new Button("", backImg);

    button.setStyle("-fx-background-color: transparent;");
    button2.setStyle("-fx-background-color: transparent;");
    button3.setStyle("-fx-background-color: transparent;");
    button4.setStyle("-fx-background-color: transparent;");
    back.setStyle("-fx-background-color: transparent;");

    back.setTranslateX(850);
    back.setTranslateY(-450);

    button.setOnAction(e -> {
      if (levels.get("level1")) {
        playMusic.stop();
        GameStage theStage = new GameStage(primaryStage, scene, LevelData.LEVEL1);
        theStage.setStage();
      }
    });
    button2.setOnAction(e -> {
      if (levels.get("level2")) {
        playMusic.stop();
        GameStage theStage = new GameStage(primaryStage, scene, LevelData.LEVEL2);
        theStage.setStage();
      }
    });
    button3.setOnAction(e -> {
      if (levels.get("level3")) {
        playMusic.stop();
        GameStage theStage = new GameStage(primaryStage, scene, LevelData.LEVEL3);
        theStage.setStage();
      }
    });
    button4.setOnAction(e -> {
      playMusic.stop();
      GameStage theStage = new GameStage(primaryStage, scene, LevelData.BONUS_LEVEL);
      theStage.setStage();
    });
    back.setOnAction(e -> {
      playMusic.stop();
      new MainMenu(primaryStage);
    });

    buttons.getChildren().addAll(button, button2, button3, button4);

    layout.getChildren().addAll(labelPane, buttons, back);
  }
}
