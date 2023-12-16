package entities;

import javafx.animation.Animation;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;

public class Player extends Sprite {
    private final static int PLAYER_SIZE = 50;
    public final static Image PLAYER_SPRITE_IDLE = new Image("images/Character1M_1_idle_0.png", PLAYER_SIZE, PLAYER_SIZE, false, false);
    public final static Image PLAYER_SPRITE_MOVE7 = new Image("images/Character1M_1_run_6.png", PLAYER_SIZE, PLAYER_SIZE, false, false);
    public final static Image PLAYER_SPRITE_JUMP = new Image("images/Character1M_1_jump_0.png", PLAYER_SIZE, PLAYER_SIZE, false, false);
    private final Timeline idleAnimation;
    private final Timeline moveAnimation;
    private final Timeline jumpAnimation;
    public boolean isJumping = false;


    public Player(int x_pos, int y_pos) {
        super(Player.PLAYER_SPRITE_IDLE);
        this.collisionShape = new Rectangle(PLAYER_SIZE, PLAYER_SIZE);
        this.setTranslateX(x_pos);
        this.setTranslateY(y_pos);

        idleAnimation = createAnimation(8,"Character1M_1_idle_", PLAYER_SIZE, PLAYER_SIZE, Timeline.INDEFINITE, false);
        moveAnimation = createAnimation(8,"Character1M_1_run_", PLAYER_SIZE, PLAYER_SIZE, Timeline.INDEFINITE, false);
        jumpAnimation = createAnimation(2,"Character1M_1_jump_", PLAYER_SIZE, PLAYER_SIZE, Timeline.INDEFINITE, false);

        // Start the animation
        idleAnimation.play();
//        idleAnimation.play();
    }

    public void startMoveAnimation() {
        if (isJumping) return;
        if (moveAnimation.getStatus() == Animation.Status.STOPPED) {
            this.setImage(PLAYER_SPRITE_MOVE7);
        }
        idleAnimation.stop();
        moveAnimation.play();
    }

    // Add a method to stop the move animation
    public void stopMoveAnimation() {
        if (isJumping) return;
        moveAnimation.stop();
        this.setImage(PLAYER_SPRITE_IDLE);
        idleAnimation.play();
    }
    public void startJumpAnimation() {
        isJumping = true;
        this.setImage(PLAYER_SPRITE_JUMP);
        jumpAnimation.play();
    }
    public void stopJumpAnimation() {
        jumpAnimation.stop();
        isJumping = false;
    }
}
