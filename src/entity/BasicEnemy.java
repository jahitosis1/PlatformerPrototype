package entity;

import javafx.animation.Animation;
import javafx.animation.Timeline;
import javafx.scene.image.Image;

public class BasicEnemy extends Enemy {
    private final static Image ENEMY_SPRITE_IDLE = new Image("images/Bringer-of-Death_Idle_0.png", ENEMY_SIZE, ENEMY_SIZE, false, false);
    private final static Image ENEMY_SPRITE_MOVE7 = new Image("images/Bringer-of-Death_Walk_0.png", ENEMY_SIZE, ENEMY_SIZE, false, false);
    private final static Image ENEMY_SPRITE_DEATH = new Image("images/Bringer-of-Death_Death_0.png", ENEMY_SIZE, ENEMY_SIZE, false, false);
    private boolean movingRight;


    public BasicEnemy(int x_pos, int y_pos) {
        super(BasicEnemy.ENEMY_SPRITE_IDLE, x_pos, y_pos);
        this.maxRange = 200;

        idleAnimation = createAnimation(8,"Bringer-of-Death_Idle_", ENEMY_SIZE*2, ENEMY_SIZE, Timeline.INDEFINITE, false);
        moveAnimation = createAnimation(8,"Bringer-of-Death_Walk_", ENEMY_SIZE*2, ENEMY_SIZE, Timeline.INDEFINITE, false);
        deathAnimation = createAnimation(10,"Bringer-of-Death_Death_", ENEMY_SIZE*2, ENEMY_SIZE, Timeline.INDEFINITE, false);

        // Start the animation
        idleAnimation.play();
    }
    public void move() {
        if (!isAlive) return;
        if (this.getTranslateX() >= this.orig_x + maxRange) movingRight = false;
        if (this.getTranslateX() <= this.orig_x - maxRange) movingRight = true;
        if (movingRight) {
            setScaleX(-1);
            this.setTranslateX(this.getTranslateX() + 0.25);
        } else {
            setScaleX(1);
            this.setTranslateX(this.getTranslateX() - 0.25);
        }
        if (moveAnimation.getStatus() == Animation.Status.STOPPED) this.setImage(ENEMY_SPRITE_MOVE7);
        idleAnimation.stop();
        moveAnimation.play();
    }
    public void die() {
        isAlive = false;
        moveAnimation.stop();
        deathAnimation.play();
    }
}
