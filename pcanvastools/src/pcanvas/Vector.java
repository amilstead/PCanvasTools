package pcanvas;

import static processing.core.PApplet.sq;
import static processing.core.PApplet.sqrt;

/**
 * Basic Vector class. Contains three floats; x, y and z, which represent x, y and z displacements (respectively) on
 * some coordinate system.
 */
public class Vector {
    /** Vector x displacement. */
    public float x;

    /** Vector y displacement. */
    public float y;

    /** Vector z displacement. */
    public float z;

    /**
     * Basic constructor which represents the vector startend or (end - start).
     * @param start the starting {@link Point} of this vector.
     * @param end the ending {@link Point} of this vector.
     */
    public Vector(final Point start, final Point end) {
        this.x = end.x - start.x;
        this.y = end.y - start.y;
        this.z = end.z - start.z;
    }

    /**
     * Basic constructor which represents an initial x and y displacement.
     * @param x the initial x displacement of this vector.
     * @param y the initial y displacement of this vector.
     */
    public Vector(final float x, final float y) {
        this.x = x;
        this.y = y;
        this.z = 0.0F;
    }

    /**
     * Basic constructor which represents an initial x, y and z displacement.
     * @param x the initial x displacement of this vector.
     * @param y the initial y displacement of this vector.
     * @param z the initial z displacement of this vector.
     */
    public Vector(final float x, final float y, final float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Arithmetic vector addition. Adds another vector's componenets to this vector's components.
     * @param other the vector to add.
     * @return this updated Vector instance.
     */
    public Vector add(final Vector other) {
        this.x += other.x;
        this.y += other.y;
        this.z += other.z;
        return this;
    }

    /**
     * Arithmetic vector subtraction. Subtracts another vector's componenets to this vector's components.
     * @param other the vector to subtract.
     * @return this updated Vector instance.
     */
    public Vector subtract(final Vector other) {
        this.x -= other.x;
        this.y -= other.y;
        this.z -= other.z;
        return this;
    }

    /**
     * Basic vector scale. Scales this vector's components by some value.
     * @param scalar the float to scale this vector by.
     * @return this updated Vector instance.
     */
    public Vector scale(final float scalar) {
        this.x *= scalar;
        this.y *= scalar;
        this.z *= scalar;
        return this;
    }

    /**
     * Basic vector 'division'. Sometimes is makes sense to divide a vector instead of scale by an inverse. This method
     * accomplishes that.
     * @param divisor the amount to divide this vector by.
     * @return this updated Vector instance.
     */
    public Vector divide(final float divisor) {
        this.x /= divisor;
        this.y /= divisor;
        this.z /= divisor;
        return this;
    }

    /**
     * So maybe you want to divide the x and y by different things... Who's gonna tell you not to?
     * @param dx the x-divisor.
     * @param dy the y-divisor
     * @return this updated Vector instance.
     */
    public Vector divide(final float dx, final float dy) {
        this.x /= dx;
        this.y /= dy;
        return this;
    }

    /**
     * Again, I say, "What's so wrong about dividing vector components by different values?".
     * @param dx the x-divisor.
     * @param dy the y-divisor.
     * @param dz the z-divisor.
     * @return this updated Vector instance.
     */
    public Vector divide(final float dx, final float dy, final float dz) {
        this.x /= dx;
        this.y /= dy;
        this.z /= z;
        return this;
    }

    /**
     * Whoa now... you want to divide a vector by <i>another</i> vector? You're...you're just crazy.
     * @param other another {@link Vector}.
     * @return this updated Vector instance.
     */
    public Vector divide(final Vector other) {
        this.x /= other.x;
        this.y /= other.y;
        this.z /= other.z;
        return this;
    }

    /**
     * Crosses this {@link Vector} with another vector. Sets itself as the result.
     * @param other a vector to cross by.
     * @return this updated Vector instance.
     */
    public Vector cross(final Vector other) {
        this.x = (this.y * other.z) - (this.z * other.y);
        this.y = (this.z * other.x) - (this.x * other.z);
        this.z = (this.x * other.y) - (this.y * other.z);
        return this; 
    }

    /**
     * Normalizes this vector.
     * @return this updated Vector instance.
     */
    public Vector normalize() {
        float n = sqrt(sq(this.x) + sq(this.y) + sq(this.z));
        if (n < 0.000001) n = 1;
        this.x /= n;
        this.y /= n;
        this.z /= n;
        return this;
    }

    /**
     * Returns the normal (magnitude) of this vector.
     * @return the magnitude of this vector.
     */
    public float norm() {
        return sqrt(sq(this.x) + sq(this.y) + sq(this.z));
    }

    /**
     * Returns the... magnitude... of this vector. >.> <.<
     * @return this vector's magnitude.
     */
    public float magnitude() {
        return sqrt(sq(this.x) + sq(this.y) + sq(this.z));
    }

    /**
     * Performs a dot product on another vector.
     * @param other some other vector.
     * @return the result of a vector dot product.
     */
    public float dot(final Vector other) {
        return (this.x * other.x) + (this.y * other.y) + (this.z * other.z);
    }

    /**
     * Returns the norm (or unit-length V/|V|) of this vector.
     * @return this vector's norm.
     */
    public Vector normal() {
        float n = sqrt(sq(this.x) + sq(this.y) + sq(this.z));
        if (n < 0.000001) n = 1;
        return new Vector(this.x / n, this.y / n, this.z / n);
    }

    /**
     * Sets this vector's x and y components.
     * @param x a new x-displacement.
     * @param y a new y-displacement.
     * @return this updated Vector instance.
     */
    public Vector set(final float x, final float y) {
        this.x = x;
        this.y = y;
        return this;
    }

    /**
     * Sets this vector's x, y and z components.
     * @param x a new x-displacement.
     * @param y a new y-displacement.
     * @param z a new z-displacement.
     * @return this updated Vector instance.
     */
    public Vector set(final float x, final float y, final float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    /**
     * Sets this vector to another vector. Copy cat...
     * @param vector the vector to set this vector to.
     * @return this updated Vector instance.
     */
    public Vector set(final Vector vector) {
        this.x = vector.x;
        this.y = vector.y;
        this.z = vector.z;
        return this;
    }

    /* Static functions for immutable arithmetic. */

    public static Vector addVectors(final Vector vector, final Vector other) {
        return new Vector(vector.x + other.x, vector.y + other.y, vector.z + other.z);
    }

    public static Vector subtractVectors(final Vector vector, final Vector other) {
        return new Vector(vector.x - other.x, vector.y - other.y, vector.z - other.z);
    }

    public static Vector scaleVector(final Vector vector, final float scalar) {
        return new Vector(vector.x * scalar, vector.y * scalar, vector.z * scalar);
    }

    public static Vector divideVector(final Vector vector, final float divisor) {
        return new Vector(vector.x / divisor, vector.y / divisor, vector.z / divisor);
    }

    public static Vector divideVector(final Vector vector, final float dx, final float dy) {
        return new Vector(vector.x / dx, vector.y / dy, vector.z);
    }

    public static Vector divideVector(final Vector vector, final float dx, final float dy, final float dz) {
        return new Vector(vector.x / dx, vector.y / dy, vector.z / dz);
    }

    public Vector divideVector(final Vector vector, final Vector other) {
        return new Vector(vector.x / other.x, vector.y / other.y, vector.z / other.z);
    }

    public static Vector crossProduct(final Vector vector, final Vector other) {
        return new Vector((vector.y * other.z) - (vector.z * other.y),
                          (vector.z * other.x) - (vector.x * other.z),
                          (vector.x * other.y) - (vector.y * other.z));
    }

    public static float normal(final Vector vector) {
        return vector.norm();
    }

    public static float magnitude(final Vector vector) {
        return vector.magnitude();
    }
    
    public static float dotProduct(final Vector vector, final Vector other) {
        return (vector.x * other.x) + (vector.y * other.y) + (vector.z * other.z);
    }

    /* Basic overrides functinonality. */

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vector vector = (Vector) o;

        return ((Float.compare(vector.x, x) == 0) && (Float.compare(vector.y, y) == 0) && (Float.compare(vector.z, z) == 0));
    }

    @Override
    public Vector clone() {
        return new Vector(this.x, this.y, this.z);
    }

    @Override
    public String toString() {
        return String.format("Vector [%f, %f, %f]", this.x, this.y, this.z);
    }
}
