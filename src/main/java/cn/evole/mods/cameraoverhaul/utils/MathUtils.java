package cn.evole.mods.cameraoverhaul.utils;

public class MathUtils {
    public static float Clamp01(float value) {
        return value < 0f ? 0f : (value > 1f ? 1f : value);
    }

    public static double Clamp01(double value) {
        return value < 0d ? 0d : (value > 1d ? 1d : value);
    }

    public static float Lerp(float a, float b, float time) {
        return a + (b - a) * Clamp01(time);
    }

    public static double Lerp(double a, double b, double time) {
        return a + (b - a) * Clamp01(time);
    }

    public static int floor(float f) {
        int i = (int)f;
        return f < (float)i ? i - 1 : i;
    }

    public static int floor(double d) {
        int i = (int)d;
        return d < (double)i ? i - 1 : i;
    }

    public static long lfloor(double d) {
        long l = (long)d;
        return d < (double)l ? l - 1L : l;
    }

    public static int lerpInt(float f, int i, int j) {
        return i + floor(f * (float)(j - i));
    }

    public static float lerp(float f, float g, float h) {
        return g + f * (h - g);
    }

    public static double lerp(double d, double e, double f) {
        return e + d * (f - e);
    }
}