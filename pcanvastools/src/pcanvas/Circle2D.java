package pcanvas;

import java.util.ArrayList;
import java.util.List;

import static pcanvas.Point.translatePoint;
import static pcanvas.geom.GeomUtils.PointRotate;
import static processing.core.PConstants.PI;

/**
 * Basic 2D circle. Uses a radius and/or precision (number of points on the circle) to generate circle points.
 */
public class Circle2D {
    /** Center of the circle. */
    public final Point center;

    /** Points on this circle. */
    public final List<Point> points;

    /** The circle's radius. */
    public final float radius;

    /**
     * Creates a circle based on a center and a radius.
     * @param center the circle center.
     * @param radius the radius of the circle.
     */
    public Circle2D(final Point center, final float radius) {
        this.center = center;
        this.radius = radius;
        this.points = this.computePointsFromRadius(radius);
    }

    /**
     * Creates a circle based on a radius, center and number of points on the circle.
     * @param center the circle center.
     * @param radius the radius of the circle.
     * @param precision the number of the points that should be on a circle.
     */
    public Circle2D(final Point center, final float radius, final int precision) {
        this.center = center;
        this.radius = radius;
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
        Point start = translatePoint(center, new Vector(radius, 0, 0));
        for (float angle = 0; angle < 2 * PI; angle += PI / precision) {
            points.add(PointRotate(this.center, start, angle));
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
