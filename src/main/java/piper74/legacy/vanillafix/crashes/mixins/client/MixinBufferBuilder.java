package piper74.legacy.vanillafix.crashes.mixins.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.BufferBuilder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import piper74.legacy.vanillafix.util.StateManager;

@Environment(EnvType.CLIENT)
@Mixin(BufferBuilder.class)
public abstract class MixinBufferBuilder implements StateManager.IResettable {
    @Shadow
    private boolean building;

    @Shadow
    public void end() {}

    @Inject(method = "<init>", at = @At(value = "RETURN"))
    public void onInit(int initialCapacity, CallbackInfo ci)
    {
        register();
    }

    @Override
    public void resetState()
    {
        if(building) end();
    }
}
