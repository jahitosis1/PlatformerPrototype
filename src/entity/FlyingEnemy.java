package entity;

import javafx.animation.Animation;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.shape.Circle;

public class FlyingEnemy extends Enemy{

    public Circle searchRadius;
    private final static Image ENEMY_SPRITE_IDLE = new Image("images/Tengu_Fly_0.png", ENEMY_SIZE, ENEMY_SIZE, false, false);
    private final static Image ENEMY_SPRITE_DIVE = new Image("images/Tengu_Dive_0.png", ENEMY_SIZE, ENEMY_SIZE, false, false);
    private final Player player;
    public FlyingEnemy(int x_pos, int y_pos, Player player) {
        super(ENEMY_SPRITE_IDLE, x_pos, y_pos);
        this.searchRadius = new Circle(x_pos, y_pos,ENEMY_SIZE*4);
        this.player = player;

        idleAnimation = createAnimation(8, "Tengu_Fly_", ENEMY_SIZE, ENEMY_SIZE, Timeline.INDEFINITE, false);
        moveAnimation = createAnimation(4, "Tengu_Fly_", ENEMY_SIZE, ENEMY_SIZE, Timeline.INDEFINITE, false);

        idleAnimation.play();
    }
    
    @Override
    public void move() {
        if (!isAlive) return;
        if (player.getBoundsInParent().intersects(((FlyingEnemy) this).searchRadius.getBoundsInParent())) {
            double diffX = player.getTranslateX() - this.getTranslateX();
            double diffY = player.getTranslateY() - this.getTranslateY();
            double distance = Math.sqrt(Math.pow(diffX, 2) + Math.pow(diffY, 2));

            double angle = Math.atan2(diffY, diffX);

            double speed = 1.5; // The speed of the this

            this.setTranslateX(this.getTranslateX() + speed * Math.cos(angle));
            this.setTranslateY(this.getTranslateY() + speed * Math.sin(angle));

            if (Math.cos(angle) > 0) this.setScaleX(1);
            if (Math.cos(angle) < 0) this.setScaleX(-1);

            startDiveAnimation();
        }else{
            this.returnToPos();
        }
    }
    public void startDiveAnimation() {
        idleAnimation.stop();
        if (moveAnimation.getStatus() == Animation.Status.STOPPED) {
            this.setImage(ENEMY_SPRITE_DIVE);
        }
        moveAnimation.play();
    }
    public void stopDiveAnimation() {
        moveAnimation.stop();
        if (idleAnimation.getStatus() == Animation.Status.STOPPED) {
            this.setImage(ENEMY_SPRITE_IDLE);
        }
        idleAnimation.play();
    }

    public void returnToPos() {
        double diffX = orig_x - this.getTranslateX();
        double diffY = orig_y - this.getTranslateY();

        double angle = Math.atan2(diffY, diffX);

        double speed = 1.0; // The speed of the enemy

        this.setTranslateX(this.getTranslateX() + speed * Math.cos(angle));
        this.setTranslateY(this.getTranslateY() + speed * Math.sin(angle));

        stopDiveAnimation();
    }

    @Override
    public void die() {
    }
}
