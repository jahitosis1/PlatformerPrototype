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
  protected Image img;
  public Shape collisionShape;
  protected int x, y, dx, dy;
  protected boolean visible;
  protected double width;
  protected double height;

  public Sprite (Image image) {
    super(image);
  }

  //method to set the object's image
  protected void loadImage(Image img){
    try{
      this.img = img;
      this.setSize();
    } catch(Exception e){}
  }

  //method to set the image to the image view node
  public void render(GraphicsContext gc){
    gc.drawImage(this.img, this.x, this.y);

  }

  //method to set the object's width and height properties
  private void setSize(){
    this.width = this.img.getWidth();
    this.height = this.img.getHeight();
  }

  private Rectangle2D getBounds(){
    return new Rectangle2D(this.x, this.y, this.width, this.height);
  }

  public int getDX(){
    return this.dx;
  }
  public int getDY(){
    return this.dy;
  }

  public boolean getVisible(){
    return visible;
  }

  //setters
  public void setDX(int dx){
    this.dx = dx;
  }

  public void setDY(int dy){
    this.dy = dy;
  }

  public void setWidth(double val){
    this.width = val;
  }

  public void setHeight(double val){
    this.height = val;
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
