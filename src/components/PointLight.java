package components;

import engine.Component;
import org.joml.Vector3f;
import renderEngine.Window;

public class PointLight extends Component {
    private Vector3f colour;
    private float intensity, constant, linear, exponent;

    public PointLight(){
        this.colour = new Vector3f(1, 1, 1);
        this.intensity = 1;
        this.constant = 1;
        this.linear = 0;
        this.exponent = 0;
    }

    public PointLight(Vector3f colour){
        this.colour = colour;
        this.intensity = 1;
        this.constant = 1;
        this.linear = 0;
        this.exponent = 0;
    }

    @Override
    public void start(){
        Window.get().getCurrentScene().addPointLightToScene(this);
    }

    //@Override
    //public void imgui(){
    //    float[] imColor = {colour.x, colour.y, colour.z};
    //    if (ImGui.colorPicker3("##colorPicker", imColor)) {
    //        colour.set(imColor[0], imColor[1], imColor[2]);
    //    }
    //}

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

    public float getConstant() {
        return constant;
    }

    public void setConstant(float constant) {
        this.constant = constant;
    }

    public float getLinear() {
        return linear;
    }

    public void setLinear(float linear) {
        this.linear = linear;
    }

    public float getExponent() {
        return exponent;
    }

    public void setExponent(float exponent) {
        this.exponent = exponent;
    }
}
