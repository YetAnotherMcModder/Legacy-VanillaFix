package piper74.legacy.vanillafix.particlecull;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.render.CameraView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import piper74.legacy.vanillafix.LegacyVanillaFix;
import piper74.legacy.vanillafix.config.LegacyVanillaFixConfig;

// Used

public class CullParticle {
    private static Logger LOGGER = LogManager.getLogger("legacyvanillafix");
    static LegacyVanillaFixConfig config = LegacyVanillaFix.getConfig();

    //public static boolean shouldRenderParticle(Particle particle) {
        public static boolean shouldRenderParticle(Particle instance)
        {
        if(!config.cullParticles) {
            return true;
        }

        CameraView camera = ((ICameraView) MinecraftClient.getInstance().worldRenderer).getCamera();

        if(camera != null)
        {
            return camera.isBoxInFrustum(instance.getBoundingBox());
        }

        return false;

    }
}