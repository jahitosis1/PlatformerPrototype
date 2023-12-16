package game;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Credits {
    private final Pane credits;
   public Credits(StackPane uiRoot) {

       credits = new Pane();
       credits.setPrefSize(1920, 1080);

       Pane layout = new Pane();
       layout.setPrefSize(1920,1080);
//       layout.setSpacing(20);

       Rectangle bg = new Rectangle(1920, 1080, Color.LIGHTBLUE);
       bg.setOpacity(0.7);
       Button resume = new Button("", new ImageView("images/hudX.png"));
       resume.setStyle("-fx-background-color: transparent;");
       resume.setOnMouseClicked(event -> uiRoot.getChildren().remove(credits));
       resume.setLayoutX(1800);
       resume.setLayoutY(0);

       Image image1 = new Image("images/ken.jpg");
       ImageView imageView1 = new ImageView(image1);
       imageView1.setFitWidth(300);
       imageView1.setFitHeight(300);

       Image image2 = new Image("images/ron.jpg");
       ImageView imageView2 = new ImageView(image2);
       imageView2.setFitWidth(300);
       imageView2.setFitHeight(300);

       Image image3 = new Image("images/joshua.jpg");
       ImageView imageView3 = new ImageView(image3);
       imageView3.setFitWidth(300);
       imageView3.setFitHeight(300);

       imageView1.setPreserveRatio(true);
       imageView2.setPreserveRatio(true);
       imageView3.setPreserveRatio(true);

       HBox hbox = new HBox();
       hbox.setPrefSize(1920, 1080);
       hbox.setAlignment(Pos.CENTER);
       hbox.setSpacing(30);
       hbox.getChildren().addAll(imageView1, imageView2, imageView3);

       layout.getChildren().addAll(hbox, resume);
       credits.getChildren().addAll(bg, layout);
   }

   public void openCredits(StackPane layout) {
       layout.getChildren().add(credits);
   }
    
}
