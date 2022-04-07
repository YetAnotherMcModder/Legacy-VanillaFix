package piper74.legacy.vanillafix.particlecull.mixins;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import piper74.legacy.vanillafix.particlecull.CullParticle;

@Environment(EnvType.CLIENT)
@Mixin(ParticleManager.class)
public abstract class ParticleManagerMixin {

@Redirect(method = "method_1296", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/Particle;method_1283(Lnet/minecraft/client/render/BufferBuilder;Lnet/minecraft/entity/Entity;FFFFFF)V"))
public void method_1283a(Particle instance, BufferBuilder builder, Entity entity, float f, float g, float h, float i, float j, float k) {
    if(CullParticle.shouldRenderParticle(instance))
        instance.method_1283(builder, entity, f, g, h, i, j, k);
    }

    @Redirect(method = "method_1299", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/Particle;method_1283(Lnet/minecraft/client/render/BufferBuilder;Lnet/minecraft/entity/Entity;FFFFFF)V"))
    public void method_1283b(Particle instance, BufferBuilder builder, Entity entity, float f, float g, float h, float i, float j, float k) {
        if(CullParticle.shouldRenderParticle(instance))
           instance.method_1283(builder, entity, f, g, h, i, j, k);
    }

}
