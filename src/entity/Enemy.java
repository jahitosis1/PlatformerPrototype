package entity;

import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

public abstract class Enemy extends Sprite {
    protected final static int ENEMY_SIZE = 100;
    protected Timeline deathAnimation;
    protected double orig_x;
    protected double orig_y;
    protected double maxRange;
    protected boolean isAlive = true;
    private final Media deathSFX = new Media(new File("images/enemy_death.wav").toURI().toString());
    private final MediaPlayer on_death = new MediaPlayer(deathSFX);
    public Enemy(Image sprite, int orig_x, int orig_y) {
        super(sprite);
        this.setTranslateX(orig_x);
        this.setTranslateY(orig_y);
        this.orig_x = orig_x;
        this.orig_y = orig_y;
    }

    public abstract void move();
    public void die(){
        this.on_death.play();
    }

}
