package pcanvas.geom;

import java.util.ArrayList;
import java.util.List;

import Jama.Matrix;
import pcanvas.Circle3D;
import pcanvas.Point;
import pcanvas.Vector;

import static pcanvas.Vector.crossProduct;
import static pcanvas.Vector.dotProduct;
import static processing.core.PApplet.abs;
import static processing.core.PApplet.atan2;
import static processing.core.PApplet.cos;
import static processing.core.PApplet.degrees;
import static processing.core.PApplet.radians;
import static processing.core.PApplet.sin;
import static processing.core.PApplet.sq;
import static processing.core.PApplet.sqrt;
import static processing.core.PConstants.HALF_PI;
import static processing.core.PConstants.PI;

/**
 * Geometric utility functions for {@link Point}s and {@link Vector}s.
 */
public class GeomUtils {

    /**
     * Enum which represents the three origin-axes unit vectors.
     */
    public enum UnitVectors {
        /** Origin x-axis unit vector. */
        I(new Vector(1, 0, 0)),

        /** Origin y-axis unit vector. */
        J(new Vector(0, 1, 0)),

        /** Origin z-axis unit vector. */
        K(new Vector(0, 0, 1));

        /**
         * Checks if the provided vector is one of these unit vectors. If not, it returns null.
         * @param vec the vector to check for unit-axis properties.
         * @return a {@link UnitVectors} axis if it checks out, else null.
         */
        public static UnitVectors get(final Vector vec) {
            if (vec.equals(I.getVector()) || vec.clone().scale(-1).equals(I.getVector())) {
                return I;
            } else if (vec.equals(J.getVector()) || vec.clone().scale(-1).equals(J.getVector())) {
                return J;
            } else if (vec.equals(K.getVector()) || vec.clone().scale(-1).equals(K.getVector())) {
                return K;
            } else {
                return null;
            }
        }

        /** The underlying vector representation of the unit axes. */
        private final Vector vector;

        /**
         * Basic, necessary enum constructor. Takes a {@link Vector} to represent the underlying vector representation
         * of a unit-axis.
         * @param vector the underlying unit {@link Vector}.
         */
        private UnitVectors(final Vector vector) {
            this.vector = vector;
        }

        /**
         * Retrieves the underlying unit {@link Vector}.
         * @return this enum's {@link Vector}.
         */
        public Vector getVector() {
            return this.vector;
        }
    }
    /* Vector Functions. */

    /**
     * Creates a new {@link Vector} instance which represents B - A.
     * @param A some point.
     * @param B some other point.
     * @return a new {@link Vector}, AB (or B-A).
     */
    public static Vector Vector(final Point A, final Point B) {
        return new Vector(A, B);
    }

    /**
     * Basic {@link Vector} sum.
     * @param vectors the vectors to sum.
     * @return a new {@link Vector} which represents the given vector's sum.
     */
    public static Vector VectorSum(final Vector ... vectors) {
        float avgX = 0, avgY = 0, avgZ = 0;
        for (Vector each : vectors) {
            avgX += each.x;
            avgY += each.y;
            avgZ += each.z;
        }
        return new Vector(avgX, avgY, avgZ);
    }

    /**
     * Basic {@link Vector} average.
     * @param vectors the vectors to average.
     * @return a new {@link Vector} which represnts the average of the given vectors.
     */
    public static Vector VectorAverage(final Vector ... vectors) {
        float avgX = 0, avgY = 0, avgZ = 0;
        float length = vectors.length;
        for (Vector each : vectors) {
            avgX += each.x;
            avgY += each.y;
            avgZ += each.z;
        }
        return new Vector(avgX / length, avgY/ length, avgZ / length);
    }

    /**
     * Rotates a vector about a {@link UnitVectors} axis.
     * @param vector the vector to rotate.
     * @param axis the unit vector to rotate about.
     * @param angle the angle of rotation in radians.
     * @return the rotated vector.
     */
    public static Vector VectorRotate3D(final Vector vector, final UnitVectors axis, final float angle) {
        float x, y, z;
        switch (axis) {
            case I:
                y = cos(angle) * vector.y - sin(angle) * vector.z;
                z = sin(angle) * vector.y + cos(angle) * vector.z;
                return new Vector(vector.x, y, z);
            case J:
                z = cos(angle) * vector.z + sin(angle) * vector.x;
                x = sin(angle) * vector.z + cos(angle) * vector.x;
                return new Vector(x, vector.y, z);
            case K:
                x = cos(angle) * vector.x - sin(angle) * vector.y;
                y = sin(angle) * vector.x + cos(angle) * vector.y;
                return new Vector(x, y, vector.z);
            default:
                return VectorRotate3D(vector, UnitVectors.I, angle);

        }
    }

    /**
     * Same as {@link #VectorRotate3D(Vector, UnitVectors, float)} except the angle is provided in degrees.
     * @param vector the vector to rotate.
     * @param axis the unit vector to rotate about.
     * @param angle the angle of rotation in degrees.
     * @return the rotated vector.
     */
    public static Vector VectorRotateByDegrees3D(final Vector vector, final UnitVectors axis, final float angle) {
        return VectorRotate3D(vector, axis, radians(angle));
    }

    /**
     * Rotates a 2D vector.
     * @param vector the vector to rotate.
     * @param angle the angle of rotation, in radians.
     * @return the rotated vector.
     */
    public static Vector VectorRotate2D(final Vector vector, final float angle) {
        float dx = vector.x, dy = vector.y;
        float c = cos(angle), s = sin(angle);

        return new Vector((dx * c) - (dy * s),
                          (dx * s) + (dy * c),
                          vector.z);
    }

    /**
     * Same as {@link #VectorRotate2D(Vector, float)} except the angle is provided in degrees.
     * @param vector the vector to rotate.
     * @param angle the angle of rotation, in degrees.
     * @return the rotated vector.
     */
    public static Vector VectorRotateByDegrees2D(final Vector vector, final float angle) {
        return VectorRotate2D(vector, radians(angle));
    }

    /**
     * Performs a scalar triple (mixed) product.
     * @param U a vector.
     * @param V a vector.
     * @param W a vector.
     * @return the result of a scalar triple product of these three vectors.
     */
    public static float ScalarTripleProduct(final Vector U, final Vector V, final Vector W) {
        return dotProduct(U, crossProduct(V, W));
    }

    /**
     * Performs a vector triple (mixed) product.
     * @param U a vector.
     * @param V a vector.
     * @param W a vector.
     * @return the result of a vector triple product of these three vectors.
     */
    public static Vector VectorTripleProduct(final Vector U, final Vector V, final Vector W) {
        return crossProduct(U, crossProduct(V, W));
    }

    /**
     * "Turns the vector left". This isn't exactly a proper function, as left and right rotations are much more complex
     * when there is a z-component to the vector. For 3D vector "turning" (rotations), see {@link #PointRotate3D(Point, Point, Vector, float)}
     * and use two points to act as the vector needing to be turned.
     * @param vector the vector to "turn left".
     * @return a vector "turned left".
     */
    public static Vector VectorTurnedLeft(final Vector vector) {
        return new Vector(-vector.y, vector.x);
    }

    /* Point Functions. */

    /**
     * Returns the distance between two points.
     * @param start a start point.
     * @param end an end point.
     * @return the distance between start and end.
     */
    public static float PointDistance(final Point start, final Point end) {
        return (sqrt(sq(end.x - start.x) + sq(end.y - start.y) + sq(end.z - start.z)));
    }

    /**
     * Returns the distance between a point and a line-segment defined by two points..
     * @param point the point.
     * @param lineStart beginning of a line-segment.
     * @param lineEnd end of a line-segment.
     * @return the distance from point to the line defined by lineStart and lineEnd.
     */
    public static float PointDistanceToLine(final Point point, final Point lineStart, final Point lineEnd) {
        Vector rotated = VectorTurnedLeft(Vector(lineStart, lineEnd).normal());
        Vector base = Vector(point, lineStart);
        return abs(dotProduct(base, rotated));
    }

    /**
     * Rotates a point about another point. This is primarily only useful for 2D point rotations.
     * <br />
     * <br />
     * For 3D rotations, see {@link #PointRotate3D(Point, Point, Vector, float)}.
     * @param origin a point to rotate about.
     * @param toRotate the point to rotate.
     * @param angle an angle of rotation, in radians.
     * @return the rotated point.
     */
    public static Point PointRotate(final Point origin, final Point toRotate, final float angle) {
        float dx = toRotate.x - origin.x, dy = toRotate.y - origin.y;
        float c = cos(angle), s = sin(angle);
        float rotatedX = origin.x + (c * dx) - (s * dy), rotatedY = origin.y + (s * dx) + (c * dy);
        return new Point(rotatedX, rotatedY);
    }

    /**
     * Same as {@link #PointRotate(Point, Point, float)}, except the angle is in degrees.
     * @param origin a point to rotate about.
     * @param toRotate the point to rotate.
     * @param angle an angle of rotation, in degrees.
     * @return the rotated point.
     */
    public static Point PointRotateByDegrees(final Point origin, final Point toRotate, final float angle) {
        return PointRotate(origin, toRotate, radians(angle));
    }

    /**
     * Performs a point rotation about an arbitrary axis. Unfortunately this does not use quaternions, so deal with it. =)
     * @param origin an origin point.
     * @param toRotate the point to rotate.
     * @param rotationAxis the axis of rotation.
     * @param angle an angle of rotation, in radians.
     * @return a rotated point.
     */
    public static Point PointRotate3D(final Point origin, final Point toRotate, final Vector rotationAxis, final float angle) {        
        Vector axis = (rotationAxis.magnitude() == 1) ? rotationAxis : rotationAxis.normal();
        float dist = sqrt(sq(axis.y) + sq(axis.z));                                                
        Matrix T = new Matrix(new double[][]{
                new double[] {1, 0, 0, -origin.x},
                new double[] {0, 1, 0, -origin.y},
                new double[] {0, 0, 1, -origin.z},
                new double[] {0, 0, 0, 1},
        });
        Matrix TInv = T.inverse();

        float aZ = (axis.z == 0 && dist == 0) ? 0 : (axis.z / dist);
        float aY = (axis.y == 0 && dist == 0) ? 0 : (axis.y / dist);
        Matrix Rx = new Matrix(new double[][]{
                new double[] {1, 0, 0, 0},
                new double[] {0, aZ, -aY, 0},
                new double[] {0, aY, aZ, 0},
                new double[] {0, 0, 0, 1},
        });

        Matrix RxInv = new Matrix(new double[][]{
                new double[] {1, 0, 0, 0},
                new double[] {0, aZ, aY, 0},
                new double[] {0, -aY, aZ, 0},
                new double[] {0, 0, 0, 1},
        });

       Matrix Ry = new Matrix(new double[][]{
                new double[] {dist, 0, -axis.x, 0},
                new double[] {0, 1, 0, 0},
                new double[] {axis.x, 0, dist, 0},
                new double[] {0, 0, 0, 1},
        });

        Matrix RyInv = Ry.inverse();

        Matrix Rz = new Matrix(new double[][]{
                new double[] {cos(angle), -sin(angle), 0, 0},
                new double[] {sin(angle), cos(angle), 0, 0},
                new double[] {0, 0, 1, 0},
                new double[] {0, 0, 0, 1},
        });

        Matrix origSpace = new Matrix(new double[][] {
                new double[]{toRotate.x},
                new double[]{toRotate.y},
                new double[]{toRotate.z},
                new double[]{1}
        });
        Matrix rotated = TInv.times(RxInv).times(RyInv).times(Rz).times(Ry).times(Rx).times(T).times(origSpace);

        return new Point((float) rotated.get(0, 0), (float) rotated.get(1, 0), (float) rotated.get(2, 0));
    }

    /**
     * Rotates a point about a {@link UnitVectors} axis. Almost identical to {@link #VectorRotate3D(Vector, UnitVectors, float)} except
     * we're using points instead of vectors.
     * @param point the point to rotate.
     * @param axis the unit vector to rotate about.
     * @param angle the angle of rotation in radians.
     * @return the rotated vector.
     */
    public static Point PointRotate3D(final Point point, final UnitVectors axis, final float angle) {
        float x, y, z;
        switch (axis) {
            case I:
                y = cos(angle) * point.y - sin(angle) * point.z;
                z = sin(angle) * point.y + cos(angle) * point.z;
                return new Point(point.x, y, z);
            case J:
                z = cos(angle) * point.z + sin(angle) * point.x;
                x = sin(angle) * point.z + cos(angle) * point.x;
                return new Point(x, point.y, z);
            case K:
                x = cos(angle) * point.x - sin(angle) * point.y;
                y = sin(angle) * point.x + cos(angle) * point.y;
                return new Point(x, y, point.z);
            default:
                return null;

        }
    }

    /**
     * Returns the point of intersection of a two line-segments defined by four points. Currently this only works
     * for 2D points.
     * @param startA beginning of the first line-segment.
     * @param endA end of the first line-segment.
     * @param startB beginning of the second line-segment.
     * @param endB end of the second line-segment.
     * @return their point of intersection.
     */
    public static Point PointOfIntersection(final Point startA, final Point endA,
                                            final Point startB, final Point endB) {
        float x1 = startA.x, x2 = endA.x, x3 = startB.x, x4 = endB.x;
        float y1 = startA.y, y2 = endA.y, y3 = startB.y, y4 = endB.y;
        float denom = ((x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4));
        float intersectXNum = ((x1*y2 - y1*x2) * (x3 - x4)) - ((x1 - x2) * (x3*y4 - y3*x4));
        float intersectYNum = ((x1*y2 - y1*x2) * (y3 - y4)) - ((y1 - y2) * (x3*y4 - y3*x4));
        return new Point(intersectXNum / denom, intersectYNum / denom);
    }

    /**
     * Calculates the {@link Point} which represents the sum of the provided points.
     * @param points the points to sum.
     * @return a point representing their sum.
     */
    public static Point PointSum(final Point ... points) {
        float avgX = 0, avgY = 0, avgZ = 0;
        for (Point each : points) {
            avgX += each.x;
            avgY += each.y;
            avgZ += each.z;
        }
        return new Point(avgX, avgY, avgZ);
    }

    /**
     * Calculates the {@link Point} which represents the average of the provided points.
     * @param points the points to average.
     * @return a point representing their average.
     */
    public static Point PointAverage(final Point ... points) {
        float avgX = 0, avgY = 0, avgZ = 0;
        float length = points.length;
        for (Point each : points) {
            avgX += each.x;
            avgY += each.y;
            avgZ += each.z;
        }
        return new Point(avgX / length, avgY / length, avgZ / length);
    }

    /* Orientation Tests */

    /**
     * Checks if a provided point is left of a line-segment defined by two other points.
     * @param point the point in question.
     * @param lineStart start of the line.
     * @param lineEnd end of the line.
     * @return true if the point is "left" of the line, else false.
     */
    public static Boolean PointIsLeftOf(final Point point, final Point lineStart, final Point lineEnd) {
        Vector PS = Vector(lineStart, point), PQ = Vector(lineStart, lineEnd);
        return (dotProduct(PS, PQ) > 0);
    }

    /**
     * Checks if a provided point is left of a line-segment defined by two other points, within some provided range
     * of error.
     * @param point the point in question.
     * @param lineStart start of the line.
     * @param lineEnd end of the line.
     * @param error the error the point can be within.
     * @return true if the point is "left" of the line, else false.
     */
    public static Boolean PointIsLeftOfWithError(final Point point, final Point lineStart, final Point lineEnd, final float error) {
        Vector PS = Vector(lineStart, point), PQ = Vector(lineStart, lineEnd);
        return (dotProduct(PS, PQ) > error);
    }
    
    /* Inclusion Tests */

    /**
     * Checks if a point is inside a circle defined by another point and a radius.
     * @param point the point in question.
     * @param center the center of a circle.
     * @param radius the radius of the circle.
     * @return true if the point is inside, else false.
     */
    public static Boolean PointInCircle(final Point point, final Point center, final float radius) {
        return (PointDistance(point, center) <= radius);
    }

    /**
     * Checks to see if a point is inside a triangle defined by three separate points.
     * @param point the point in question.
     * @param A triangle vertex.
     * @param B triangle vertex.
     * @param C triangle vertex.
     * @return true if the point is inside the bounds of the triangle, else false.
     */
    public static Boolean PointInTriangle(final Point point, final Point A, final Point B, final Point C) {
        Boolean a = PointIsLeftOf(point, B, C), b = PointIsLeftOf(point, C, A), c = PointIsLeftOf(point, A, B);
        return ((a && b && c) || (!a && !b && !c));
    }
    
    /* Miscellaneous Arithmetic and geometric object observations. */

    /**
     * Calculates the angle between two vectors.
     * @param vecA a vector.
     * @param vecB some other vector.
     * @return the angle between vecA and vecB, in radians.
     */
    public static float AngleBetweenVectors(final Vector vecA, final Vector vecB) {
        Vector rotated = VectorRotate2D(vecA, HALF_PI);
        return atan2(dotProduct(rotated, vecB), dotProduct(vecA, vecB));
    }

    /**
     * Calculates the angle between two vectors.
     * @param vecA a vector.
     * @param vecB some other vector.
     * @return the angle between vecA and vecB, in degrees.
     */
    public static float AngleBetweenVectorsInDegrees(final Vector vecA, final Vector vecB) {
        return degrees(AngleBetweenVectors(vecA, vecB));
    }

    /**
     * Returns the 'angle' of a vector. This only really make sense in 2-dimensions.
     * @param vector the vector we need the angle of.
     * @return the angle of the vector, in radians.
     */
    public static float AngleOfVector(final Vector vector) {
        return atan2(vector.x, vector.y);
    }

    /**
     * Returns the 'angle' of a vector. This only really make sense in 2-dimensions.
     * @param vector the vector we need the angle of.
     * @return the angle of the vector, in degrees.
     */
    public static float AngleOfVectorInDegrees(final Vector vector) {
        return degrees(AngleOfVector(vector));
    }

    public static Point[] ComputeArc2D(final Point center, final Point leftBound, final Point rightBound, final Integer precision) {
        List<Point> arcPoints = new ArrayList<Point>();
        float boundingAngle = abs(AngleBetweenVectors(new Vector(center, leftBound), new Vector(center, rightBound)));
        if ((rightBound.y-leftBound.y)*(center.x-leftBound.x)>(rightBound.x-leftBound.x)*(center.y-leftBound.y)) {
            boundingAngle = 2 * PI  - boundingAngle;
        }

        float angle = 0;
        while (angle < boundingAngle) {
            Point newPoint = PointRotate(center, leftBound, angle);
            arcPoints.add(newPoint);
            angle += (boundingAngle / (precision - 1));
        }
        //arcPoints.remove(arcPoints.size() - 1);
        if (arcPoints.size() == precision) {
            arcPoints.remove(arcPoints.size() - 1);
        }
        arcPoints.add(rightBound);

        return arcPoints.toArray(new Point[arcPoints.size()]);
    }

    /**
     * Given two sets of uniform points (here called 'top' and 'bottom', but they could be in any orientation to
     * each other), this function will return a List of {@link Circle3D} objects which represent a circular 'skeleton'
     * based on the given points.
     * @param topPoints the first set of points.
     * @param bottomPoints the second set of points
     * @return a list of Circle3D objects based on the given uniform point sets.
     */
    public static List<Circle3D> ComputeCirclesBetween(
            final List<Point> topPoints, final List<Point> bottomPoints) {
        if (topPoints.size() != bottomPoints.size()) {
            throw new IllegalArgumentException("Cannot compute circle skeleton with non-uniform point sets!");
        }
        List<Circle3D> circles = new ArrayList<Circle3D>();
        for (int i = 0; i < Math.min(topPoints.size(), bottomPoints.size()); i++) {
            Point right = bottomPoints.get(i);
            Point left = topPoints.get(i);
            Point center = PointAverage(left, right);

            Vector lVec = new Vector(left, center);
            Vector axis = VectorRotate2D(lVec, -PI / 2).normal();
            circles.add(new Circle3D(center, axis, lVec.magnitude(), 20));
        }
        return circles;
    }

    


    /* Utility/Debugging */

    /**
     * Internal utility function used for debugging purposes. Prints a {@link Matrix} in a nice, neat looking text format.
     * @param m the Matrix to print.
     */
    private static void printMatrix(final Matrix m) {
        System.out.println("====== BEGIN MATRIX ======");
        for (int i = 0; i < m.getRowDimension(); i++ ){
            for (int j = 0; j < m.getColumnDimension(); j++) {
                String comma = (j == m.getColumnDimension() - 1) ? " " : ", ";
                System.out.print(m.get(i, j) + comma);
            }
            System.out.println("");
        }
        System.out.println("====== END MATRIX ======");
    }
}
