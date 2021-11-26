// This code was based on
// github.com/FreakingChicken/TransparentSkins/blob/master/src/main/java/me/FreakingChicken/TransparentSkins/client/renderer/entity/RenderPlayerOverride.java

package piper74.legacy.vanillafix.bugs.mixins;

import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin extends LivingEntityRenderer<AbstractClientPlayerEntity> {

    public PlayerEntityRendererMixin(EntityRenderDispatcher dispatcher, EntityModel model, float shadowSize) {
        super(dispatcher, model, shadowSize);
    }

    @Shadow
    private void setModelPose(AbstractClientPlayerEntity abstractClientPlayerEntity) {}


    @Shadow public abstract PlayerEntityModel getModel();

    /**
     * @author piper74
     * @reason Enables semi-transparency on skins
     */

    @Inject(method = "render(Lnet/minecraft/client/network/AbstractClientPlayerEntity;DDDFF)V", at=@At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/PlayerEntityRenderer;setModelPose(Lnet/minecraft/client/network/AbstractClientPlayerEntity;)V"))
    public void enableblend(AbstractClientPlayerEntity abstractClientPlayerEntity, double d, double e, double f, float g, float h, CallbackInfo ci) {
        GlStateManager.enableBlend();
        GlStateManager.blendFuncSeparate(770, 771, 1, 0);
    }

    @Inject(method = "render(Lnet/minecraft/client/network/AbstractClientPlayerEntity;DDDFF)V", at=@At("TAIL"))
    public void disableblend(AbstractClientPlayerEntity abstractClientPlayerEntity, double d, double e, double f, float g, float h, CallbackInfo ci) {
        GlStateManager.disableBlend();
    }


    /**
     * @author piper74
     * @reason Enables semi-transparency on arms
     */
    @Overwrite
    public void method_4121(AbstractClientPlayerEntity abstractClientPlayerEntity) {
        float f = 1.0F;
        GlStateManager.color3f(f, f, f);
        PlayerEntityModel playerEntityModel = this.getModel();
        this.setModelPose(abstractClientPlayerEntity);
        GlStateManager.enableBlend();
        GlStateManager.blendFuncSeparate(770, 771, 1, 0);
        playerEntityModel.field_3797 = 0.0F;
        playerEntityModel.field_3790 = false;
        playerEntityModel.setAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, abstractClientPlayerEntity);
        playerEntityModel.method_3060();
        GlStateManager.disableBlend();
    }

    /**
     * @author piper74
     * @reason Enables semi-transparency on arms
     */
    @Overwrite
    public void method_4122(AbstractClientPlayerEntity abstractClientPlayerEntity) {
        float f = 1.0F;
        GlStateManager.color3f(f, f, f);
        PlayerEntityModel playerEntityModel = this.getModel();
        this.setModelPose(abstractClientPlayerEntity);
        GlStateManager.enableBlend();
        GlStateManager.blendFuncSeparate(770, 771, 1, 0);
        playerEntityModel.field_3790 = false;
        playerEntityModel.field_3797 = 0.0F;
        playerEntityModel.setAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, abstractClientPlayerEntity);
        playerEntityModel.method_3061();
        GlStateManager.disableBlend();
    }

}
