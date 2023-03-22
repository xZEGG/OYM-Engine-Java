package renderEngine;

import components.*;
import engine.GameObject;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.*;

import static org.lwjgl.opengl.GL11.*;

public class Renderer {
    private final Window window;
    private EntityRenderer entityRenderer;

    private static boolean isCulling = false;

    public Renderer(){
        window = Window.get();
    }

    public void init(){
        entityRenderer = new EntityRenderer();

        entityRenderer.init();
    }

    public void render(){
        entityRenderer.render();
    }

    public static void enableCulling(){
        if(!isCulling){
            GL11.glEnable(GL_CULL_FACE);
            GL11.glCullFace(GL_BACK);
            isCulling = true;
        }
    }

    public static void disableCulling(){
        if(isCulling){
            GL11.glDisable(GL_CULL_FACE);
            isCulling = false;
        }
    }

    public void add(GameObject gameObject){
        MeshRenderer meshRenderer = gameObject.getComponent(MeshRenderer.class);

        if(meshRenderer != null){
            List<GameObject> gameObjectsList = entityRenderer.getGameObjects().get(gameObject.getComponent(MeshRenderer.class));
            if(gameObjectsList != null)
                gameObjectsList.add(gameObject);
            else {
                List<GameObject> newGameObjectList = new ArrayList<>();
                newGameObjectList.add(gameObject);
                entityRenderer.getGameObjects().put(gameObject.getComponent(MeshRenderer.class), newGameObjectList);
            }
        }
    }

    public void removeGameObject(GameObject gameObject){
        entityRenderer.getGameObjects().remove(gameObject);
    }

    public void removePointLight(PointLight pointLight){
        entityRenderer.removePointLight(pointLight);
    }

    public void clear(){
        glClear(GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }

    public void cleanup(){
        entityRenderer.cleanup();
    }

    public void addPointLight(PointLight pointLight){
        entityRenderer.addPointLight(pointLight);
    }

    public void addSpotLight(SpotLight spotLight){
        entityRenderer.addSpotLight(spotLight);
    }
}