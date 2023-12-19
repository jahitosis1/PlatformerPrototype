package game;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Credits extends Menu{
  public Credits(Stage primaryStage) {
    super(primaryStage);
  }

  @Override
  protected void initContent(StackPane layout) {

    Button resume = new Button("", new ImageView("images/UI Elements/UI_Flat_Cross_Large.png"));
    resume.setStyle("-fx-background-color: transparent;");
    resume.setOnAction(e -> {
      playMusic.stop();
      new MainMenu(primaryStage);
    });
    resume.setTranslateX(850);
    resume.setTranslateY(-450);

    ImageView creditText = new ImageView("images/UI Elements/credits.png");
    creditText.setTranslateY(-350);

    VBox cluster1 = new VBox();
    cluster1.setAlignment(Pos.CENTER);
    cluster1.setSpacing(50);
    Image image1 = new Image("images/ken.jpg");
    ImageView imageView1 = new ImageView(image1);
    imageView1.setFitWidth(300);
    imageView1.setFitHeight(300);
    ImageView text1 = new ImageView("images/UI Elements/ken.png");
    cluster1.getChildren().addAll(imageView1, text1);


    VBox cluster2 = new VBox();
    cluster2.setAlignment(Pos.CENTER);
    cluster2.setSpacing(50);
    Image image2 = new Image("images/ron.jpg");
    ImageView imageView2 = new ImageView(image2);
    imageView2.setFitWidth(300);
    imageView2.setFitHeight(300);
    ImageView text2 = new ImageView("images/UI Elements/aeron.png");
    cluster2.getChildren().addAll(imageView2, text2);

    VBox cluster3 = new VBox();
    cluster3.setAlignment(Pos.CENTER);
    cluster3.setSpacing(50);
    Image image3 = new Image("images/joshua.jpg");
    ImageView imageView3 = new ImageView(image3);
    imageView3.setFitWidth(300);
    imageView3.setFitHeight(300);
    ImageView text3 = new ImageView("images/UI Elements/joshua.png");
    cluster3.getChildren().addAll(imageView3, text3);

    imageView1.setPreserveRatio(true);
    imageView2.setPreserveRatio(true);
    imageView3.setPreserveRatio(true);

    HBox hbox = new HBox();
    hbox.setPrefSize(1920, 1080);
    hbox.setAlignment(Pos.CENTER_LEFT);
    hbox.setTranslateX(212);
    hbox.setTranslateY(15);
    hbox.setSpacing(300);
    hbox.getChildren().addAll(cluster1, cluster2, cluster3);

    layout.getChildren().addAll(hbox, creditText, resume);
  }
}
