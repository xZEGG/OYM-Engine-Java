package engine;

import org.joml.Matrix4f;
import org.joml.Vector2d;
import org.joml.Vector3f;
import org.joml.Vector4f;
import components.Camera;
import renderEngine.Window;
import utils.Transformation;

import java.util.List;

public class MousePicking extends CameraPicking {

    private final Matrix4f invProjectionMatrix;

    private final Matrix4f invViewMatrix;

    private final Vector3f mouseDir;

    private final Vector4f tmpVec;

    public MousePicking() {
        super();
        invProjectionMatrix = new Matrix4f();
        invViewMatrix = new Matrix4f();
        mouseDir = new Vector3f();
        tmpVec = new Vector4f();
    }

    public boolean selectGameItem(List<GameObject> gameObjects, Window window, Vector2d mousePos, Camera camera) {
        int wdwWitdh = window.getWidth();
        int wdwHeight = window.getHeight();

        float x = (float)(2 * mousePos.x) / (float)wdwWitdh - 1.0f;
        float y = 1.0f - (float)(2 * mousePos.y) / (float)wdwHeight;
        float z = -1.0f;

        invProjectionMatrix.set(window.getProjectionMatrix());
        invProjectionMatrix.invert();

        tmpVec.set(x, y, z, 1.0f);
        tmpVec.mul(invProjectionMatrix);
        tmpVec.z = -1.0f;
        tmpVec.w = 0.0f;

        Matrix4f viewMatrix = Transformation.getViewMatrix(camera);
        invViewMatrix.set(viewMatrix);
        invViewMatrix.invert();
        tmpVec.mul(invViewMatrix);

        mouseDir.set(tmpVec.x, tmpVec.y, tmpVec.z);

        return selectGameItem(gameObjects, camera.getPosition(), mouseDir);
    }
}