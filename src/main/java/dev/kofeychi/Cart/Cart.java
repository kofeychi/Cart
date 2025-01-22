package dev.kofeychi.Cart;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.kofeychi.Cart.API.ScreenShakeAPI;
import dev.kofeychi.Cart.SSModule.EnabledAffections;
import dev.kofeychi.Cart.SSModule.SSHandler;
import dev.kofeychi.Cart.SSModule.SSInstance;
import dev.kofeychi.Cart.SSModule.SSPacket;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.impl.util.ExceptionUtil;
import net.minecraft.client.render.Camera;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.objectweb.asm.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;

public class Cart implements ModInitializer, ClientModInitializer {
    public static Logger LOGGER = LoggerFactory.getLogger("Cart");
    public static Gson GSON = new GsonBuilder().setLenient().setPrettyPrinting().create();
    public static CartConf CONFIG;
    public static MinecraftServer SERVER;
    public static boolean isUsingTntMixin=false;
    public static long rtime = 0;
    public static Identifier of(String n){
        return Identifier.of("cart",n);
    }
    @Override
    public void onInitialize() {
        HudRenderCallback.EVENT.register(((drawContext, tickCounter) -> {
            if (rtime >= 500000) {rtime = 0;}
            rtime++;
        }));
        ServerLifecycleEvents.SERVER_STARTING.register(server -> SERVER = server);
        ServerLifecycleEvents.SERVER_STOPPING.register(server -> SERVER = null);
        PayloadTypeRegistry.playS2C().register(SSPacket.ID, SSPacket.CODEC);
        AutoConfig.register(CartConf.class, GsonConfigSerializer::new);
        CONFIG = AutoConfig.getConfigHolder(CartConf.class).getConfig();
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(CommandManager.literal("cart").requires(source -> source.hasPermissionLevel(1))
                    .then(CommandManager.literal("debug").requires(source -> source.hasPermissionLevel(1))
                            .then(CommandManager.literal("freezessinstances").requires(source -> source.hasPermissionLevel(1)).then(CommandManager.argument("isFrozen", BoolArgumentType.bool()).executes(Cart::freezessinstances)))
                            .then(CommandManager.literal("delinstances").requires(source -> source.hasPermissionLevel(1)).executes(Cart::delinstances))
                            .then(CommandManager.literal("ssdebugrender").requires(source -> source.hasPermissionLevel(1)).then(CommandManager.argument("isDebug", BoolArgumentType.bool()).executes(Cart::ssdebugrenderer)))
                            .then(CommandManager.literal("testTntMixin").requires(source -> source.hasPermissionLevel(1)).then(CommandManager.argument("isTntMixin", BoolArgumentType.bool()).executes(Cart::tntMixin)))
                            .then(CommandManager.literal("sendss").requires(source -> source.hasPermissionLevel(1)).then(CommandManager.argument("ssi", StringArgumentType.string()).then(CommandManager.argument("target", EntityArgumentType.players()).then(CommandManager.argument("isPSI", BoolArgumentType.bool()).executes(Cart::ssi)))))
                    ));
        });
    }
    public static int ssi(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        char a = 39;
        char b = 34;
        String ssi = StringArgumentType.getString(context,"ssi").replace(a,b);
        boolean bo = BoolArgumentType.getBool(context,"isPSI");
        String type = !bo ? "ssi" : "psi";
        Collection<ServerPlayerEntity> players = EntityArgumentType.getPlayers(context,"target");
        players.forEach((spe)-> ServerPlayNetworking.send(spe,new SSPacket(ssi,type)));
        return 1;
    }
    public static int delinstances(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        SSHandler.Instances.clear();
        return 1;
    }
    public static int freezessinstances(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        SSHandler.isFrozen = BoolArgumentType.getBool(context,"isFrozen");
        CONFIG.Debug_Stuff.isInstancesFrozen = SSHandler.isFrozen;
        return 1;
    }
    public static int ssdebugrenderer(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        SSHandler.isDebugRenderer = BoolArgumentType.getBool(context,"isDebug");
        CONFIG.Debug_Stuff.isDebugRendered =  SSHandler.isDebugRenderer;
        return 1;
    }
    public static int tntMixin(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        isUsingTntMixin = BoolArgumentType.getBool(context,"isTntMixin");
        CONFIG.Debug_Stuff.isDebugRendered = isUsingTntMixin;
        return 1;
    }
    @Override
    public void onInitializeClient() {

    }
}
