package net.nexarys.alilib.utils;

import com.google.common.base.Preconditions;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class AliColor implements Cloneable {
    private static final int BIT_MASK = 0xff;
    private static final int DEFAULT_ALPHA = 255;

    private final byte alpha;
    private final byte red;
    private final byte green;
    private final byte blue;

    public record TimedColor(AliColor color, int duration) {
        public String toString() {
            return "[%s:%s]".formatted(color, duration);
        }
    }

    private List<TimedColor> sequence;

    public static AliColor fromARGB(int alpha, int red, int green, int blue) throws IllegalArgumentException {
        return new AliColor(alpha, red, green, blue);
    }

    public static AliColor fromRGB(int red, int green, int blue) throws IllegalArgumentException {
        return new AliColor(DEFAULT_ALPHA, red, green, blue);
    }

    public static AliColor fromBGR(int blue, int green, int red) throws IllegalArgumentException {
        return new AliColor(DEFAULT_ALPHA, red, green, blue);
    }

    public static AliColor fromRGB(int rgb) throws IllegalArgumentException {
        Preconditions.checkArgument((rgb >> 24) == 0, "Extraneous data in: %s", rgb);
        return fromRGB(rgb >> 16 & BIT_MASK, rgb >> 8 & BIT_MASK, rgb & BIT_MASK);
    }

    public static AliColor fromARGB(int argb) {
        return fromARGB(argb >> 24 & BIT_MASK, argb >> 16 & BIT_MASK, argb >> 8 & BIT_MASK, argb & BIT_MASK);
    }

    public static AliColor fromHex(String hexColor, int opacity) {
        if (hexColor == null || !hexColor.matches("#[0-9a-fA-F]{6}"))
            throw new IllegalArgumentException("Invalid hex color format. Must be #RRGGBB.");

        Color color = Color.decode(hexColor);
        return fromARGB(opacity, color.getRed(), color.getGreen(), color.getBlue());
    }

    private AliColor(int red, int green, int blue) {
        this(DEFAULT_ALPHA, red, green, blue);
    }

    private AliColor(int alpha, int red, int green, int blue) {
        Preconditions.checkArgument(alpha >= 0 && alpha <= BIT_MASK, "Alpha[%s] is not between 0-255", alpha);
        Preconditions.checkArgument(red >= 0 && red <= BIT_MASK, "Red[%s] is not between 0-255", red);
        Preconditions.checkArgument(green >= 0 && green <= BIT_MASK, "Green[%s] is not between 0-255", green);
        Preconditions.checkArgument(blue >= 0 && blue <= BIT_MASK, "Blue[%s] is not between 0-255", blue);

        this.alpha = (byte) alpha;
        this.red = (byte) red;
        this.green = (byte) green;
        this.blue = (byte) blue;
        this.sequence = new ArrayList<>();
    }

    public void addToSequence(AliColor color, int duration) {
        sequence.add(new TimedColor(color, duration));
    }

    public TimedColor getColor(int tick) {
        if (sequence.isEmpty()) return null;

        int totalDuration = sequence.stream().mapToInt(TimedColor::duration).sum();
        int loopTick = tick % totalDuration;

        int currentTick = 0;
        for (TimedColor timedColor : sequence) {
            currentTick += timedColor.duration();
            if (loopTick < currentTick) {
                return timedColor;
            }
        }

        return sequence.get(sequence.size() - 1);
    }

    public List<TimedColor> getSequence() {
        return sequence;
    }

    public void clearSequence() {
        sequence.clear();
    }

    @Override
    public AliColor clone() {
        try {
            AliColor clone = (AliColor) super.clone();
            clone.sequence = new ArrayList<>(this.sequence);
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public int getAlpha() {
        return BIT_MASK & alpha;
    }

    public AliColor setAlpha(int alpha) {
        return fromARGB(alpha, getRed(), getGreen(), getBlue());
    }

    public int getRed() {
        return BIT_MASK & red;
    }

    public AliColor setRed(int red) {
        return fromARGB(getAlpha(), red, getGreen(), getBlue());
    }

    public int getGreen() {
        return BIT_MASK & green;
    }

    public AliColor setGreen(int green) {
        return fromARGB(getAlpha(), getRed(), green, getBlue());
    }

    public int getBlue() {
        return BIT_MASK & blue;
    }

    public AliColor setBlue(int blue) {
        return fromARGB(getAlpha(), getRed(), getGreen(), blue);
    }

    public int asARGB() {
        return getAlpha() << 24 | getRed() << 16 | getGreen() << 8 | getBlue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AliColor)) return false;
        AliColor color = (AliColor) o;
        return alpha == color.alpha && red == color.red && green == color.green && blue == color.blue;
    }

    @Override
    public int hashCode() {
        return Objects.hash(alpha, red, green, blue);
    }

    @Override
    public String toString() {
        return "AliColor{" +
                "alpha=" + getAlpha() +
                ", red=" + getRed() +
                ", green=" + getGreen() +
                ", blue=" + getBlue() +
                '}';
    }
}