package utils;

import org.lwjgl.system.MemoryUtil;
import renderEngine.Texture;

import java.io.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.*;

public class Utils {
    private static Map<String, Texture> textures = new HashMap<>();

    public static String loadResource(String fileName) {
        String result = null;
        try (InputStream in = Utils.class.getResourceAsStream(fileName);
             Scanner scanner = new Scanner(in, java.nio.charset.StandardCharsets.UTF_8.name())) {
            result = scanner.useDelimiter("\\A").next();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String loadScript(String scriptPath) {
        File myObj = new File(scriptPath);
        String result = null;
        try {
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                result = myReader.nextLine();
                System.out.println(result);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return result;
    }

    public static Texture getTexture(String resourceName) {
        File file = new File(resourceName);
        if (textures.containsKey(file.getAbsolutePath())) {
            return textures.get(file.getAbsolutePath());
        } else {
            Texture texture = new Texture();
            textures.put(file.getAbsolutePath(), texture);
            return texture;
        }
    }

    public static List<String> readAllLines(String fileName) throws Exception {
        List<String> list = new ArrayList<>();
        InputStream stream = new FileInputStream(fileName);
        try (BufferedReader br = new BufferedReader(new InputStreamReader(stream))) {
            String line;
            while ((line = br.readLine()) != null) {
                list.add(line);
            }
        }
        return list;
    }

    public static FloatBuffer storeDataInFloatBuffer(float[] data){
        FloatBuffer buffer = MemoryUtil.memAllocFloat(data.length);
        buffer.put(data).flip();
        return buffer;
    }

    public static IntBuffer storeDataInIntBuffer(int[] data){
        IntBuffer buffer = MemoryUtil.memAllocInt(data.length);
        buffer.put(data).flip();
        return buffer;
    }
}
