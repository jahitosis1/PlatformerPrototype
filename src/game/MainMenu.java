package game;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class MainMenu {
    public void setStage(Stage primaryStage) {

        StackPane layout = new StackPane();
        layout.setPrefSize(1920, 1080);
        VBox buttons = new VBox();
        layout.setAlignment(Pos.CENTER);
        buttons.setAlignment(Pos.CENTER);
        buttons.setSpacing(20);

        Scene scene = new Scene(layout, 1920 , 1080);
        Credits credits = new Credits(layout);

        Image bg = new Image("images/colored_land.png");
        ImageView image2 = new ImageView("images/play button.png");
        ImageView image3 = new ImageView("images/options_button.png");
        ImageView image4 = new ImageView("images/quit_button.png");
        Button button = new Button("", image2);
        Button button2 = new Button("", image3);
        Button button3 = new Button("", image4);
        button.setStyle("-fx-background-color: transparent;");
        button2.setStyle("-fx-background-color: transparent;");
        button3.setStyle("-fx-background-color: transparent;");
        button.setOnAction(e -> {
            GameStage theStage = new GameStage(primaryStage, scene);
            theStage.setStage(primaryStage);
        });
        button2.setOnAction(e -> credits.openCredits(layout));
        button3.setOnAction(e -> Platform.exit());

        BackgroundImage backgroundimage = new BackgroundImage(bg,
                BackgroundRepeat.REPEAT,
                BackgroundRepeat.REPEAT,
                BackgroundPosition.DEFAULT,
                new BackgroundSize(1920.0, 1080.0, false, false, false, false));
        Background background = new Background(backgroundimage);
        layout.setBackground(background);


        buttons.getChildren().addAll(button, button2, button3);
        layout.getChildren().addAll(buttons);

        primaryStage.setFullScreen(true);
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
