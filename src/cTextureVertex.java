public class cTextureVertex {
    cTextureVector position;

    cTextureVertex() {
        position = new cTextureVector(0.0f, 0.0f, 0.0f);
    }

    void setPosition(float u, float v, float w) {
        position.u = u;
        position.v = v;
        position.w = w;
    }
}
