package components;

import engine.Component;
import engine.KeyInput;
import org.lwjgl.glfw.GLFW;

public class CharacterController extends Component {
    public float characterSpeed;

    @Override
    public void runtimeUpdate(float dt){
        if(KeyInput.isKeyPressed(GLFW.GLFW_KEY_W)){
            this.gameObject.transform.position.x += characterSpeed;
        }
    }
}
