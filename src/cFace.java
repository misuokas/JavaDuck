public class cFace {
    int v1, v2, v3;
    int t1, t2, t3;
    int n1, n2, n3;

    cFace() {
        v1 = 0;
        v2 = 0;
        v3 = 0;
        t1 = 0;
        t2 = 0;
        t3 = 0;
        n1 = 0;
        n2 = 0;
        n3 = 0;
    }

    void setVertices(int v1, int v2, int v3) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
    }

    void setTextureVertices(int t1, int t2, int t3) {
        this.t1 = t1;
        this.t2 = t2;
        this.t3 = t3;
    }

    void setNormalVertices(int n1, int n2, int n3) {
        this.n1 = n1;
        this.n2 = n2;
        this.n3 = n3;
    }
}
