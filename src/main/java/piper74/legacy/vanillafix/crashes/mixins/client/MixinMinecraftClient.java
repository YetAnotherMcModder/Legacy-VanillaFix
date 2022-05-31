/*
 *This file is modified based on
 *https://github.com/vfyjxf/BetterCrashes/blob/da95d1f801d83b11479511b01d9d83bc822dfa2b/src/main/java/vfyjxf/bettercrashes/mixins/client/MixinMinecraft.java
 *The source file uses the MIT License.
 */

package piper74.legacy.vanillafix.crashes.mixins.client;

import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.resource.AssetsIndex;
import net.minecraft.client.resource.ResourcePackLoader;
import net.minecraft.client.resource.language.LanguageManager;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.Window;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.options.GameOptions;
import net.minecraft.resource.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.MetadataSerializer;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashException;
import net.minecraft.world.level.LevelInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
//import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.*;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

//import net.minecraft.network.ClientConnection;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import piper74.legacy.vanillafix.LegacyVanillaFix;
import piper74.legacy.vanillafix.config.LegacyVanillaFixConfig;

import piper74.legacy.vanillafix.util.CrashUtils;
import piper74.legacy.vanillafix.util.GuiCrashScreen;
import piper74.legacy.vanillafix.util.GuiInitErrorScreen;
import piper74.legacy.vanillafix.util.StateManager;
import piper74.legacy.vanillafix.util.GlUtil;
import net.minecraft.text.LiteralText;
import net.minecraft.client.gui.hud.InGameHud;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.font.TextRenderer;

import net.minecraft.util.ThreadExecutor;
import net.minecraft.util.snooper.Snoopable;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Queue;

/**
 * @author Runemoro
 */
@Environment(EnvType.CLIENT)
@Mixin(MinecraftClient.class)
public abstract class MixinMinecraftClient implements ThreadExecutor, Snoopable {
	
	LegacyVanillaFixConfig config = LegacyVanillaFix.getConfig();

	@Final
	@Mutable
	@Shadow
    private static Logger LOGGER = LogManager.getLogger();
	@Shadow
    volatile boolean running;
	@Shadow
	private CrashReport crashReport;
	@Shadow
	private void initializeGame() throws LWJGLException, IOException {}
	@Shadow
	public static byte[] memoryReservedForCrash;
	
	@Shadow
	public abstract CrashReport addSystemDetailsToCrashReport(CrashReport crashReport);
	
	@Shadow
	public abstract void stop();
	
	@Shadow
	private void runGameLoop() {}
	
	@Shadow
	private boolean crashed;
	
	@Shadow
	public GameOptions options;
	
	@Shadow
	public abstract void openScreen(Screen screen);
	
	@Shadow
	public ClientWorld world;
	
	//@Shadow
	private CrashReport crashReport2;
	
	@Shadow
	public InGameHud inGameHud;
	
	@Shadow
	private long f3CTime;
	
	@Shadow
	public abstract ClientPlayNetworkHandler getNetworkHandler();

	@Shadow
	public void connect(ClientWorld world) {}

	@Shadow public boolean focused;
	@Shadow private boolean glErrors;
	private static int clientCrashCount = 0;
    private static int serverCrashCount = 0;

	@Shadow
	private ReloadableResourceManager resourceManager;

	@Shadow
	private ResourcePackLoader loader;

	@Shadow
	private LanguageManager languageManager;

	@Final
	@Mutable
	@Shadow
	private MetadataSerializer metadataSerializer = new MetadataSerializer();

	@Shadow
	public void stitchTextures() {}

	@Shadow
	public TextRenderer textRenderer;
	@Shadow
	public TextRenderer shadowTextRenderer;
	@Shadow
	public Screen currentScreen;
	@Shadow
	private TextureManager textureManager;
	@Shadow
	private SoundManager soundManager;

	@Shadow
	public int width;

	@Shadow
	public int height;

	@Shadow
	private void setDefaultIcon() {}

	@Shadow
	private void setDisplayBounds() throws LWJGLException {}

	@Shadow
	private void setPixelFormat() throws LWJGLException {}

	@Mutable
	@Final
	@Shadow
	private DefaultResourcePack defaultResourcePack;

	@Shadow @Final private List<ResourcePack> resourcePacks;

	/**
     * @author Runemoro
     * @reason Overwrite Minecraft.run()
     */
	@Overwrite
	public void run(){
	
	      running = true;

      //CrashReport crashReport2;
      try {
         this.initializeGame();
      } catch (Throwable var11) {
         crashReport2 = CrashReport.create(var11, "Initializing game");
         crashReport2.addElement("Initialization");
         this.printCrashReport(addSystemDetailsToCrashReport(crashReport2));
         //return;
		 this.stop();
		  //displayInitErrorScreen(crashReport2);
		  //return;
      }



	  
	  while(running) {
			  if (!crashed || crashReport == null) {
				  try {
					  runGameLoop();
				  } catch (CrashException e) {
                     clientCrashCount++;
					 addSystemDetailsToCrashReport(e.getReport());
					 addInfoToCrash(e.getReport());
					 //if (config.betterCrashes)
					 resetGameState();
						//else
						//cleanHeap();
					 LOGGER.fatal("Reported exception thrown!", e);
					 if (config.betterCrashes) {
					 displayCrashScreen(e.getReport(), clientCrashCount);
					 } else {
					 printCrashReport(e.getReport());
					 this.stop();
					 }
				  } catch (Throwable e) {
                     clientCrashCount++;
					 CrashReport report = new CrashReport("Unexpected error", e);
					 addSystemDetailsToCrashReport(report);
					 addInfoToCrash(report);
					 //if (config.betterCrashes)
					 resetGameState();
						//else
						//cleanHeap();
					 LOGGER.fatal("Unreported exception thrown!", e);
					 if (config.betterCrashes) {
					 displayCrashScreen(report, clientCrashCount);
					 } else {
					 printCrashReport(report);
					 this.stop();
					 }
				  }
			  } else {
			  serverCrashCount++;
			  addInfoToCrash(crashReport2);
			  // FREE MEMORY HERE!
			  //Runtime.getRuntime().freeMemory();
			  cleanHeap();
			  displayCrashScreen(crashReport2, serverCrashCount);
			  crashed = false;
			  crashReport2 = null;
			  }
		  }
		  
		  this.stop();
	  }
			 
    private static void addInfoToCrash(CrashReport report) {
        report.getSystemDetailsSection().add("Client Crashes Since Restart", () -> String.valueOf(clientCrashCount));
        report.getSystemDetailsSection().add("Integrated Server Crashes Since Restart", () -> String.valueOf(serverCrashCount));
    }			 
	
	 /**
     * @author Runemoro
     */
    public void resetGameState() {
        try {
            // Free up memory such that this works properly in case of an OutOfMemoryError
            int originalMemoryReserveSize = -1;
            try { // In case another mod actually deletes the memoryReservedForCrash field
                if (memoryReservedForCrash != null) {
                    originalMemoryReserveSize = memoryReservedForCrash.length;
                    memoryReservedForCrash = new byte[0];
                }
            } catch (Throwable ignored) {}

            StateManager.resetStates();

			if (getNetworkHandler() != null) {
			getNetworkHandler().getClientConnection().disconnect(new LiteralText(String.format("[%s] Client crashed", "Legacy VanillaFix")));
			}

         this.world.disconnect();
         this.connect((ClientWorld)null);
			
            //field_152351_aB.clear(); // TODO: Figure out why this isn't necessary for vanilla disconnect
			
		GlUtil.resetState();

            if (originalMemoryReserveSize != -1) {
                try {
                    memoryReservedForCrash = new byte[originalMemoryReserveSize];
                } catch (Throwable ignored) {}
            }
            System.gc();
        } catch (Throwable t) {
            LOGGER.error("Failed to reset state after a crash", t);
            try {
                StateManager.resetStates();
				GlUtil.resetState();
            } catch (Throwable ignored) {}
        }
    }

	/**
	 * @author Runemoro
	 * @reason Disconnect from the current world and free memory, using a memory reserve
	 * to make sure that an OutOfMemory doesn't happen while doing this.
	 * <p>
	 * Bugs Fixed:
	 * - https://bugs.mojang.com/browse/MC-128953
	 * - Memory reserve not recreated after out-of memory
	 **/
	@Overwrite
	public void cleanHeap() {
		resetGameState();
	}
	
	 /**
     * @author Runemoro
     * @param report, crashCount
     */
    public void displayCrashScreen(CrashReport report, int crashCount) {
        try {
            CrashUtils.outputReport(report);
            // Reset crashed, f3CTime
            crashed = false;
			this.f3CTime = -1L;

            if (crashCount > 19) {
                throw new IllegalStateException("The game has crashed an excessive amount of times!");
            }
            // Vanilla does this when switching to main menu but not our custom crash screen
            // nor the out of memory screen (see https://bugs.mojang.com/browse/MC-128953)
            options.debugEnabled = false;
			inGameHud.getChatHud().clear();


            // Display the crash screen
            openScreen(new GuiCrashScreen(report));
        } catch (Throwable t) {
            // The crash screen has crashed. Report it normally instead.
            LOGGER.error("An uncaught exception occurred while displaying the crash screen, making normal report instead", t);
            //displayCrashReport(report);
			printCrashReport(report);
            System.exit(report.getFile() != null ? -1 : -2);
        }
    }


	public void displayInitErrorScreen() {
		//MinecraftClient.getInstance().
		//crashReport = CrashReport.create(var11, "Initializing game");
		//crashReport.addElement("Initialization");

		CrashUtils.outputReport(crashReport2);
		try {

			this.openScreen(new GuiInitErrorScreen(crashReport2));
		} catch (Throwable t) {
			CrashReport additionalReport = CrashReport.create(t, "Displaying init error screen");
			LOGGER.error("An uncaught exception occured while displaying the init error screen, making normal report instead", t);
			printCrashReport(additionalReport);
			System.exit(additionalReport.getFile() != null ? -1 : -2);
		}
	}

	 /**
     * @author Runemoro
     * @reason
     * @param report
     */
    @Overwrite
    public void printCrashReport(CrashReport report) {
        CrashUtils.outputReport(report);
    }

	/****
	 * @author ZombieHDGaming
	 * @reason removes a call to system.gc() to make world loading faster
	****/

	// breaks loading of singleplayer worlds
	/*
	@Inject(method="startGame", at = @At(value = "INVOKE", target = "Ljava/lang/System;gc()V"), cancellable = true)
	public void startGame(String worldName, String string, LevelInfo levelInfo, CallbackInfo ci) {
		ci.cancel();
	}
	*/

}


