package components;

import engine.GameObject;
import imgui.ImGui;
import imgui.extension.imguizmo.ImGuizmo;
import imgui.extension.imguizmo.flag.Mode;
import imgui.extension.imguizmo.flag.Operation;
import org.joml.Matrix4f;
import renderEngine.Window;
import utils.Transformation;

public class Gizmos {
    //Gizmos
    private static int currentGizmoOperation = Operation.ROTATE;
    private static int currentMode = Mode.WORLD;

    private static final float FLT_EPSILON = 1.19209290E-07f;

    public void imguizmo(){
        ImGuizmo.setOrthographic(false);
        ImGuizmo.setEnabled(true);
        ImGuizmo.setDrawList();

        GameObject selectedObject = Window.get().getHierarchyWindow().getSelectedGameObject();

        //VIEW MATRIX AND PROJECTION MATRIX
        Matrix4f view = Transformation.getViewMatrix(Window.get().getCurrentScene().getCamera());
        //view.invert();
        //view.m30(0);
        //view.m31(0);
        //view.m32(0);
        //Matrix4f modelViewMatrix = Transformation.getModelViewMatrix(selectedObject, view);
        float[] cameraView = get4x4(Transformation.getViewMatrix(Window.get().getCurrentScene().getCamera()));
        //float aspect = ImGui.getWindowWidth() / ImGui.getWindowHeight();
        float[] cameraProjection = Transformation.getProjectionMatrix(60, 3840, 2160, 0.01f, 1000);

        //NEW
        //float[] cameraView = Transformation.getInverseViewMatrixArray(Window.get().currentScene.getCamera());
        //float[] cameraProjection = perspective(60, aspect, 0.01f, 1000f);
        ///////////////////////////////////

        final float[] IDENTITY_MATRIX = {
                1.f, 0.f, 0.f, 0.f,
                0.f, 1.f, 0.f, 0.f,
                0.f, 0.f, 1.f, 0.f,
                0.f, 0.f, 0.f, 1.f
        };

        final float[][] OBJECT_MATRICES = {
                {
                        1.f, 0.f, 0.f, 0.f,
                        0.f, 1.f, 0.f, 0.f,
                        0.f, 0.f, 1.f, 0.f,
                        0.f, 0.f, 0.f, 1.f
                },
                {
                        1.f, 0.f, 0.f, 0.f,
                        0.f, 1.f, 0.f, 0.f,
                        0.f, 0.f, 1.f, 0.f,
                        2.f, 0.f, 0.f, 1.f
                },
                {
                        1.f, 0.f, 0.f, 0.f,
                        0.f, 1.f, 0.f, 0.f,
                        0.f, 0.f, 1.f, 0.f,
                        2.f, 0.f, 2.f, 1.f
                },
                {
                        1.f, 0.f, 0.f, 0.f,
                        0.f, 1.f, 0.f, 0.f,
                        0.f, 0.f, 1.f, 0.f,
                        0.f, 0.f, 2.f, 1.f
                }
        };

        float windowWidth = ImGui.getWindowWidth();
        float windowHeight = ImGui.getWindowHeight();
        ImGuizmo.setRect(ImGui.getWindowPosX(), ImGui.getWindowPosY(), windowWidth, windowHeight);

        ImGuizmo.drawGrid(cameraView, cameraProjection, IDENTITY_MATRIX, 100);
        ImGuizmo.setId(0);
        //ImGuizmo.drawCubes(cameraView, cameraProjection, OBJECT_MATRICES[0]);

        if (selectedObject != null && selectedObject.getComponent(Transform.class) != null && selectedObject.getComponent(MeshRenderer.class) != null) {
            Matrix4f modelViewMatrix = Transformation.getModelViewMatrix(selectedObject, view);
            float[] model = get4x4(Transformation.getModelViewMatrix(selectedObject, view));
            ImGuizmo.manipulate(cameraView, cameraProjection, model, currentGizmoOperation, currentMode);

            float viewManipulateRight = ImGui.getWindowPosX() + windowWidth;
            float viewManipulateTop = ImGui.getWindowPosY();
            ImGuizmo.viewManipulate(cameraView, Transformation.distance(Window.get().getCurrentScene().getCamera().gameObject, selectedObject),
                    new float[]{viewManipulateRight - 128, viewManipulateTop}, new float[]{128f, 128f}, 0x10101010);
        }

        ImGuizmo.isOver();
    }

    private float[] get4x4(Matrix4f mat){
        float[] m = {
                mat.m00(), mat.m01(), mat.m02(), mat.m03(),
                mat.m10(), mat.m11(), mat.m12(), mat.m13(),
                mat.m20(), mat.m21(), mat.m22(), mat.m23(),
                mat.m30(), mat.m31(), mat.m32(), mat.m33()
        };
        return m;
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

    private static float[] cross(float[] a, float[] b) {
        float[] r = new float[3];
        r[0] = a[1] * b[2] - a[2] * b[1];
        r[1] = a[2] * b[0] - a[0] * b[2];
        r[2] = a[0] * b[1] - a[1] * b[0];
        return r;
    }

    private static float dot(float[] a, float[] b) {
        return a[0] * b[0] + a[1] * b[1] + a[2] * b[2];
    }

    private static float[] normalize(float[] a) {
        float[] r = new float[3];
        float il = (float) (1.f / (Math.sqrt(dot(a, a)) + FLT_EPSILON));
        r[0] = a[0] * il;
        r[1] = a[1] * il;
        r[2] = a[2] * il;
        return r;
    }
}