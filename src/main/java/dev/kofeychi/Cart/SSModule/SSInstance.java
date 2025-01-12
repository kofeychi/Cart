package dev.kofeychi.Cart.SSModule;

import dev.kofeychi.Cart.Util.Easing;
import net.minecraft.client.render.Camera;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import org.joml.Vector3f;

public class SSInstance {
    public String getType(){return type;}
    public String type = "SSInstance";
    public int progress = 0,duration = 0;
    public String InCurve = Easing.LINEAR.name;
    public String OutCurve = Easing.LINEAR.name;
    public String LinearCurve = Easing.LINEAR.name;
    public SSModes.SSEase EaseMode = SSModes.SSEase.LINEAR;
    public SSModes.SSRng RngMode = SSModes.SSRng.RANDOM;
    public float PerlinSpeedI = 0;
    public EnabledAffections AffectedValues = new EnabledAffections("yyynnn");
    public Vector3f Rot1=new Vector3f(),Rot2=new Vector3f(),Pos1=new Vector3f(),Pos2=new Vector3f();

    public SSInstance(int d,SSModes.SSEase easeMode,SSModes.SSRng rngMode,EnabledAffections af){
        duration = d;
        EaseMode = easeMode;
        RngMode = rngMode;
        AffectedValues = af;
    }

    public SSInstance setRot1(Vector3f vec){Rot1=vec;return this;}
    public SSInstance setRot2(Vector3f vec){Rot2=vec;return this;}
    public SSInstance setPos1(Vector3f vec){Pos1=vec;return this;}
    public SSInstance setPos2(Vector3f vec){Pos2=vec;return this;}

    public SSInstance setInCurve(Easing c){InCurve=c.name;return this;}
    public SSInstance setOutCurve(Easing c){OutCurve=c.name;return this;}
    public SSInstance setLinearCurve(Easing c){LinearCurve=c.name;return this;}

    public SSInstance setPerlinSpeedI(float perlinSpeedI) {PerlinSpeedI = perlinSpeedI;return this;}
    public float getPerlinSpeedI() {
        if (PerlinSpeedI == 0||RngMode == SSModes.SSRng.RANDOM) {
            return 1;
        } else {
            return PerlinSpeedI;
        }
    }

    public float updRotX(){return AffectedValues.RotX ? updateIntensity(Rot1.x, Rot2.x) : 0;}
    public float updRotY(){return AffectedValues.RotY ? updateIntensity(Rot1.y, Rot2.y) : 0;}
    public float updRotZ(){return AffectedValues.RotZ ? updateIntensity(Rot1.z, Rot2.z) : 0;}
    public float updPosX(){return AffectedValues.PosX ? updateIntensity(Pos1.x, Pos2.x) : 0;}
    public float updPosY(){return AffectedValues.PosY ? updateIntensity(Pos1.y, Pos2.y) : 0;}
    public float updPosZ(){return AffectedValues.PosZ ? updateIntensity(Pos1.z, Pos2.z) : 0;}

    public float updateIntensity(float int1,float int2) {
        if (!SSHandler.isFrozen) {
            progress++;
        }
        float percentage = (progress / (float) duration);
        if (EaseMode == SSModes.SSEase.INOUT) {
            if (percentage >= 0.5f) {
                return MathHelper.lerp(Easing.valueOf(OutCurve).ease(percentage - 0.5f, 0, 1, 0.5f), int2, int1);
            } else {
                return MathHelper.lerp(Easing.valueOf(InCurve).ease(percentage, 0, 1, 0.5f), int1, int2);
            }
        } else {
            return MathHelper.lerp(Easing.valueOf(LinearCurve).ease(percentage, 0, 1, 1), int1, int2);
        }
    }

    @Override
    public String toString() {
        return toStringy();
    }

    public String toStringy() {
        return "SSInstance{progress=" + progress +", duration=" + duration + ", InCurve=" + InCurve + ", OutCurve=" + OutCurve + ", LinearCurve=" + LinearCurve + ", EaseMode=" + EaseMode.name() + ", RngMode=" + RngMode.name() + ", PerlinSpeedI=" + PerlinSpeedI + ", AffectedValues=" + AffectedValues.toString() + ", Rot1=" + Rot1.toString() + ", Rot2=" + Rot2.toString() + ", Pos1=" + Pos1.toString() + ", Pos2=" + Pos2.toString() + '}';
    }
}
