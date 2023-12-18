package main;

import game.MainMenu;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

  @Override
  public void start(Stage primaryStage){
    MainMenu menu = new MainMenu(primaryStage);
  }

  public static void main(String[] args) {
    launch();
  }

}
