package dev.kofeychi.Cart;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;
import net.minecraft.text.Text;

public class CartConfigBuilder {
    public static ConfigBuilder generate(){
        ConfigBuilder builder = ConfigBuilder.create().setTitle(Text.literal("Cart"));
        builder.setGlobalized(true);
        builder.setGlobalizedExpanded(false);
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        ConfigCategory ScreenShake = builder.getOrCreateCategory(Text.literal("ScreenShake"));
        ScreenShake.addEntry(entryBuilder.startIntSlider(Text.literal("ScreenShake Intensity"), (int) Cart.CONFIG.ScreenShakeIntensity,0, 200)
                .setMax(100)
                .setMin(0)
                .setDefaultValue((int) Cart.CONFIG.ScreenShakeIntensity)
                .setTooltip(Text.literal("Configures the intensity of the ScreenShakes in the modAPI."))
                .setSaveConsumer((val)->{Cart.CONFIG.ScreenShakeIntensity = val;})
                .build()
        );
        ScreenShake.addEntry(entryBuilder.startIntSlider(Text.literal("Perlin Speed"), (int) Cart.CONFIG.SpeedOfPerlin,0, 500)
                .setMax(500)
                .setMin(0)
                .setDefaultValue((int) Cart.CONFIG.SpeedOfPerlin)
                .setTooltip(Text.literal("If i was you,i would leave this as it is honestly.."))
                .setSaveConsumer((val)->{Cart.CONFIG.SpeedOfPerlin = val;})
                .build()
        );
        SubCategoryBuilder SSDebug = entryBuilder.startSubCategory(Text.literal("Debug")).setExpanded(true).setTooltip(Text.literal("I would not want to touch anything here.."));
        SSDebug.add(entryBuilder.startBooleanToggle(Text.literal("Is Instances frozen"),Cart.CONFIG.Debug_Stuff.isInstancesFrozen)
                .setTooltip(Text.literal("Freezes all screenShake instances in place,"),Text.literal("Making them dont tick and render."))
                .setDefaultValue(Cart.CONFIG.Debug_Stuff.isInstancesFrozen)
                .setSaveConsumer((val)->{Cart.CONFIG.Debug_Stuff.isInstancesFrozen = val;})
                .build()
        );
        SSDebug.add(entryBuilder.startBooleanToggle(Text.literal("Debug renderer"),Cart.CONFIG.Debug_Stuff.isDebugRendered)
                .setTooltip(Text.literal("Shows the debug info,instances and some test stuff"),Text.literal("Sometimes usefull to share info to report issue."))
                .setDefaultValue(Cart.CONFIG.Debug_Stuff.isDebugRendered)
                .setSaveConsumer((val)->{Cart.CONFIG.Debug_Stuff.isDebugRendered = val;})
                .build()
        );
        SSDebug.add(entryBuilder.startBooleanToggle(Text.literal("Experimental Tnt Mixin"),Cart.CONFIG.Debug_Stuff.isUsingTestTntMixin)
                .setTooltip(Text.literal("Enables the test tnt mixin,which add a new instance once tnt explodes"),Text.literal("Usefull to test work of instances."))
                .setDefaultValue(Cart.CONFIG.Debug_Stuff.isUsingTestTntMixin)
                .setSaveConsumer((val)->{Cart.CONFIG.Debug_Stuff.isUsingTestTntMixin = val;})
                .build()
        );
        ScreenShake.addEntry(SSDebug.build());
        builder.transparentBackground();
        return builder;
    }
}
