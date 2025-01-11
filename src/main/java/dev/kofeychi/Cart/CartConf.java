package dev.kofeychi.Cart;


import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "cart")
public class CartConf implements ConfigData {
    @ConfigEntry.BoundedDiscrete(min = 0,max = 100)
    public float ScreenShakeIntensity = 100;
    @ConfigEntry.Gui.CollapsibleObject
    public DebugStuff Debug_Stuff = new DebugStuff();
    public static class DebugStuff {
        public boolean isInstancesFrozen = false;
        public boolean isDebugRendered = false;
        public boolean isUsingTestTntMixin = false;
    }
}
