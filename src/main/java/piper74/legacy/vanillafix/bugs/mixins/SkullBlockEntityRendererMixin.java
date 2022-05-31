// This code was based on
// https://github.com/FreakingChicken/TransparentSkins/blob/master/src/main/java/me/FreakingChicken/TransparentSkins/client/renderer/tileentity/TileEntitySkullRendererOverride.java

package piper74.legacy.vanillafix.bugs.mixins;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.block.entity.SkullBlockEntityRenderer;
import net.minecraft.util.math.Direction;
import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.platform.GlStateManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(SkullBlockEntityRenderer.class)
public class SkullBlockEntityRendererMixin {
    @Inject(method = "render(FFFLnet/minecraft/util/math/Direction;FILcom/mojang/authlib/GameProfile;I)V", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/GlStateManager;enableAlphaTest()V"))
    public void method_10108(float f, float g, float h, Direction direction, float j, int k, GameProfile profile, int i, CallbackInfo ci) {
            GlStateManager.enableBlend();
            GlStateManager.blendFuncSeparate(770, 771, 1, 0);
        }

    @Inject(method = "render(FFFLnet/minecraft/util/math/Direction;FILcom/mojang/authlib/GameProfile;I)V", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/GlStateManager;popMatrix()V", ordinal = 0))
    public void disableblend(float f, float g, float h, Direction direction, float j, int k, GameProfile profile, int i, CallbackInfo ci) {
        GlStateManager.disableBlend();
    }
    }
