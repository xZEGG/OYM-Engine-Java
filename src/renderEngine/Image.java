package renderEngine;

import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.stb.STBImage.stbi_load;

public class Image {
    private ByteBuffer image;
    private int width, height;

    public Image(int width, int height, ByteBuffer image) {
        this.image = image;
        this.height = height;
        this.width = width;
    }

    public static Image loadImage(String path) {
        ByteBuffer image;
        int width, heigh;
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer comp = stack.mallocInt(1);
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);

            image = stbi_load(path, w, h, comp, 4);
            if (image == null) {
                System.out.println("Could not load image resources.");
            }
            width = w.get();
            heigh = h.get();
        }
        return new Image(width, heigh, image);
    }

    public ByteBuffer getImage() {
        return image;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
