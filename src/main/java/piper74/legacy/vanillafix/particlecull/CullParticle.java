package piper74.legacy.vanillafix.particlecull;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.debug.CameraView;
import net.minecraft.client.render.debug.StructureDebugRenderer;
import net.minecraft.entity.Entity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import piper74.legacy.vanillafix.LegacyVanillaFix;

// Unused

public class CullParticle {
    private static Logger LOGGER = LogManager.getLogger("legacyvanillafix");

    public static boolean shouldRenderParticle(Particle particle, BufferBuilder buffer, Entity entity) {
        //Entity ClientCamera = MinecraftClient.getInstance().getCameraEntity();

        CameraView camera = new StructureDebugRenderer();

        if (camera.isBoxInFrustum(particle.getBoundingBox())) {
           LegacyVanillaFix.LOGGER.warn("Particle is visible!");
            return true;
        } else {LegacyVanillaFix.LOGGER.warn("Particle is invisible!");}
        return false;
    }
}