package game;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class About extends Menu{
    public About(Stage primaryStage) {
        super(primaryStage);
    }

    @Override
    protected void initContent(StackPane layout) {

        ImageView closeButton =  new ImageView("images/UI Elements/UI_Flat_Cross_Large.png");

        VBox cluster = new VBox();
        cluster.setTranslateY(50);
        cluster.setPrefSize(1920, 1080);
        cluster.setAlignment(Pos.CENTER);
        cluster.setSpacing(20);
        
        Rectangle window1 = new Rectangle(1520, 400, Color.LIGHTBLUE);
        Rectangle window2 = new Rectangle(1520, 400, Color.LIGHTBLUE);

        window1.setOpacity(0.7);
        window2.setOpacity(0.7);

        window1.setArcHeight(20);
        window1.setArcWidth(20);
        window2.setArcHeight(20);
        window2.setArcWidth(20);

        cluster.getChildren().addAll(window1, window2);

        Button resume = new Button("", closeButton);

        resume.setStyle("-fx-background-color: transparent;");
        resume.setTranslateX(850);
        resume.setTranslateY(-450);

        resume.setOnAction(e -> new MainMenu(primaryStage));

        layout.getChildren().addAll(cluster, resume);
    }
}
