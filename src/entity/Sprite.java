package entity;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Shape;
import javafx.util.Duration;


public abstract class Sprite extends ImageView {
  protected Timeline idleAnimation;
  protected Timeline moveAnimation;
  protected Timeline jumpAnimation;

  public Sprite (Image image) {
    super(image);
  }

  protected Timeline createAnimation(int frameCount, String state, int width, int height, int cycleCount, boolean autoReverse) {
    Timeline newAnim = new Timeline();
    newAnim.setCycleCount(cycleCount);
    newAnim.setAutoReverse(autoReverse);
    for (int i = 0; i < frameCount; i++) {
      String filename = "images/" + state + i + ".png";
      Image sprite = new Image(filename, width, height, false, false);
      newAnim.getKeyFrames().add(
              new KeyFrame(Duration.millis(100*i), new KeyValue(this.imageProperty(), sprite))
      );
    }
    return newAnim;
  }

}
