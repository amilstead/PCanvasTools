package pcanvas.draw;

import java.util.List;

import pcanvas.Circle3D;
import pcanvas.Point;
import pcanvas.Vector;
import processing.core.PApplet;

import static processing.core.PApplet.CLOSE;
import static processing.core.PApplet.QUADS;


/**
 * An extension of {@link CanvasPen} which is used to specifically draw 3-Dimensional objects.
 */
public class CanvasPen3D extends CanvasPen {

    /** Default radius for point draws. */
    protected int pointRadius = 1;

    /**
     *  Whether or not we should draw objects to the screen in wireframe mode.
     * This simply ensures that the {@link PApplet#stroke(int)} method is called before drawing.
     */
    protected Boolean wireMode = false;

    /** Default color for the wireframe draws. */
    protected Colors wireframeColor;

    /**
     * Constructor which accepts a {@link PApplet} needed to make the calls we are abstracting from the user for
     * simplicity in drawing objects to the canvas.
     * @param applet the base {@link PApplet} needed for canvas draws.
     */
    public CanvasPen3D(final PApplet applet) {
        super(applet);
    }

    /**
     * Plots a point onto the {@link PApplet} canvas that will be drawn (most often in-between begin/endShape() calls}.
     * @param point some {@link Point} to be drawn.
     */
    @Override
    protected void plotPoint(final Point point) {
        this.applet.vertex(point.x, point.y, point.z);
    }

    /**
     * Draws a 3-dimensional line from one point to another using the provided color. Resets the stroke weight and color
     * after drawing.
     * @param start and end point of the line-segment.
     * @param end another end point of the line-segment.
     * @param color the color to make the line-segment.
     */
    @Override
    public void drawLine(final Point start, final Point end, final Colors color) {
        this.setStrokeColorAndWeight(color, this.strokeWeight);
        this.applet.line(start.x, start.y, start.z, end.x, end.y, end.z);
        if (!wireMode) this.applet.noStroke();
        this.applet.color(Colors.BLACK.getRgb());
    }

    /**
     * Draws a 3-dimensional line from one point to another using the provided color and stroke weight. Resets the
     * stroke weight and color after drawing. 
     * @param start and end point of the line-segment.
     * @param end another end point of the line-segment.
     * @param color the color to make the line-segment.
     * @param strokeWeight the desired weight of the line to be drawn.
     */
    @Override
    public void drawLine(final Point start, final Point end, final Colors color, float strokeWeight) {
        this.setStrokeColorAndWeight(color, strokeWeight);
        this.applet.line(start.x, start.y, start.z, end.x, end.y, end.z);
        if (!wireMode) this.applet.noStroke();
        this.applet.color(Colors.BLACK.getRgb());
    }

    /**
     * Sets the wireframe color.
     * @param color a {@link Colors} object.
     */
    public void setWireframeColor(final Colors color) {
        this.wireframeColor = color;
    }

    /**
     * Toggles wireframe mode based on user input.
     * @param wireMode whether or not to draw in wireframe mode.
     */
    public void setWireframeMode(final Boolean wireMode) {
        this.wireMode = wireMode;
    }

    /**
     * Tells the user if we're currently drawing in wireframe mode.
     * @return the local {@link #wireMode} boolean.
     */
    public Boolean getWireframeMode() {
        return this.wireMode;
    }

    /**
     * Draws a 3-Dimensional {@link Point} onto the canvas with a given radius and color.
     * @param point the {@link Point} to draw.
     * @param radius a radius, as an integer.
     * @param color the {@link Colors} value for the object to be drawn.
     */
    public void drawPoint3D(final Point point, final int radius, final Colors color) {
        if (wireMode) this.applet.stroke(wireframeColor.getRgb()); else this.applet.noStroke();
        this.applet.fill(color.getRgb());
        this.applet.pushMatrix();
        this.applet.translate(point.x, point.y, point.z);
        this.applet.sphere(radius);
        this.applet.noFill();
        this.applet.popMatrix();
    }

    /**
     * Draws a 3-Dimensional {@link Point} onto the canvas with a given radius and color.
     * @param point the {@link Point} to draw.
     * @param radius a radius, as a float.
     * @param color the {@link Colors} value for the object to be drawn.
     */
    public void drawPoint3D(final Point point, final float radius, final Colors color) {
        if (wireMode) this.applet.stroke(wireframeColor.getRgb()); else this.applet.noStroke();
        this.applet.fill(color.getRgb());
        this.applet.pushMatrix();
        this.applet.translate(point.x, point.y, point.z);
        this.applet.sphere(radius);
        this.applet.noFill();
        this.applet.popMatrix();        
    }

    /**
     * Draws a 3-Dimensional {@link Point} onto the canvas with a given radius. The default point color is {@link Colors#BLACK}.
     * @param point the {@link Point} to draw.
     * @param radius a radius, as an integer.
     */
    public void drawPoint3D(final Point point, final int radius) {
        if (wireMode) this.applet.stroke(wireframeColor.getRgb()); else this.applet.noStroke();
        this.applet.pushMatrix();
        this.applet.fill(Colors.BLACK.getRgb());
        this.applet.translate(point.x, point.y, point.z);
        this.applet.sphere(radius);
        this.applet.noFill();
        this.applet.popMatrix();
    }

    /**
     * Draws a 3-Dimensional {@link Point} onto the canvas with a given radius. The default point color is {@link Colors#BLACK}.
     * @param point the {@link Point} to draw.
     * @param radius a radius, as a float.
     */
    public void drawPoint3D(final Point point, final float radius) {
        if (wireMode) this.applet.stroke(wireframeColor.getRgb()); else this.applet.noStroke();
        this.applet.pushMatrix();
        this.applet.fill(Colors.BLACK.getRgb());
        this.applet.translate(point.x, point.y, point.z);
        this.applet.sphere(radius);
        this.applet.noFill();
        this.applet.popMatrix();
    }

    /**
     * Draws a 3-Dimensional box onto the canvas whose center is the provided {@link Point} and whose size (distance to a face
     * from the center) is the provided size. Default box color is {@link Colors#BLACK}.
     * @param point the {@link Point} center of the box.
     * @param size the size (distance from center to faces) of the box as a float.
     */
    public void drawBox(final Point point, final float size) {
        if (wireMode) this.applet.stroke(wireframeColor.getRgb()); else this.applet.noStroke();
        this.applet.pushMatrix();
        this.applet.fill(Colors.BLACK.getRgb());
        this.applet.translate(point.x, point.y, point.z);
        this.applet.box(size);
        this.applet.noFill();
        this.applet.popMatrix();
    }

    /**
     * Draws a 3-Dimensional box onto the canvas whose center is the provided {@link Point} and whose size (distance to a face
     * from the center) is the provided size. Default box color is {@link Colors#BLACK}.
     * @param point the {@link Point} center of the box.
     * @param size the size (distance from center to faces) of the box an integer.
     */
    public void drawBox(final Point point, final int size) {
        if (wireMode) this.applet.stroke(wireframeColor.getRgb()); else this.applet.noStroke();
        this.applet.pushMatrix();
        this.applet.fill(Colors.BLACK.getRgb());
        this.applet.translate(point.x, point.y, point.z);
        this.applet.box(size);
        this.applet.noFill();
        this.applet.popMatrix();
    }

    /**
     * Draws a 3-Dimensional box onto the canvas whose center is the provided {@link Point}, whose size (distance to a face
     * from the center) is the provided size. Default box color is {@link Colors#BLACK} and whose color is the {@link Colors}
     * enum provided.
     * @param point the {@link Point} center of the box.
     * @param size the size (distance from center to faces) of the box an integer.
     * @param color the provided color of the box.
     */
    public void drawBox(final Point point, final int size, final Colors color) {
        if (wireMode) this.applet.stroke(wireframeColor.getRgb()); else this.applet.noStroke();
        this.applet.fill(color.getRgb());
        this.applet.pushMatrix();
        this.applet.translate(point.x, point.y, point.z);
        this.applet.box(size);
        this.applet.popMatrix();
        this.applet.noFill();
    }

    /**
     * Draws a 3-Dimensional box onto the canvas whose center is the provided {@link Point}, whose size (distance to a face
     * from the center) is the provided size. Default box color is {@link Colors#BLACK} and whose color is the {@link Colors}
     * enum provided.
     * @param point the {@link Point} center of the box.
     * @param size the size (distance from center to faces) of the box a float.
     * @param color the provided color of the box.
     */
    public void drawBox(final Point point, final float size, final Colors color) {
        if (wireMode) this.applet.stroke(wireframeColor.getRgb()); else this.applet.noStroke();
        this.applet.fill(color.getRgb());
        this.applet.pushMatrix();
        this.applet.translate(point.x, point.y, point.z);
        this.applet.box(size);
        this.applet.popMatrix();
        this.applet.noFill();
    }

    /**
     * Draws a label next to a 3-Dimensional {@link Point} in space using a provided displacement away from that point.
     * @param point the target of the label draw.
     * @param text the text of the label.
     * @param labelDisplacement the displacement vector from point to label.
     */
    public void drawPointLabel3D(final Point point, final String text, final Vector labelDisplacement) {
        this.applet.text(text, point.x + labelDisplacement.x,
                               point.y + labelDisplacement.y,
                               point.z + labelDisplacement.z);
    }

    /**
     * Calls {@link #drawSmoothShading(List, Colors)} defaulting the color to {@link Colors#WHITE}.
     * @param circles the circles to draw smooth shading around.
     */
    public void drawSmoothShading(final List<Circle3D> circles) {
        this.drawSmoothShading(circles, Colors.WHITE);
    }

    /**
     * Creates a 3D smooth object whose 'skeleton' is represented by the provided {@link Circle3D} objects.
     * @param circles the 'skeleton' circles.
     * @param color the color for shading.
     */
    public void drawSmoothShading(final List<Circle3D> circles, final Colors color) {
        for (int i = 0; i < circles.size() - 1; i++) {
            Circle3D circle = circles.get(i);
            Point center = circle.center;
            List<Point> points = circle.points;
            Circle3D nextCircle = circles.get(i + 1);
            Point nextCenter = nextCircle.center;
            List<Point> nextPoints = nextCircle.points;
            this.applet.fill(color.getRgb());
            this.applet.beginShape(QUADS);
            for (int j = 0; j < points.size(); j++) {
                int index = (j == points.size() - 1 ?  points.size() - j : j + 1);
                Point topLeft = points.get(j), topRight = nextPoints.get(j);
                Point bottomLeft = points.get(index), bottomRight = nextPoints.get(index);
                Vector tlNormal = new Vector(center, topLeft).normal();
                Vector trNormal = new Vector(nextCenter, topRight).normal();
                Vector blNormal = new Vector(center, bottomLeft).normal();
                Vector brNormal = new Vector(nextCenter, bottomRight).normal();


                this.applet.normal(tlNormal.x, tlNormal.y, tlNormal.z);
                this.applet.vertex(topLeft.x, topLeft.y, topLeft.z);

                this.applet.normal(trNormal.x, trNormal.y, trNormal.z);
                this.applet.vertex(topRight.x, topRight.y, topRight.z);

                this.applet.normal(brNormal.x, brNormal.y, brNormal.z);
                this.applet.vertex(bottomRight.x, bottomRight.y, bottomRight.z);

                this.applet.normal(blNormal.x, blNormal.y, blNormal.z);
                this.applet.vertex(bottomLeft.x, bottomLeft.y, bottomLeft.z);
            }
            this.applet.endShape(CLOSE);
        }
    }

    /**
     * Creates a 3D smooth object whose 'skeleton' is represented by the provided {@link Circle3D} objects.
     * @param circles the 'skeleton' circles.
     * @param color the color for shading.
     */
    public void drawSmoothShading(final List<Circle3D> circles, final int color) {
        for (int i = 0; i < circles.size() - 1; i++) {
            Circle3D circle = circles.get(i);
            Point center = circle.center;
            List<Point> points = circle.points;
            Circle3D nextCircle = circles.get(i + 1);
            Point nextCenter = nextCircle.center;
            List<Point> nextPoints = nextCircle.points;
            this.applet.fill(color);
            this.applet.beginShape(QUADS);
            for (int j = 0; j < points.size(); j++) {
                int index = (j == points.size() - 1 ?  points.size() - j : j + 1);
                Point topLeft = points.get(j), topRight = nextPoints.get(j);
                Point bottomLeft = points.get(index), bottomRight = nextPoints.get(index);
                Vector tlNormal = new Vector(center, topLeft).normal();
                Vector trNormal = new Vector(nextCenter, topRight).normal();
                Vector blNormal = new Vector(center, bottomLeft).normal();
                Vector brNormal = new Vector(nextCenter, bottomRight).normal();


                this.applet.normal(tlNormal.x, tlNormal.y, tlNormal.z);
                this.applet.vertex(topLeft.x, topLeft.y, topLeft.z);

                this.applet.normal(trNormal.x, trNormal.y, trNormal.z);
                this.applet.vertex(topRight.x, topRight.y, topRight.z);

                this.applet.normal(brNormal.x, brNormal.y, brNormal.z);
                this.applet.vertex(bottomRight.x, bottomRight.y, bottomRight.z);

                this.applet.normal(blNormal.x, blNormal.y, blNormal.z);
                this.applet.vertex(bottomLeft.x, bottomLeft.y, bottomLeft.z);
            }
            this.applet.endShape(CLOSE);
        }
    }
}
