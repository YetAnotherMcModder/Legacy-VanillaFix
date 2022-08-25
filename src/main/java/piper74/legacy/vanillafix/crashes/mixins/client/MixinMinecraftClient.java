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
import net.minecraft.client.MouseInput;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.render.ClientTickTracker;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.resource.ResourcePackLoader;
import net.minecraft.client.resource.language.LanguageManager;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.Window;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.resource.DefaultResourcePack;
import net.minecraft.resource.ReloadableResourceManager;
import net.minecraft.resource.ReloadableResourceManagerImpl;
import net.minecraft.resource.ResourcePack;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import net.minecraft.util.MetadataSerializer;
import net.minecraft.util.ThreadExecutor;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.snooper.Snoopable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.spongepowered.asm.mixin.*;
import piper74.legacy.vanillafix.LegacyVanillaFix;
import piper74.legacy.vanillafix.config.LegacyVanillaFixConfig;
import piper74.legacy.vanillafix.util.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

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

	@Shadow
	private Framebuffer fbo;
	@Shadow public MouseInput mouse;

	@Shadow public abstract void updateDisplay();

	@Shadow protected abstract void setGlErrorMessage(String message);

	@Shadow private int attackCooldown;

	@Mutable
	@Final
	@Shadow
	private File resourcePackDir;

	@Mutable
	@Final
	@Shadow
	public File runDirectory;

	@Shadow
	private void initializeTimerHackThread() {}

	@Shadow
	public GameRenderer gameRenderer;

	@Shadow
	private void registerMetadataSerializers() {}

	@Shadow
	public WorldRenderer worldRenderer;

	@Shadow
	private ClientTickTracker tricker = new ClientTickTracker(20.0f);

	@Shadow public boolean skipGameRender;

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
         //this.printCrashReport(addSystemDetailsToCrashReport(crashReport2));
         //return;
		 //this.stop();
		  if(config.catchInitCrashes) {
			  displayInitErrorScreen(crashReport2);
		  } else
		  {
			  this.printCrashReport(addSystemDetailsToCrashReport(crashReport2));
			  this.stop();
		  }
		  return;
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
            runGuiLoop(new GuiCrashScreen(report));
        } catch (Throwable t) {
            // The crash screen has crashed. Report it normally instead.
            LOGGER.error("An uncaught exception occurred while displaying the crash screen, making normal report instead", t);
            //displayCrashReport(report);
			printCrashReport(report);
            System.exit(report.getFile() != null ? -1 : -2);
        }
    }

	private void runGuiLoop(Screen screen) throws IOException
	{
		openScreen(screen);
		this.focused = true;
		while (running && currentScreen != null && !(currentScreen instanceof TitleScreen)) {
			Window window = new Window(MinecraftClient.getInstance());
			int i = window.getScaleFactor();

			if(Display.isCreated() && Display.isCloseRequested()) System.exit(0);

			textureManager.tick();

			attackCooldown = 10000;
			currentScreen.handleInput();
			currentScreen.getClass().getCanonicalName();
			currentScreen.tick();
			//currentScreen.getClass().getCanonicalName();


			soundManager.tick();

			mouse.updateMouse();

			//currentScreen.

			GlStateManager.pushMatrix();
			GlStateManager.clear(16640);
			fbo.bind(true);
			GlStateManager.enableTexture(); //OG ONE


			GlStateManager.viewPort(0, 0, width, height);

			GlStateManager.clear(256);
			GlStateManager.matrixMode(5889);
			GlStateManager.loadIdentity();

			GlStateManager.ortho(0.0D, window.getScaledWidth(), window.getScaledHeight(), 0, 1000, 3000);

			GlStateManager.matrixMode(5888);
			GlStateManager.loadIdentity();
			GlStateManager.translatef(0, 0, -2000);
			GlStateManager.clear(256);

			int windowWidth = window.getWidth();
			int windowHeight = window.getHeight();

			//GlStateManager.enableBlend();

			currentScreen.render(
					(int) (Mouse.getX() * windowWidth / width),
					(int) (windowHeight - Mouse.getY() * windowHeight / height - 1),
					tricker.tickDelta
			);

			//GlStateManager.disableBlend();

			fbo.endWrite();
			GlStateManager.popMatrix();

			GlStateManager.pushMatrix();
			//fbo.draw(window.getWidth() * i, window.getHeight() * i);
			fbo.draw(width, height);
			GlStateManager.popMatrix();

			//LegacyVanillaFix.LOGGER.info("RunGUILOOP FINISHED!");


			this.updateDisplay();
			Thread.yield();
			Display.sync(60);
			this.setGlErrorMessage("Legacy VanillaFix GUI Loop");
		}
	}

	public void displayInitErrorScreen(CrashReport crashReport) {
		//MinecraftClient.getInstance().
		//crashReport = CrashReport.create(var11, "Initializing game");
		//crashReport.addElement("Initialization");

		CrashUtils.outputReport(crashReport2);
		try {
			options = new GameOptions(MinecraftClient.getInstance(), runDirectory);
			resourcePacks.add(defaultResourcePack);
			initializeTimerHackThread();

			setDefaultIcon();
			setDisplayBounds();
			setPixelFormat();
			GLX.createContext();

			this.fbo = new Framebuffer(width, height, true);
			this.fbo.setClearColor(0.0f, 0.0f, 0.0f, 0.0f);

			registerMetadataSerializers();
			this.loader = new ResourcePackLoader(this.resourcePackDir, new File(runDirectory, "server-resource-packs"), defaultResourcePack, metadataSerializer, options);
			this.resourceManager = new ReloadableResourceManagerImpl(this.metadataSerializer);

			this.languageManager = new LanguageManager(this.metadataSerializer, options.language);
			this.resourceManager.registerListener(languageManager);

			this.textureManager = new TextureManager(this.resourceManager);
			this.resourceManager.registerListener(this.textureManager);

			stitchTextures();

			this.textRenderer = new TextRenderer(this.options, new Identifier("textures/font/ascii.png"), this.textureManager, false);
			this.shadowTextRenderer = new TextRenderer(options, new Identifier("textures/font/ascii_sga.png"), this.textureManager, false);
			this.resourceManager.registerListener(this.textRenderer);
			this.resourceManager.registerListener(this.shadowTextRenderer);

			soundManager = new SoundManager(resourceManager, options);
			resourceManager.registerListener(soundManager);

			// DO NOT INITIALISE THIS, CAUSES FURTHER CRASHING PROBLEMS
			/*
			gameRenderer = new GameRenderer(MinecraftClient.getInstance(), resourceManager);
			resourceManager.registerListener(gameRenderer);

			this.worldRenderer = new WorldRenderer(MinecraftClient.getInstance());
			this.resourceManager.registerListener(this.worldRenderer);
			*/

			mouse = new MouseInput();

			running = true;

			//LegacyVanillaFix.LOGGER.info("DisplayInitErrorScreen ran!");

			runGuiLoop(new GuiInitErrorScreen(crashReport2));
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


