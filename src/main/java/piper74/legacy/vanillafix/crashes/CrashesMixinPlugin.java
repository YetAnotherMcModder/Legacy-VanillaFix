package piper74.legacy.vanillafix.crashes;

import java.util.List;
import java.util.Set;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.Version;
import net.fabricmc.loader.api.VersionParsingException;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import piper74.legacy.vanillafix.LegacyVanillaFix;

public class CrashesMixinPlugin implements IMixinConfigPlugin {

    @Override
    public void onLoad(String mixinPackage) {
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (mixinClassName.contains("piper74.legacy.vanillafix.crashes.mixins.client.MixinBufferBuilder")) {
            try {
                // Disable bufferbuilder mixin in 1.8 (and other subversions) because it causes a crash
                return (Version.parse("1.8.9").equals(FabricLoader.getInstance().getModContainer("minecraft").get().getMetadata().getVersion()));
            } catch (VersionParsingException e) {
                throw new RuntimeException(e);
            }
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
