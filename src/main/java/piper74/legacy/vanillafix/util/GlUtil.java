package piper74.legacy.vanillafix.util;

import com.mojang.blaze3d.platform.GlStateManager;
import org.lwjgl.opengl.*;

public class GlUtil {
    public static void resetState() {
//        // Reset texture
        GlStateManager.bindTexture(0);
        GlStateManager.disableTexture();

        // Reset depth
        GlStateManager.disableDepthTest();
        GlStateManager.depthFunc(513);
        GlStateManager.depthMask(true);

        // Reset blend mode
        GlStateManager.disableBlend();
        GlStateManager.blendFunc(1, 0);
        GlStateManager.blendFuncSeparate(1, 0, 1, 0);

        // Reset polygon offset
        GlStateManager.polygonOffset(0.0F, 0.0F);
        GlStateManager.disablePolyOffset();

        // Reset color logic
        GlStateManager.disableColorLogic();
        GlStateManager.logicOp(5387);
        // Disable lightmap
        GlStateManager.activeTexture(GL13.GL_TEXTURE1);
        GlStateManager.disableTexture();

        GlStateManager.activeTexture(GL13.GL_TEXTURE0);

		/*
        // Reset texture parameters
        GlStateManager.glTexParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GlStateManager.texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST_MIPMAP_LINEAR);
        GlStateManager.texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        GlStateManager.texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
        GlStateManager.texParameter(GL11.GL_TEXTURE_2D, GL12.GL_TEXTURE_MAX_LEVEL, 1000);
        GlStateManager.texParameter(GL11.GL_TEXTURE_2D, GL12.GL_TEXTURE_MAX_LOD, 1000);
        GlStateManager.texParameter(GL11.GL_TEXTURE_2D, GL12.GL_TEXTURE_MIN_LOD, -1000);
        GlStateManager.texParameter(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, 0);
		*/

        GlStateManager.colorMask(true, true, true, true);
        GlStateManager.clearDepth(1.0D);
        //GlStateManager.glLineWidth(1.0F);
        GlStateManager.polygonOffset(GL11.GL_FRONT, GL11.GL_FILL);
        GlStateManager.polygonOffset(GL11.GL_BACK, GL11.GL_FILL);

        GlStateManager.enableTexture();
        GlStateManager.clearDepth(1.0D);
        GlStateManager.enableDepthTest();
        GlStateManager.depthFunc(515);
        GlStateManager.enableCull();
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }
}
