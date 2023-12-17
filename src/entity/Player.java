package entity;

import javafx.animation.Animation;
import javafx.animation.Timeline;
import javafx.scene.image.Image;

public class Player extends Sprite {
    private final static int PLAYER_SIZE = 80;
    public final static Image PLAYER_SPRITE_IDLE = new Image("images/Character1M_1_idle_1.png", PLAYER_SIZE, PLAYER_SIZE, false, false);
    public final static Image PLAYER_SPRITE_MOVE7 = new Image("images/Character1M_1_run_0.png", PLAYER_SIZE, PLAYER_SIZE, false, false);
    public final static Image PLAYER_SPRITE_JUMP = new Image("images/Character1M_1_jump_0.png", PLAYER_SIZE, PLAYER_SIZE, false, false);
    public boolean isJumping = true;


    public Player(int x_pos, int y_pos) {
        super(Player.PLAYER_SPRITE_JUMP);
        this.setTranslateX(x_pos);
        this.setTranslateY(y_pos);

        idleAnimation = createAnimation(8,"Character1M_1_idle_", PLAYER_SIZE, PLAYER_SIZE, Timeline.INDEFINITE, false);
        moveAnimation = createAnimation(8,"Character1M_1_run_", PLAYER_SIZE, PLAYER_SIZE, Timeline.INDEFINITE, false);
        jumpAnimation = createAnimation(2,"Character1M_1_jump_", PLAYER_SIZE, PLAYER_SIZE, Timeline.INDEFINITE, false);

        // Start the animation
        jumpAnimation.play();
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
        if (idleAnimation.getStatus() == Animation.Status.STOPPED) {
            this.setImage(PLAYER_SPRITE_IDLE);
        }
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
