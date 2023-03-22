package engine;

import components.MeshRenderer;
import components.Transform;
import imgui.ImGui;
import renderEngine.Window;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameObject {
    //UUID-GUID
    private long uid = -1;
    private transient static final String numbers = "0123456789";
    private transient static SecureRandom rnd = new SecureRandom();
    private transient boolean removeComponent = false;

    public String name;
    private List<Component> components;
    public transient Transform transform;
    private boolean doSerialization = true;

    public GameObject(String name) {
        this.name = name;
        this.components = new ArrayList<>();

        //UUID-GUID
        String randomGeneratedNumberString = randomString(14);
        long randomGeneratedNumber = Long.parseLong(randomGeneratedNumberString);
        this.uid = randomGeneratedNumber;
    }

    public GameObject() {
    }

    String randomString(int length){
        StringBuilder sb = new StringBuilder(length);
        for(int i = 0; i < length; i++)
            sb.append(numbers.charAt(rnd.nextInt(numbers.length())));
        return sb.toString();
    }

    public <T extends Component> T getComponent(Class<T> componentClass) {
        for (Component c : components) {
            if (componentClass.isAssignableFrom(c.getClass())) {
                try {
                    return componentClass.cast(c);
                } catch (ClassCastException e) {
                    e.printStackTrace();
                    assert false : "Error: Could not find component.";
                }
            }
        }

        return null;
    }

    public void addComponent(Component c) {
        c.generateId();
        this.components.add(c);
        c.gameObject = this;
    }

    public void start() {
        for (int i = 0; i < components.size(); i++) {
            components.get(i).start();
        }
    }

    public void runtimeStart() {
        for (int i = 0; i < components.size(); i++) {
            components.get(i).runtimeStart();
        }
    }

    public void update(float dt) {
        for (int i = 0; i < components.size(); i++) {
            components.get(i).update(dt);
        }
    }

    public void runtimeUpdate(float dt) {
        for (int i = 0; i < components.size(); i++) {
            components.get(i).runtimeUpdate(dt);
        }
    }

    public void imgui() {
        for (Component c : components) {
            if (ImGui.collapsingHeader(c.getComponentName())) {
                c.imgui();
                //REMOVE COMPONENTS
                if (ImGui.button("+")) {
                    ImGui.openPopup("ComponentSettings");
                }
                if (ImGui.beginPopup("ComponentSettings")){
                    if (ImGui.menuItem("Remove Component")){
                        removeComponent(c.getClass());
                    }
                    ImGui.endPopup();
                }
            }
        }
        //REMOVE GAME OBJECT
        boolean removeGameObject = false;
        if (ImGui.button("Remove Game Object")){
            removeGameObject = true;
        }
        if (removeGameObject){
            Window.get().getCurrentScene().removeGameObject(this);
        }
    }

    public void debug() {
        for (Component c : components){
            c.debug();
        }
    }

    public <T extends Component> void removeComponent(Class<T> componentClass) {
        /*
        for (int i = 0; i < components.size(); i++) {
            Component c = components.get(i);
            if (componentClass.isAssignableFrom(c.getClass())) {
                components.remove(i);
                return;
            }
        }
         */
        for (Iterator<Component> iterator = components.iterator(); iterator.hasNext();) {
            Component c = iterator.next();
            if(c.getClass() == componentClass) {
                iterator.remove();
            }
        }
    }

    public void cleanup() {
        for (Component c : components) {
            c.cleanup();
        }
    }

    public void runtimeDestroy() {
        for (Component c : components) {
            c.runtimeDestroy();
        }
    }

    //public static void init(int maxId) {
    //    ID_COUNTER = maxId;
    //}

    public long getUid() {
        return this.uid;
    }

    public void setUid(long uid){
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public List<Component> getAllComponents() {
        return this.components;
    }

    public void setNoSerialize() {
        this.doSerialization = false;
    }

    public boolean doSerialization() {
        return this.doSerialization;
    }
}