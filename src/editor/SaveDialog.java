package editor;

import imgui.ImGui;
import renderEngine.Window;

public class SaveDialog {
    public void imgui(){
        if(Window.get().isCloseRequested()){
            if(ImGui.beginPopupModal("Save Dialog")){
                if(ImGui.button("Save")){
                    Window.get().getCurrentScene().save();
                }
                ImGui.endPopup();
            }
        }
    }
}
