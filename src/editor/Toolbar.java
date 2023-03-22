package editor;

import imgui.ImGui;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImBoolean;
import renderEngine.Window;

public class Toolbar {
    /*
    private boolean isPlaying = false;

    public void imgui(){
        //ImGui.pushStyleVar(ImGuiStyleVar.ItemInnerSpacing, 2);

        ImGui.begin("##toolbar", new ImBoolean(false), ImGuiWindowFlags.NoDecoration | ImGuiWindowFlags.NoScrollbar | ImGuiWindowFlags.NoScrollWithMouse);
        ImGui.beginMenuBar();
        if(ImGui.menuItem("Play", "", isPlaying, !isPlaying)){
            Window.get().getEditorLayer().setRuntimeScene(Scene.copyScene(Window.get().currentLayer.getCurrentScene()));
            Window.get().currentLayer.setCurrentScene(Window.get().getEditorLayer().getRuntimeScene());
            System.out.println("oldu");
            isPlaying = true;
        }

        if(ImGui.menuItem("Stop", "", !isPlaying, isPlaying)){
            Window.get().getEditorLayer().setCurrentScene(Window.get().getEditorLayer().getEditorScene());
            isPlaying = false;
        }

        //ImGui.popStyleVar(1);
        ImGui.endMenuBar();
        ImGui.end();
    }
     */
}
