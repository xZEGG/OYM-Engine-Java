package editor;

import components.DirectionalLight;
import engine.GameObject;
import imgui.ImGui;
import imgui.extension.imguifiledialog.ImGuiFileDialog;
import imgui.extension.imguifiledialog.callback.ImGuiFileDialogPaneFun;
import imgui.extension.imguifiledialog.flag.ImGuiFileDialogFlags;
import org.apache.commons.io.FileUtils;
import org.joml.Vector3f;
import renderEngine.Window;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class MenuBar {

    private static Map<String, String> selection = null;
    private static long userData = 0;
    private static ImGuiFileDialogPaneFun callback = new ImGuiFileDialogPaneFun() {
        @Override
        public void paneFun(String filter, long userDatas, boolean canContinue) {
            ImGui.text("Filter: " + filter);
        }
    };

    public void imgui() {
        if(ImGui.beginMenuBar()) {
            if(ImGui.beginMenu("File")) {
                // NEW FILE
                if (ImGui.button("New")) {
                    ImGuiFileDialog.openDialog("create-folder-key", "Create New Project", null, "", "", 1, 7, ImGuiFileDialogFlags.DisableCreateDirectoryButton);
                }

                // OPEN FILE
                if (ImGui.button("Open")) {
                    ImGuiFileDialog.openDialog("browse-folder-key", "Choose Project Folder", null, "", "", 1, 7, ImGuiFileDialogFlags.DisableCreateDirectoryButton);
                }

                if (ImGuiFileDialog.display("create-folder-key", ImGuiFileDialogFlags.None, 200, 400, 800, 600)) {
                    if (ImGuiFileDialog.isOk()) {
                        createFolder();
                    }
                    ImGuiFileDialog.close();
                }

                if (ImGuiFileDialog.display("browse-folder-key", ImGuiFileDialogFlags.None, 200, 400, 800, 600)) {
                    if (ImGuiFileDialog.isOk()) {
                        openFolder();
                    }

                    ImGuiFileDialog.close();
                }

                //SAVE FILE
                if(ImGui.menuItem("Save")) {
                    if(Window.get().getCurrentScene() != null){
                        Window.get().getCurrentScene().save();
                    }
                }

                ImGui.endMenu();
            }
        }
        ImGui.endMenuBar();
    }

    public void createFolder(){
        String fileName = ImGuiFileDialog.getFilePathName();
        userData = ImGuiFileDialog.getUserDatas();
        String source = "structure";
        File srcDir = new File(source);

        String destination = fileName;
        File destDir = new File(destination);

        try {
            FileUtils.copyDirectory(srcDir, destDir);
            File tmpFile = new File(destDir + "/Scenes/Main.tmp");
            tmpFile.createNewFile();
            GameObject directionalLightObject = Window.get().getCurrentScene().createGameObject("Directional Light");
            directionalLightObject.transform.rotation = new Vector3f(0, 0, 90);
            directionalLightObject.addComponent(new DirectionalLight(new Vector3f(1, 1, 1), 1));
            Window.get().getCurrentScene().cleanup();
            //Window.get().getEditorLayer().setEditorScene(new Scene());
            Window.get().getCurrentScene().addGameObjectToScene(directionalLightObject);
            Window.get().getCurrentScene().load(destDir + "/Scenes/Main.oym");
            Window.get().setCurrentScenePath(destDir + "/Scenes/Main.oym");
            //Window.get().getCurrentScene().setgetCurrentScene()TMPFile(destDir + "/Scenes/Main.tmp");
            Window.get().setAssetsDirectory(destDir + "/Assets/");
            Window.get().getCurrentScene().save();
            System.out.println("GameObjects: " + Window.get().getCurrentScene().gameObjects);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openFolder(){
        String selection = ImGuiFileDialog.getFilePathName();

        File selectedPath = new File(selection + "/Scenes/Main.oym");
        File selectedPathTMPFile = new File(selection + "/Scenes/Main.tmp");
        System.out.println(selectedPath);

        if(selectedPath != null) {
            System.out.println("Selected file: " + selectedPath.getAbsolutePath());
            if(Window.get().getCurrentScene().currentProject != selectedPath.getAbsolutePath()) {
                if(Window.get().getCurrentScene() != null){
                    Window.get().getCurrentScene().cleanup();
                }
                Scene newScene = new Scene();
                Window.get().setCurrentScene(newScene);
                //Scene newScene = new Scene();
                //Window.get().getEditorLayer().setEditorScene(newScene);
                Window.get().getCurrentScene().load(selectedPath.getAbsolutePath());
                Window.get().getCurrentScene().init();
                Window.get().getCurrentScene().start();
                Window.get().setCurrentLayerName(selectedPath.getAbsolutePath());
                Window.get().setCurrentScenePath(selectedPath.getAbsolutePath());
                //Window.get().getCurrentScene().setgetCurrentScene()TMPFile(selectedPathTMPFile.getAbsolutePath());
                Window.get().setAssetsDirectory(selection + "/Assets/");
                System.out.println("GameObjects: " + Window.get().getCurrentScene().gameObjects);
            }
        }
    }
}
