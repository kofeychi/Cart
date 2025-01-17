package dev.kofeychi.Cart.SSModule;

import dev.kofeychi.Cart.Util.Easing;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;

import static dev.kofeychi.Cart.SSModule.SSHandler.Camera;

public class PSI extends SSInstance{
    @Override
    public String getType(){return typee;}
    public String typee = "PSI";
    public final Vec3d position;
    public float falloffDistance;
    public float maxDistance;

    public PSI(int d, SSModes.SSEase easeMode, SSModes.SSRng rngMode, EnabledAffections af, Vec3d position, float falloffDistance, float maxDistance) {
        super(d, easeMode, rngMode, af);
        this.position = position;
        this.falloffDistance = falloffDistance;
        this.maxDistance = maxDistance;
    }

    @Override
    public float updateIntensity(float int1,float int2) {
        float intensity = super.updateIntensity(int1,int2);
        float distance = (float) position.distanceTo(Camera.getPos());
        if (distance > maxDistance) {
            return 0;
        }
        float distanceMultiplier = 1;
        if (distance > falloffDistance) {
            float remaining = maxDistance - falloffDistance;
            float current = distance - falloffDistance;
            distanceMultiplier = 1 - current / remaining;
        }
        Vector3f lookDirection = Camera.getHorizontalPlane();
        Vec3d directionToScreenshake = position.subtract(Camera.getPos()).normalize();
        float angle = Math.max(0, lookDirection.dot(new Vector3f((float) directionToScreenshake.x, (float) directionToScreenshake.y, (float) directionToScreenshake.z)));
        return ((intensity * distanceMultiplier) + (intensity * angle)) * 0.5f;
    }
    @Override
    public String toStringy() {
        return "PSI{progress=" + progress +", duration=" + duration + ", InCurve=" + InCurve + ", OutCurve=" + OutCurve + ", LinearCurve=" + LinearCurve + ", EaseMode=" + EaseMode.name() + ", RngMode=" + RngMode.name() + ", PerlinSpeedI=" + PerlinSpeedI + ", AffectedValues=" + AffectedValues.toString() + ", Rot1=" + Rot1.toString() + ", Rot2=" + Rot2.toString() + ", Pos1=" + Pos1.toString() + ", Pos2=" + Pos2.toString() + ", Position=" + position.toString() + ", Falloff=" + falloffDistance +", MaxDistance=" + maxDistance +'}';
    }
}
