package editor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import components.*;
import engine.*;
import renderEngine.Renderer;
import renderEngine.Shader;
import renderEngine.Window;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Scene {
    protected Renderer renderer;
    protected Shader shader;
    protected Camera primaryCamera;
    private GameObject editorTools = createGameObject("Editor Tools");
    private MousePicking selectDetector;
    private String sceneName;
    private boolean isRunning = false;

    protected List<GameObject> gameObjects = new ArrayList<GameObject>();
    protected GameObject activeGameObject = null;
    protected boolean sceneLoaded = false;
    protected String currentProject = "";
    protected String currentSceneTMPFile = "";

    public Scene() {
    }

    public void init() {
        renderer = new Renderer();
        renderer.init();
        if(primaryCamera == null){
            primaryCamera = new Camera();
            editorTools.addComponent(primaryCamera);
            editorTools.setNoSerialize();
        }
        //addGameObjectToScene(editorTools);
    }

    public void start() {
        for (GameObject go : gameObjects) {
            if(go.transform.isActive) {
                go.start();
                renderer.add(go);
            } else {
                renderer.removeGameObject(go);
            }
        }
        isRunning = true;
    }

    public void runtimeStart(){
        for (GameObject go : gameObjects) {
            go.runtimeStart();
            renderer.add(go);
        }
        //isRunning = true;
    }

    public void update(float dt) {
        primaryCamera.update();

        for (GameObject go : gameObjects) {
            if(go.transform.isActive){
                renderer.add(go);
                go.update(dt);
            } else {
                renderer.removeGameObject(go);
            }
        }
    }

    public void runtimeUpdate(float dt){
        primaryCamera.update();

        for (GameObject go : gameObjects) {
            if(go.transform.isActive){
                renderer.add(go);
                go.runtimeUpdate(dt);
            } else {
                renderer.removeGameObject(go);
            }
        }
    }

    public void runtimeDestroy(){
        for (GameObject go : gameObjects) {
            go.runtimeDestroy();
            if (go.getComponent(MeshRenderer.class) != null) {
                go.getComponent(MeshRenderer.class).cleanup();
            }
        }
        gameObjects.clear();
        renderer.cleanup();
    }

    public void render() {
        renderer.render();
    }

    public void cleanup() {
        for (GameObject go : gameObjects) {
            go.cleanup();
            if (go.getComponent(MeshRenderer.class) != null) {
                go.getComponent(MeshRenderer.class).cleanup();
            }
        }
        gameObjects.clear();
        renderer.cleanup();
    }

    public void removeGameObject(GameObject gameObject) {
        if(gameObject.getComponent(MeshRenderer.class) != null){
            gameObject.getComponent(MeshRenderer.class).cleanup();
        }
        gameObject.cleanup();
        renderer.removeGameObject(gameObject);
        gameObjects.remove(gameObject);
    }

    public void imgui() {
    }

    //GAMEOBJECT
    public void addGameObjectToScene(GameObject go) {
        if (!isRunning) {
            gameObjects.add(go);
        } else {
            gameObjects.add(go);
            go.start();
            renderer.add(go);
        }
    }

    public void addPointLightToScene(PointLight pointLight) { renderer.addPointLight(pointLight); }

    public void addSpotLightToScene(SpotLight spotLight) {
        renderer.addSpotLight(spotLight);
    }

    public GameObject createGameObject(String name) {
        GameObject go = new GameObject(name);
        go.addComponent(new Transform());
        go.transform = go.getComponent(Transform.class);
        return go;
    }

    public void save() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentDeserializer())
                .registerTypeAdapter(GameObject.class, new GameObjectDeserializer())
                .create();

        try {
            FileWriter writer = new FileWriter(currentProject);
            List<GameObject> objsToSerialize = new ArrayList<>();
            for (GameObject obj : this.gameObjects) {
                if (obj.doSerialization()) {
                    objsToSerialize.add(obj);
                }
            }
            writer.write(gson.toJson(objsToSerialize));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load(String filePath) {
        currentProject = filePath;
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentDeserializer())
                .registerTypeAdapter(GameObject.class, new GameObjectDeserializer())
                .create();

        String inFile = "";
        try {
            inFile = new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!inFile.equals("")) {
            long maxGoId = -1;
            long maxCompId = -1;
            GameObject[] objs = gson.fromJson(inFile, GameObject[].class);
            for (int i = 0; i < objs.length; i++) {
                addGameObjectToScene(objs[i]);

                for (Component c : objs[i].getAllComponents()) {
                    if (c.getUid() > maxCompId) {
                        maxCompId = c.getUid();
                    }
                }
                if (objs[i].getUid() > maxGoId) {
                    maxGoId = objs[i].getUid();
                }
            }

            maxGoId++;
            maxCompId++;
            this.sceneLoaded = true;
        }
    }

    /*
    public void saveToTMPFile(){
        System.out.println("Save: " + currentSceneTMPFile);
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentDeserializer())
                .registerTypeAdapter(GameObject.class, new GameObjectDeserializer())
                .create();

        try {
            FileWriter writer = new FileWriter(currentSceneTMPFile);
            List<GameObject> objsToSerialize = new ArrayList<>();
            for (GameObject obj : Window.get().getEditorLayer().getEditorScene().getGameObjects()) {
                if (obj.doSerialization()) {
                    objsToSerialize.add(obj);
                }
            }
            writer.write(gson.toJson(objsToSerialize));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

     */

    public void loadTMPFile(String tmpFile) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentDeserializer())
                .registerTypeAdapter(GameObject.class, new GameObjectDeserializer())
                .create();

        String inFile = "";
        System.out.println("Load: " + tmpFile);
        try {
            inFile = new String(Files.readAllBytes(Paths.get(tmpFile)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!inFile.equals("")) {
            long maxGoId = -1;
            long maxCompId = -1;
            GameObject[] objs = gson.fromJson(inFile, GameObject[].class);
            for (int i = 0; i < objs.length; i++) {
                Window.get().getCurrentScene().addGameObjectToScene(objs[i]);

                for (Component c : objs[i].getAllComponents()) {
                    if (c.getUid() > maxCompId) {
                        maxCompId = c.getUid();
                    }
                }
                if (objs[i].getUid() > maxGoId) {
                    maxGoId = objs[i].getUid();
                }
            }

            maxGoId++;
            maxCompId++;
            Window.get().getCurrentScene().sceneLoaded = true;
        }
    }

    public Shader getShader() {
        return shader;
    }

    public Renderer getRenderer() {
        return renderer;
    }

    public List<GameObject> getGameObjects() {
        return this.gameObjects;
    }

    public GameObject getGameObject(long gameObjectID) {
        Optional<GameObject> result = this.gameObjects.stream()
                .filter(gameObject -> gameObject.getUid() == gameObjectID)
                .findFirst();
        return result.orElse(null);
    }

    public Camera getCamera() {
        return primaryCamera;
    }

    public String getCurrentProject() {
        return currentProject;
    }

    public void setPrimaryCamera(Camera primaryCamera) {
        this.primaryCamera = primaryCamera;
    }

    public String getSceneName() {
        return sceneName;
    }

    public void setSceneName(String sceneName) {
        this.sceneName = sceneName;
    }

    public String getCurrentSceneTMPFile() {
        return currentSceneTMPFile;
    }

    public void setCurrentSceneTMPFile(String currentSceneTMPFile) {
        this.currentSceneTMPFile = currentSceneTMPFile;
    }
}
