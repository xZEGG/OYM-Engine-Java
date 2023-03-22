package components;

import engine.Component;
import org.joml.Vector3f;

public class DirectionalLight extends Component {
    private Vector3f colour;
    private float intensity;

    public DirectionalLight(){
        this.colour = new Vector3f(1, 1, 1);
        this.intensity = 1;
    }

    public DirectionalLight(Vector3f colour, float intensity){
        this.colour = colour;
        this.intensity = intensity;
    }

    public Vector3f getColour() {
        return colour;
    }

    public void setColour(Vector3f colour) {
        this.colour = colour;
    }

    public float getIntensity() {
        return intensity;
    }

    public void setIntensity(float intensity) {
        this.intensity = intensity;
    }
}
