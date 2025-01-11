package dev.kofeychi.Cart.Mixin;

import dev.kofeychi.Cart.Cart;
import dev.kofeychi.Cart.SSModule.EnabledAffections;
import dev.kofeychi.Cart.SSModule.SSHandler;
import dev.kofeychi.Cart.SSModule.SSInstance;
import dev.kofeychi.Cart.SSModule.SSModes;
import dev.kofeychi.Cart.Util.Easing;
import net.minecraft.entity.TntEntity;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TntEntity.class)
public class TestTntMixin {
    @Inject(at = @At(value = "HEAD"),method = "explode")
    private void Testexplode(CallbackInfo info){
        if (Cart.isUsingTntMixin) {
            SSHandler.tntmixinfunc(((TntEntity)(Object)this));
        }
    }
}
