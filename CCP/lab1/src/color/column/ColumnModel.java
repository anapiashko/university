package color.column;

import java.awt.*;

public class ColumnModel {

    private Light[] lights;

    public ColumnModel() {
        this.lights = new Light[1];
//        lights[0] = new Light(Color.RED);
//        lights[1] = new ColumnLight(Color.YELLOW, 10);
        lights[0] = new Light(Color.GREEN);
    }

    public Light[] getLights() {
        return lights;
    }

    public void setAllLights(boolean lightOn) {
        for (int i = 0; i < lights.length; i++) {
            setLightOn(i, lightOn);
        }
    }

    public void setLightOn(int index, boolean lightOn) {
        lights[index].setLightOn(lightOn);
    }

    public void setLightOn(Color color, boolean lightOn) {
        for (int i = 0; i < lights.length; i++) {
            if(lights[i].getColor().equals(color)) {
                setLightOn(i, lightOn);
            }
        }
    }

    public boolean isLightOn(int index) {
        return lights[index].isLightOn();
    }

    public boolean isLightOn(Color color) {
        for (Light light : lights) {
            if (light.getColor().equals(color)) {
                return light.isLightOn();
            }
        }
        return false;
    }

    public Color getColor(int index) {
        return lights[index].getColor();
    }

    public void setGreenLight(boolean lightOn) {
        setLightOn(Color.GREEN, lightOn);
    }

    public void setRedLight(boolean lightOn) {
        setLightOn(Color.RED, lightOn);
    }
}
