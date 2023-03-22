package engine;

import components.*;
import renderEngine.Window;

public class Prefabs {
    private static ModelLoader loader = new ModelLoader();

    public static GameObject generateGameObject() {
        GameObject gameObject = Window.get().getCurrentScene().createGameObject("GameObject");
        Window.get().getCurrentScene().addGameObjectToScene(gameObject);

        return gameObject;
    }

    public static GameObject generateCameraObject() {
        GameObject gameObject = Window.get().getCurrentScene().createGameObject("Camera");
        gameObject.addComponent(new Camera());
        Window.get().getCurrentScene().addGameObjectToScene(gameObject);

        return gameObject;
    }

    public static GameObject generateMeshObject() {
        GameObject gameObject = Window.get().getCurrentScene().createGameObject("Cube");
        gameObject.addComponent(new MeshRenderer());
        Window.get().getCurrentScene().addGameObjectToScene(gameObject);

        return gameObject;
    }

    public static GameObject generateSphereObject() {
        GameObject gameObject = Window.get().getCurrentScene().createGameObject("Sphere");
        gameObject.addComponent(new MeshRenderer());
        String spherePath = "resources/models/default/Sphere.obj";
        gameObject.getComponent(MeshRenderer.class).setMeshPath(spherePath);
        try {
            gameObject.getComponent(MeshRenderer.class).setMesh(loader.loadOBJModel(spherePath));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Window.get().getCurrentScene().addGameObjectToScene(gameObject);

        return gameObject;
    }

    public static GameObject generateCylinderObject() {
        GameObject gameObject = Window.get().getCurrentScene().createGameObject("Cylinder");
        gameObject.addComponent(new MeshRenderer());
        String cylinderPath = "resources/models/default/Cylinder.obj";
        gameObject.getComponent(MeshRenderer.class).setMeshPath(cylinderPath);
        try {
            gameObject.getComponent(MeshRenderer.class).setMesh(loader.loadOBJModel(cylinderPath));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Window.get().getCurrentScene().addGameObjectToScene(gameObject);

        return gameObject;
    }

    public static GameObject generatePlaneObject() {
        GameObject gameObject = Window.get().getCurrentScene().createGameObject("Plane");
        gameObject.addComponent(new MeshRenderer());
        String planePath = "resources/models/default/Plane.obj";
        gameObject.getComponent(MeshRenderer.class).setMeshPath(planePath);
        try {
            gameObject.getComponent(MeshRenderer.class).setMesh(loader.loadOBJModel(planePath));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Window.get().getCurrentScene().addGameObjectToScene(gameObject);

        return gameObject;
    }

    public static GameObject generateSpriteObject() {
        float[] vertices = {
                -1, -1, 0,
                -1,  1, 0,
                1,  1, 0,
                1, -1, 0
        };
        int[] indices = {
                0,1,2,
                0,2,3
        };
        float[] texCoords = {
                1, 1,
                1, 0,
                0, 0,
                0, 1
        };
        GameObject gameObject = Window.get().getCurrentScene().createGameObject("Sprite");
        gameObject.addComponent(new MeshRenderer());
        gameObject.getComponent(MeshRenderer.class).setMeshPath("");
        try {
            gameObject.getComponent(MeshRenderer.class).setMesh(loader.loadMesh(vertices, texCoords, indices));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Window.get().getCurrentScene().addGameObjectToScene(gameObject);

        return gameObject;
    }
}
