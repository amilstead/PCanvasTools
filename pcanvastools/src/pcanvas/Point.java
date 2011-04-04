package pcanvas;

import pcanvas.geom.GeomUtils;

import static processing.core.PApplet.cos;
import static processing.core.PApplet.radians;
import static processing.core.PApplet.sin;

/**
 * Basic Point class. Contains three floats; x, y and z, which represent x, y and z coordinates (respectively) on some
 * coordinate system.
 */
public class Point {
    /** The x-coordinate of this Point. */
    public float x ;

    /** The y-coordinate of this Point. */
    public float y;

    /** The z-coordinate of this Point. */
    public float z;

    /**
     * Basic constructor which takes an x and y coordinate value.
     * @param x x-coordinate.
     * @param y y-coordinate.
     */
    public Point(final float x, final float y) {
        this.x = x;
        this.y = y;
        this.z = 0.0F;
    }

    /**
     * Basic constructor which takes an x, y and z coordinate value.
     * @param x x-coordinate.
     * @param y y-coordinate.
     * @param z z-coordinate.
     */
    public Point(final float x, final float y, final float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Arithmetic point addition. Takes an x and y value to add to the current coordinates.
     * @param x x value to add.
     * @param y y value to add.
     * @return this updated Point instance.
     */
    public Point add(final float x, final float y) {
        this.x += x;
        this.y += y;
        return this;
    }

    /**
     * Arithmetic point addition. Takes an x, y and z value to add to the current coordinates.
     * @param x x value to add.
     * @param y y value to add.
     * @Param z z value to add.
     * @return this updated Point instance.
     */
    public Point add(final float x, final float y, final float z) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    /**
     * Arithmetic point addition. Add the respective x, y and z components of the other point to the values in this
     * point.
     * @param point the point to be added.
     * @return this updated Point instance.
     */
    public Point add(final Point point) {
        return this.add(point.x, point.y, point.z);
    }

    /**
     * Arithmetic point subtraction. Subtract the respective x, y and z components of the other point from the values
     * in this point.
     * @param point the point to subtract.
     * @return this updated Point instance.
     */
    public Point subtract(final Point point) {
        return this.subtract(point.x, point.y, point.z);
    }

    /**
     * Arithmetic point subtraction. Takes an x and y value and subtracts them from this point's respective coordinate
     * values.
     * @param x the x value to be subtracted.
     * @param y the y value to be subtracted.
     * @return this updated Point instance.
     */
    public Point subtract(final float x, final float y) {
        this.x -= x;
        this.y -= y;
        return this;
    }

    /**
     * Arithmetic point subtraction. Takes an x, y and z value and subtracts them from this point's respective coordinate
     * values.
     * @param x the x value to be subtracted.
     * @param y the y value to be subtracted.
     * @param z the z value to be subtracted.
     * @return this updated Point instance.
     */
    public Point subtract(final float x, final float y, final float z) {
        this.x -= x;
        this.y -= y;
        this.z -= z;
        return this;
    }

    /**
     * Arithmetic point multiplication. Scales the x, y and z coordinate values of this Point by the given scalar.
     * @param scalar a float scalar.
     * @return this updated Point instance.
     */
    public Point mult(final float scalar) {
        return this.scale(scalar, scalar, scalar);
    }

    /**
     * Arithmetic point multiplication. Scales the x and y coordinate values of this Point the respective parameter
     * values.
     * @param x the value to scale this Point's x-coordinate value by.
     * @param y the value to scale this Point's y-coordinate value by.
     * @return this updated Point instance.
     */
    public Point mult(final float x, final float y) {
        return this.scale(x, y);
    }

    /**
     * Arithmetic point multiplication. Scales the x, y and z coordinate values of this Point the respective parameter
     * values.
     * @param x the value to scale this Point's x-coordinate value by.
     * @param y the value to scale this Point's y-coordinate value by.
     * @param z the value to scale this Point's y-coordinate value by.
     * @return this updated Point instance.
     */
    public Point mult(final float x, final float y, final float z) {
        return this.scale(x, y, z);
    }

    /**
     * Scales this Point by the provided x and y values.
     * @param x value to scale this Point's x-coordinate value by.
     * @param y value to scale this Point's y-coordinate value by.
     * @return this updated Point instance.
     */
    public Point scale(final float x, final float y) {
        this.x *= x;
        this.y *= y;
        return this;
    }

    /**
     * Scales this Point by the provided x, y and z values.
     * @param x value to scale this Point's x-coordinate value by.
     * @param y value to scale this Point's y-coordinate value by.
     * @param z value to scale this Point's z-coordinate value by.
     * @return this updated Point instance.
     */
    public Point scale(final float x, final float y, final float z) {
        this.x *= x;
        this.y *= y;
        this.z *= z;
        return this;
    }

    /**
     * Arithmetic point division. Divides the x and y coordinate values by the respective parameter values.
     * @param x value to divide this Point's x-coordinate value by.
     * @param y value to divide this Point's y-coordinate value by.
     * @return this updated Point instance.
     */
    public Point divide(final float x, final float y) {
        this.x /= x;
        this.y /= y;
        return this;
    }

    /**
     * Arithmetic point division. Divides the x, y and z coordinate values by the respective parameter values.
     * @param x value to divide this Point's x-coordinate value by.
     * @param y value to divide this Point's y-coordinate value by.
     * @param z value to divide this Point's z-coordinate value by.
     * @return this updated Point instance.
     */
    public Point divide(final float x, final float y, final float z) {
        this.x /= x;
        this.y /= y;
        this.z /= z;
        return this;
    }

    /**
     * Arithmetic point division. Divides the x, y and z coordinate values of this Point by the provided divisor.
     * @param divisor the value to divide this Point's x, y and z values by.
     * @return this updated Point instance.
     */
    public Point divide(final float divisor) {
        return this.divide(divisor, divisor, divisor);
    }

    /**
     * Standard linear interpolation of a Point. Uses this point instance as the base point, for interoplation, and the
     * provided point object as the point to interpolate towards, using the provided scale.
     * @param point the point to interpolate towards.
     * @param scale the ratio of interpolation towards the provided point.
     * @return this Point instance after it has been interpolated.
     */
    public Point interpolate(final Point point, final float scale) {
        this.x += scale * (point.x - this.x);
        this.y += scale * (point.y - this.y);
        this.z += scale * (point.z - this.z);
        return this;
    }

    /**
     * Rotates this point about another point by a given angle. This is actually a bit mis-guided. If you want a real
     * 3D point rotate, please use {@link GeomUtils#PointRotate3D(Point, Point, Vector, float)}.
     * @param about which point to rotate this point about.
     * @param angle the angle of rotation, in radians.
     * @return this Point after it has been rotated to its destination.
     */
    @Deprecated
    public Point rotate(final Point about, final float angle) {
        float dx = (about.x - this.x), dy = (about.y - this.y);
        float c = cos(angle), s = sin(angle);
        this.x = about.x + (c * dx) - (s * dy);
        this.y = about.y + (s * dx) + (c * dy);
        return this;
    }

    /**
     * Same functionality as {@link Point#rotate(Point, float)} except the accepted angle value must be in degrees.
     * This is actually a bit mis-guided. If you want a real 3D point rotate, please use
     * {@link GeomUtils#PointRotate3D(Point, Point, Vector, float)}.
     * @param about which point to rotate this point about.
     * @param angle the angle of rotation, in degrees.
     * @return this Point after it has been rotated to its destination.
     */
    @Deprecated
    public Point rotateByDegrees(final Point about, final float angle) {
        return this.rotate(about, radians(angle));
    }

    /**
     * Translates this point by a displacement in the form of a {@link Vector}.
     * @param vector the {@link Vector} to displace this point by.
     * @return this Point after it has been displaced.
     */
    public Point translate(final Vector vector) {
        this.x += vector.x;
        this.y += vector.y;
        this.z += vector.z;
        return this;
    }

    /**
     * Translates this point by a displacement in the form of a {@link Vector} after that vector has been scaled by s.
     * @param s the amount to scale the provided {@link Vector}.
     * @param vector the vector to be scaled, then to displace this point by.
     * @return this Point after it has been displaced.
     */
    public Point scaledTranslate(final float s, final Vector vector) {
        return this.translate(Vector.scaleVector(vector, s));
    }

    /**
     * Sets this point's x, y and z-coordinates to the respective other point's coordinates.
     * @param other the point whose values this point will assume.
     * @return this Point after it has been updated to the new point's location.
     */
    public Point set(final Point other) {
        this.x = other.x;
        this.y = other.y;
        this.z = other.z;
        return this;
    }

    /**
     * Sets the x and y values of this point to the provided x and y values.
     * @param x the new x coordinate value.
     * @param y the new y coordinate value.
     * @return this Point instance with its new coordinate values.
     */
    public Point set(final float x, final float y) {
        this.x = x;
        this.y = y;
        return this;
    }

    /**
     * Sets the x, y and z values of this point to the provided x, y and z values.
     * @param x the new x coordinate value.
     * @param y the new y coordinate value.
     * @param z the new z coordinate value.
     * @return this Point instance with its new coordinate values.
     */
    public Point set(final float x, final float y, final float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    /* Static functions for immutable arithmetic. */

    /**
     * Arithmetic point addition. Returns a new point which represents the addition of point and other.
     * @param point some point.
     * @param other some other point.
     * @return a new Point which represents point + other.
     */
    public static Point addPoints(final Point point, final Point other) {
        return new Point(point.x + other.x, point.y + other.y, point.z + other.z);
    }

    /**
     * Arithmetic point sum. Takes a variable number of points and returns the representation of their sum.
     * @param points some points to sum.
     * @return a Point which represents the sum of the provided point array.
     */
    public static Point addPoints(final Point ... points) {
        float newX = 0, newY = 0, newZ = 0;
        for (Point point : points) {
            newX += point.x;
            newY += point.y;
            newZ += point.z;
        }
        return new Point(newX, newY, newZ);
    }

    /**
     * Arithmetic point addition. Represents point.x + x and point.y + y.
     * @param point a provided point.
     * @param x a value to add to the point's x-coordinate.
     * @param y a value to add to the point's y-coordinate.
     * @return a new Point instance which represents (point.x + x, point.y + y, point.z).
     */
    public static Point addToPoint(final Point point, final float x, final float y) {
        return new Point(point.x + x, point.y + y, point.z);
    }

    /**
     * Arithmetic point addition. Represents point.x + x and point.y + y.
     * @param point a provided point.
     * @param x a value to add to the point's x-coordinate.
     * @param y a value to add to the point's y-coordinate.
     * @param z a value to add to the point's z-coordinate.
     * @return a new Point instance which represents (point.x + x, point.y + y, point.z + z).
     */
    public static Point addToPoint(final Point point, final float x, final float y, final float z) {
        return new Point(point.x + x, point.y + y, point.z + z);
    }

    /**
     * Arithmetic point subtraction. Represents point - other.
     * @param point some point.
     * @param other a point to subtract the first parameter by.
     * @return a new Point instance which represents other - point.
     */
    public static Point subtractFromPoint(final Point point, final Point other) {
        return new Point(point.x - other.x, point.y - other.y, point.z - other.z);
    }

    /**
     * Arithmetic point subtraction. Represents point.x - x, point.y - y;
     * @param point a provided point.
     * @param x a value to be subtracted from point's x-coordinate.
     * @param y a vaule to be subtracted from point's y-coordinate.
     * @return a new Point instance which represents (point.x - x, point.y - y, point.z).
     */
    public static Point subtractFromPoint(final Point point, final float x, final float y) {
        return new Point(point.x - x, point.y - y, point.z);
    }

    /**
     * Arithmetic point subtraction. Represents point.x - x, point.y - y;
     * @param point a provided point.
     * @param x a value to be subtracted from point's x-coordinate.
     * @param y a vaule to be subtracted from point's y-coordinate.
     * @return a new Point instance which represents (point.x - x, point.y - y, point.z).
     */
    public static Point subtract(final Point point, final float x, final float y, final float z) {
        return new Point(point.x - x, point.y - y, point.z - z);
    }

    /**
     * Scales one point by another point, component-wise.
     * @param point some point to be scaled.
     * @param other some point to scale by.
     * @return a new Point instance which represents (point * other).
     */
    public static Point scalePoint(final Point point, final Point other) {
        return new Point(point.x * other.x, point.y * other.y, point.z * other.z);
    }

    /**
     * Scales some point by a scalar.
     * @param point the point to be scaled.
     * @param scalar the value to scale point's x, y an z coordinates by.
     * @return a new Point instance which represents (point.x * x, point.y * y, point.z * z).
     */
    public static Point scalePoint(final Point point, final float scalar) {
        return new Point(point.x * scalar, point.y * scalar, point.z * scalar);
    }

    /**
     * Scales some point by an xScale and yScale (respectively).
     * @param point the point to scale.
     * @param xScale the value to scale point's x-coordinate by.
     * @param yScale the value to scale point's y-coordinate by.
     * @return a new Point instance which represents (point.x * xScale, point.y * yScale, point.z).
     */
    public static Point scalePoint(final Point point, final float xScale, final float yScale) {
        return new Point(point.x * xScale, point.y * yScale);
    }

    /**
     * Scales some point by an xScale, yScale and zScale (respectively).
     * @param point the point to scale.
     * @param xScale the value to scale point's x-coordinate by.
     * @param yScale the value to scale point's y-coordinate by.
     * @return a new Point instance which represents (point.x * xScale, point.y * yScale, point.z * zScale).
     */
    public static Point scalePoint(final Point point, final float xScale, final float yScale, final float zScale) {
        return new Point(point.x * xScale, point.y * yScale, point.z * zScale);
    }

     /**
     * Arithmetic point multiplication. Multiplies one point by another point, component-wise.
     * @param point some point to be multiplied.
     * @param other some point to multiply by.
     * @return a new Point instance which represents (point * other).
     */
    public static Point multiplyPoints(final Point point, final Point other) {
        return scalePoint(point, other);
    }

    /**
     * Arithmetic point multiplication. Multiplies some point by a value.
     * @param point the point to be multiplied.
     * @param multiple the value to multiply point's x, y an z coordinates by.
     * @return a new Point instance which represents (point.x * multiple, point.y * multiple, point.z * multiple).
     */
    public static Point multiplyPoint(final Point point, final float multiple) {
        return scalePoint(point, multiple);
    }

    /**
     * Arithmetic point multiplication. Multiplies some point's x and y coordinates by provided x and y values.
     * @param point the point to be multiplied.
     * @param xMult the value to multiply point's x coordinate value by.
     * @param yMult the value to multiply point's y coordinate value by.
     * @return a new Point instance which represnts (point.x * xMult, point.y * yMult, point.z).
     */
    public static Point multiplyPoint(final Point point, final float xMult, final float yMult){
        return scalePoint(point, xMult, yMult);
    }

    /**
     * Arithmetic point multiplication. Multiplies some point's x, y and z coordinates by provided x, y and z values.
     * @param point the point to be multiplied.
     * @param xMult the value to multiply point's x coordinate value by.
     * @param yMult the value to multiply point's y coordinate value by.
     * @param zMult the value to multiply point's z coordinate value by.
     * @return a new Point instance which represents (point.x * xMult, point.y * yMult, point.z * zMult).
     */
    public static Point multiplyPoint(final Point point, final float xMult, final float yMult, final float zMult) {
        return scalePoint(point, xMult, yMult, zMult);
    }

    /**
     * Arithmetic point division. Creates a representation of the division of the provided point's x and y coordinates
     * by the provided x and y values.
     * @param point the point to divide.
     * @param x value to divide point's x coordinate value by.
     * @param y value to divide point's y coordinate value by.
     * @return a new Point instance which represents (point.x / x, point.y / y, point.z).
     */
    public static Point dividePoint(final Point point, final float x, final float y) {
        return new Point(point.x / x, point.y / y);
    }

    /**
     * Arithmetic point division. Creates a representation of the division of the provided point's x, y and z coordinates
     * by the provided x, y and z values.
     * @param point the point to divide.
     * @param x value to divide point's x coordinate value by.
     * @param y value to divide point's y coordinate value by.
     * @param z value to divide point's z coordinate value by.
     * @return a new Point instance which represents (point.x / x, point.y / y, point.z / z).
     */
    public static Point dividePoint(final Point point, final float x, final float y, final float z) {
        return new Point(point.x / x, point.y / y, point.z / z);
    }

    /**
     * Arithmetic point division. Creates a representation of the division of the given point by some divisor.
     * @param point the point to divide.
     * @param divisor what to divide the point by.
     * @return a new Point instance which represents (point / divisor).
     */
    public static Point dividePoint(final Point point, final float divisor) {
        return dividePoint(point, divisor, divisor, divisor);
    }

    /**
     * Performs a linear interpolation of point towards other by the ratio scale.
     * @param point the point to interpolate.
     * @param other the point ot interpolate towards.
     * @param scale the ratio of interpolation.
     * @return a new Point instance which represents the interpolation.
     */
    public static Point interpolate(final Point point, final Point other, final float scale) {
        float lerpX = point.x + scale * (other.x - point.x);
        float lerpY = point.y + scale * (other.y - point.y);
        float lerpZ = point.z + scale * (other.z - point.z);
        return new Point(lerpX, lerpY, lerpZ);
    }

    /**
     * Performs a rotation of a point about another point by some angle of rotation (in radians).
     * <br />
     * <br />
     *  This is actually a bit mis-guided. If you want a real 3D point rotate, please use
     * {@link GeomUtils#PointRotate3D(Point, Point, Vector, float)}.
     * @param point the point to rotate.
     * @param about the point to rotate about.
     * @param angle the angle of rotation.
     * @return a new Point which represents point rotated by angle around about.
     */
    @Deprecated
    public static Point rotatePoint(final Point point, final Point about, final float angle) {
        float dx = (about.x - point.x), dy = (about.y - point.y);
        float c = cos(angle), s = sin(angle);
        return new Point(about.x + (c * dx) - (s * dy), about.y + (s * dx) + (c * dy));
    }

    /**
     * Performs a rotation of a point about another point by some angle of rotation (in degrees).
     * <br />
     * <br />
     *  This is actually a bit mis-guided. If you want a real 3D point rotate, please use
     * {@link GeomUtils#PointRotate3D(Point, Point, Vector, float)}.
     * @param point the point to rotate.
     * @param about the point to rotate about.
     * @param angle the angle of rotation, in degrees.
     * @return a new Point which represents point rotated by angle around about.
     */
    @Deprecated
    public static Point rotatePointByDegrees(final Point point, final Point about, final float angle) {
        return rotatePoint(point, about, radians(angle));
    }

    /**
     * Creates the representation of a point translated by a vector.
     * @param point the point to translate.
     * @param translator the {@link Vector} to translated our point by.
     * @return a new Point instance which represents point translated by translator.
     */
    public static Point translatePoint(final Point point, final Vector translator) {
        return new Point(point.x + translator.x, point.y + translator.y, point.z + translator.z);
    }

    /**
     * Creates the representation of a scaled translation of a point by a vector.
     * @param point the point to translate.
     * @param translator the {@link Vector} to translate by.
     * @param scalar the scalar to scale the translator by before translating.
     * @return a new Point instance which represents point translated by the scaled vector translator.
     */
    public static Point scaledTranslate(final Point point, final Vector translator, final float scalar) {
        return new Point(point.x + (translator.x * scalar),
                         point.y + (translator.y * scalar),
                         point.z + (translator.z * scalar));
    }

    /**
     * Finds the mid-point (average) between two points.
     * @param point first point.
     * @param other second point.
     * @return a mid-{@link Point} (or average point).
     */
    public static Point midPoint(final Point point, final Point other) {
        return new Point((point.x + other.x) * 0.5F, (point.y + other.y) * 0.5F, (point.z + other.z) * 0.5F);
    }

    /* Basic overrides functinonality. */

    /**
     * Checks for equality between this point and some object instance.
     * @param o the object to check equality against.
     * @return true if the object reference is the same, and if the instance is a point and has the same x, y and z coordinates as this point. Else false.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Point point = (Point) o;
        return ((Float.compare(point.x, x) == 0) && (Float.compare(point.y, y) == 0) && (Float.compare(point.z, z) == 0));
    }

    /**
     * Clones this point.
     * @return a new Point with this point's x, y and z coordinate values.
     */
    @Override
    public Point clone() {
        return new Point(this.x, this.y, this.z);
    }

    /**
     * Returns a string representation of this point.
     * @return the string "Point [{@link #x}, {@link #y}, {@link #z}]."
     */
    @Override
    public String toString() {
        return String.format("Point [%f, %f, %f]", this.x, this.y, this.z);
    }
}
