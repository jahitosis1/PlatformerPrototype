package main;

import game.Credits;
import game.GameStage;
import game.MainMenu;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Main extends Application {

  @Override
  public void start(Stage primaryStage){
    MainMenu menu = new MainMenu();
    menu.setStage(primaryStage);
  }

  public static void main(String[] args) {
    launch();
  }

}
