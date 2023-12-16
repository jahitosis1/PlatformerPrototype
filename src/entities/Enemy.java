package entities;

import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;

public class Enemy extends Sprite {
    private final static int ENEMY_SIZE = 50;
    public final static Image ENEMY_SPRITE_IDLE = new Image("images/Bringer-of-Death_Idle_0.png", ENEMY_SIZE, ENEMY_SIZE, false, false);
    public final static Image ENEMY_SPRITE_MOVE7 = new Image("images/Bringer-of-Death_Walk_0.png", ENEMY_SIZE, ENEMY_SIZE, false, false);
    public final static Image ENEMY_SPRITE_ATTACK = new Image("images/Bringer-of-Death_Attack_0.png", ENEMY_SIZE, ENEMY_SIZE, false, false);
    public final static Image ENEMY_SPRITE_DEATH = new Image("images/Bringer-of-Death_Death_0.png", ENEMY_SIZE, ENEMY_SIZE, false, false);
    private final Timeline idleAnimation;
    private final Timeline moveAnimation;
    private final Timeline attackAnimation;
    private final Timeline deathAnimation;


    public Enemy(int x_pos, int y_pos) {
        super(Enemy.ENEMY_SPRITE_IDLE);
        this.collisionShape = new Rectangle(ENEMY_SIZE, ENEMY_SIZE);
        this.setTranslateX(x_pos);
        this.setTranslateY(y_pos);

        idleAnimation = createAnimation(8,"Bringer-of-Death_Idle_", ENEMY_SIZE, ENEMY_SIZE, Timeline.INDEFINITE, false);
        moveAnimation = createAnimation(8,"Bringer-of-Death_Walk_", ENEMY_SIZE, ENEMY_SIZE, Timeline.INDEFINITE, false);
        attackAnimation = createAnimation(10,"Bringer-of-Death_Attack_", ENEMY_SIZE, ENEMY_SIZE, Timeline.INDEFINITE, false);
        deathAnimation = createAnimation(10,"Bringer-of-Death_Death_", ENEMY_SIZE, ENEMY_SIZE, Timeline.INDEFINITE, false);

        // Start the animation
        idleAnimation.play();
//        idleAnimation.play();
    }
}
