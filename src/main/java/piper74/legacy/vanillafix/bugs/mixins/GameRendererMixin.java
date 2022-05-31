package piper74.legacy.vanillafix.bugs.mixins;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import piper74.legacy.vanillafix.LegacyVanillaFix;
import piper74.legacy.vanillafix.config.LegacyVanillaFixConfig;

@Environment(EnvType.CLIENT)
@Mixin(value = GameRenderer.class, priority = 1500)
@Unique
public class GameRendererMixin {

	@Shadow private MinecraftClient client;

	LegacyVanillaFixConfig config = LegacyVanillaFix.getConfig();

	/**
	 * @reason Fixes camera getting stuck on transparent blocks
	 * @author piper74
	 */
	@Redirect(method = "transformCamera(F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;rayTrace(Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;)Lnet/minecraft/util/hit/BlockHitResult;"), expect = 0 )
	private BlockHitResult rayTrace(ClientWorld world, Vec3d vec3d, Vec3d vec3d2) {
			if (config.F5Fix) {
				return world.rayTrace(vec3d, vec3d2, false, true, false);
			}
		return world.rayTrace(vec3d, vec3d2, false, false, false);
	}

	@Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;getBrightness(Lnet/minecraft/util/math/BlockPos;)F"))
	private float getBrightness(ClientWorld world, BlockPos pos) {
			if (config.skyDarknessFix) {
				return client.player.getBrightnessAtEyes(1);
			}
		return world.getBrightness(new BlockPos(client.getCameraEntity()));
	}
}