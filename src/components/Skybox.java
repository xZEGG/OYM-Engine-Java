package components;

import engine.Component;
import engine.ModelLoader;
import renderEngine.Mesh;
import renderEngine.Texture;

public class Skybox extends Component {
    private Mesh skyboxMesh;
    private Material skyboxMaterial;
    private ModelLoader modelLoader;

    public Skybox(){
        modelLoader = new ModelLoader();
        try {
            this.skyboxMesh = modelLoader.loadOBJModel("/models/default/Cube.obj");
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.skyboxMaterial = new Material(new Texture(modelLoader.loadTexture("resources/textures/necati.JPG")));
    }

    public Skybox(String texturePath){
        modelLoader = new ModelLoader();
        try {
            this.skyboxMesh = modelLoader.loadOBJModel("/models/default/Cube.obj");
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.skyboxMaterial = new Material(new Texture(modelLoader.loadTexture(texturePath)));
    }

    //GETTERS AND SETTERS

    public Mesh getSkyboxMesh() {
        return skyboxMesh;
    }

    public Material getSkyboxMaterial() {
        return skyboxMaterial;
    }
}
