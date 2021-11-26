package piper74.legacy.vanillafix.bugs.mixins;

import net.minecraft.server.MinecraftServer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import piper74.legacy.vanillafix.LegacyVanillaFix;
import piper74.legacy.vanillafix.config.LegacyVanillaFixConfig;

@Mixin(value = MinecraftServer.class, priority = 1)
public class MixinMinecraftServer {
	LegacyVanillaFixConfig config = LegacyVanillaFix.getConfig();
    /**
     * @reason Disable initial world chunk load. This makes world load much faster, but in exchange
     * the player may see incomplete chunks (like when teleporting to a new area).
	 * @author ?
     */
    //@Overwrite
    //protected void prepareWorlds() {}
	@Inject(method = "prepareWorlds", at = @At("HEAD"), cancellable = true)
	protected void prepareWorlds(CallbackInfo ci) {
		if(config.disableInitialChunkLoad) {
			ci.cancel();
		}
	}
}