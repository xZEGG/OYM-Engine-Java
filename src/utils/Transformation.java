package utils;

import engine.GameObject;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import components.Camera;
import renderEngine.Window;

public class Transformation {
    public static Matrix4f createTransformationMatrix(GameObject go){
        Matrix4f matrix = new Matrix4f();
        matrix.identity().translate(go.transform.getPosition()).
                rotateX((float) Math.toRadians(go.transform.getRotation().x)).
                rotateY((float) Math.toRadians(go.transform.getRotation().y)).
                rotateZ((float) Math.toRadians(go.transform.getRotation().z)).
                scale(go.transform.getScale());
        return matrix;
    }

    public static float[] getViewMatrixArray(Camera camera){
        Vector3f pos = camera.getPosition();
        Vector3f rot = camera.getRotation();
        Matrix4f matrix = new Matrix4f();
        matrix.identity();
        matrix.rotate((float) Math.toRadians(rot.x), new Vector3f(1, 0, 0))
                .rotate((float) Math.toRadians(rot.y), new Vector3f(0, 1, 0))
                .rotate((float) Math.toRadians(rot.z), new Vector3f(0, 0, 1));
        matrix.translate(-pos.x, -pos.y, -pos.z);
        float[] v = {matrix.m00(), matrix.m10(), matrix.m20(), matrix.m30(),
                matrix.m01(), matrix.m11(), matrix.m21(), matrix.m31(),
                matrix.m02(), matrix.m12(), matrix.m22(), matrix.m32(),
                matrix.m03(), matrix.m13(), matrix.m23(), matrix.m33()};

        return v;
    }

    public static float[] getInverseViewMatrixArray(Camera camera){
        Vector3f pos = camera.getPosition();
        Vector3f rot = camera.getRotation();
        Matrix4f matrix = new Matrix4f();
        matrix.identity();
        matrix.rotate((float) Math.toRadians(rot.x), new Vector3f(1, 0, 0))
                .rotate((float) Math.toRadians(rot.y), new Vector3f(0, 1, 0))
                .rotate((float) Math.toRadians(rot.z), new Vector3f(0, 0, 1));
        matrix.translate(-pos.x, -pos.y, -pos.z);
        matrix.invert();
        float[] v = {matrix.m00(), matrix.m10(), matrix.m20(), matrix.m30(),
                matrix.m01(), matrix.m11(), matrix.m21(), matrix.m31(),
                matrix.m02(), matrix.m12(), matrix.m22(), matrix.m32(),
                matrix.m03(), matrix.m13(), matrix.m23(), matrix.m33()};

        return v;
    }

    /*public static Matrix4f getViewMatrix(Camera camera){
        Vector3f pos = camera.getPosition();
        Vector3f rot = camera.getRotation();
        Matrix4f matrix = new Matrix4f();
        matrix.identity();
        matrix.rotate((float) Math.toRadians(rot.x), new Vector3f(1, 0, 0))
                .rotate((float) Math.toRadians(rot.y), new Vector3f(0, 1, 0))
                .rotate((float) Math.toRadians(rot.z), new Vector3f(0, 0, 1));
        matrix.translate(-pos.x, -pos.y, -pos.z);

        return matrix;
    }*/

    public static Matrix4f getInverseView(Camera camera){
        Vector3f pos = camera.getPosition();
        Vector3f rot = camera.getRotation();
        Matrix4f matrix = new Matrix4f();
        matrix.identity();
        matrix.rotate((float) Math.toRadians(rot.x), new Vector3f(1, 0, 0))
                .rotate((float) Math.toRadians(rot.y), new Vector3f(0, 1, 0))
                .rotate((float) Math.toRadians(rot.z), new Vector3f(0, 0, 1));
        matrix.translate(-pos.x, -pos.y, -pos.z);
        matrix.invert();

        return matrix;
    }

    public static Matrix4f getViewMatrix(Camera camera) {
        Matrix4f viewMatrix =  new Matrix4f();
        Vector3f cameraPos = camera.getPosition();
        Vector3f rotation = camera.getRotation();

        viewMatrix.identity();
        // First do the rotation so camera rotates over its position
        viewMatrix.rotate((float)Math.toRadians(rotation.x), new Vector3f(1, 0, 0))
                .rotate((float)Math.toRadians(rotation.y), new Vector3f(0, 1, 0));
        // Then do the translation
        viewMatrix.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);
        return viewMatrix;
    }

    //DISTANCE BETWEEN TWO GAMEOBJECTS
    public static float distance(GameObject obj1, GameObject obj2){
        double result = Math.sqrt(Math.pow((obj1.transform.position.x - obj2.transform.position.x), 2)
                + Math.pow((obj1.transform.position.y - obj2.transform.position.y), 2)
                + Math.pow((obj1.transform.position.z - obj2.transform.position.z), 2));
        float distance = (float)result;
        return(distance);
    }

    public static Matrix4f getModelViewMatrix(GameObject gameObject, Matrix4f viewMatrix) {
        Matrix4f modelViewMatrix = new Matrix4f();
        Vector3f rotation = gameObject.transform.getRotation();
        modelViewMatrix.identity().translate(gameObject.transform.getPosition()).
                rotateX((float)Math.toRadians(-rotation.x)).
                rotateY((float)Math.toRadians(-rotation.y)).
                rotateZ((float)Math.toRadians(-rotation.z)).
                scale(gameObject.transform.getScale());
        Matrix4f viewCurr = new Matrix4f(viewMatrix);
        return viewCurr.mul(modelViewMatrix);
    }

    ////////////////////////////////////////////////////

    public static Matrix4f getCameraProjection(Window window){
        float aspect = 1920 / 1080;
        float[] pers = perspective(window.getFOV(), aspect, window.Z_NEAR, window.Z_FAR);
        Matrix4f mat = new Matrix4f();
        mat.set(pers);
        return mat;
    }

    public static float[] getProjectionMatrix(float fov, float width, float height, float zNear, float zFar) {
        Matrix4f projectionMatrix = new Matrix4f();
        float aspectRatio = width / height;
        projectionMatrix.identity();
        projectionMatrix.perspective(fov, aspectRatio, zNear, zFar);
        float[] v = {projectionMatrix.m00(), projectionMatrix.m10(), projectionMatrix.m20(), projectionMatrix.m30(),
                projectionMatrix.m01(), projectionMatrix.m11(), projectionMatrix.m21(), projectionMatrix.m31(),
                projectionMatrix.m02(), projectionMatrix.m12(), projectionMatrix.m22(), projectionMatrix.m32(),
                projectionMatrix.m03(), projectionMatrix.m13(), projectionMatrix.m23(), projectionMatrix.m33()};

        return v;
    }

    private static float[] perspective(float fovY, float aspect, float near, float far) {
        float ymax, xmax;
        ymax = (float) (near * Math.tan(fovY * Math.PI / 180.0f));
        xmax = ymax * aspect;
        return frustum(-xmax, xmax, -ymax, ymax, near, far);
    }

    private static float[] frustum(float left, float right, float bottom, float top, float near, float far) {
        float[] r = new float[16];
        float temp = 2.0f * near;
        float temp2 = right - left;
        float temp3 = top - bottom;
        float temp4 = far - near;
        r[0] = temp / temp2;
        r[1] = 0.0f;
        r[2] = 0.0f;
        r[3] = 0.0f;
        r[4] = 0.0f;
        r[5] = temp / temp3;
        r[6] = 0.0f;
        r[7] = 0.0f;
        r[8] = (right + left) / temp2;
        r[9] = (top + bottom) / temp3;
        r[10] = (-far - near) / temp4;
        r[11] = -1.0f;
        r[12] = 0.0f;
        r[13] = 0.0f;
        r[14] = (-temp * far) / temp4;
        r[15] = 0.0f;
        return r;
    }
}
