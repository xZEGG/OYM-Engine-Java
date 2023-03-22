package components;

import engine.Component;
import org.joml.Vector3f;

public class SpotLight extends Component {
    private PointLight pointLight;

    private Vector3f coneDirection;

    private float cutoff;

    public SpotLight(PointLight pointLight, float cutoff){
        this.pointLight = pointLight;
        this.coneDirection = new Vector3f(0, 0, 0);
        this.cutoff = cutoff;
    }

    public SpotLight(SpotLight spotLight){
        this.pointLight = spotLight.getPointLight();
        setCutoff(spotLight.getCutoff());
    }

    public PointLight getPointLight() {
        return pointLight;
    }

    public void setPointLight(PointLight pointLight) {
        this.pointLight = pointLight;
    }

    public Vector3f getConeDirection() {
        return coneDirection;
    }

    public void setConeDirection(Vector3f coneDirection) {
        this.coneDirection = coneDirection;
    }

    public float getCutoff() {
        return cutoff;
    }

    public void setCutoff(float cutoff) {
        this.cutoff = cutoff;
    }
}
