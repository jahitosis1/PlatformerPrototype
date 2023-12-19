package game;

import javafx.animation.*;
import javafx.scene.control.Button;
import javafx.scene.effect.ColorAdjust;
import javafx.util.Duration;

public class SetUpButton {
    public SetUpButton(Button button) {

        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(0.0);

        button.setEffect(colorAdjust);

        button.setOnMouseEntered(e -> {
            Timeline fadeInTimeline = new Timeline(
                    new KeyFrame(Duration.seconds(0),
                            new KeyValue(colorAdjust.brightnessProperty(), colorAdjust.brightnessProperty().getValue(), Interpolator.LINEAR)),
                    new KeyFrame(Duration.millis(250), new KeyValue(colorAdjust.brightnessProperty(), -0.5, Interpolator.LINEAR)
                    ));
            fadeInTimeline.setCycleCount(1);
            fadeInTimeline.setAutoReverse(false);
            fadeInTimeline.play();
        });

        button.setOnMouseExited(e -> {
            Timeline fadeOutTimeline = new Timeline(
                    new KeyFrame(Duration.seconds(0),
                            new KeyValue(colorAdjust.brightnessProperty(), colorAdjust.brightnessProperty().getValue(), Interpolator.LINEAR)),
                    new KeyFrame(Duration.millis(250), new KeyValue(colorAdjust.brightnessProperty(), 0, Interpolator.LINEAR)
                    ));
            fadeOutTimeline.setCycleCount(1);
            fadeOutTimeline.setAutoReverse(false);
            fadeOutTimeline.play();
        });

    }
    
}
