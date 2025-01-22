package dev.kofeychi.Cart;

import dev.kofeychi.Cart.SSModule.PSI;
import dev.kofeychi.Cart.SSModule.SSInstance;
import dev.kofeychi.Cart.Util.Color;
import dev.kofeychi.Cart.Util.DQuaternion;
import dev.kofeychi.Cart.Util.Util;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

import java.util.Iterator;
import java.util.Random;

import static dev.kofeychi.Cart.Util.Util.getRandomSmoothRotation;

public class Test {
    public static double val1 = 0;
    public static double val2 = 150;
    public static double val3 = 300;
    public static double val4 = 450;
    public static void DebugRenderer(MatrixStack matrices, VertexConsumerProvider.Immediate vertexConsumers, double cameraX, double cameraY, double cameraZ) {
        VertexConsumer vc = vertexConsumers.getBuffer(RenderLayer.LINES);
        //Random random = new Random();
        //renderSmallCubeFrame(vc,new Vec3d(cameraX,cameraY,cameraZ),new Vec3d(0,0,0), Color.ofRGBA(255,255,255,255).getColor(),1,matrices,(val1+random.nextDouble(1)));
        //renderSmallCubeFrame(vc,new Vec3d(cameraX,cameraY,cameraZ),new Vec3d(0,0,0), Color.ofRGBA(255,255,255,255).getColor(),1.2,matrices,(val2+random.nextDouble(1)));
        //renderSmallCubeFrame(vc,new Vec3d(cameraX,cameraY,cameraZ),new Vec3d(0,0,0), Color.ofRGBA(255,255,255,255).getColor(),1.4,matrices,(val3+random.nextDouble(1)));
        //renderSmallCubeFrame(vc,new Vec3d(cameraX,cameraY,cameraZ),new Vec3d(0,0,0), Color.ofRGBA(255,255,255,255).getColor(),1.6,matrices,(val4+random.nextDouble(1)));
    }
    public static void renderSmallCubeFrame(
            VertexConsumer vertexConsumer, Vec3d cameraPos, Vec3d boxCenter,
            int color, double scale,
            MatrixStack matrixStack,
            double offst
    ) {
        Random random = new Random(color);
        MatrixStack ms = new MatrixStack();
        ms.translate(
                boxCenter.x - cameraPos.x,
                boxCenter.y - cameraPos.y,
                boxCenter.z - cameraPos.z
        );
        DQuaternion rotation = DQuaternion.identity;
        rotation = rotation.hamiltonProduct(
                DQuaternion.rotationByDegrees(
                        Util.randomVec(random),
                        Cart.rtime+offst//MinecraftClient.getInstance().world.getTime()+offst
                )
        );


        ms.multiply(rotation.toMcQuaternion());
        float alpha = ((color >> 24) & 0xff) / 255f;
        float red = ((color >> 16) & 0xff) / 255f;
        float green = ((color >> 8) & 0xff) / 255f;
        float blue = (color & 0xff) / 255f;
        WorldRenderer.drawBox(
                ms,
                vertexConsumer,
                -scale / 2,
                -scale / 2,
                -scale / 2,
                scale / 2,
                scale / 2,
                scale / 2,
                red, green, blue, alpha
        );
    }
}
