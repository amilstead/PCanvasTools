package pcanvas;

import java.util.ArrayList;
import java.util.List;

import static pcanvas.Point.translatePoint;
import static pcanvas.geom.GeomUtils.PointRotate3D;
import static pcanvas.geom.GeomUtils.VectorRotate2D;
import static processing.core.PConstants.PI;

/**
 * Basic 3D circle. Uses an axis of rotation in order to compute an accurate representation of the circle.
 */
public class Circle3D {
    /** Center of the circle. */
    public final Point center;

    /** Points on this circle. */
    public final List<Point> points;

    /** The circle's radius. */
    public final float radius;

    /** This circle's axis of rotation. */
    public final Vector axis;



    /**
     * Creates a circles from a center, an axis of rotation and a radius. The default precision (number of points which
     * make up the circle) value is 20.
     * @param center center of the circle.
     * @param axis circle's axis of rotation.
     * @param radius radius of the circle.
     */
    public Circle3D(final Point center, final Vector axis, final float radius) {
        this.center = center;
        this.radius = radius;
        this.axis = axis;
        this.points = this.computePointsFromRadius(radius);
    }

    /**
     * Creates a circles from a center, an axis of rotation and a radius. The default precision (number of points which
     * make up the circle) value is 20.
     * @param center center of the circle.
     * @param axis circle's axis of rotation.
     * @param radius radius of the circle.
     * @param precision the number of points on this circle.
     */
    public Circle3D(final Point center, final Vector axis, final float radius, final int precision) {
        this.center = center;
        this.radius = radius;
        this.axis = axis;
        this.points = this.computePointsWithPrecision(radius, precision);
    }

    /**
     * Basic circle point computation. Uses a radius and a precision.
     * @param radius the radius of the circle.
     * @param precision the number of points on the circle.
     * @return the generated circle points.
     */
    private List<Point> computePointsWithPrecision(final float radius, final int precision) {
        List<Point> points = new ArrayList<Point>();
        Vector translator = VectorRotate2D(this.axis, -PI / 2).normal().scale(radius);
        Point start = translatePoint(center, translator);
        for (float angle = 0; angle < 2 * PI; angle += PI / precision) {
            points.add(PointRotate3D(this.center, start, this.axis, angle));
        }
        return points;
    }

    /**
     * Same as {@link #computePointsWithPrecision(float, int)} -- defaults precision to 20.
     * @param radius the radius of this circle.
     * @return the generated circle points.
     */
    private List<Point> computePointsFromRadius(final float radius) {
        return this.computePointsWithPrecision(radius, 20);
    }
}
