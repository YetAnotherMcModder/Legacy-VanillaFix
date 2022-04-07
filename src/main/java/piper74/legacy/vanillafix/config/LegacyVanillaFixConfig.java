
package piper74.legacy.vanillafix.config;

import com.google.gson.Gson;
import com.google.gson.stream.JsonWriter;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class LegacyVanillaFixConfig {
		public static final LegacyVanillaFixConfig DEFAULT = new LegacyVanillaFixConfig();
	
	public boolean betterCrashes = true;
	public boolean fasterDimensionChange = true;
	public boolean allowGUIsInNetherPortals = true;
	//public String hasteUrl = "https://hastebin.com";
	public boolean disableInitialChunkLoad = false;
    public boolean F5Fix = true;
    public boolean F11Fix = true;
	public boolean enableSkinSemiTransparency = true;
    public boolean cullParticles = true;
	
	    public static LegacyVanillaFixConfig load() throws IOException {
        Path configFile = FabricLoader.getInstance().getConfigDir().resolve("legacy-vanillafix.json");
        Gson gson = new Gson();

        if (!Files.exists(configFile)) {
            save(new LegacyVanillaFixConfig());
        }

        return gson.fromJson(Files.newBufferedReader(configFile), LegacyVanillaFixConfig.class);
    }
	
	    public static void save(LegacyVanillaFixConfig config) throws IOException {
        Path configFile = FabricLoader.getInstance().getConfigDir().resolve("legacy-vanillafix.json");
        Gson gson = new Gson();
        JsonWriter writer = new JsonWriter(Files.newBufferedWriter(configFile));

        writer.setIndent("    ");
        gson.toJson(gson.toJsonTree(config, LegacyVanillaFixConfig.class), writer);
        writer.close();
    }
	
}
