package dev.kofeychi.Cart;

import com.mojang.blaze3d.systems.RenderSystem;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.example.ExampleConfig;
import me.shedaniel.clothconfig2.ClothConfigDemo;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;

@Environment(EnvType.CLIENT)
public class CartMM implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return (screen) -> {
            return RenderSystem.isOnRenderThread() && Screen.hasShiftDown() && Screen.hasControlDown() ? (Screen)AutoConfig.getConfigScreen(CartConf.class, screen).get() : CartConfigBuilder.generate().setParentScreen(screen).build();
        };
    }
}