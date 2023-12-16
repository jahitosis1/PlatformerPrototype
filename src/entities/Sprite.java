package entities;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
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
  protected double collisionX, collisionY;

//  public Sprite(int xPos, int yPos){
//    this.x = xPos;
//    this.y = yPos;
//    this.visible = true;
//  }
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
  //method that will check for collision of two sprites
  public boolean collidesWith(Sprite rect2)	{
    Rectangle2D rectangle1 = this.getBounds();
    Rectangle2D rectangle2 = rect2.getBounds();

    return rectangle1.intersects(rectangle2);
  }
  //method that will return the bounds of an image
  private Rectangle2D getBounds(){
    return new Rectangle2D(this.x, this.y, this.width, this.height);
  }

  //method to return the image
//  Image getImage(){
//    return this.img;
//  }
  //getters
//  public int getX() {
//    return this.x;
//  }
//
//  public int getY() {
//    return this.y;
//  }

  public int getDX(){
    return this.dx;
  }
  public int getDY(){
    return this.dy;
  }

  public boolean getVisible(){
    return visible;
  }
//  public boolean isVisible(){
//    if(visible) return true;
//    return false;
//  }

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

//  public void setVisible(boolean value){
//    this.visible = value;
//  }
  
  // protected abstract void setCollision();
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
