package dev.kofeychi.Cart;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.kofeychi.Cart.SSModule.EnabledAffections;
import dev.kofeychi.Cart.SSModule.SSHandler;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.client.render.Camera;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import org.objectweb.asm.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Cart implements ModInitializer, ClientModInitializer {
    public static Logger LOGGER = LoggerFactory.getLogger("Cart");
    public static Gson GSON = new GsonBuilder().setLenient().setPrettyPrinting().create();
    public static CartConf CONFIG;
    public static boolean isUsingTntMixin=false;
    @Override
    public void onInitialize() {
        AutoConfig.register(CartConf.class, GsonConfigSerializer::new);
        CONFIG = AutoConfig.getConfigHolder(CartConf.class).getConfig();
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(CommandManager.literal("cart").requires(source -> source.hasPermissionLevel(1))
                    .then(CommandManager.literal("debug").requires(source -> source.hasPermissionLevel(1))
                            .then(CommandManager.literal("freezessinstances").requires(source -> source.hasPermissionLevel(1)).then(CommandManager.argument("isFrozen", BoolArgumentType.bool()).executes(Cart::freezessinstances)))
                            .then(CommandManager.literal("ssdebugrender").requires(source -> source.hasPermissionLevel(1)).then(CommandManager.argument("isDebug", BoolArgumentType.bool()).executes(Cart::ssdebugrenderer)))
                            .then(CommandManager.literal("testTntMixin").requires(source -> source.hasPermissionLevel(1)).then(CommandManager.argument("isTntMixin", BoolArgumentType.bool()).executes(Cart::tntMixin)))
                    ));
        });
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
