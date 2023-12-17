package game;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.HashMap;

public class StageMenu {

    public static final HashMap<String, Boolean> levels = new HashMap<>() {{
        put("level1", true);
        put("level2", false);
        put("level3", false);
    }};

    public void setStage(Stage primaryStage) {

        StackPane layout = new StackPane();
        layout.setPrefSize(1920, 1080);
        HBox buttons = new HBox();
        layout.setAlignment(Pos.CENTER);
        buttons.setAlignment(Pos.CENTER);
        buttons.setSpacing(20);

        Scene scene = new Scene(layout, 1920 , 1080);

        Image bg = new Image("images/tempbg.png");
        ImageView image2 = new ImageView("images/play_button.png");
        ImageView image3 = new ImageView("images/options_button.png");
        ImageView image4 = new ImageView("images/quit_button.png");

        Button button = new Button("", image2);
        Button button2 = new Button("", image3);
        Button button3 = new Button("", image4);
        Button button4 = new Button("");

        button.setStyle("-fx-background-color: transparent;");
        button2.setStyle("-fx-background-color: transparent;");
        button3.setStyle("-fx-background-color: transparent;");
        button4.setStyle("-fx-background-color: transparent;");

        button4.setViewOrder(1);

        button.setOnAction(e -> {
            if (levels.get("level1")) {
                GameStage theStage = new GameStage(primaryStage, scene, LevelData.LEVEL1);
                theStage.setStage(primaryStage);
            }
        });
        button2.setOnAction(e -> {
            if (levels.get("level2")) {
                GameStage theStage = new GameStage(primaryStage, scene, LevelData.LEVEL2);
                theStage.setStage(primaryStage);
            }
        });
        button3.setOnAction(e -> {
            if (levels.get("level3")) {
                GameStage theStage = new GameStage(primaryStage, scene, LevelData.LEVEL3);
                theStage.setStage(primaryStage);
            }
        });
        button4.setOnAction(e -> {
            GameStage theStage = new GameStage(primaryStage, scene, LevelData.BONUS_LEVEL);
            theStage.setStage(primaryStage);
        });

        BackgroundImage backgroundimage = new BackgroundImage(bg,
                BackgroundRepeat.REPEAT,
                BackgroundRepeat.REPEAT,
                BackgroundPosition.DEFAULT,
                new BackgroundSize(1920.0, 1080.0, false, false, false, false));
        Background background = new Background(backgroundimage);
        layout.setBackground(background);


        buttons.getChildren().addAll(button, button2, button3, button4);

        layout.getChildren().addAll(buttons);

        primaryStage.setScene(scene);
        primaryStage.setFullScreen(false);
        primaryStage.setResizable(false);

    }

}
