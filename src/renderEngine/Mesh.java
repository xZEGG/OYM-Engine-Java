package renderEngine;

public class Mesh {
    private int id;
    private int vertexCount;
    private transient float[] vertices;

    public Mesh(){
    }

    public Mesh(int id, int vertexCount){
        this.id = id;
        this.vertexCount = vertexCount;
    }

    public Mesh(float[] vertices, int id, int vertexCount){
        this.id = id;
        this.vertexCount = vertexCount;
        this.vertices = vertices;
    }

    public Mesh(int id, int vertexCount, Texture texture){
        this.id = id;
        this.vertexCount = vertexCount;
    }

    public Mesh(Mesh mesh, Texture texture){
        this.id = mesh.getId();
        this.vertexCount = mesh.getVertexCount();
    }

    // GETTERS

    public int getId(){
        return id;
    }

    public float[] getVertices() {
        return vertices;
    }

    public int getVertexCount(){
        return vertexCount;
    }
}