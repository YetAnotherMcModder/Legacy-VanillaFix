// Code is from
// https://github.com/comp500/BorderlessMining/blob/ef93ff0409d03211c4e7384dfad31e89edec5268/src/main/java/link/infra/borderlessmining/mixin/F11FixMixin.java

package piper74.legacy.vanillafix.bugs.mixins;

import net.minecraft.client.options.GameOptions;
import net.minecraft.client.MinecraftClient;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * This Mixin fixes a bug in Keyboard that prevents F11 changes from being saved.
 * See: https://bugs.mojang.com/browse/MC-175431
 */
@Mixin(MinecraftClient.class)
public class F11FixMixin {

	@Shadow
	public GameOptions options;

	@Inject(method = "toggleFullscreen", at = @At(value = "FIELD", target = "Lnet/minecraft/client/options/GameOptions;fullscreen:Z", opcode = Opcodes.PUTFIELD, ordinal = 0, shift = At.Shift.BY, by = 1))
	public void mc175431(CallbackInfo ci) {
		this.options.save();
	}
}