package pcanvas.draw;

import processing.core.PApplet;

import java.awt.Color;

/**
 * An enumerated type which will convert a given hex value into something usable
 * by the {@link PApplet} fill, stroke and other related functions.
 */
public enum Colors {
    /**
     * The color yellow.
     */
    YELLOW(0xFEFF00),

    /**
     * The color brown.
     */
    BROWN(0x8B5701),

    /**
     * The color red.
     */
    RED(0xFF0000),

    /**
     * A darker red.
     */
    DARK_RED(0x740D0D),

    /**
     * The color black.
     */
    BLACK(0x000000),

    /**
     * The color magenta.
     */
    MAGENTA(0xFF00FB),

    /**
     * The color blue.
     */
    BLUE(0x0300FF),

    /**
     * A darker blue.
     */
    DARK_BLUE(0x0D0F74),

    /**
     * The color cyan.
     */
    CYAN(0x00FDFF),

    /**
     * A bright green.
     */
    BRIGHT_GREEN(0x00FF01),

    /**
     * The color green.
     */
    GREEN(0x018100),

    /**
     * The color orange.
     */
    ORANGE(0xFFC000),

    /**
     * The color white.
     */
    WHITE(0xFFFFFF),

    /**
     * The color grey.
     */
    GRAY(0xC6C6C6);

    /** The integer hex value of the color to use. */
    private final int hex;
    /**
     * Enum constructor. Takes a hex value to use for the color.
     * @param hex a hex color value.
     */
    private Colors(final int hex) {
        this.hex = hex;
    }

    public static int ramp(int c, int m) {
        float f = (90.0F * c) / m;

        return new Color((f/3), 30+f, 30+f).getRGB();
    }

    /**
     * Returns the "rgb" value of this color's {@link #hex} value.
     * @return an integer representation of the rgb value of the color enum.
     */
    public int getRgb() {
        return new Color(this.hex).getRGB();
    }

}