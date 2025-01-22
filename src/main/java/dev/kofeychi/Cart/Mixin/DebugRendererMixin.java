package dev.kofeychi.Cart.Mixin;

import dev.kofeychi.Cart.Cart;
import dev.kofeychi.Cart.SSModule.SSHandler;
import dev.kofeychi.Cart.Test;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.debug.DebugRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DebugRenderer.class)
public class DebugRendererMixin {
    @Inject(at = @At(value = "HEAD"),method = "render",cancellable = true)
    public void Cart$render(MatrixStack matrices, VertexConsumerProvider.Immediate vertexConsumers, double cameraX, double cameraY, double cameraZ, CallbackInfo info){
        if (Cart.CONFIG.Debug_Stuff.isDebugRendered) {
            SSHandler.DebugRenderer(matrices, vertexConsumers, cameraX, cameraY, cameraZ);
            Test.DebugRenderer(matrices, vertexConsumers, cameraX, cameraY, cameraZ);
        }
    }
}
