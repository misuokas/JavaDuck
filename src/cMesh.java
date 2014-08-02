import java.awt.*;

public class cMesh {
    cVertex[] vertex;
    cTextureVertex[] textureVertex;
    cVertex[] vertexNormal;
    cFace[] face;

    cVertex[] rotatedVertex;
    cVertex[] faceNormal;
    cVertex[] rotatedFaceNormal;

    cColor color;
    cVertex ambientLight;

    int vertices;
    int textureVertices;
    int vertexNormals;
    int faces;

    private int zValue[];

    private float[][] createRotationMatrix(float x, float y, float z, float ax, float ay, float az) {
        float[][] matrix = new float[4][4];

        matrix[0][0] = (float) (Math.cos(ay) * Math.cos(az));
        matrix[1][0] = (float) (Math.cos(ay) * Math.sin(az));
        matrix[2][0] = (float) (-Math.sin(ay));
        matrix[3][0] = 0.0f;
        matrix[0][1] = (float) (Math.sin(ax) * Math.sin(ay) * Math.cos(az) - Math.cos(ax) * Math.sin(az));
        matrix[1][1] = (float) (Math.sin(ax) * Math.sin(ay) * Math.sin(az) + Math.cos(ax) * Math.cos(az));
        matrix[2][1] = (float) (Math.sin(ax) * Math.cos(ay));
        matrix[3][1] = 0.0f;
        matrix[0][2] = (float) (Math.cos(ax) * Math.sin(ay) * Math.cos(az) + Math.sin(ax) * Math.sin(az));
        matrix[1][2] = (float) (Math.cos(ax) * Math.sin(ay) * Math.sin(az) - Math.sin(ax) * Math.cos(az));
        matrix[2][2] = (float) (Math.cos(ax) * Math.cos(ay));
        matrix[3][2] = 0.0f;
        matrix[0][3] = x;
        matrix[1][3] = y;
        matrix[2][3] = z;
        matrix[3][3] = 1.0f;

        return matrix;
    }

    private boolean isPolygonVisible(cVertex v1, cVertex v2, cVertex v3) {
        boolean returnValue = false;
        float dx1 = v3.position.x - v1.position.x;
        float dy1 = v3.position.y - v1.position.y;
        float dx2 = v3.position.x - v2.position.x;
        float dy2 = v3.position.y - v2.position.y;

        if ((dx1 * (dy2 - dy1) - (dx2 - dx1) * dy1) > 0) {
            returnValue = true;
        }

        return returnValue;
    }

    private void sort(int top, int bottom) {
        int i, j;
        int x, tmp;

        i = top;
        j = bottom;
        x = zValue[(top + bottom) / 2];
        do {
            while (zValue[i] < x) i++;

            while (x < zValue[j]) j--;

            if (i < j) {
                tmp = zValue[i];
                zValue[i] = zValue[j];
                zValue[j] = tmp;
            }

            if (i <= j) {
                i++;
                j--;
            }
        } while (i <= j);

        if (top < j) sort(top, j);
        if (i < bottom) sort(i, bottom);
    }

    private int limitColor(int value) {
        if (value < 0) {
            value = 0;
        } else if (value > 255) {
            value = 255;
        }

        return value;
    }

    private void initLambertFlat(Graphics g, cVertex n) {
        int delta = 0;
        int red = 0;
        int green = 0;
        int blue = 0;

        delta = (int) (ambientLight.position.x * n.position.x +
                ambientLight.position.y * n.position.y +
                ambientLight.position.z * n.position.z);

        red = (int) (color.r * 256) + delta;
        green = (int) (color.g * 256) + delta;
        blue = (int) (color.b * 256) + delta;

        g.setColor(new Color(limitColor(red), limitColor(green), limitColor(blue)));
    }

    private void renderLambertFlat(Graphics g, cVertex v1, cVertex v2, cVertex v3) {
        int xPoints[] = new int[3];
        int yPoints[] = new int[3];

        xPoints[0] = (int) v1.position.x;
        yPoints[0] = (int) v1.position.y;
        xPoints[1] = (int) v2.position.x;
        yPoints[1] = (int) v2.position.y;
        xPoints[2] = (int) v3.position.x;
        yPoints[2] = (int) v3.position.y;

        g.fillPolygon(xPoints, yPoints, 3);
    }

/* ---------------------------------------------------------------------- */

    cMesh() {
        vertex = null;
        textureVertex = null;
        vertexNormal = null;
        face = null;

        rotatedVertex = null;
        faceNormal = null;
        rotatedFaceNormal = null;

        ambientLight = new cVertex();
        setAmbientLight(1.0f, 1.0f, 1.0f, 64.0f);

        color = new cColor(0.5f, 0.5f, 0.5f);

        vertices = 0;
        textureVertices = 0;
        vertexNormals = 0;
        faces = 0;
    }

    void rotate(float x, float y, float z, float ax, float ay, float az) {
        float rz = 0.0f;
        float[][] matrix = null;

        matrix = createRotationMatrix(0, 0, 0, ax, ay, az);
        for (int i = 0; i < getFaces(); i++) {
            rotatedFaceNormal[i].setPosition(
                    matrix[0][0] * faceNormal[i].position.x +
                            matrix[0][1] * faceNormal[i].position.y +
                            matrix[0][2] * faceNormal[i].position.z +
                            matrix[0][3],
                    matrix[1][0] * faceNormal[i].position.x +
                            matrix[1][1] * faceNormal[i].position.y +
                            matrix[1][2] * faceNormal[i].position.z +
                            matrix[1][3],
                    matrix[2][0] * faceNormal[i].position.x +
                            matrix[2][1] * faceNormal[i].position.y +
                            matrix[2][2] * faceNormal[i].position.z +
                            matrix[2][3]);
        }

        matrix = createRotationMatrix(x, y, z, ax, ay, az);
        for (int i = 0; i < getVertices(); i++) {
            rz = matrix[2][0] * vertex[i].position.x +
                    matrix[2][1] * vertex[i].position.y +
                    matrix[2][2] * vertex[i].position.z + matrix[2][3];

            rotatedVertex[i].setPosition(
                    (1024 * (matrix[0][0] * vertex[i].position.x +
                            matrix[0][1] * vertex[i].position.y +
                            matrix[0][2] * vertex[i].position.z +
                            matrix[0][3])) / rz,
                    (1024 * (matrix[1][0] * vertex[i].position.x +
                            matrix[1][1] * vertex[i].position.y +
                            matrix[1][2] * vertex[i].position.z +
                            matrix[1][3])) / rz,
                    rz);
        }
    }

    void render(Graphics g) {
        int n = 0;

        zValue = new int[faces];
        for (int i = 0; i < getFaces(); i++) {
            if (true == isPolygonVisible(
                    rotatedVertex[face[i].v1],
                    rotatedVertex[face[i].v2],
                    rotatedVertex[face[i].v3])) {
                zValue[n] = i;
                zValue[n] += (int) (
                        rotatedVertex[face[i].v1].position.z +
                                rotatedVertex[face[i].v2].position.z +
                                rotatedVertex[face[i].v3].position.z) << 16;
                n++;
            }
        }

        sort(0, n - 1);

        for (int i = 0; i < n; i++) {
            initLambertFlat(g, rotatedFaceNormal[zValue[i] & 65535]);

            renderLambertFlat(g,
                    rotatedVertex[face[zValue[i] & 65535].v1],
                    rotatedVertex[face[zValue[i] & 65535].v2],
                    rotatedVertex[face[zValue[i] & 65535].v3]);
        }
    }

    void allocateVertices(int vertices) {
        vertex = new cVertex[vertices];
        rotatedVertex = new cVertex[vertices];

        this.vertices = vertices;
        for (int i = 0; i < vertices; i++) {
            vertex[i] = new cVertex();
            rotatedVertex[i] = new cVertex();
        }
    }

    void allocateTextureVertices(int textureVertices) {
        textureVertex = new cTextureVertex[textureVertices];

        this.textureVertices = textureVertices;
        for (int i = 0; i < textureVertices; i++) {
            textureVertex[i] = new cTextureVertex();
        }
    }

    void allocateVertexNormals(int vertexNormals) {
        vertexNormal = new cVertex[vertexNormals];

        this.vertexNormals = vertexNormals;
        for (int i = 0; i < vertexNormals; i++) {
            vertexNormal[i] = new cVertex();
        }
    }

    void allocateFaces(int faces) {
        face = new cFace[faces];
        faceNormal = new cVertex[faces];
        rotatedFaceNormal = new cVertex[faces];

        this.faces = faces;
        for (int i = 0; i < faces; i++) {
            face[i] = new cFace();
            faceNormal[i] = new cVertex();
            rotatedFaceNormal[i] = new cVertex();
        }
    }

    int getVertices() {
        return vertices;
    }

    int getTextureVertices() {
        return textureVertices;
    }

    int getVertexNormals() {
        return vertexNormals;
    }

    int getFaces() {
        return faces;
    }

    void calculateFaceNormals() {
        for (int i = 0; i < faces; i++) {
            float x1 = vertex[face[i].v2].position.x - vertex[face[i].v1].position.x;
            float y1 = vertex[face[i].v2].position.y - vertex[face[i].v1].position.y;
            float z1 = vertex[face[i].v2].position.z - vertex[face[i].v1].position.z;
            float x2 = vertex[face[i].v3].position.x - vertex[face[i].v1].position.x;
            float y2 = vertex[face[i].v3].position.y - vertex[face[i].v1].position.y;
            float z2 = vertex[face[i].v3].position.z - vertex[face[i].v1].position.z;

            float nx = y1 * z2 - z1 * y2;
            float ny = z1 * x2 - x1 * z2;
            float nz = x1 * y2 - y1 * x2;

            float d = (float) Math.sqrt(nx * nx + ny * ny + nz * nz);

            faceNormal[i].setPosition(nx / d, ny / d, nz / d);
        }

        setAmbientLight(
                ambientLight.position.x,
                ambientLight.position.y,
                ambientLight.position.z,
                64.0f);
    }

    void setAmbientLight(float x, float y, float z, float f) {
        float d = (float) Math.sqrt(x * x + y * y + z * z);

        ambientLight.setPosition(x * f / d, y * f / d, z * f / d);
    }

    void setColor(float r, float g, float b) {
        color.set(r, g, b);
    }

    void debug() {
        System.out.println("Vertices: " + vertices);
        for (int i = 0; i < getVertices(); i++) {
            System.out.println(
                    "X: " + vertex[i].position.x +
                            " Y: " + vertex[i].position.y +
                            " Z: " + vertex[i].position.z);
        }

        System.out.println("Texture Vertices: " + textureVertices);
        for (int i = 0; i < getTextureVertices(); i++) {
            System.out.println(
                    "U: " + textureVertex[i].position.u +
                            " V: " + textureVertex[i].position.v +
                            " W: " + textureVertex[i].position.w);
        }

        System.out.println("Vertex Normals: " + vertexNormals);
        for (int i = 0; i < getVertexNormals(); i++) {
            System.out.println(
                    "X: " + vertexNormal[i].position.x +
                            " Y: " + vertexNormal[i].position.y +
                            " Z: " + vertexNormal[i].position.z);
        }

        System.out.println("Faces: " + faces);
        for (int i = 0; i < getFaces(); i++) {
            System.out.println(
                    "A: " + face[i].v1 + "/" + face[i].t1 +
                            "/" + face[i].n1 +
                            " B: " + face[i].v2 + "/" + face[i].t2 + "/" +
                            face[i].n2 +
                            " C: " + face[i].v3 + "/" + face[i].t3 + "/" +
                            face[i].n3);
        }
    }
}
