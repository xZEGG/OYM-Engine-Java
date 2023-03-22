package engine;

import imgui.ImGui;
import org.joml.Random;
import org.joml.Vector3f;
import org.lwjglx.util.vector.Vector4f;
import renderEngine.Window;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.security.SecureRandom;

public abstract class Component {
    //UUID-GUID
    private long uid = -1;
    private transient static final String numbers = "0123456789";
    private transient static SecureRandom rnd = new SecureRandom();

    public transient GameObject gameObject = null;

    private String componentName = getClass().getSimpleName();

    public void start() {}

    public void runtimeStart() {}

    public void update(float dt) {}

    public void runtimeUpdate(float dt) {}

    public void imgui() {
        try {
            Field[] fields = this.getClass().getDeclaredFields();
            for (Field field : fields) {
                boolean isTransient = Modifier.isTransient(field.getModifiers());
                if (isTransient) {
                    continue;
                }

                boolean isPrivate = Modifier.isPrivate(field.getModifiers());
                if (isPrivate) {
                    field.setAccessible(true);
                }

                Class type = field.getType();
                Object value = field.get(this);
                String name = field.getName();

                if (type == int.class) {
                    int val = (int) value;
                    int[] imInt = {val};
                    if (ImGui.dragInt(name + ": ", imInt)) {
                        field.set(this, imInt[0]);
                    }
                } else if (type == float.class) {
                    float val = (float) value;
                    float[] imFloat = {val};
                    if (ImGui.dragFloat(name + ": ", imFloat)) {
                        field.set(this, imFloat[0]);
                    }
                } else if (type == boolean.class) {
                    boolean val = (boolean) value;
                    if (ImGui.checkbox(name + ": ", val)) {
                        field.set(this, !val);
                    }
                } else if (type == Vector3f.class) {
                    Vector3f val = (Vector3f) value;
                    float[] imVec = {val.x, val.y, val.z};
                    if (ImGui.dragFloat3(name + ": ", imVec)) {
                        val.set(imVec[0], imVec[1], imVec[2]);
                    }
                } else if (type == Vector4f.class) {
                    Vector4f val = (Vector4f) value;
                    float[] imVec = {val.x, val.y, val.z, val.w};
                    if (ImGui.dragFloat4(name + ": ", imVec)) {
                        val.set(imVec[0], imVec[1], imVec[2], imVec[3]);
                    }
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void debug() {}

    public void cleanup() {}

    public void runtimeDestroy() {}

    public void generateId() {
        if (this.uid == -1) {
            String randomGeneratedNumberString = randomString(14);
            long randomGeneratedNumber = Long.parseLong(randomGeneratedNumberString);
            this.uid = randomGeneratedNumber;
        }
    }

    String randomString(int length){
        StringBuilder sb = new StringBuilder(length);
        for(int i = 0; i < length; i++)
            sb.append(numbers.charAt(rnd.nextInt(numbers.length())));
        return sb.toString();
    }

    public long getUid() {
        return this.uid;
    }

    public void setUid(long uid) { this.uid = uid; }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public String getComponentName() {
        return componentName;
    }
}