package game;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class About extends Menu {
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
        
        ImageView tutorial = new ImageView("images/UI Elements/tutorial.png");
        ImageView ref = new ImageView("images/UI Elements/references.png");
        
        Rectangle rect1 = new Rectangle(1520, 400, Color.LIGHTBLUE);
        Rectangle rect2 = new Rectangle(1520, 400, Color.LIGHTBLUE);

        rect1.setOpacity(0.7);
        rect2.setOpacity(0.7);

        rect1.setArcHeight(20);
        rect2.setArcHeight(20);
        rect1.setArcWidth(20);
        rect2.setArcWidth(20);
        
        StackPane window1 = new StackPane(rect1, tutorial);
        StackPane window2 = new StackPane(rect2, ref);
        
        cluster.getChildren().addAll(window1, window2);

        Button resume = new Button("", closeButton);

        resume.setStyle("-fx-background-color: transparent;");
        resume.setTranslateX(850);
        resume.setTranslateY(-450);
        new SetUpButton(resume);

        resume.setOnAction(e -> {
            playMusic.stop();
            new MainMenu(primaryStage);
        });

        layout.getChildren().addAll(cluster, resume);
    }
}
