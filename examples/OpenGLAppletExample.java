import pcanvas.Point;
import pcanvas.applet.OpenGLApplet;
import pcanvas.draw.CanvasPen3D;
import pcanvas.draw.Colors;

/**
 * Example on how to create a {@link OpenGLApplet} using PCanvasTools.
 */
public class OpenGLAppletExample extends OpenGLApplet {

    private Point testPt;
    private CanvasPen3D pen;

    @Override
    public void setup() {
        super.setup();
        this.size(600, 600, OPENGL);
        this.testPt = new Point(width / 2, height / 2, 400);
        this.pen = new CanvasPen3D(this);
        this.pen.setWireframeColor(Colors.BLACK);
        this.pen.setWireframeMode(true);
    }

    @Override
    public void draw() {
        this.background(255);
        this.pen.drawPoint3D(testPt, 10, Colors.RED);
    }

}
