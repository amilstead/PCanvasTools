package pcanvas.draw;

import pcanvas.Circle2D;
import pcanvas.Point;
import pcanvas.Vector;
import processing.core.PApplet;
import processing.core.PImage;

import static pcanvas.Point.rotatePointByDegrees;
import static pcanvas.Point.translatePoint;
import static processing.core.PApplet.CLOSE;
import static processing.core.PApplet.PI;
import static processing.core.PApplet.cos;
import static processing.core.PApplet.max;
import static processing.core.PApplet.sin;
import static processing.core.PApplet.sq;
import static processing.core.PApplet.sqrt;

/**
 * Various utilities for drawing objects onto a {@link PApplet} canvas.
 */
public class CanvasPen {

    /** A {@link PApplet} whose canvas this object will manipulate to draw objects onto the screen. */
    protected final PApplet applet;

    protected Float strokeWeight = 1.0F;
    
    /**
     * Basic constructor. Requires a {@link PApplet} in order to invoke its graphics methods.
     * @param applet the applet whose graphics we will be manipulating.
     */
    public CanvasPen(final PApplet applet) {
        this.applet = applet;
    }

    /**
     * Plots a {@link Point} to be later drawn onto the screen.
     * @param point some {@link Point} to be drawn.
     */
    protected void plotPoint(final Point point) {
        this.applet.vertex(point.x, point.y);
    }

    /**
     * Sets the x and y coordinates of a {@link Point} to the current mouse position on the canvas.
     * @param point some point whose coordinates will be set.
     */
    public void setPointToMouse(final Point point) {
        point.set(this.applet.mouseX, this.applet.mouseY);
    }

    /* Point draws */
    /**
     * Draws a single {@link Point} onto the screen at the current mouse location.
     */
    public void drawPointAtMouse() {
        this.drawPointAtMouse(this.mouseLoc());
    }

    /**
     * Sets a {@link Point}'s x and y coordinates to the mouse location, then draws it on the screen.
     * @param point the point to be set at the mouse location.
     */
    public void drawPointAtMouse(final Point point) {
        this.setPointToMouse(point);
        this.drawPoint(point);
    }

    /**
     * Draws a {@link Point} onto the canvas using a given fillColor.
     * @param point the point to draw.
     * @param fillColor the color of the given point.
     */
    public void drawPoint(final Point point, final Colors fillColor) {
        this.applet.fill(this.applet.color(fillColor.getRgb()));
        this.applet.ellipse(point.x, point.y, 4.0F, 4.0F);

    }

    public void drawPoint(final Point point, final int color) {
        this.applet.fill(color);
        this.applet.ellipse(point.x, point.y, 4.0F, 4.0F);
    }

    /**
     * Draws a {@link Point} onto the canvas, defaulting the color to {@link Colors#BLACK}.
     * @param point a {@link Point} to draw onto the canvas.
     */
    public void drawPoint(final Point point) {
        this.drawPoint(point, Colors.BLACK);
    }

    /**
     * Draws a {@link Point} on to the canvas, with a provided radius and fill color.
     * @param point some point to draw.
     * @param radius a radius for the point.
     * @param fillColor the fill color of the point.
     */
    public void drawPoint(final Point point, final float radius, final Colors fillColor) {
        this.applet.fill(this.applet.color(fillColor.getRgb()));
        this.applet.ellipse(point.x, point.y, 2*radius, 2*radius);
    }

    public void drawPoint(final Point point, final float radius, final int color) {
        this.applet.fill(color);
        this.applet.ellipse(point.x, point.y, 2*radius, 2*radius);
    }

    /**
     * Same as {@link #drawPoint(Point point, float radius, Colors fillColor)} except the fillColor defaults to
     * {@link Colors#BLACK}.
     * @param point a point to be drawn.
     * @param radius a radius for the point.
     */
    public void drawPoint(final Point point, final float radius) {
        this.drawPoint(point, radius, Colors.BLACK);
    }

    /* Vector draws */

    /**
     * Draws a {@link Vector} onto the canvas starting at the provided {@link Point}.
     * @param from the point to start the vector draw from.
     * @param vector the vector to draw.
     */
    public void drawVector(final Point from, final Vector vector) {
        this.drawLine(from, new Point(from.x + vector.x, from.y + vector.y));
    }

    /**
     * Same as {@link #drawVector(Point from, Vector vector)} except puts a pretty little arrow on top to denote
     * vector direction.
     * @param from the point to start the vector draw from.
     * @param vector the vector to draw.
     */
    public void drawArrowVector(final Point from, final Vector vector) {
        this.drawVector(from, vector);
        Point top = translatePoint(from, vector);
        Point ratio = translatePoint(from, vector.scale(9/10F));
        Point left = rotatePointByDegrees(ratio, top, -215);
        Point right = rotatePointByDegrees(ratio, top, 215);
        this.drawShape(Colors.BLACK, false, left, top, right);

    }

    /* Label draws */

    /**
     * Draws a label onto the canvas using a given string of text at some location (x, y).
     * @param text the text to draw.
     * @param x x-coordinate.
     * @param y y-coordinate.
     */
    public void drawLabel(final String text, final float x, final float y) {
        this.applet.text(text, x, y);
    }

    /**
     * Draws a label onto the screen using a given number for the text, still at location (x, y).
     * @param number a number to draw.
     * @param x x-coordinate.
     * @param y y-coordinate.
     */
    public void drawLabel(final Integer number, final float x, final float y) {
        this.drawLabel(number.toString(), x, y);
    }

    /**
     * Draws a label of text next to a {@link Point} on the canvas.
     * @param text text to draw.
     * @param point point to draw it next to.
     */
    public void drawPointLabel(final String text, final Point point) {
        this.drawLabel(text, point.x + 5, point.y + 4);
    }

    /**
     * Draws a line-segment between two {@link Point}s on the canvas. Defaults the line color to {@link Colors#BLACK}.
     * @param start and end point of the line-segment.
     * @param end another end point of the line-segment.
     */
    public void drawLine(final Point start, final Point end) {
        this.drawLine(start, end, Colors.BLACK);
    }

    /**
     * Draws a line-segment between two {@link Point}s on the canvas, coloring it with the provided color.
     * @param start and end point of the line-segment.
     * @param end another end point of the line-segment.
     * @param color the color to make the line-segment.
     */
    public void drawLine(final Point start, final Point end, final Colors color) {
        this.setStrokeColorAndWeight(color, this.strokeWeight);
        this.applet.line(start.x, start.y, end.x, end.y);
    }

    /**
     * Draws a line-segment between two {@link Point}s on the canvas, coloring it with the provided color and stroke
     * weight.
     * @param start and end point of the line-segment.
     * @param end another end point of the line-segment.
     * @param color the color to make the line-segment.
     * @param strokeWeight the desired weight of the line to be drawn.
     */
    public void drawLine(final Point start, final Point end, final Colors color, float strokeWeight) {
        this.setStrokeColorAndWeight(color, strokeWeight);
        this.applet.line(start.x, start.y, end.x, end.y);       
        this.applet.noStroke();
        this.applet.color(Colors.BLACK.getRgb());
    }


    /* Point-defined shape draws. */

    /**
     * Draws a shape defined by a varying-size array of {@link Point} objects. Defaults the fill color of the shape
     * to {@link Colors#BLACK}.
     * @param points the points which should make up some shape.
     */
    public void drawShape(final Point ... points) {
        this.drawShape(Colors.BLACK, points);
    }

    /**
     * Draws a shape defined by a varying-size array of {@link Point} objects, filling the shape's color with the
     * provided fillColor.
     * @param fillColor what color to paint the object.
     * @param points the points which should make up some shape.
     */
    public void drawShape(final Colors fillColor, final Point ... points) {
        this.drawShape(fillColor, true, points);
    }

    /**
     * Draws a shape defined by a varying-size array of {@link Point} objects, filling the shape's color with the
     * provided fillColor. Point colors default to black.
     * @param points the points which should make up some shape.
     */
    public void drawShapeNoFill(final Point ... points) {
        this.drawShapeNoFill(Colors.BLACK, true, points);
    }


    /**
     * Draws a shape defined by a varying-size array of {@link Point} objects, filling the shape's color with the
     * provided fillColor.
     * @param fillColor what color to paint the object.
     * @param points the points which should make up some shape.
     */
    public void drawShape(final Colors fillColor, final Colors pointColor, final Point ... points) {
        this.drawShape(fillColor, true, points);
    }

    public void drawShapeNoFill(final Colors lineColor, final Point ... points) {
        this.drawShapeNoFill(lineColor, true, points);
    }

    /**
     * Draws a shape defined by a varying-size array of {@link Point} objects, filling the shape's color with the
     * provided fillColor.
     * @param fillColor what color to paint the object.
     * @param drawPoints true -> draw all of the vertices of the shape; false -> just draw the shape.
     * @param points the points which should make up some shape.
     */
    public void drawShape(final Colors fillColor, final boolean drawPoints, final Point ... points) {
        this.applet.fill(this.applet.color(fillColor.getRgb()));
        this.applet.beginShape();

        for (Point point : points) {
            this.plotPoint(point);
        }

        this.applet.endShape(CLOSE);
        if (drawPoints) this.drawPoints(Colors.BLACK, points);
    }

    /**
     * Draws a shape defined by a varying-size array of {@link Point} objects, filling the shape's color with the
     * provided fillColor.
     * @param shapeColor what color to paint the lines and points.
     * @param drawPoints true -> draw all of the vertices of the shape; false -> just draw the shape.
     * @param points the points which should make up some shape.
     */
    public void drawShapeNoFill(final Colors shapeColor, final boolean drawPoints, final Point ... points) {
        this.applet.noFill();
        this.applet.beginShape();

        Point first = points[0];
        for (int i = 0; i < points.length; i++) {
            Point point = points[i];

            this.plotPoint(point);

            if (i != 0) {
                this.drawLine(first, point, shapeColor);
            } else {
                this.drawLine(points[0], points[points.length - 1], shapeColor);
            }
            first = point;
        }

        this.applet.endShape(CLOSE);
        if (drawPoints) this.drawPoints(shapeColor, points);
        this.applet.noStroke();
    }

    /**
     * Draws a shape defined by a varying-size array of {@link Point} objects, filling the shape's color with the
     * provided fillColor.
     * @param drawPoints true -> draw all of the vertices of the shape; false -> just draw the shape.
     * @param points the points which should make up some shape.
     */
    public void drawShapeNoFill(final boolean drawPoints, final Point ... points) {
        this.applet.noFill();
        this.applet.beginShape();

        Point first = points[0];
        for (int i = 0; i < points.length; i++) {
            Point point = points[i];

            this.plotPoint(point);

            if (i != 0) {
                this.drawLine(first, point);
            } else {
                this.drawLine(points[0], points[points.length - 1]);
            }
            first = point;
        }

        this.applet.endShape(CLOSE);
        if (drawPoints) this.drawPoints(Colors.BLACK, points);
    }

    /**
     * Draws a given set of {@link Point} objects onto the canvas.
     * @param fillColor the color to paint the points.
     * @param points the points to draw onto the canvas.
     */
    public void drawPoints(final Colors fillColor, final Point ... points) {
        this.applet.fill(this.applet.color(fillColor.getRgb()));
        for (Point point : points) {
            this.drawPoint(point);
        }
    }

    /**
     * Draws a line segment to the canvas using the given line color, point color and set of points.
     * @param lineColor color to draw the line segment.
     * @param pointColor color to make the points.
     * @param points the points that make up the line-segment.
     */
    public void drawLineSegments(final Colors lineColor, final Colors pointColor, final Point ... points) {
        this.applet.fill(this.applet.color(pointColor.getRgb()));
        this.applet.stroke(this.applet.color(lineColor.getRgb()));
        for (int i = 0; (i + 1) < points.length; i++) {
            this.drawPoint(points[i]);
            this.drawPoint(points[i + 1]);
            this.drawLine(points[i], points[i + 1]);
        }
        this.applet.noFill();
    }

    /**
     * Draws a line-segment to the canvas. By default, it does not draw the physical {#link Point objects}.
     * @param lineColor the color of the line to be drawn.
     * @param points the points that make up the line segment.
     */
    public void drawLineSegments(final Colors lineColor, final Point ... points) {
        this.drawLineSegments(lineColor, false, points);
    }

    /**
     * Draws a line-segment to the canvas. Uses the provided lineColor as the color to draw the line-segment,
     *
     * @param lineColor the color of the line to be drawn.
     * @param drawPoints whether or not to draw the points provided.
     * @param points the points to be drawn.
     */
    public void drawLineSegments(final Colors lineColor, final Boolean drawPoints, final Point ... points) {
        this.applet.stroke(this.applet.color(lineColor.getRgb()));
        for (int i = 0; (i + 1) < points.length; i++) {
            if (drawPoints) {
                this.drawPoint(points[i]);
                this.drawPoint(points[i + 1]);
            }
            this.drawLine(points[i], points[i + 1], lineColor);
        }
    }

    /**
     * Default line-segment draw. This draws the line-segment with the color {@link Colors#BLACK}, and does not draw
     * the physical points.
     * @param points the points which make up the line-segment.
     */
    public void drawLineSegments(final Point ... points) {
        this.drawLineSegments(Colors.BLACK, points);
    }

    /* Circle3D draws */

    /**
     * Draws a circle on to the canvas.
     * @param circle the circle to draw.
     */
    public void drawCircle(final Circle2D circle) {
        Point point = circle.center;
        float radius = circle.radius;
        this.applet.ellipse(point.x, point.y, 2 * radius, 2 * radius);
    }

    /* Canvas/Mouse related utilities */

    /**
     * Creates a new {@link Point} which represents the current mouse location.
     * @return a new {@link Point} at the current mouse location.
     */
    public Point mouseLoc() {
        return new Point(this.applet.mouseX, this.applet.mouseY);
    }

    /**
     * Creates a new {@link Point} which represents the most previous mouse location.
     * @return a new {@link Point} at the previous mouse location.
     */
    public Point lastMouseLoc() {
        return new Point(this.applet.pmouseX, this.applet.pmouseY);
    }

    /**
     * Sets the provided {@link Point}'s x and y coordinates to the current mouse location.
     * @param point the {@link Point} to be set.
     */
    public void mouseLoc(final Point point) {
        point.x = this.applet.mouseX;
        point.y = this.applet.mouseY;
    }

    /**
     * Sets the provided {@link Point}'s x and y coordinates to the most previous mouse location.
     * @param point the {@link Point} to be set.
     */
    public void lastMouseLoc(final Point point) {
        point.x = this.applet.pmouseX;
        point.y = this.applet.pmouseY;
    }

    /**
     * Creates a {@link Vector} which represents the displacement between the current mouse location and the mose
     * recent known previous mouse location.
     * @return a "mouse-dragged" {@link Vector} vector.
     */
    public Vector mouseDraggedVector() {
        float mouseX = this.applet.mouseX, mouseY = this.applet.mouseY;
        float pMouseX = this.applet.pmouseX, pMouseY = this.applet.pmouseY;

        return new Vector(mouseX - pMouseX, mouseY - pMouseY);
    }

    /**
     * Adds a displacement of the mouse dragged vector to a given point.
     * @param point the {@link Point} to add displacement to.
     */
    public void dragMouse(final Point point) {
        point.x += this.applet.mouseX - this.applet.pmouseX;
        point.y += this.applet.mouseY - this.applet.pmouseY;
    }

    /**
     * Returns a {@link Point} representation of the closest point to the current mouse location.
     * @return a new {@link Point} which is the closest point to the mouse, inside the canvas.
     */
    public Point nearestMouseInWindow() {
        float mouseX = this.applet.mouseX, mouseY = this.applet.mouseY;
        float x = max(mouseX, 0), y = max(mouseY, 0);
        x = max(x, this.applet.width);
        y = max(y, this.applet.height);
        return new Point(x, y);
    }

    /**
     * Gets the center of the canvas as a {@link Point}.
     * @return the {@link Point} center of the canvas.
     */
    public Point canvasCenter() {
        return new Point(this.applet.width / 2, this.applet.height /2);
    }

    /**
     * Checks to see if the current mouse location is inside the bounds of the canvas.
     * @return true if the mouse is inside the canvas, else false.
     */
    public Boolean mouseOnCanvas() {
        float mouseX = this.applet.mouseX, mouseY = this.applet.mouseY;
        float height = this.applet.height, width = this.applet.width;
        return (((mouseX > 0) && (mouseX < width)) && ((mouseY > 0) && (mouseY < height)));
    }

    /**
     * Returns the Euclidean distance between the given point and the current moust location.
     * @param point some arbitrary {@link Point}.
     * @return the Euclidean distance between the point and the current mouse loc.
     */
    public float distanceToMouse(final Point point) {
        return (sqrt(sq(point.x - this.applet.mouseX) + sq(point.y - this.applet.mouseY)));
    }

    /* Texture mapping: */

    /**
     * Maps a circle represented by a source point location on the image to the canvas via a destination point, a radius
     * and a 'precision', or how many slices of PI to use for the circle.
     * @param source a source point with respect to the provided {@link PImage} texture.
     * @param destination the point to begin mapping to.
     * @param texture the text to map off of.
     * @param radius how large the mapped texture should appear on the canvas.
     */
    public void mapCircleTexture(final Point source, final Point destination, final PImage texture, final float radius, final float precision) {
        this.applet.beginShape();
        this.applet.texture(texture);
        for (float angle = -PI; angle < PI; angle += PI / precision) {
            float x = source.x + (radius * cos(angle));
            float y = source.y + (radius * sin(angle));
            this.applet.vertex(x, y, destination.x + (radius * cos(angle)), destination.y + (radius * sin(angle)));
        }
        this.applet.endShape(CLOSE);
    }

    /* Helper methods: */

    /**
     * Sets the stroke color of the canvas pen to a given {@link Colors} object.
     * @param color a color to set the stroke to.
     */
    public void setStrokeColor(final Colors color) {
        this.applet.stroke(color.getRgb());
    }

    /**
     * Sets the stroke weight of the canvas pen to a given number.
     * @param weight a pen stroke weight.
     */
    public void setStrokeWeight(final float weight) {
        this.strokeWeight = weight;
        this.applet.strokeWeight(this.strokeWeight);
    }

    /**
     * Sets the color and weight of the canvas pen.
     * @param color a {@link Colors} object.
     * @param weight a pen weight.
     */
    public void setStrokeColorAndWeight(final Colors color, final float weight) {
        this.setStrokeColor(color);
        this.setStrokeWeight(weight);
    }

    /**
     * Sets the fill color of the canvas paintbrush to a given {@link Colors} object.
     * @param color the color to set the canvas paintbrush.
     */
    public void setFillColor(final Colors color) {
        this.applet.fill(color.getRgb());
    }
}
