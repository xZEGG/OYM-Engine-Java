package editor;

import engine.Prefabs;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiMouseButton;
import renderEngine.Window;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class AssetBrowser {
    private String id;
    private String assetsDirectory = "resources/";
    private String currentDirectory = assetsDirectory;
    File dir;
    File[] directoryListing;
    private float padding = 16.0f;
    private float thumbnailSize = 85;

    //Extensions
    List<String> imageExtensions = Arrays.asList("jpg", "JPG", "png", "PNG");
    List<String> modelExtensions = Arrays.asList("obj", "OBJ");

    public void imgui() {
        dir = new File(currentDirectory);
        directoryListing = dir.listFiles();

        ImGui.begin("Assets");

        float cellSize = thumbnailSize + padding;

        float panelWidth = ImGui.getContentRegionAvail().x;
        int columnCount = (int) (panelWidth / cellSize);
        if (columnCount < 1) {
            columnCount = 1;
        }

        ImGui.columns(columnCount, "0", false);

        if (currentDirectory != assetsDirectory) {
            if (ImGui.button("<-")) {
                currentDirectory = assetsDirectory;
            }
        }

        if (ImGui.isWindowHovered() && !ImGui.isItemHovered() && ImGui.isMouseDown(1) && !Window.get().getGameViewWindow().getWantCaptureMouse() && Window.get().getMouseState() == false) {
            ImGui.openPopup("Create");
        }

        if (ImGui.beginPopup("Create")) {
            if (ImGui.beginMenu("Create")) {
                if (ImGui.menuItem("Scene File")) {
                    System.out.println("Bruh");
                }
                ImGui.endMenu();
            }
            ImGui.endPopup();
        }

        if (ImGui.isItemHovered() && ImGui.isMouseClicked(1)) {
            ImGui.openPopup("FileSettings");
        }
        if (ImGui.beginPopup("FileSettings")) {
            if (ImGui.menuItem("Rename")) {

            }
            ImGui.endPopup();
        }

        int index = 0;
        if(directoryListing != null){
            for (File child : directoryListing) {
                ImGui.pushID(index);
                if (imageExtensions.contains(getFileExtension(child))) {
                    id = child.getName();
                    //icon = AssetPool.getImage(child.getPath());
                } else if (modelExtensions.contains(getFileExtension(child))) {
                    id = child.getName();
                    //icon = AssetPool.getImage("resources/engine/icons/file_icon.png");
                } else if (child.isDirectory()) {
                    id = child.getName();
                    //icon = AssetPool.getImage("resources/engine/icons/folder_icon.png");
                } else if (child.isFile()) {
                    id = child.getName();
                    //icon = AssetPool.getImage("resources/engine/icons/file_icon.png");
                }
                ImGui.popID();
                index++;
                ImGui.pushStyleColor(ImGuiCol.Button, 0, 0, 0, 0);
                ImGui.button(id, 85, 85);
                if (ImGui.beginDragDropSource()) {
                    if (child.isFile()) {
                        ImGui.setDragDropPayload("CONTENT_BROWSER_ITEM", child.getPath(), ImGuiCond.Once);
                        ImGui.text(child.getPath());
                    }
                    ImGui.endDragDropSource();
                }
                ImGui.popStyleColor();
                if (ImGui.isItemHovered() && ImGui.isMouseDoubleClicked(ImGuiMouseButton.Left)) {
                    if (child.isDirectory()) {
                        currentDirectory = currentDirectory + child.getName() + "/";
                    }
                }
                ImGui.nextColumn();
            }
        }
        ImGui.columns(1);

        ImGui.end();
    }

    String getFileExtension(File file) {
        if (file == null) {
            return "";
        }
        String name = file.getName();
        int i = name.lastIndexOf('.');
        String ext = i > 0 ? name.substring(i + 1) : "";
        return ext;
    }

    public void setAssetsDirectory(String directory) {
        this.assetsDirectory = directory;
        this.currentDirectory = directory;
    }
}
