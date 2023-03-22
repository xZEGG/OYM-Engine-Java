package renderEngine;

import components.Camera;
import components.DirectionalLight;
import components.MeshRenderer;
import engine.GameObject;

public interface IRenderer<T> {
    public void init();

    public void render();

    public void bind(MeshRenderer meshRenderer);

    public void unbind();

    public void prepare(T t, Camera camera);

    public void cleanup();
}