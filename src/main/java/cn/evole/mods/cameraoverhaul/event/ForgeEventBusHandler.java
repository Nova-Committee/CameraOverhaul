package cn.evole.mods.cameraoverhaul.event;

import cn.evole.mods.cameraoverhaul.Const;
import cn.evole.mods.cameraoverhaul.config.ConfigData;
import cn.evole.mods.cameraoverhaul.config.Configuration;
import cn.evole.mods.cameraoverhaul.structures.Transform;
import cn.evole.mods.cameraoverhaul.utils.MathUtils;
import cn.evole.mods.cameraoverhaul.utils.Vec2fUtils;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static net.minecraft.client.renderer.ActiveRenderInfo.*;

/**
 * ForgeEventBusHandler
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/5/29 下午6:33
 */
@Mod.EventBusSubscriber(modid = Const.MOD_ID)
public class ForgeEventBusHandler {
    private static double prevForwardVelocityPitchOffset;
    private static double prevVerticalVelocityPitchOffset;
    private static double prevStrafingRollOffset;
    private static double prevCameraYaw;

    private static double yawDeltaRollOffset;
    private static double yawDeltaRollTargetOffset;
    private static final double lerpSpeed = 1d;
    private static final Transform offsetTransform = new Transform();

    @SubscribeEvent
    public static void cameraUpdate(EntityViewRenderEvent.CameraSetup event){
        //Reset the offset transform
        offsetTransform.position = new Vec3d(0d, 0d, 0d);
        offsetTransform.rotation = new Vec3d(0d, 0d, 0d);

        ConfigData config = Configuration.LoadConfig(ConfigData.class, Const.MOD_ID, ConfigData.ConfigVersion);

        if (!config.enabled) {
            return;
        }

        Entity entity = event.getEntity();
        float pitch = event.getPitch();
        float yaw = event.getYaw();
        float roll = event.getRoll();
        double deltaTime = event.getRenderPartialTicks();

        Vec3d vec3d = projectViewFromEntity(entity, deltaTime);
        Transform cameraTransform = new Transform(vec3d, new Vec3d(pitch, yaw, 0d));

        Vec3d velocity = new Vec3d(entity.motionX, entity.motionY, entity.motionZ);
        Vec2f relativeXZVelocity = Vec2fUtils.Rotate(new Vec2f((float) velocity.x, (float) velocity.z), 360f - (float) cameraTransform.rotation.y);

        prevCameraYaw = cameraTransform.rotation.y;

        //X
        VerticalVelocityPitchOffset(velocity, deltaTime, config.verticalVelocityPitchFactor);
        ForwardVelocityPitchOffset(relativeXZVelocity, deltaTime, config.forwardVelocityPitchFactor);
        //Z
        YawDeltaRollOffset(cameraTransform, deltaTime, config.yawDeltaRollFactor);
        StrafingRollOffset(relativeXZVelocity, deltaTime, config.strafingRollFactor);


        event.setPitch((float)(pitch + offsetTransform.rotation.x));
        event.setYaw((float)(yaw + offsetTransform.rotation.y));
        event.setRoll(-(float)(roll + offsetTransform.rotation.z));
    }


    private static void VerticalVelocityPitchOffset(Vec3d velocity, double deltaTime, float intensity) {
        double verticalVelocityPitchOffset = velocity.y * 2.75d;

        if (velocity.y < 0f) {
            verticalVelocityPitchOffset *= 2.25d;
        }

        prevVerticalVelocityPitchOffset = verticalVelocityPitchOffset = MathUtils.Lerp(prevVerticalVelocityPitchOffset, verticalVelocityPitchOffset, deltaTime * lerpSpeed);

        ForgeEventBusHandler.offsetTransform.rotation = ForgeEventBusHandler.offsetTransform.rotation.add(verticalVelocityPitchOffset * intensity, 0d, 0d);
    }

    private static void ForwardVelocityPitchOffset(Vec2f relativeXZVelocity, double deltaTime, float intensity) {
        double forwardVelocityPitchOffset = relativeXZVelocity.y * 5d;

        prevForwardVelocityPitchOffset = forwardVelocityPitchOffset = MathUtils.Lerp(prevForwardVelocityPitchOffset, forwardVelocityPitchOffset, deltaTime * lerpSpeed);

        ForgeEventBusHandler.offsetTransform.rotation = ForgeEventBusHandler.offsetTransform.rotation.add(forwardVelocityPitchOffset * intensity, 0d, 0d);
    }

    private static void YawDeltaRollOffset(Transform inputTransform, double deltaTime, float intensity) {
        double yawDelta = prevCameraYaw - inputTransform.rotation.y;

        if (yawDelta > 180) {
            yawDelta = 360 - yawDelta;
        } else if (yawDelta < -180) {
            yawDelta = -360 - yawDelta;
        }

        yawDeltaRollTargetOffset += yawDelta * 0.07d;
        yawDeltaRollOffset = MathUtils.Lerp(yawDeltaRollOffset, yawDeltaRollTargetOffset, deltaTime * lerpSpeed * 10d);

        ForgeEventBusHandler.offsetTransform.rotation = ForgeEventBusHandler.offsetTransform.rotation.add(0d, 0d, yawDeltaRollOffset * intensity);

        yawDeltaRollTargetOffset = MathUtils.Lerp(yawDeltaRollTargetOffset, 0d, deltaTime * 0.35d);
    }

    private static void StrafingRollOffset(Vec2f relativeXZVelocity, double deltaTime, float intensity) {
        double strafingRollOffset = -relativeXZVelocity.x * 15d;

        prevStrafingRollOffset = strafingRollOffset = MathUtils.Lerp(prevStrafingRollOffset, strafingRollOffset, deltaTime * lerpSpeed);

        ForgeEventBusHandler.offsetTransform.rotation = ForgeEventBusHandler.offsetTransform.rotation.add(0d, 0d, strafingRollOffset * intensity);
    }

}
