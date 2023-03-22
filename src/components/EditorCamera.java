package components;

import engine.Component;
import engine.KeyInput;
import engine.MouseInput;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import renderEngine.Window;

public class EditorCamera extends Component {
    private Camera editorCamera;
    private Vector3f cameraInc;
    private float cameraSpeed = 0.1f;
    private float currentSpeed;

    public float moveSpeed, normalSpeed = 0.01f, boostSpeed = 0.05f, mouseSensitivity = 0.15f;
    public double oldMouseX, oldMouseY, newMouseX, newMouseY;

    private Transform lastTransform;
    private boolean isDirty = false;

    public EditorCamera(){
        editorCamera = new Camera();
        cameraInc = new Vector3f();
    }

    @Override
    public void start(){
        this.lastTransform = this.gameObject.transform;
    }

    public void update(){
        newMouseX = MouseInput.getX();
        newMouseY = MouseInput.getY();

        float dx = (float) (newMouseX - oldMouseX);
        float dy = (float) (newMouseY - oldMouseY);
        if (MouseInput.mouseButtonDown(GLFW.GLFW_MOUSE_BUTTON_RIGHT)) {
            Window.get().mouseState(true);

            cameraInc.set(0,0,0);
            if(KeyInput.isKeyPressed(GLFW.GLFW_KEY_W)){
                cameraInc.z = -1;
            }
            if(KeyInput.isKeyPressed(GLFW.GLFW_KEY_S)){
                cameraInc.z = 1;
            }
            if(KeyInput.isKeyPressed(GLFW.GLFW_KEY_A)){
                cameraInc.x = -1;
            }
            if(KeyInput.isKeyPressed(GLFW.GLFW_KEY_D)){
                cameraInc.x = 1;
            }
            if(KeyInput.isKeyPressed(GLFW.GLFW_KEY_Q)){
                cameraInc.y = 1;
            }
            if(KeyInput.isKeyPressed(GLFW.GLFW_KEY_E)){
                cameraInc.y = -1;
            }
            if(KeyInput.isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT)){
                currentSpeed = cameraSpeed * 2f;
            } else {
                currentSpeed = cameraSpeed;
            }
            if (KeyInput.isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT)) moveSpeed = boostSpeed;
            else moveSpeed = normalSpeed;

            movePosition(cameraInc.x * currentSpeed, cameraInc.y * currentSpeed, cameraInc.z * currentSpeed);

            //Editor Camera Rotation

            moveRotation(dy * mouseSensitivity, dx * mouseSensitivity, 0);

        } else if(!MouseInput.mouseButtonDown(GLFW.GLFW_MOUSE_BUTTON_RIGHT)){
            Window.get().mouseState(false);
        }
        oldMouseX = newMouseX;
        oldMouseY = newMouseY;
    }

    public void movePosition(float x, float y, float z){
        if(z != 0){
            this.gameObject.transform.position.x += (float) Math.sin(Math.toRadians(this.gameObject.transform.rotation.y)) * -1.0f * z;
            this.gameObject.transform.position.z += (float) Math.cos(Math.toRadians(this.gameObject.transform.rotation.y)) * z;
        }
        if(x != 0){
            this.gameObject.transform.position.x += (float) Math.sin(Math.toRadians(this.gameObject.transform.rotation.y - 90)) * -1.0f * x;
            this.gameObject.transform.position.z += (float) Math.cos(Math.toRadians(this.gameObject.transform.rotation.y - 90)) * x;
        }
        if(y != 0){
            this.gameObject.transform.position.y += y;
        }
    }

    public void setPosition(float x, float y, float z){
        this.gameObject.transform.position.x = x;
        this.gameObject.transform.position.y = y;
        this.gameObject.transform.position.z = z;
    }

    public void setRotation(float x, float y, float z){
        this.gameObject.transform.rotation.x = x;
        this.gameObject.transform.rotation.y = y;
        this.gameObject.transform.rotation.z = z;
    }

    public void moveRotation(float x, float y, float z){
        this.gameObject.transform.rotation.x += x;
        this.gameObject.transform.rotation.y += y;
        this.gameObject.transform.rotation.z += z;
    }

    public Vector3f getPosition() {
        return this.gameObject.transform.position;
    }

    public void setRotation(Vector3f rotation){
        this.gameObject.transform.rotation = rotation;
    }

    public Vector3f getRotation() {
        return this.gameObject.transform.rotation;
    }

    public boolean isDirty() {
        return this.isDirty;
    }

    public void setClean() {
        this.isDirty = false;
    }

    public Camera getEditorCamera() {
        return editorCamera;
    }
}
