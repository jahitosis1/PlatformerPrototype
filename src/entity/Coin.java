package entity;

import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;

public class Coin extends Sprite {
    private final static int COIN_SIZE = 120;
    private final static Image COIN_SPRITE = new Image("images/Coin_0.png", COIN_SIZE, COIN_SIZE, false, false);
    private final Timeline idleAnimation;


    public Coin(int x_pos, int y_pos) {
        super(Coin.COIN_SPRITE);
        this.setTranslateX(x_pos);
        this.setTranslateY(y_pos);

        idleAnimation = createAnimation(5,"Coin_", COIN_SIZE, COIN_SIZE, Timeline.INDEFINITE, false);

        // Start the animation
        idleAnimation.play();
//        idleAnimation.play();
    }
}
