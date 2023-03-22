package renderEngine;

import java.util.HashMap;
import java.util.Map;

import components.*;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryStack;

public class Shader {
    private final int programID;
    private int vertexShaderID, fragmentShaderID;

    private final Map<String, Integer> uniforms;

    public Shader(){
        programID = GL20.glCreateProgram();
        if(programID == 0){
            System.out.println("Could not create shader.");
        }

        uniforms = new HashMap<>();
    }

    public void createUniform(String uniformName){
        int uniformLocation = GL20.glGetUniformLocation(programID, uniformName);
        if(uniformLocation < 0){
            System.out.println("Could not find uniform " + uniformName);
        }
        uniforms.put(uniformName, uniformLocation);
    }

    public void createDirectionalLightUniform(String uniformName){
        createUniform(uniformName + ".colour");
        createUniform(uniformName + ".direction");
        createUniform(uniformName + ".intensity");
    }

    public void createMaterialUniform(String uniformName){
        createUniform(uniformName + ".ambient");
        createUniform(uniformName + ".diffuse");
        createUniform(uniformName + ".specular");
        createUniform(uniformName + ".hasTexture");
        createUniform(uniformName + ".reflectence");
    }

    public void createPointLightUniform(String uniformName){
        createUniform(uniformName + ".colour");
        createUniform(uniformName + ".position");
        createUniform(uniformName + ".intensity");
        createUniform(uniformName + ".constant");
        createUniform(uniformName + ".linear");
        createUniform(uniformName + ".exponent");
    }

    public void createPointLightListUniform(String uniformName, int size){
        for(int i = 0; i < size; i++){
            createPointLightUniform(uniformName + "[" + i + "]");
        }
    }

    public void createSpotLightListUniform(String uniformName, int size){
        for(int i = 0; i < size; i++){
            createSpotlightUniform(uniformName + "[" + i + "]");
        }
    }

    public void createSpotlightUniform(String uniformName){
        createPointLightUniform(uniformName + ".pl");
        createUniform(uniformName + ".conedir");
        createUniform(uniformName + ".cutoff");
    }

    public void setUniform(String uniformName, Matrix4f value){
        try(MemoryStack stack = MemoryStack.stackPush()){
            GL20.glUniformMatrix4fv(uniforms.get(uniformName), false,
                    value.get(stack.mallocFloat(16)));
        }
    }

    public void setUniform(String uniformName, Vector4f value){
        GL20.glUniform4f(uniforms.get(uniformName), value.x, value.y, value.z, value.w);
    }

    public void setUniform(String uniformName, Vector3f value){
        GL20.glUniform3f(uniforms.get(uniformName), value.x, value.y, value.z);
    }

    public void setUniform(String uniformName, boolean value) {
        float res = 0;
        if(value)
            res = 1;
        GL20.glUniform1f(uniforms.get(uniformName), res);
    }

    public void setUniform(String uniformName, int value){
        GL20.glUniform1i(uniforms.get(uniformName), value);
    }

    public void setUniform(String uniformName, float value){
        GL20.glUniform1f(uniforms.get(uniformName), value);
    }

    public void setUniform(String uniformName, Material material){
        setUniform(uniformName + ".ambient", material.getAmbientColour());
        setUniform(uniformName + ".diffuse", material.getDiffuseColour());
        setUniform(uniformName + ".specular", material.getSpecularColour());
        setUniform(uniformName + ".hasTexture", material.hasTexture() ? 1 : 0);
        setUniform(uniformName + ".reflectence", material.getReflectance());
    }

    public void setUniform(String uniformName, DirectionalLight directionalLight){
        setUniform(uniformName + ".colour", directionalLight.getColour());
        setUniform(uniformName + ".direction", directionalLight.gameObject.transform.getRotation());
        setUniform(uniformName + ".intensity", directionalLight.getIntensity());
    }

    public void setUniform(String uniformName, PointLight pointLight){
        setUniform(uniformName + ".colour", pointLight.getColour());
        setUniform(uniformName + ".position", pointLight.gameObject.transform.getPosition());
        setUniform(uniformName + ".intensity", pointLight.getIntensity());
        setUniform(uniformName + ".constant", pointLight.getConstant());
        setUniform(uniformName + ".linear", pointLight.getLinear());
        setUniform(uniformName + ".exponent", pointLight.getExponent());
    }

    public void setUniform(String uniformName, SpotLight spotLight){
        setUniform(uniformName + ".pl", spotLight.getPointLight());
        setUniform(uniformName + ".conedir", spotLight.getConeDirection());
        setUniform(uniformName + ".cutoff", spotLight.getCutoff());
    }

    public void setUniform(String uniformName, PointLight pointLight, int pos){
        setUniform(uniformName + "[" + pos + "]", pointLight);
    }

    public void setUniform(String uniformName, SpotLight spotLight, int pos){
        setUniform(uniformName + "[" + pos + "]", spotLight);
    }

    public void createVertexShader(String shaderCode){
        vertexShaderID = createShader(shaderCode, GL20.GL_VERTEX_SHADER);
    }

    public void createFragmentShader(String shaderCode){
        fragmentShaderID = createShader(shaderCode, GL20.GL_FRAGMENT_SHADER);
    }

    public int createShader(String shaderCode, int shaderType){
        int shaderID = GL20.glCreateShader(shaderType);
        if(shaderID == 0){
            System.out.println("Error creating shader. Type: " + shaderType);
        }

        GL20.glShaderSource(shaderID, shaderCode);
        GL20.glCompileShader(shaderID);

        if(GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == 0){
            System.out.println("Error compiling shader code: Type: " + shaderType + " Info " + GL20.glGetShaderInfoLog(shaderID, 1024));
        }

        GL20.glAttachShader(programID, shaderID);

        return shaderID;
    }

    public void link(){
        GL20.glLinkProgram(programID);
        if(GL20.glGetProgrami(programID, GL20.GL_LINK_STATUS) == 0){
            System.out.println("Error linking shader code " + " Info " + GL20.glGetProgramInfoLog(programID, 1024));
        }

        if(vertexShaderID != 0){
            GL20.glDetachShader(programID, vertexShaderID);
        }

        if(fragmentShaderID != 0){
            GL20.glDetachShader(programID, fragmentShaderID);
        }

        GL20.glValidateProgram(programID);
        if(GL20.glGetProgrami(programID, GL20.GL_VALIDATE_STATUS) == 0){
            System.out.println("Unable to validate shader code: " + GL20.glGetProgramInfoLog(programID, 1024));
        }
    }

    public void bind(){
        GL20.glUseProgram(programID);
    }

    public void unbind(){
        GL20.glUseProgram(0);
    }

    public void cleanup(){
        unbind();
        if(programID != 0){
            GL20.glDeleteProgram(programID);
        }
    }
}