package game;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
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
        buttons.setTranslateY(75);
        buttons.setSpacing(20);
        Scene scene = new Scene(layout, 1920 , 1080);
        Credits credits = new Credits(layout);

        Image bg = new Image("images/tempbg.png");
        ImageView title = new ImageView("images/UI Elements/title.png");
        title.setFitWidth(1000);
        title.setPreserveRatio(true);
        title.setTranslateY(-250);
        ImageView image2 = new ImageView("images/UI Elements/play_button.png");
        ImageView image3 = new ImageView("images/UI Elements/credits_button.png");
        ImageView image4 = new ImageView("images/UI Elements/quit_button.png");
        Button button = new Button("", image2);
        Button button2 = new Button("", image3);
        Button button3 = new Button("", image4);
        button.setStyle("-fx-background-color: transparent;");
        button2.setStyle("-fx-background-color: transparent;");
        button3.setStyle("-fx-background-color: transparent;");
        button.setOnAction(e -> {
            StageMenu levelMenu = new StageMenu();
            levelMenu.setStage(primaryStage);
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
        layout.getChildren().addAll(title, buttons);

        primaryStage.setScene(scene);
        primaryStage.setFullScreen(false);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
