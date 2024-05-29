package cn.evole.mods.cameraoverhaul.structures;

import net.minecraft.util.math.Vec3d;

public class Transform {
    public Vec3d position;
    public Vec3d rotation;

    public Transform() {
        this.position = new Vec3d(0d, 0d, 0d);
        this.rotation = new Vec3d(0d, 0d, 0d);
    }

    public Transform(Vec3d position, Vec3d rotation) {
        this.position = position;
        this.rotation = rotation;
    }
}