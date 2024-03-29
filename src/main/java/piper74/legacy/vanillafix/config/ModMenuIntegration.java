package piper74.legacy.vanillafix.config;

import io.github.prospector.modmenu.api.ModMenuApi;
import piper74.legacy.vanillafix.LegacyVanillaFix;
import net.minecraft.client.gui.screen.Screen;

// Note:
// The way ModMenuApi was implemented it messes with
// IntelliJ's Intellisense, just ignore it

import java.util.function.Function;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public Function<Screen, ? extends Screen> getConfigScreenFactory() {
        return ConfigScreen::new;
    }

    @Override
    public String getModId() {
        return LegacyVanillaFix.MOD_ID;
    }
}
