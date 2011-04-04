
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import pcanvas.Point;
import pcanvas.draw.Colors;
import pcanvas.applet.OpenGLApplet;
import pcanvas.camera.Camera;
import pcanvas.draw.CanvasPen3D;

import static pcanvas.Point.addPoints;

/**
 * TODO: Enter class description.
 */
public class OpenGLCameraExample extends OpenGLApplet {
    private Camera cam;
    private CanvasPen3D pen;
    private Point ballPt;
    private Point ctrlPt;
    public void setup() {
        super.setup();
        this.size(640, 360, OPENGL);
        this.background(0);
        this.ballPt = new Point(width/2, height/2, 0);
        this.ctrlPt = ballPt.clone();
        this.pen = new CanvasPen3D(this);
        this.cam = new Camera(this, ballPt, 25);
        this.pen.setWireframeMode(true);
        this.pen.setWireframeColor(Colors.WHITE);
        frustum(-width, width, -height, height, (height/2.0F) / tan(PI * 20F / 360.0F), 30);

        this.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                cam.updateDistance(cam.getDistanceToTarget() + e.getWheelRotation());
            }
        });
    }

    @Override
    public void draw() {
        //perspective(60, width/height, ballPt.z, ballPt.z + 100);
        this.background(0);
        this.pen.drawPoint3D(addPoints(new Point(-10, 0, 0), ctrlPt), 5, Colors.GRAY);
        this.pen.drawPoint3D(ballPt, 5, Colors.GRAY);
        this.pen.drawPoint3D(addPoints(new Point(10, 0, 0), ctrlPt), 5, Colors.GRAY);
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

    @Override
    public void mouseDragged() {
        float yaw = PI * (mouseX - pmouseX) / width;
        float pitch = PI * (mouseY - pmouseY) / height;
        //pitch = max(-PI / 2, pitch); pitch = min(PI / 2, pitch);
        this.cam.updateYaw(yaw);
        this.cam.updatePitch(pitch);
    }
}
