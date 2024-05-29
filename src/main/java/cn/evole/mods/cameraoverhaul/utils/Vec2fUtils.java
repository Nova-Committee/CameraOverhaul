package cn.evole.mods.cameraoverhaul.utils;

import net.minecraft.util.math.Vec2f;

public class Vec2fUtils {
	public static float Length(Vec2f vec) {
		return (float) Math.sqrt(vec.x * vec.x + vec.y * vec.y);
	}

	public static Vec2f Rotate(Vec2f vec, float degrees) {
		double radians = Math.toRadians(degrees);
		float sin = (float) Math.sin(radians);
		float cos = (float) Math.cos(radians);

		return new Vec2f((cos * vec.x) - (sin * vec.y), (sin * vec.x) + (cos * vec.y));
	}

	public static Vec2f Lerp(Vec2f vecFrom, Vec2f vecTo, float step) {
		return new Vec2f(MathUtils.lerp(step, vecFrom.x, vecTo.x), MathUtils.lerp(step, vecFrom.y, vecTo.y));
	}

	public static Vec2f Multiply(Vec2f vec, float value) {
		return new Vec2f(vec.x * value, vec.y * value);
	}

	public static Vec2f Multiply(Vec2f vec, Vec2f value) {
		return new Vec2f(vec.x * value.x, vec.y * value.y);
	}
}
