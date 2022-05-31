package piper74.legacy.vanillafix.particlecull.mixins;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.CameraView;
import net.minecraft.entity.Entity;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import piper74.legacy.vanillafix.particlecull.ICameraView;

@Environment(EnvType.CLIENT)
@Mixin(WorldRenderer.class)
public class WorldRendererMixin implements ICameraView {
    @Shadow @Final private static Logger LOGGER;
    @Unique
    CameraView cameraView;

    @Inject(method = "method_9906", at = @At("HEAD"))
    public void method_9906(Entity entity, double d, CameraView cameraView, int i, boolean bl, CallbackInfo ci)
    {
        this.cameraView = cameraView;
    }

    @Override
    public CameraView getCamera()
    {
        return this.cameraView;
    }

}
