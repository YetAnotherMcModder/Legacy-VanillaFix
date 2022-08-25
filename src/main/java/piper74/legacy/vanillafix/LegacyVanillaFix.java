package piper74.legacy.vanillafix;

import piper74.legacy.vanillafix.config.LegacyVanillaFixConfig;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

public class LegacyVanillaFix implements ModInitializer {
	
	public static final String MOD_ID = "legacyvanillafix";
	public static final Logger LOGGER = LogManager.getLogger();
	public static final ModContainer MOD = FabricLoader.getInstance().getModContainer("legacyvanillafix").orElseThrow(IllegalStateException::new);
	private static LegacyVanillaFixConfig config;
	
	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		initConfig();
		//LOGGER.warn("Initalizing Legacy VanillaFix");
		//System.out.println("Hello Fabric world!");

		// DEBUG INIT CRASH
		//throw new RuntimeException("Legacy VanillaFix init test crash");
	}
	
		private static void initConfig() {
		try {
			config = LegacyVanillaFixConfig.load();
		} catch (IOException e) {
			throw new RuntimeException("Error loading Legacy VanillaFix config!", e);
		}
	}
	
	public static LegacyVanillaFixConfig getConfig() {
		if (config == null) {
			initConfig();
		}
		return config;
	}
	
}
