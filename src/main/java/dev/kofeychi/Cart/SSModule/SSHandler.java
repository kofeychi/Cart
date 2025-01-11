package dev.kofeychi.Cart.SSModule;

import com.google.gson.JsonObject;
import dev.kofeychi.Cart.Cart;
import dev.kofeychi.Cart.Util.Easing;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.TntEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import org.apache.logging.log4j.core.util.JsonUtils;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.joml.SimplexNoise.noise;

public class SSHandler implements ClientModInitializer {
    public static Identifier SSPacketID = Cart.of("sspackedjson");
    public static ArrayList<SSInstance> Instances = new ArrayList<>();
    public static Vector3f Rot=new Vector3f(),Pos=new Vector3f(),IRot=new Vector3f(),IPos=new Vector3f();
    public static float PerlinX,PerlinY,PerlinSpeed;
    public static Random random;
    public static boolean isFrozen = false;
    public static boolean isDebugRenderer = false;
    public static Camera Camera;

    public static void render(Camera camera,Random rng){
        Camera = camera;
        PerlinX += PerlinSpeed/50;
        if (PerlinX >= 5) {PerlinX = -5;PerlinY += PerlinSpeed/50;}
        if (PerlinY >= 5) {PerlinY = -5;}
        random = rng;
        camera.setPos(camera.getPos());
        generate(rng);
    }
    public static boolean checkif(){
        if (Instances.isEmpty()) {return false;}
        int i = 0;
        for(Iterator<SSInstance> var2 = Instances.iterator(); var2.hasNext(); i++) {
            SSInstance instance = (SSInstance) var2.next();
            if (instance.RngMode == SSModes.SSRng.PERLIN) {
                return true;
            }
        }
        return false;
    }
    public static void tick(){
        SSHandler.isDebugRenderer = Cart.CONFIG.Debug_Stuff.isDebugRendered;
        SSHandler.isFrozen = Cart.CONFIG.Debug_Stuff.isInstancesFrozen;
        Cart.isUsingTntMixin = Cart.CONFIG.Debug_Stuff.isUsingTestTntMixin;
        float inten = Cart.CONFIG.ScreenShakeIntensity;
        PerlinSpeed = checkif() ? (float) (Instances.stream().mapToDouble(SSInstance::getPerlinSpeedI).sum() / Instances.size()) : 0;
        IRot = MakeSureNonNaN(new Vector3f(
                (float) Math.pow(Instances.stream().mapToDouble(SSInstance::updRotX).sum(),curved(Instances.stream().mapToDouble(SSInstance::updRotX).sum()))*inten,
                (float) Math.pow(Instances.stream().mapToDouble(SSInstance::updRotY).sum(),curved(Instances.stream().mapToDouble(SSInstance::updRotY).sum()))*inten,
                (float) Math.pow(Instances.stream().mapToDouble(SSInstance::updRotZ).sum(),curved(Instances.stream().mapToDouble(SSInstance::updRotZ).sum()))*inten
        ));
        IPos = MakeSureNonNaN(new Vector3f(
                (float) Math.pow(Instances.stream().mapToDouble(SSInstance::updPosX).sum(),curved(Instances.stream().mapToDouble(SSInstance::updPosX).sum()))*inten,
                (float) Math.pow(Instances.stream().mapToDouble(SSInstance::updPosY).sum(),curved(Instances.stream().mapToDouble(SSInstance::updPosY).sum()))*inten,
                (float) Math.pow(Instances.stream().mapToDouble(SSInstance::updPosZ).sum(),curved(Instances.stream().mapToDouble(SSInstance::updPosZ).sum()))*inten
        ));
        Instances.removeIf(i -> i.progress - 0.5f >= i.duration);
    }

    public static void addInstance(SSInstance instance){
        Instances.add(instance);
    }
    public static double curved(double a){
        if (a >= 1) {a=1;}
        return MathHelper.lerp(Easing.CIRC_OUT.ease((float) a,0,1,1),3,1);
    }
    public static void tntmixinfunc(TntEntity entity){
        Vector3f vec = new Vector3f((float) 2 / 50, (float) 2 / 50, (float) 2 / 50).mul(10);
        Cart.SERVER.getPlayerManager().getPlayerList().forEach((spe)-> ServerPlayNetworking.send(spe,new SSPacket(Cart.GSON.toJson(new PSI(120, SSModes.SSEase.LINEAR, SSModes.SSRng.RANDOM, new EnabledAffections("nnnyyy"),entity.getPos(),5,40)
                .setRot1(vec)
                .setRot2(new Vector3f(0, 0, 0))
                .setPos1(vec)
                .setPos2(new Vector3f(0, 0, 0))
                .setLinearCurve(Easing.QUAD_OUT)
                ))
        ));
    }
    public static Vector3f MakeSureNonNaN(Vector3f vec){
        if (Float.isNaN(vec.x)||10000<vec.x){
            vec.x = 0;
        }
        if (Float.isNaN(vec.y)||10000<vec.y){
            vec.y = 0;
        }
        if (Float.isNaN(vec.z)||10000<vec.z){
            vec.z = 0;
        }
        return vec;
    }
    public static Vec3d MakeSureNonNaN(Vec3d vec){
        if (Double.isNaN(vec.x)||10000<vec.x){
            vec = new Vec3d(0, vec.y,vec.z);
        }
        if (Double.isNaN(vec.y)||10000<vec.x){
            vec = new Vec3d(vec.x, 0,vec.z);
        }
        if (Double.isNaN(vec.z)||10000<vec.x){
            vec = new Vec3d(vec.x, vec.y,0);
        }
        return vec;
    }
    private static float test = 0;
    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(SSPacket.ID, (payload, context) -> {
            context.client().execute(() -> {
                String data = payload.data();
                SSInstance ssi = Cart.GSON.fromJson(data, SSInstance.class);
                PSI psi = Cart.GSON.fromJson(data, PSI.class);
                if (Objects.equals(ssi.getType(), "SSInstance")){
                    addInstance(ssi);
                }
                if (Objects.equals(ssi.getType(), "PSI")){
                    addInstance(psi);
                }
            });
        });
        ClientTickEvents.START_CLIENT_TICK.register((client -> {
            if (!isFrozen) {
                tick();
            }
        }));
        ClientTickEvents.START_WORLD_TICK.register((world) -> {
            /*if (world.getTime() % 80 == 0&&!isFrozen) {
                addInstance(
                        new SSInstance(300, SSModes.SSEase.LINEAR, SSModes.SSRng.RANDOM,new EnabledAffections("yyynnn"))
                                .setRot1(new Vector3f((float) 4 /20,(float) 4 /20,(float) 4 /20))
                                .setRot2(new Vector3f(0,0,0))
                                .setLinearCurve(Easing.CUBIC_IN)
                );
            }
             */
        });
            HudRenderCallback.EVENT.register((drawContext, tickCounter) -> {
                if (isDebugRenderer) {
                    if (test >= 1) {test = 0;}
                    test += .01f;
                    int a = 8;
                    String data = "";
                    int i = 0;
                    for(Iterator<SSInstance> var2 = Instances.iterator(); var2.hasNext(); i++) {
                        SSInstance instance = (SSInstance) var2.next();
                        data += "ID: "+i+" Data: "+instance.toString();
                    }
                    int maxLenght = 120;
                    Pattern p = Pattern.compile("\\G\\s*(.{1," + maxLenght + "})(?=\\s|$)", Pattern.DOTALL);
                    Matcher m = p.matcher(data);
                    int b = 0;
                    drawContext.drawText(MinecraftClient.getInstance().textRenderer, "Instances: ", a, a * 1, 0xFFFFFFFF, false);
                    while (m.find()) {
                        b += a;
                        drawContext.drawText(MinecraftClient.getInstance().textRenderer, m.group(1), a, a * 2 + b, 0xFFFFFFFF, false);
                    }
                    b += a * 2;
                    drawContext.drawText(MinecraftClient.getInstance().textRenderer, "Rot: " + Rot.toString(), a, a * 2 + b, 0xFFFFFFFF, false);
                    drawContext.drawText(MinecraftClient.getInstance().textRenderer, "Pos: " + Pos.toString(), a, a * 3 + b, 0xFFFFFFFF, false);
                    drawContext.drawText(MinecraftClient.getInstance().textRenderer, "IRot: " + IRot.toString(), a, a * 4 + b, 0xFFFFFFFF, false);
                    drawContext.drawText(MinecraftClient.getInstance().textRenderer, "IPos: " + IPos.toString(), a, a * 5 + b, 0xFFFFFFFF, false);
                    drawContext.drawText(MinecraftClient.getInstance().textRenderer, "Instances amount: " + (Instances.size()), a, a * 6 + b, 0xFFFFFFFF, false);
                    drawContext.drawText(MinecraftClient.getInstance().textRenderer, "ps: " + PerlinSpeed, a, a * 7 + b, 0xFFFFFFFF, false);
                    float eas = -Easing.CIRC_IN_OUT.ease(test,0,1,1)+1;
                    drawContext.drawBorder(a,a*10,a+1*30,a/2+1*27,0xFF0000FF);
                    drawContext.fill((int) (a+test*30), (int) ((a * 8) + b + (eas*30)),((int) (a+test*30))+1, ((int) ((a * 8) + b + (eas*30)))+1,0xFFFFFFFF);
                }
                });
    }
    public static void generate(Random rng){
        if (!isFrozen) {
            Rot = genRot(rng);
            Pos = genPos(rng);
        }
    }
    public static Vector3f genRot(Random rng){
        return new Vector3f(
                genVal(rng,IRot.x),
                genVal(rng,IRot.y),
                genVal(rng,IRot.z)
        );
    }
    public static Vector3f genPos(Random rng){
        return new Vector3f(
                genVal(rng,IPos.x),
                genVal(rng,IPos.y),
                genVal(rng,IPos.z)
        );
    }
    public static float genVal(Random rng,float val){
        return MathHelper.nextFloat(rng,-val,val) + ((noise(PerlinX,PerlinY)*val)*PerlinSpeed);
    }
}
