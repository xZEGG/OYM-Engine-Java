package components;

import editor.JImGui;
import engine.Component;
import engine.ModelLoader;
import imgui.ImGui;
import imgui.type.ImBoolean;
import org.joml.Vector4f;
import renderEngine.Texture;
import utils.Consts;

public class Material extends Component {
    private Vector4f ambientColour, diffuseColour, specularColour;
    private float reflectance;
    private Texture texture;
    private String texturePath = "resources/textures/Default.jpg";
    private ModelLoader loader = new ModelLoader();
    private Material material;
    private boolean disableCulling;

    public Material() {
        this.ambientColour = Consts.DEFAULT_COLOUR;
        this.diffuseColour = Consts.DEFAULT_COLOUR;
        this.specularColour = Consts.DEFAULT_COLOUR;
        this.texture = null;
        this.disableCulling = false;
        this.reflectance = 0;
    }

    public Material(Vector4f colour, float reflectance){
        this(colour, colour, colour, reflectance, null);
    }

    public Material(Vector4f colour, float reflectance, Texture texture){
        this(colour, colour, colour, reflectance, texture);
    }

    public Material(Texture texture){
        this(Consts.DEFAULT_COLOUR, Consts.DEFAULT_COLOUR, Consts.DEFAULT_COLOUR, 0, texture);
    }

    public Material(Vector4f ambientColour, Vector4f diffuseColour, Vector4f specularColour, float reflectance, Texture texture){
        this.ambientColour = ambientColour;
        this.diffuseColour = diffuseColour;
        this.specularColour = specularColour;
        this.texture = null;
        this.reflectance = reflectance;
    }

    /*
    @Override
    public void imgui() {
        //ImGui.button("Material", 200, 20);
        //if (ImGui.beginDragDropTarget()) {
        //    Object filePath = ImGui.acceptDragDropPayload("CONTENT_BROWSER_ITEM");
        //    if (filePath != null) {
        //        setTexturePath(filePath.toString());
        //        setTexture(new Texture(loader.loadTexture(texturePath)));
        //    }
        //    ImGui.endDragDropTarget();
        //}

        //float[] sColor = {specularColour.x, specularColour.y, specularColour.z, specularColour.w};
        //if (ImGui.colorEdit4("##colorPicker", sColor)) {
        //    specularColour.set(sColor[0], sColor[1], sColor[2], sColor[3]);
        //}

        float[] rfl = {reflectance};
        if(ImGui.dragFloat("Reflectance", rfl)){
            setReflectance(rfl[0]);
        }

        JImGui.booleanCheckbox("Enable Culling", disableCulling);
    }
    */

    //Getters and Setters

    public Vector4f getAmbientColour() {
        return ambientColour;
    }

    public void setAmbientColour(Vector4f ambientColour) {
        this.ambientColour = ambientColour;
    }

    public Vector4f getDiffuseColour() {
        return diffuseColour;
    }

    public void setDiffuseColour(Vector4f diffuseColour) {
        this.diffuseColour = diffuseColour;
    }

    public Vector4f getSpecularColour() {
        return specularColour;
    }

    public void setSpecularColour(Vector4f specularColour) {
        this.specularColour = specularColour;
    }

    public float getReflectance() {
        return reflectance;
    }

    public void setReflectance(float reflectance) {
        this.reflectance = reflectance;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public void setTexturePath(String texturePath) {
        this.texturePath = texturePath;
    }

    public void setTexture(String filePath) {
        ModelLoader modelLoader = new ModelLoader();
        this.texture = new Texture(modelLoader.loadTexture(filePath));
    }

    public boolean hasTexture(){
        return texture != null;
    }

    public boolean isDisableCulling() {
        return disableCulling;
    }

    public void setDisableCulling(boolean disableCulling) {
        this.disableCulling = disableCulling;
    }
}
