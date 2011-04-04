package pcanvas.camera;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import pcanvas.Point;
import pcanvas.Vector;
import pcanvas.applet.OpenGLApplet;
import pcanvas.draw.CanvasPen3D;
import processing.core.PApplet;

import static pcanvas.Point.interpolate;
import static pcanvas.geom.GeomUtils.PointDistance;
import static pcanvas.geom.GeomUtils.PointRotate3D;
import static processing.core.PApplet.tan;
import static processing.core.PConstants.PI;

/**
 * Camera object for use on {@link PApplet} and {@link OpenGLApplet} processing windows.
 */
public class Camera {

    /** Default distance to target object if distance is not specified. */
    protected static float DEFAULT_TDISTANCE = 10.0F;

    /** Camera Position, Target location and Target's previous location, respectively. */
    private Point position, target, targetPrev;

    /** Whether or not this camera should 'track' the target (stay stationary instead of follow). */
    private Boolean isTracking = true;

    /** Distance from camera to target. */
    private float distanceToTarget = DEFAULT_TDISTANCE;

    /** Scale factor when calculating zooms towards and away from the target. */
    private float zoomScale = 1.0F;

    /** The {@link PApplet} used to make {@link PApplet#camera()} calls. */
    private final PApplet applet;

    /** The vector which represents which direction is 'up' in 3-space. */
    private final Vector upVector = new Vector(0, 1, 0);

    /** A {@link CanvasPen3D} for debugging purposes. */
    private CanvasPen3D pen;

    private Boolean enabled = true;

    /**
     * Most basic constructor necessary for creating a camera. Accepts a {@link PApplet} object in order to make
     * base API {@link PApplet#camera(float, float, float, float, float, float, float, float, float)} calls.
     * 
     * @param applet the applet this camera is attached to.
     */
    public Camera(final PApplet applet) {
        this.applet = applet;
        this.pen = new CanvasPen3D(applet);
        this.setDefaults(true);
        this.upVector.set(new Vector(0, 1, 0));
    }

    /**
     * More advanced constructor which accepts a base {@link PApplet} and a custom upVector. The upVector can also
     * be set from the {@link #setUpVector(Vector)} method.
     *
     * @param applet the applet this camera is attached to.
     * @param upVector the upVector this camera uses for calculating rotations and positional information.
     */
    public Camera(final PApplet applet, final Vector upVector) {
        this.applet = applet;
        this.setDefaults(true);
        this.upVector.set(upVector);
    }

    public Camera(final PApplet applet, final Point target, final float distance) {
        this.applet = applet;
        this.target = new Point(target.x, target.y, target.z);
        this.pen = new CanvasPen3D(applet);
        this.distanceToTarget = distance;
        this.setDefaults(false);
        this.upVector.set(new Vector(0, 1, 0));
    }

    public Camera(final PApplet applet, final Vector upVector, final Point target, final float distance) {
        this.applet = applet;
        this.target = new Point(target.x, target.y, target.z);
        this.distanceToTarget = distance;
        this.setDefaults(false);
        this.upVector.set(upVector);
    }

    /**
     * Sets the zoom scale for this camera. This dictates how much the camera should scale the wheel rotation values
     * when zooming towards or away from the {@link #target}.
     * <br />
     * <br />
     * The default zooms scale is 1.0F.
     * @param zoomScale the new zoom scale.
     */
    public void setZoomScale(final float zoomScale) {
        this.zoomScale = zoomScale;
    }

    /**
     * Sets the {@link #isTracking} boolean to the provided boolean.
     * @param isTracking whether or not to track or follow the target.
     */
    public void setIsTracking(final Boolean isTracking) {
        this.isTracking = isTracking;
    }

    public void enable() {
        this.enabled = true;
    }

    public void disable() {
        this.enabled = false;
    }

    public Boolean isEnabled() {
        return this.enabled;
    }

    /**
     * Updates the distance between this camera and its target.
     *
     * @param distance new distance to {@link #target}.
     */
    public void updateDistance(final float distance) {
        if (enabled && !(distance == this.distanceToTarget) && distance > 0.0F) {
            float ratio = distance / this.distanceToTarget;
            this.position.interpolate(target, 1 - ratio);
            this.distanceToTarget = distance;
        }
    }

    /**
     * Retrieves the distance from this camera to its target.
     * @return {@link #distanceToTarget} - the distance to our target {@link Point}.
     */
    public float getDistanceToTarget() {
        return this.distanceToTarget;
    }

    /**
     * Updates the camera's {@link #target}. This should be called every frame in the {@link PApplet#draw()} method in
     * order to observe proper camera behavior.
     * @param target the new target.
     */
    public void updateTarget(final Point target) {
        if (!this.isTracking) {
            this.position.translate(new Vector(this.targetPrev, this.target));
        }
        this.targetPrev = new Point(this.target.x, this.target.y, this.target.z);
        this.target = target;
    }

    /**
     * Changes the {@link #upVector} this camera uses to calculate rotations, zooms and overall perspective correctness.
     * @param upVector a new {@link Vector} representing which direction is up.
     */
    public void setUpVector(final Vector upVector) {
        this.upVector.set(upVector);
    }

    /**
     * The main update method for the call. Along with {@link #updateTarget(Point)}, this method should also be called
     * every {@link PApplet#draw()} frame. It is effectively a wrapper call for
     * {@link PApplet#camera(float, float, float, float, float, float, float, float, float)} using our stored, rotated, etc.
     * {@link #position}, {@link #target} and {@link #upVector}.
     */
    public void update() {
        this.applet.camera(position.x, position.y, position.z,
                           target.x, target.y, target.z,
                           upVector.x, upVector.y, upVector.z);
    }

    /**
     * Updates the pitch rotation of the camera about the {@link #target}.
     * @param pitch a new pitch angle, in radians.
     */
    public void updatePitch(final float pitch) {
        if (enabled) {
            Point axisBase = PointRotate3D(target, position, upVector, PI / 2);
            Vector zRotVec = new Vector(target, axisBase);
            position = PointRotate3D(target, position, zRotVec, pitch);
        }
    }


    /**
     * Updates the yaw rotation of the camera about the {@link #target}.
     * @param yaw a new yaw angle, in radians.
     */
    public void updateYaw(final float yaw) {
        if (enabled) position = PointRotate3D(target, position, upVector, -yaw);
    }

    /**
     * Resets the camera to its original position.
     * <br /> Take a look at <a href="http://processing.org/reference/camera_.html"> processing documentation</a> for details on exact locations.
     */
    public void reset() {
        Point cDefault = new Point((this.applet.width / 2.0F),
                                  (this.applet.height / 2.0F),
                                  (this.applet.height / 2.0F) / tan(PI * 60.0F / 360.0F));
        float baseDist = PointDistance(target, cDefault);
        this.targetPrev = interpolate(target, cDefault, distanceToTarget / baseDist);
        this.position = interpolate(targetPrev, cDefault, 0);
    }

    /**
     * Private, internal method used to set the initial default values for the camera. Accepts a Boolean describing
     * whether or not to use default Processing locations see: {@link #reset()} for more information on initial positions.
     *
     * @param useAppDefaults whether or not to use processing defaults for the camera and target positions.
     */
    private void setDefaults(final Boolean useAppDefaults) {
        Point tDefault = new Point(this.applet.width / 2.0F, this.applet.height / 2.0F, 0);
        Point cDefault = new Point((this.applet.width / 2.0F),
                                  (this.applet.height / 2.0F),
                                  (this.applet.height / 2.0F) / tan(PI * 60.0F / 360.0F));

        if (useAppDefaults) {
            this.target = new Point(tDefault.x, tDefault.y, tDefault.z);
            this.position = cDefault;
            this.distanceToTarget = PointDistance(target, cDefault);
        } else {
            float baseDist = PointDistance(target, cDefault);
            this.position = interpolate(target, cDefault, distanceToTarget / baseDist);
        }
        this.targetPrev = new Point(target.x, target.y, target.z);
        this.setupWheelListener();
        this.setupRotationListeners();
    }

    /**
     * Private method used to inject a {@link MouseWheelListener} to the main {@link PApplet} in order to ensure
     * proper zoom functionality.
     */
    private void setupWheelListener() {
        this.applet.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                updateDistance(getDistanceToTarget() + zoomScale * e.getWheelRotation());
            }
        });
    }

    /**
     * Private method used to inject a {@link MouseMotionListener} to the main {@link PApplet} in order to ensure
     * proper rotation updates.
     */
    private void setupRotationListeners() {
        this.applet.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                float yaw = PI * (applet.mouseX - applet.pmouseX) / applet.width;
                float pitch = PI * (applet.mouseY - applet.pmouseY) / applet.height;
                //pitch = max(-PI / 2, pitch); pitch = min(PI / 2, pitch);
                updateYaw(yaw);
                updatePitch(pitch);
            }

            @Override
            public void mouseMoved(MouseEvent e) {

            }
        });
    }
}

