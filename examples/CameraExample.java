
import pcanvas.Point;
import pcanvas.draw.CanvasPen3D;
import pcanvas.draw.Colors;
import pcanvas.camera.Camera;
import processing.core.PApplet;

/**
 * TODO: Enter class description.
 */
public class CameraExample extends PApplet {
    private Camera cam;
    private CanvasPen3D pen;
    private Point ballPt;

    @Override
    public void setup() {
        this.size(640, 360, P3D);
        this.background(0);
        this.pen = new CanvasPen3D(this);
        this.ballPt = new Point(width/2, height/2, 0);
        this.pen.setWireframeMode(true);
        this.pen.setWireframeColor(Colors.WHITE);
        this.cam = new Camera(this, ballPt, 50);
        this.cam.setIsTracking(false);
    }

    @Override
    public void draw() {
        this.background(0);
        this.pen.drawPoint3D(ballPt, 5, Colors.GRAY);
        this.cam.updateTarget(ballPt);
        this.cam.update();
    }

    @Override
    public void keyPressed() {
        if (key == 'W') {
            this.pen.setWireframeMode(!this.pen.getWireframeMode());
        }
        if (key == 'R') {
            this.cam.reset();
        }
        if (keyCode == UP) {
            this.ballPt.y--;
        }
        if (keyCode == DOWN) {
            this.ballPt.y++;
        }
        if (keyCode == LEFT) {
            this.ballPt.x--;
        }
        if (keyCode == RIGHT) {
            this.ballPt.x++;
        }
    }
}
