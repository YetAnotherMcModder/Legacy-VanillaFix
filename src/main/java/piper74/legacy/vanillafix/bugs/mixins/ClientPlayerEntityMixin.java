package piper74.legacy.vanillafix.bugs.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.ClientPlayerEntity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {
	/**
	 * @reason Enables opening GUIs in nether portals. (see https://bugs.mojang.com/browse/MC-2071)
	 * This works by making minecraft think that GUI pauses the game
	 */
	@Redirect(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Screen;shouldPauseGame()Z"))
	private boolean shouldPauseGame(Screen guiScreen) {
		return true;
	}
}