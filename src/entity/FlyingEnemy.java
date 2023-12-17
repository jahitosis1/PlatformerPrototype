package entity;

import javafx.animation.Animation;
import javafx.animation.Timeline;
import javafx.scene.image.Image;

public class FlyingEnemy extends Enemy {
    public final static Image ENEMY_SPRITE_IDLE = new Image("images/Tengu_Fly_0.png", ENEMY_SIZE, ENEMY_SIZE, false, false);
    public final static Image ENEMY_SPRITE_DIVE = new Image("images/Tengu_Dive_0.png", ENEMY_SIZE, ENEMY_SIZE, false, false);
    private Timeline diveAnimation;
    private boolean movingRight;


    public FlyingEnemy(int x_pos, int y_pos) {
        super(FlyingEnemy.ENEMY_SPRITE_IDLE, x_pos, y_pos);
        this.setTranslateX(x_pos);
        this.setTranslateY(y_pos);

        idleAnimation = createAnimation(10,"Tengu_Fly_", ENEMY_SIZE, ENEMY_SIZE, Timeline.INDEFINITE, false);
        diveAnimation = createAnimation(4,"Tengu_Dive_", ENEMY_SIZE, ENEMY_SIZE, Timeline.INDEFINITE, false);
        // Start the animation
        idleAnimation.play();
//        idleAnimation.play();
    }
    public void move() {
        if (!isAlive) return;
        if (this.getTranslateX() >= this.orig_x + 100) movingRight = false;
        if (this.getTranslateX() <= this.orig_x - 100) movingRight = true;
        if (movingRight) {
            setScaleX(-1);
            this.setTranslateX(this.getTranslateX() + 0.25);
        } else {
            setScaleX(1);
            this.setTranslateX(this.getTranslateX() - 0.25);
        }
    }
    public void die() {}
}
