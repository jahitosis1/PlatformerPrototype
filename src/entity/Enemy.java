package entity;

import javafx.animation.Timeline;
import javafx.scene.image.Image;

public abstract class Enemy extends Sprite{
    protected final static int ENEMY_SIZE = 50;
    protected Timeline moveAnimation;
    protected Timeline deathAnimation;
    protected double orig_x;
    protected double orig_y;
    protected boolean isAlive = true;
    public Enemy(Image sprite, int orig_x, int orig_y) {
        super(sprite);
        this.orig_x = orig_x;
        this.orig_y = orig_y;
    }

    public abstract void move();
    public abstract void die();

}
