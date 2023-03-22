package engine;

import renderEngine.Window;

public class Application {
    private Window window;
    private int frames;
    private static long time;

    public void start(){
        window = Window.get();

        window.initWindow();
        //update();
        window.run();
        window.destroy();

        time = System.currentTimeMillis();
    }

    /*
    private void update() {
        float beginTime = (float) GLFW.glfwGetTime();
        float endTime;
        float dt = -1.0f;

        while (!glfwWindowShouldClose(window.getGlfwWindow())) {
            glfwPollEvents();

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

            GL11.glViewport(0, 0, 3840, 2160);
            window.getFramebuffer().bind();
            GL11.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

            frames++;

            if (dt >= 0) {
                if(!window.getEditorLayer().isRunning()){
                    Window.get().getCurrentScene().update(dt);
                    Window.get().getCurrentScene().render();
                } else if (window.getEditorLayer().isRunning()){
                    Window.get().getCurrentScene().runtimeUpdate(dt);
                    Window.get().getCurrentScene().render();
                }
            }

            window.getFramebuffer().unbind();

            //imguiLayer.update(dt, currentScene);
            window.getEditorLayer().update(dt);
            glfwSwapBuffers(window.getGlfwWindow());

            if (System.currentTimeMillis() > time + 1000) {
                System.out.println(frames);
                Debug.logFPS(frames);
                time = System.currentTimeMillis();
                frames = 0;
            }

            endTime = (float) GLFW.glfwGetTime();
            dt = endTime - beginTime;
            beginTime = endTime;
        }
    }
    */
}
