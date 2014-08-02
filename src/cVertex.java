public class cVertex {
    cVector position;

    cVertex() {
        position = new cVector(0.0f, 0.0f, 0.0f);
    }

    void setPosition(float x, float y, float z) {
        position.x = x;
        position.y = y;
        position.z = z;
    }
}