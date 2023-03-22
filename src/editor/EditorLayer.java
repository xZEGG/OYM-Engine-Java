package editor;

import components.Transform;
import engine.Component;
import engine.GameObject;
import engine.Layer;
import renderEngine.Window;

import java.util.List;

public class EditorLayer extends Layer {
    private GUILayer editorGUI;
    private boolean isRunning = false;
    private Scene editorScene;

    public EditorLayer(){
        this.layerName = "Editor Layer";
        editorScene = new Scene();
        editorScene.setSceneName("Editor Scene");
        changeCurrentScene(editorScene);
    }

    public void changeCurrentScene(Scene scene){
        Window.get().setCurrentScene(scene);
        Window.get().getCurrentScene().init();
        Window.get().getCurrentScene().start();
    }

    @Override
    public void start(){
        editorGUI = new GUILayer(Window.get().getGlfwWindow());
        editorGUI.init();
        Window.get().imguiLayer = editorGUI;
    }

    @Override
    public void update(float dt) {
        editorGUI.update(Window.get().getCurrentScene());
    }

    @Override
    public void imgui() {
        Window.get().getCurrentScene().imgui();
    }

    @Override
    public void cleanup() {
        Window.get().getCurrentScene().cleanup();
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public Scene getEditorScene() {
        return editorScene;
    }

    public void setEditorScene(Scene editorScene) {
        this.editorScene = editorScene;
    }
}
