package piper74.legacy.vanillafix.bugs.mixins;

import net.minecraft.server.integrated.IntegratedServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.concurrent.Future;
import com.google.common.util.concurrent.Futures;
import java.lang.Object;

@Mixin(IntegratedServer.class)
public abstract class MixinIntegratedServer {
    /**
     * @reason If the everyone leaves the integrated server, and a shutdown is then
     * initiated, there is a possibility that by the time initiateShutdown is called,
     * the server is still running but will shut down before running scheduled tasks.
     * Don't wait for the task to run, since that may never happen.
     */
	@SuppressWarnings(value = "all")
    @Redirect(method = "stopRunning", at = @At(value = "INVOKE", target = "Lcom/google/common/util/concurrent/Futures;getUnchecked(Ljava/util/concurrent/Future;)Ljava/lang/Object;", ordinal = 0))
    private <V> V getUnchecked(Future<V> future) {
        return null;
    }
}