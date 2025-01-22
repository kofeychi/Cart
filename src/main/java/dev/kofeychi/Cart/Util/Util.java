package dev.kofeychi.Cart.Util;

import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class Util {
    public static DQuaternion getRandomSmoothRotation(Random random) {

        DQuaternion rotation = DQuaternion.identity;

        for (int i = 0; i < 6; i++) {
            rotation = rotation.hamiltonProduct(
                    DQuaternion.rotationByDegrees(
                            randomVec(random), random.nextInt(30, 60) * 360
                    )
            );
        }

        return rotation;
    }
    @NotNull
    public static Vec3d randomVec(Random random) {
        return new Vec3d(random.nextDouble() - 0.5, random.nextDouble() - 0.5, random.nextDouble() - 0.5);
    }
}
