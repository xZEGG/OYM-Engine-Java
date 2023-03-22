package editor;

import components.*;
import engine.GameObject;
import imgui.ImGui;
import renderEngine.Window;

public class Properties {
    private GameObject activeGameObject = null;

    public Properties() {

    }

    public void update(Scene currentScene) {
        if(Window.get().getHierarchyWindow().getSelectedGameObject() != null){
            long gameObjectId = Window.get().getHierarchyWindow().getSelectedGameObject().getUid();
            GameObject pickedObj = currentScene.getGameObject(gameObjectId);
            if (pickedObj != null) {
                activeGameObject = pickedObj;
            } else if (pickedObj == null) {
                activeGameObject = null;
            }
        }
    }

    public void imgui() {
        if (activeGameObject != null) {
            ImGui.begin("Properties");

            activeGameObject.imgui();

            // Adding Components //
            if (ImGui.button("+ Add Component", 150, 25)) {
                ImGui.openPopup("Add Component");
            }

            if (ImGui.beginPopup("Add Component")) {
                if (ImGui.menuItem("Add Mesh Renderer")) {
                    if (activeGameObject.getComponent(MeshRenderer.class) == null && activeGameObject.getComponent(Material.class) == null) {
                        activeGameObject.addComponent(new MeshRenderer());
                    }
                }

                if (ImGui.menuItem("Add Camera")) {
                    if (activeGameObject.getComponent(Camera.class) == null) {
                        activeGameObject.addComponent(new Camera());
                    }
                }

                if (ImGui.menuItem("Add Point Light")) {
                    if (activeGameObject.getComponent(PointLight.class) == null) {
                        PointLight pointLight = new PointLight();
                        activeGameObject.addComponent(pointLight);
                        Window.get().getCurrentScene().addPointLightToScene(pointLight);
                    }
                }

                if (ImGui.menuItem("Add Spot Light")) {
                    if (activeGameObject.getComponent(SpotLight.class) == null) {
                        activeGameObject.addComponent(new SpotLight(new PointLight(), 1));
                    }
                }

                if (ImGui.menuItem("Add Character Controller")) {
                    if (activeGameObject.getComponent(CharacterController.class) == null) {
                        CharacterController characterController = new CharacterController();
                        activeGameObject.addComponent(characterController);
                    }
                }

                ImGui.endPopup();
            }
            ///////////////////////////////////////////////////////////////////////

            ImGui.end();
        } else {
            ImGui.begin("Properties");
            ImGui.end();
        }
    }
}
