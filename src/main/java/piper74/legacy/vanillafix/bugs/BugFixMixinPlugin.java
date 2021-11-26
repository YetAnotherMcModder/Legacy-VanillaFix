package piper74.legacy.vanillafix.bugs;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import piper74.legacy.vanillafix.LegacyVanillaFix;
import piper74.legacy.vanillafix.config.LegacyVanillaFixConfig;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

public class BugFixMixinPlugin implements IMixinConfigPlugin {
	
	LegacyVanillaFixConfig config = LegacyVanillaFix.getConfig();
	
	@Override
	public void onLoad(String mixinPackage) {
	}

	@Override
	public String getRefMapperConfig() {
		return null;
	}

	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
		if (mixinClassName.contains("piper74.legacy.vanillafix.bugs.mixins.ClientPlayNetworkHandlerMixin")) {
			if (!config.fasterDimensionChange)
				return false;
		} else if (mixinClassName.contains("piper74.legacy.vanillafix.bugs.mixins.ClientPlayerEntityMixin")) {
			if (!config.allowGUIsInNetherPortals)
				return false;
		} else if (mixinClassName.contains("piper74.legacy.vanillafix.bugs.mixins.MixinMinecraftServer")) {
			if (!config.disableInitialChunkLoad)
				return false;
		} else if (mixinClassName.contains("piper74.legacy.vanillafix.bugs.mixins.GameRendererMixin")) {
			if (!config.F5Fix)
				return false;
		} else if (mixinClassName.contains("piper74.legacy.vanillafix.bugs.mixins.F11FixMixin")) {
			if (!config.F11Fix)
				return false;
		} else if (mixinClassName.contains("piper74.legacy.vanillafix.bugs.mixins.PlayerEntityRendererMixin") || mixinClassName.contains("piper74.legacy.vanillafix.bugs.mixins.SkullBlockEntityRendererMixin")) {
			if (!config.enableSkinSemiTransparency)
				return false;
			}
		return true;
	}

	@Override
	public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
	}

	@Override
	public List<String> getMixins() {
		return null;
	}

	@Override
	public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
	}

	@Override
	public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
	}
}
