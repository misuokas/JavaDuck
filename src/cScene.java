import java.awt.*;

public class cScene {
    cMesh mesh;

    cScene() {
        mesh = null;
    }

    void load(String filename) {
        mesh = cFileObj.load(filename);

        if (null != mesh) {
            mesh.calculateFaceNormals();
        }
    }

    void rotate(float x, float y, float z, float ax, float ay, float az) {
        if (null != mesh) {
            mesh.rotate(x, y, z, ax, ay, az);
        }
    }

    void render(Graphics g) {
        if (null != mesh) {
            mesh.render(g);
        }
    }

    void setAmbientLight(float x, float y, float z, float f) {
        if (null != mesh) {
            mesh.setAmbientLight(x, y, z, f);
        }
    }
}
