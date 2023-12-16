package entity;

import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;

public class Trap extends Sprite {
    private final static int TRAP_SIZE = 50;
    public final static Image TRAP_POPUP = new Image("images/Idle_0.png", TRAP_SIZE, TRAP_SIZE, false, false);
    private final Timeline popUpAnimation;


    public Trap(int x_pos, int y_pos) {
        super(Trap.TRAP_POPUP);
        this.collisionShape = new Rectangle(TRAP_SIZE, TRAP_SIZE);
        this.setTranslateX(x_pos);
        this.setTranslateY(y_pos);

        popUpAnimation = createAnimation(6,"Idle_", TRAP_SIZE, TRAP_SIZE, Timeline.INDEFINITE, false);
        
        // Start the animation
        popUpAnimation.play();
//        idleAnimation.play();
    }
}
