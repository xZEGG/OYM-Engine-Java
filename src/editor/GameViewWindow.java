package editor;

import components.Camera;
import components.Gizmos;
import components.Transform;
import engine.*;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiWindowFlags;
import renderEngine.Window;

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

public class GameViewWindow {
    private static GameViewWindow instance;
    private float leftX, rightX, topY, bottomY;
    private Gizmos gizmos;
    private String currentSceneTMPFile;
    private transient static final String numbers = "0123456789";
    private transient static SecureRandom rnd = new SecureRandom();

    public GameViewWindow() {
        this.gizmos = new Gizmos();
    }

    public void imgui() {
        ImGui.begin("Viewport", ImGuiWindowFlags.NoScrollbar | ImGuiWindowFlags.NoScrollWithMouse);

        if(ImGui.beginMenu("Status")){
            if(ImGui.menuItem("Play", "", Window.get().getEditorLayer().isRunning(), !Window.get().getEditorLayer().isRunning())) {
                /*
                Window.get().getEditorLayer().getEditorScene().saveToTMPFile();
                Scene runtimeScene = new Scene();
                if(Window.get().getEditorLayer().getEditorScene().getCurrentSceneTMPFile() != null){
                    Window.get().getEditorLayer().changeCurrentScene(runtimeScene);
                } else {
                    Window.get().getEditorLayer().setRunning(false);
                }
                runtimeScene.loadTMPFile(Window.get().getEditorLayer().getEditorScene().getCurrentSceneTMPFile());

                for(GameObject gameObject : Window.get().getEditorLayer().getCurrentScene().getGameObjects()){
                    if(gameObject.getComponent(Camera.class) != null && Window.get().getEditorLayer().getCurrentScene() != Window.get().getEditorLayer().getEditorScene()){
                        Camera newCamera = gameObject.getComponent(Camera.class);
                        Window.get().getEditorLayer().getCurrentScene().setPrimaryCamera(newCamera);
                    }
                }
                Window.get().getEditorLayer().getCurrentScene().runtimeStart();
                Window.get().getEditorLayer().setRunning(true);
                Window.get().changeScene(copyScene(Window.get().getEditorLayer().getEditorScene()));
                for(GameObject gameObject : Window.get().getCurrentScene().getGameObjects()){
                    if(gameObject.getComponent(Camera.class) != null && Window.get().getCurrentScene() != Window.get().getEditorLayer().getEditorScene()){
                        Camera newCamera = gameObject.getComponent(Camera.class);
                        Window.get().getCurrentScene().setPrimaryCamera(newCamera);
                    }
                }
                Window.get().getCurrentScene().runtimeStart();
                Window.get().getEditorLayer().setRunning(true);
            }

            if(ImGui.menuItem("Stop", "", !Window.get().getEditorLayer().isRunning(), Window.get().getEditorLayer().isRunning())){
                Window.get().getEditorLayer().setRunning(false);
                Window.get().getCurrentScene().runtimeDestroy();
                Window.get().changeScene(Window.get().getEditorLayer().getEditorScene());
            }
            */
            }
            ImGui.endMenu();
        }

        ImVec2 windowSize = getLargestSizeForViewport();
        ImVec2 windowPos = getCenteredPositionForViewport(windowSize);

        ImGui.setCursorPos(windowPos.x, windowPos.y);

        ImVec2 topLeft = new ImVec2();
        ImGui.getCursorScreenPos(topLeft);
        topLeft.x -= ImGui.getScrollX();
        topLeft.y -= ImGui.getScrollY();
        leftX = topLeft.x;
        bottomY = topLeft.y;
        rightX = topLeft.x + windowSize.x;
        topY = topLeft.y + windowSize.y;

        int textureId = Window.getFramebuffer().getTextureID();
        ImGui.image(textureId, windowSize.x, windowSize.y, 0, 1, 1, 0);

        gizmos.imguizmo();

        //if (ImGui.beginDragDropTarget()) {
        //    Object filePath = ImGui.acceptDragDropPayload("CONTENT_BROWSER_ITEM");
        //    if (filePath != null) {
        //        GameObject object = Prefabs.generateMeshObject(filePath.toString());
        //        Window.getScene().addGameObjectToScene(object);
        //    }
        //    ImGui.endDragDropTarget();
        //}

        ImGui.end();

        //System.out.println(Window.get().getCurrentLayer().getCurrentScene().getSceneName());
    }

    public boolean getWantCaptureMouse() {
        return MouseInput.getX() >= leftX && MouseInput.getX() <= rightX &&
                MouseInput.getY() >= bottomY && MouseInput.getY() <= topY;
    }

    public static GameViewWindow get() {
        if (GameViewWindow.instance == null) {
            GameViewWindow.instance = new GameViewWindow();
        }

        return GameViewWindow.instance;
    }

    private ImVec2 getLargestSizeForViewport() {
        ImVec2 windowSize = new ImVec2();
        ImGui.getContentRegionAvail(windowSize);
        windowSize.x -= ImGui.getScrollX();
        windowSize.y -= ImGui.getScrollY();

        float aspectWidth = windowSize.x;
        float aspectHeight = aspectWidth / Window.getTargetAspectRatio();
        if (aspectHeight > windowSize.y) {
            // We must switch to pillarbox mode
            aspectHeight = windowSize.y;
            aspectWidth = aspectHeight * Window.getTargetAspectRatio();
        }

        return new ImVec2(aspectWidth, aspectHeight);
    }

    private ImVec2 getCenteredPositionForViewport(ImVec2 aspectSize) {
        ImVec2 windowSize = new ImVec2();
        ImGui.getContentRegionAvail(windowSize);
        windowSize.x -= ImGui.getScrollX();
        windowSize.y -= ImGui.getScrollY();

        float viewportX = (windowSize.x / 2.0f) - (aspectSize.x / 2.0f);
        float viewportY = (windowSize.y / 2.0f) - (aspectSize.y / 2.0f);

        return new ImVec2(viewportX + ImGui.getCursorPosX(),
                viewportY + ImGui.getCursorPosY());
    }
}