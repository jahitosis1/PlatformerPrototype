package game;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class MainMenu extends Menu{
    public MainMenu(Stage primaryStage) {
        super(primaryStage);
    }

    protected void initContent(StackPane layout){
        VBox buttons = new VBox();
        buttons.setAlignment(Pos.CENTER);
        buttons.setTranslateY(75);
        buttons.setSpacing(20);

        Image bg = new Image("images/tempbg.png");
        ImageView title = new ImageView("images/UI Elements/title.png");
        title.setFitWidth(1000);
        title.setPreserveRatio(true);
        title.setTranslateY(-250);
        ImageView image2 = new ImageView("images/UI Elements/play_button.png");
        ImageView image3 = new ImageView("images/UI Elements/play_button.png");
        ImageView image4 = new ImageView("images/UI Elements/credits_button.png");
        ImageView image5 = new ImageView("images/UI Elements/quit_button.png");
        Button button = new Button("", image2);
        Button button2 = new Button("", image3);
        Button button3 = new Button("", image4);
        Button button4 = new Button("", image5);
        button.setStyle("-fx-background-color: transparent;");
        button2.setStyle("-fx-background-color: transparent;");
        button3.setStyle("-fx-background-color: transparent;");
        button4.setStyle("-fx-background-color: transparent;");
        button.setOnAction(e -> new StageMenu(primaryStage));
        button2.setOnAction(e -> new About(primaryStage));
        button3.setOnAction(e -> new Credits(primaryStage));
        button4.setOnAction(e -> Platform.exit());

        BackgroundImage backgroundimage = new BackgroundImage(bg,
                BackgroundRepeat.REPEAT,
                BackgroundRepeat.REPEAT,
                BackgroundPosition.DEFAULT,
                new BackgroundSize(1920.0, 1080.0, false, false, false, false));
        Background background = new Background(backgroundimage);
        layout.setBackground(background);


        buttons.getChildren().addAll(button, button2, button3, button4);
        layout.getChildren().addAll(title, buttons);
    }
}
