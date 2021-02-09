package MediaPlayer;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * This class extens Parent.
 * It is used to create the bars or meters in the audio equalizer
 */
public class Meter extends Parent {
    private final Color BAR_COLOR = Color.web("Blue");
    private Rectangle[] bars = new Rectangle[20];
    private DoubleProperty value = new SimpleDoubleProperty(0) {
        @Override protected void invalidated() {
            super.invalidated();
            double lastBar = get()*bars.length;
            for(int i=0; i<bars.length;i++) {
                bars[i].setVisible(i < lastBar);
            }
        }
    };

    /**
     * This function sets the value for meter as v
     * @param v
     */
    public void setValue(double v) { value.set(v); }

    /**
     * This function fetches the meter value
     * @return a double value
     */
    public double getValue() { return value.get(); }

    /**
     * This function fetches the meter value
     * @return DoubleProperty
     */
    public DoubleProperty valueProperty() { return value; }

    /**
     * Constructor method
     */
    public Meter() {
        for(int i=0; i<bars.length;i++) {
            bars[i] = new Rectangle(26, 2);
            bars[i].setFill(BAR_COLOR);
            bars[i].setX(-13);
            bars[i].setY(1-(i*4));
        }
        getChildren().addAll(bars);
    }
}
