package renderEngine;

import components.*;
import engine.GameObject;
import engine.ModelLoader;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import utils.Consts;
import utils.Transformation;
import utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;

public class EntityRenderer implements IRenderer {
    Shader shader;

    private Map<MeshRenderer, List<GameObject>> gameObjects;

    private List<PointLight> pointLights;
    private List<SpotLight> spotLights;

    private ModelLoader loader = new ModelLoader();

    public EntityRenderer(){
        gameObjects = new HashMap<>();
        pointLights = new ArrayList<>();
        spotLights = new ArrayList<>();
        shader = new Shader();
    }

    @Override
    public void init() {
        shader.createVertexShader(Utils.loadResource("/shaders/entity_vertex.vs"));
        shader.createFragmentShader(Utils.loadResource("/shaders/entity_fragment.fs"));
        shader.link();
        shader.createUniform("textureSampler");
        shader.createUniform("transformationMatrix");
        shader.createUniform("projectionMatrix");
        shader.createUniform("viewMatrix");
        shader.createUniform("ambientLight");
        shader.createMaterialUniform("material");
        shader.createUniform("specularPower");
        shader.createDirectionalLightUniform("directionalLight");
        shader.createPointLightListUniform("pointLights", 5);
        shader.createSpotLightListUniform("spotLights", 5);
    }

    @Override
    public void render() {
        shader.bind();

        shader.setUniform("projectionMatrix", Window.get().updateProjectionMatrix());

        shader.setUniform("ambientLight", Consts.AMBIENT_LIGHT);
        shader.setUniform("specularPower", Consts.SPECULAR_POWER);

        for(GameObject go : Window.get().getCurrentScene().getGameObjects()){
            if(go.getComponent(DirectionalLight.class) != null){
                DirectionalLight directionalLight = go.getComponent(DirectionalLight.class);
                shader.setUniform("directionalLight", directionalLight);
            }
        }

        int numberPointLights = pointLights != null ? pointLights.size() : 0;
        for(int i = 0; i < numberPointLights; i++) {
            shader.setUniform("pointLights", pointLights.get(i), i);
        }

        int numSpotLights = spotLights != null ? spotLights.size() : 0;
        for(int i = 0; i < numSpotLights; i++) {
            shader.setUniform("spotLights", spotLights.get(i), i);
        }

        for(MeshRenderer meshRenderer : gameObjects.keySet()){
            if(meshRenderer.getMesh() != null && meshRenderer.getMaterial().getTexture() != null){
                bind(meshRenderer);
                List<GameObject> gameObjectsList = gameObjects.get(meshRenderer);
                for(GameObject gameObject : gameObjectsList){
                    prepare(gameObject, Window.get().getCurrentScene().getCamera());
                    GL11.glDrawElements(GL_TRIANGLES, gameObject.getComponent(MeshRenderer.class).getMesh().getVertexCount(), GL_UNSIGNED_INT, 0);
                }
                unbind();
            }
        }

        gameObjects.clear();
        shader.unbind();
    }

    public void bind(MeshRenderer meshRenderer) {
        GL30.glBindVertexArray(meshRenderer.getMesh().getId());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);

        if(meshRenderer.getMaterial().isDisableCulling())
            Renderer.disableCulling();
        else
            Renderer.enableCulling();

        shader.setUniform("material", meshRenderer.getMaterial());
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL_TEXTURE_2D, meshRenderer.getTexture().getId());
    }

    @Override
    public void unbind() {
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }

    @Override
    public void prepare(Object gameObject, Camera camera) {
        shader.setUniform("textureSampler", 0);
        shader.setUniform("transformationMatrix", Transformation.createTransformationMatrix((GameObject) gameObject));
        shader.setUniform("viewMatrix", Transformation.getViewMatrix(camera));
    }

    @Override
    public void cleanup() {
        shader.cleanup();
    }

    public Map<MeshRenderer, List<GameObject>> getGameObjects() {
        return gameObjects;
    }

    public void removePointLight(PointLight pointLight){
        pointLights.remove(pointLight);
    }

    public void addPointLight(PointLight pointLight) {
        this.pointLights.add(pointLight);
    }

    public void addSpotLight(SpotLight spotLight) {
        this.spotLights.add(spotLight);
    }
}
