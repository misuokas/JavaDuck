import java.applet.Applet;
import java.awt.*;

public class JavaDuck extends Applet implements Runnable {
	private static final long serialVersionUID = 1L;
	public Thread anim;
    cScene scene = new cScene();

    public void init() {
        scene.load("duck.obj");
        scene.setAmbientLight(1.0f, 1.0f, 1.0f, 64.0f);
        if (null != scene.mesh) {
            scene.mesh.setColor(0.5f, 0.5f, 0.0f);
        }
    }

    public void start() {
        anim = new Thread(this);
        anim.start();
    }

    public void run() {
        long lastTime = 0;

        Graphics g = getGraphics();
        Toolkit t = getToolkit();
        Image bufimage = createImage(800, 600);
        Graphics buf = bufimage.getGraphics();

        float x = 0.0f;
        float y = 0.0f;
        float z = 0.0f;
        float delta = 0.0f;
        while (0 != 1) {
            long currentTime = System.nanoTime();
            if(lastTime != 0) {
            	delta = (float)(currentTime - lastTime);
            }
            lastTime = currentTime;

            buf.setColor(Color.black);
            buf.fillRect(0, 0, 800, 600);
            scene.rotate(1.0f, 1.0f, 5, x, y, z);
            scene.render(buf);
            g.drawImage(bufimage, 0, 0, null);

            delta = delta * 0.000000001f;
           	x += delta;
            y -= delta;
            z -= delta;

            t.sync();
        }
    }
}