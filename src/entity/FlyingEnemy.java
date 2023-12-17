package entity;

import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.shape.Circle;

public class FlyingEnemy extends Enemy{

    public Circle searchRadius;
    public final static Image ENEMY_SPRITE_IDLE = new Image("images/Tengu_Fly_0.png", ENEMY_SIZE, ENEMY_SIZE, false, false);
    public final static Image ENEMY_SPRITE_DIVE = new Image("images/Tengu_Dive_0.png", ENEMY_SIZE, ENEMY_SIZE, false, false);
    public FlyingEnemy(int x_pos, int y_pos) {
        super(ENEMY_SPRITE_IDLE, x_pos, y_pos);
        this.searchRadius = new Circle(x_pos, y_pos,ENEMY_SIZE*4);

        idleAnimation = createAnimation(8, "Tengu_Fly_", ENEMY_SIZE, ENEMY_SIZE, Timeline.INDEFINITE, false);
        moveAnimation = createAnimation(4, "Tengu_Fly_", ENEMY_SIZE, ENEMY_SIZE, Timeline.INDEFINITE, false);

        idleAnimation.play();
    }
    
    @Override
    public void move() {
        moveAnimation.stop();
        idleAnimation.play();
    }
    public void dive() {
        idleAnimation.stop();
        moveAnimation.play();
    }

    public void returnToPos() {
        double diffX = orig_x - this.getTranslateX();
        double diffY = orig_y - this.getTranslateY();

        double angle = Math.atan2(diffY, diffX);

        double speed = 1.0; // The speed of the enemy

        this.setTranslateX(this.getTranslateX() + speed * Math.cos(angle));
        this.setTranslateY(this.getTranslateY() + speed * Math.sin(angle));
        this.move();
    }

    @Override
    public void die() {
    }
}
