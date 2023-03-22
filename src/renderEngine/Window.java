package renderEngine;

import editor.*;
import engine.KeyInput;
import engine.Layer;
import engine.MouseInput;
import org.joml.Matrix4f;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    private static Window window = null;

    private Scene currentScene = null;
    private Layer currentLayer = null;
    public GUILayer imguiLayer;
    private EditorLayer editorLayer;
    private static Framebuffer framebuffer;
    private PickingTexture pickingTexture;
    private String currentScenePath = null;

    public final float FOV = (float) Math.toRadians(60);
    public final float Z_NEAR = 0.01f;
    public final float Z_FAR = 1000f;

    private String title;
    private final Image icon = Image.loadImage("resources/engine/icons/oym_icon.png");

    private int width, height;
    private long glfwWindow;
    private boolean isResized;
    private boolean isCloseRequested = false;
    private boolean mouseState = false;

    private GLFWWindowSizeCallback sizeCallback;
    private boolean vSync;

    private final Matrix4f projectionMatrix;

    private MouseInput mouseInput;

    public Window(String title, int width, int height, boolean vSync) {
        this.title = title;
        this.width = width;
        this.height = height;
        this.vSync = vSync;
        projectionMatrix = new Matrix4f();
        //mouseInput = new MouseInput();
    }

    /*
    public void init() {
        initWindow();
        //mouseInput.init();
        run();
        //saveDialog();
        destroy();
    }
    */

    public void changeScene(Scene scene) {
        currentScene = scene;
        //currentScene.load("Project.oym");
        currentScene.init();
        currentScene.start();
    }

    public void changeLayer(Layer layer){
        currentLayer = layer;
        currentLayer.start();
    }

    public void initWindow() {
        GLFWErrorCallback.createPrint(System.err).set();

        if(!GLFW.glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW.");

        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW_VISIBLE, GL_FALSE);
        GLFW.glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);
        GLFW.glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        GLFW.glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        GLFW.glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        GLFW.glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
        GLFW.glfwWindowHint(GLFW.GLFW_STENCIL_BITS, 16);
        GLFW.glfwWindowHint(GLFW.GLFW_SAMPLES, 16);

        boolean maximized = false;
        if(width == 0 || height == 0) {
            width = 100;
            height = 100;
            GLFW.glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);
            maximized = true;
        }

        glfwWindow = GLFW.glfwCreateWindow(width, height, title, MemoryUtil.NULL, MemoryUtil.NULL);
        if(glfwWindow == NULL)
            throw new RuntimeException("Failed to create GLFW window.");

        GLFW.glfwSetFramebufferSizeCallback(glfwWindow, (window, width, height) -> {
            this.width = width;
            this.height = height;
            this.isResized = true;
        });

        GLFW.glfwSetKeyCallback(glfwWindow, (window, key, scancode, action, mods) -> {
            if(key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                GLFW.glfwSetWindowShouldClose(glfwWindow, true);
        });
//
        GLFW.glfwSetKeyCallback(glfwWindow, KeyInput::keyCallback);

        if(maximized)
            GLFW.glfwMaximizeWindow(glfwWindow);
        else {
            GLFWVidMode vidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
            GLFW.glfwSetWindowPos(glfwWindow, (vidMode.width() - width) / 2,
                    (vidMode.height() - height) / 2);
        }

        glfwMakeContextCurrent(glfwWindow);

        GL.createCapabilities();

        glEnable(GL_DEPTH_TEST);
        glEnable(GL_STENCIL_TEST);
        glEnable(GL11.GL_BLEND);
        glBlendFunc(GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_ALPHA);

        createCallbacks();

        glfwShowWindow(glfwWindow);

        GLFWImage image = GLFWImage.malloc(); GLFWImage.Buffer imagebf = GLFWImage.malloc(1);
        image.set(icon.getWidth(), icon.getHeight(), icon.getImage());
        imagebf.put(0, image);
        glfwSetWindowIcon(glfwWindow, imagebf);

        if(isvSync())
            glfwSwapInterval(1);

        //imguiLayer = new GUILayer(glfwWindow);
        //imguiLayer.init();

        this.framebuffer = new Framebuffer(3840, 2160);
        //this.pickingTexture = new PickingTexture(3840, 2160);
        GL11.glViewport(0, 0, 3840, 2160);

        //
        editorLayer = new EditorLayer();
        changeLayer(editorLayer);
        currentLayer.start();
        currentScene.cleanup();
    }

    public void run() {
        float beginTime = (float) GLFW.glfwGetTime();
        float endTime;
        float dt = -1.0f;

        while (!glfwWindowShouldClose(glfwWindow)) {
            glfwPollEvents();

            /*
            //RENDER PASS 1 - PICKING TEXTURE
            glDisable(GL_BLEND);
            pickingTexture.enableWriting();

            glViewport(0, 0, 3840, 2160);
            glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

            currentScene.getRenderer().bindShader("/shaders/picking_vertex.glsl", "/shaders/picking_fragment.glsl");
            currentScene.render();

            if (MouseInput.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)) {
                int x = (int)MouseInput.getScreenX();
                int y = (int)MouseInput.getScreenY();
                System.out.println(pickingTexture.readPixel(x, y));
            }

            pickingTexture.disableWriting();
            glEnable(GL_BLEND);

            this.framebuffer.bind();
            glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT);
             */

            GL11.glViewport(0, 0, 3840, 2160);
            this.framebuffer.bind();
            GL11.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

            if (dt >= 0) {
                getCurrentScene().update(dt);
                getCurrentScene().render();
            }

            this.framebuffer.unbind();

            if (isResized) {
                isResized = false;
            }

            //imguiLayer.update(dt, currentScene);
            editorLayer.update(dt);
            glfwSwapBuffers(glfwWindow);

            endTime = (float) GLFW.glfwGetTime();
            dt = endTime - beginTime;
            beginTime = endTime;
        }
    }

    public boolean isCloseRequested(){
        if(glfwWindowShouldClose(glfwWindow)){
            isCloseRequested = true;
        }
        return isCloseRequested;
    }

    public void destroy() {
        sizeCallback.free();
        currentScene.cleanup();
        currentLayer.cleanup();
        Callbacks.glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);
        glfwTerminate();
        GL.setCapabilities(null);
    }

    private void createCallbacks() {
        sizeCallback = new GLFWWindowSizeCallback() {
            public void invoke(long window, int w, int h) {
                width = w;
                height = h;
                isResized = true;
            }
        };

        GLFW.glfwSetCursorPosCallback(glfwWindow, MouseInput::mousePosCallback);
        GLFW.glfwSetMouseButtonCallback(glfwWindow, MouseInput::mouseButtonCallback);
        GLFW.glfwSetScrollCallback(glfwWindow, MouseInput::mouseScrollCallback);
        GLFW.glfwSetKeyCallback(glfwWindow, KeyInput::keyCallback);
        GLFW.glfwSetWindowSizeCallback(glfwWindow, (w, newWidth, newHeight) -> {
            setWidth(newWidth);
            setHeight(newHeight);
        });
    }

    public void mouseState(boolean lock) {
        GLFW.glfwSetInputMode(glfwWindow, GLFW.GLFW_CURSOR, lock ? GLFW.GLFW_CURSOR_DISABLED : GLFW.GLFW_CURSOR_NORMAL);
        setMouseState(lock ? true : false);
    }

    public void setMouseState(boolean mouseState) {
        this.mouseState = mouseState;
    }

    public boolean getMouseState() {
        return mouseState;
    }

    public Matrix4f updateProjectionMatrix(){
        float aspectRatio = (float) width / height;
        return projectionMatrix.setPerspective(FOV, aspectRatio, Z_NEAR, Z_FAR);
    }

    public Matrix4f updateProjectionMatrix(Matrix4f matrix, int width, int height){
        float aspectRatio = (float) width / height;
        return matrix.setPerspective(FOV, aspectRatio, Z_NEAR, Z_FAR);
    }

    //GETTERS
    //public Scene getCurrentScene() {
        //return currentScene;
    //}

    public void setAssetsDirectory(String assetsDirectory) {
        this.imguiLayer.getAssetBrowser().setAssetsDirectory(assetsDirectory);
    }

    public static Window get() {
        if (Window.window == null) {
            Window.window = new Window("OYM Engine", 1280, 720, true);
        }

        return Window.window;
    }

    public long getGlfwWindow() {
        return glfwWindow;
    }

    public Hierarchy getHierarchyWindow() {
        return this.imguiLayer.getHierarchyWindow();
    }

    public GameViewWindow getGameViewWindow() {
        return this.imguiLayer.getGameViewWindow();
    }

    public static Framebuffer getFramebuffer() {
        return framebuffer;
    }

    public MouseInput getMouseInput() {
        return mouseInput;
    }

    public Layer getCurrentLayer() {
        return currentLayer;
    }

    public static int getWidth() {
        return get().width;
    }

    public static int getHeight() {
        return get().height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setCurrentLayerName(String currentLayerName) {
        window.title = "OYM Engine" + " | " + currentLayer.getLayerName();
    }

    public Scene getCurrentScene() {
        return currentScene;
    }

    public void setCurrentScene(Scene currentScene) {
        this.currentScene = currentScene;
    }

    public String getCurrentScenePath() {
        return currentScenePath;
    }

    public void setCurrentScenePath(String currentScenePath) {
        this.currentScenePath = currentScenePath;
    }

    public EditorLayer getEditorLayer() {
        return editorLayer;
    }

    public Matrix4f getProjectionMatrix(){
        return projectionMatrix;
    }

    public static float getTargetAspectRatio() {
        return 16.0f / 9.0f;
    }

    public float getFOV() {
        return FOV;
    }

    public float getZ_NEAR() {
        return Z_NEAR;
    }

    public float getZ_FAR() {
        return Z_FAR;
    }

    public boolean isvSync() {
        return vSync;
    }
}