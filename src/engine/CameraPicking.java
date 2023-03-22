package engine;

import org.joml.Intersectionf;
import org.joml.Vector2f;
import org.joml.Vector3f;
import components.Camera;
import utils.Transformation;

import java.util.List;

public class CameraPicking {

    private final Vector3f max;

    private final Vector3f min;

    private final Vector2f nearFar;

    private Vector3f dir;

    public CameraPicking() {
        dir = new Vector3f();
        min = new Vector3f();
        max = new Vector3f();
        nearFar = new Vector2f();
    }

    public void selectGameItem(List<GameObject> gameObjects, Camera camera) {
        dir = Transformation.getViewMatrix(camera).positiveZ(dir).negate();
        selectGameItem(gameObjects, camera.getPosition(), dir);
    }

    protected boolean selectGameItem(List<GameObject> gameObjects, Vector3f center, Vector3f dir) {
        boolean selected = false;
        GameObject selectedGameobject = null;
        float closestDistance = Float.POSITIVE_INFINITY;

        for (GameObject go : gameObjects) {
            min.set(go.transform.getPosition());
            max.set(go.transform.getPosition());
            min.add(-go.transform.getScale().x, -go.transform.getScale().y, -go.transform.getScale().z);
            max.add(go.transform.getScale().x, go.transform.getScale().y, go.transform.getScale().z);
            if (Intersectionf.intersectRayAab(center, dir, min, max, nearFar) && nearFar.x < closestDistance) {
                closestDistance = nearFar.x;
                System.out.println(selectedGameobject);
                selectedGameobject = go;
            }
        }

        if (selectedGameobject != null) {
            selected = true;
        }
        return selected;
    }
}