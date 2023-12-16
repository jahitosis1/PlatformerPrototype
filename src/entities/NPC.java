package entities;

import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;

public class NPC extends Sprite {
    private final static int NPC_SIZE = 60;
    public final static Image NPC_SPRITE = new Image("Idle_0.png", NPC_SIZE, NPC_SIZE, false, false);
    private final Timeline idleAnimation;

    public NPC(int x_pos, int y_pos) {
        super(NPC.NPC_SPRITE);
        this.collisionShape = new Rectangle(NPC_SIZE, NPC_SIZE);
        this.setTranslateX(x_pos);
        this.setTranslateY(y_pos);

        idleAnimation = createAnimation(6,"Idle_", NPC_SIZE, NPC_SIZE, Timeline.INDEFINITE, false);

        // Start the animation
        idleAnimation.play();
//        idleAnimation.play();
    }
}
