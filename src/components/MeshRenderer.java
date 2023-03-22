package components;

import engine.Component;
import engine.ModelLoader;
import imgui.ImGui;
import renderEngine.Mesh;
import renderEngine.Texture;

public class MeshRenderer extends Component {
    private Mesh mesh;
    private String meshPath = "resources/models/default/Cube.obj";
    private Material material;
    private String texturePath = "resources/textures/Default.jpg";
    private transient ModelLoader loader = new ModelLoader();
    private boolean isDoubleSided;

    private transient boolean isDirty = false;

    @Override
    public void start() {
        mesh = new Mesh();
        material = new Material();
        try {
            mesh = loader.loadOBJModel(meshPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        setTexture(new Texture(loader.loadTexture(texturePath)));
    }

    @Override
    public void update(float dt){
    }

    @Override
    public void imgui() {
        //Mesh
        ImGui.button("Mesh", 200, 20);
        try {
            if (ImGui.beginDragDropTarget()) {
                Object meshPath = ImGui.acceptDragDropPayload("CONTENT_BROWSER_ITEM");
                if (meshPath != null) {
                    setTexturePath(texturePath);
                    setTexture(new Texture(loader.loadTexture(texturePath)));
                    setMeshPath(meshPath.toString());
                    setMesh(loader.loadOBJModel(meshPath.toString()));
                }
                ImGui.endDragDropTarget();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Material
        ImGui.button("Material", 200, 20);
        if (ImGui.beginDragDropTarget()) {
            Object texturePath = ImGui.acceptDragDropPayload("CONTENT_BROWSER_ITEM");
            if (texturePath != null) {
                setTexturePath(texturePath.toString());
                setTexture(new Texture(loader.loadTexture(texturePath.toString())));
            }
            ImGui.endDragDropTarget();
        }

        material.imgui();
//
        //ImGui.button("Material", 200, 20);
        //if (ImGui.beginDragDropTarget()) {
        //    Object filePath = ImGui.acceptDragDropPayload("CONTENT_BROWSER_ITEM");
        //    if (filePath != null) {
        //        System.out.println("yes");
        //        setTexturePath(filePath.toString());
        //        setTexture(new Texture(loader.loadTexture(texturePath)));
        //    }
        //    ImGui.endDragDropTarget();
        //}
////
        //JImGui.booleanCheckbox("isDoubleSided", isDoubleSided);
    }

    @Override
    public void cleanup(){
        loader.cleanup();
        material.cleanup();
    }

    //Getters and Setters

    public void setMesh(Mesh mesh) {
        this.mesh = mesh;
    }

    public void setMeshPath(String filePath) {
        this.meshPath = filePath;
    }

    public void setTexture(Texture texture) {
        material.setTexture(texture);
    }

    public void setTexturePath(String texturePath) {
        this.texturePath = texturePath;
    }

    public Mesh getMesh() {
        return mesh;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public Texture getTexture(){
        return material.getTexture();
    }

    public String getTexturePath() {
        return texturePath;
    }

    public boolean isDoubleSided() {
        return isDoubleSided;
    }
}