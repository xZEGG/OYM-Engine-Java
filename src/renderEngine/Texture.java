package renderEngine;

import static org.lwjgl.opengl.GL11.*;

public class Texture {
    private int id;
    private String filepath;
    private int width, height;

    public Texture(int id){
        this.id = id;
    }

    public Texture(int width, int height) {
        this.filepath = "Generated";

        // Generate texture on GPU
        id = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, id);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height,
                0, GL_RGBA, GL_UNSIGNED_BYTE, 0);
    }

    public Texture() {
        width = -1;
        height = -1;
        this.filepath = "Generated";

        // Generate texture on GPU
        id = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, id);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height,
                0, GL_RGBA, GL_UNSIGNED_BYTE, 0);
    }

    public int getId(){
        return id;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getFilepath() {
        return this.filepath;
    }
}
