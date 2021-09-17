package color.column;

import java.awt.Color;

public class Light {

    private boolean lightOn;

    private final Color color;

    private Shape shape = Shape.CIRCLE;

    public Light(Color color) {
        this.color = color;
        this.lightOn = true;
    }

    public boolean isLightOn() {
        return lightOn;
    }

    public void setLightOn(boolean lightOn) {
        this.lightOn = lightOn;
    }

    public Color getColor() {
        return color;
    }

    public void setShape(Shape shape) {
        this.shape = shape;
    }

	public Shape getShape() {
		return this.shape;
	}
}