package editor;

import engine.GameObject;
import engine.Prefabs;
import imgui.ImGui;
import imgui.flag.ImGuiTreeNodeFlags;
import renderEngine.Window;

import java.util.List;

public class Hierarchy {
    private static Hierarchy hierarchy = null;
    private GameObject selectedObject = new GameObject("GameObject");

    public void imgui() {
        ImGui.begin("Hierarchy");

        if (ImGui.isWindowHovered() && !ImGui.isItemHovered() && ImGui.isMouseDown(1) && !Window.get().getGameViewWindow().getWantCaptureMouse() && Window.get().getMouseState() == false) {
            ImGui.openPopup("Create GameObject");
        }

        if (ImGui.beginPopup("Create GameObject")) {
            if (ImGui.menuItem("Create Empty Game Object")) {
                Prefabs.generateGameObject();
            }

            if (ImGui.menuItem("Create Camera")) {
                Prefabs.generateCameraObject();
            }

            if(ImGui.beginMenu("3D Object")){
                if (ImGui.menuItem("Cube")) {
                    Prefabs.generateMeshObject();
                }
                if (ImGui.menuItem("Sphere")) {
                    Prefabs.generateSphereObject();
                }
                if (ImGui.menuItem("Cylinder")) {
                    Prefabs.generateCylinderObject();
                }
                if (ImGui.menuItem("Plane")) {
                    Prefabs.generatePlaneObject();
                }
                ImGui.endMenu();
            }

            ImGui.endPopup();
        }

        // List of Game Objects
        int index = 0;
        if(Window.get().getCurrentScene().getGameObjects() != null){
            List<GameObject> gameObjects = Window.get().getCurrentScene().getGameObjects();
            for (GameObject obj : gameObjects) {
                ImGui.pushID(index);
                boolean treeNodeOpen = ImGui.treeNodeEx(
                        obj.name,
                        ImGuiTreeNodeFlags.DefaultOpen |
                                ImGuiTreeNodeFlags.FramePadding |
                                ImGuiTreeNodeFlags.OpenOnArrow |
                                ImGuiTreeNodeFlags.SpanAvailWidth,
                        obj.name
                );
                ImGui.popID();

                if (treeNodeOpen) {
                    ImGui.treePop();
                }

                //SELECT GAME OBJECT
                if (ImGui.isItemClicked()) {
                    selectedObject = obj;
                }

                //REMOVE GAME OBJECT
                boolean removeGameObject = false;
                if (ImGui.isItemHovered() && ImGui.isMouseClicked(1)) {
                    selectedObject = obj;
                    ImGui.openPopup("GameObjectSettings");
                }
                if (ImGui.beginPopup("GameObjectSettings")) {
                    if (ImGui.menuItem("Remove Game Object")) {
                        removeGameObject = true;
                    }
                    ImGui.endPopup();
                }
                if (removeGameObject){
                    Window.get().getCurrentScene().removeGameObject(selectedObject);
                }

                //DESELECT GAME OBJECT
                if (ImGui.isWindowHovered() && !ImGui.isItemHovered() && ImGui.isMouseDown(0)) {
                    selectedObject = new GameObject("GameObject");
                }

                index++;
            }
        }

        ImGui.end();
    }

    public static Hierarchy get() {
        if (Hierarchy.hierarchy == null) {
            Hierarchy.hierarchy = new Hierarchy();
        }

        return Hierarchy.hierarchy;
    }

    public GameObject getSelectedGameObject() {
        return selectedObject;
    }
}
