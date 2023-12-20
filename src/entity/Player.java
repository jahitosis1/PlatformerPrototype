package entity;

import javafx.animation.*;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;

public class Player extends Sprite {
    private final static int PLAYER_SIZE = 80;
    private final static Image PLAYER_SPRITE_IDLE = new Image("images/Character1M_1_idle_1.png", PLAYER_SIZE, PLAYER_SIZE, false, false);
    private final static Image PLAYER_SPRITE_MOVE7 = new Image("images/Character1M_1_run_0.png", PLAYER_SIZE, PLAYER_SIZE, false, false);
    private final static Image PLAYER_SPRITE_JUMP = new Image("images/Character1M_1_jump_0.png", PLAYER_SIZE, PLAYER_SIZE, false, false);
    private final Media damageSFX = new Media(new File("images/on_damage.wav").toURI().toString());
    private final MediaPlayer on_damage = new MediaPlayer(damageSFX);
    public boolean isJumping = true;
    public boolean invulnerable = false;
    public int player_hp = 10;


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
    private void invulnerabilityTimer() {
        // Change invulnerable to true
        invulnerable = true;

        // Create a FadeTransition on the player
        FadeTransition fade = new FadeTransition(Duration.seconds(3), this);
        fade.setFromValue(1.0);
        fade.setToValue(0.0);
        fade.setDuration(Duration.millis(250));
        fade.setAutoReverse(true);
        fade.setCycleCount(Transition.INDEFINITE);

        // Start the FadeTransition
        fade.play();

        // Create a PauseTransition with a duration of 3 seconds
        PauseTransition pause = new PauseTransition(Duration.seconds(1));

        // Set a ChangeListener to change invulnerable back to false after the pause
        pause.setOnFinished(event -> {
            invulnerable = false;
            fade.stop();
        });

        // Start the PauseTransition
        pause.play();
    }

    public void damagePlayer(int value) {
        if (!invulnerable) {
            on_damage.stop();
            on_damage.seek(Duration.ZERO);
            on_damage.play();
            player_hp -= value;
            invulnerabilityTimer();
        }
    }

}
