package piper74.legacy.vanillafix.bugs.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
//import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.MinecraftClient;

import net.minecraft.world.World;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
@Mixin(value = GameRenderer.class, priority = 1500)
public class GameRendererMixin {
	
	/**
	 * @reason Fixes camera getting stuck on transparent blocks
	 * @author piper74
	 */
	@Redirect(method = "transformCamera(F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;rayTrace(Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;)Lnet/minecraft/util/hit/HitResult;"), expect = 0 )
	private HitResult rayTrace(ClientWorld world, Vec3d vec3d, Vec3d vec3d2) {
		return world.rayTrace(vec3d, vec3d2, false, true, false);
	}
}