package components;

import editor.JImGui;
import engine.Component;
import org.joml.Vector3f;

public class Transform extends Component {
    public Vector3f position;
    public Vector3f rotation;
    public Vector3f scale;
    public boolean isActive;

    public Transform() {
        init(new Vector3f(), new Vector3f(), new Vector3f(1,1,1));
    }

    public Transform(Vector3f position) {
        init(position, new Vector3f(0, 0, 0), new Vector3f(1, 1, 1));
    }

    public Transform(Vector3f position, Vector3f rotation) {
        init(position, rotation, new Vector3f(1, 1, 1));
    }

    public Transform(Vector3f position, Vector3f rotation, Vector3f scale) {
        init(position, rotation, scale);
    }

    public void init(Vector3f position, Vector3f rotation, Vector3f scale) {
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
        this.isActive = true;
    }

    @Override
    public void imgui() {
        gameObject.name = JImGui.inputText("Name: ", gameObject.name);
        isActive = JImGui.booleanCheckbox("Is Active", isActive);
        JImGui.drawVec3Control("Position", this.position);
        JImGui.drawVec3Control("Scale", this.scale, 1.0f);
        JImGui.drawVec3Control("Rotation", this.rotation);
    }

    public Transform copy() {
        return new Transform(new Vector3f(this.position.x, this.position.y, this.position.z), new Vector3f(this.scale.x, this.scale.y, this.scale.z));
    }

    public void copy(Transform to) {
        to.position.set(this.position);
        to.scale.set(this.scale);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (!(o instanceof Transform)) return false;

        Transform t = (Transform) o;
        return t.position.equals(this.position) && t.rotation.equals(this.rotation) && t.scale.equals(this.scale);
    }

    public float[] getAll() {
        return new float[]{position.x, position.y, position.z, rotation.x, rotation.y, rotation.z, scale.x, scale.y, scale.z};
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public Vector3f getScale() {
        return scale;
    }
}
