package game;

import java.io.File;

import javafx.geometry.Pos;
import javafx.scene.media.*;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public abstract class Menu {
    protected final StackPane layout;
    protected final Scene scene;
    protected final Stage primaryStage;
    protected final Media music = new Media(new File("images/Eight_Bit_Undercover.mp3").toURI().toString());
    public Menu(Stage primaryStage) {
        layout = new StackPane();

        layout.setPrefSize(1920, 1080);
        layout.setAlignment(Pos.CENTER);
        scene = new Scene(layout, 1920 , 1080);
        this.primaryStage = primaryStage;
        setStage(scene);
    }

    public void setStage(Scene scene) {
    	MediaPlayer playMusic = new MediaPlayer(music);
        playMusic.play();
        Image bg = new Image("images/tempbg.png");
        BackgroundImage backgroundimage = new BackgroundImage(bg,
                BackgroundRepeat.REPEAT,
                BackgroundRepeat.REPEAT,
                BackgroundPosition.DEFAULT,
                new BackgroundSize(1920.0, 1080.0, false, false, false, false));
        Background background = new Background(backgroundimage);
        layout.setBackground(background);

        initContent(layout);

        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
    protected abstract void initContent(StackPane layout);

}
