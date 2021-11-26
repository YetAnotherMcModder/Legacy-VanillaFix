/*
 *This file is modified based on
 *https://github.com/DimensionalDevelopment/VanillaFix/blob/99cb47cc05b4790e8ef02bbcac932b21dafa107f/src/main/java/org/dimdev/vanillafix/crashes/CrashUtils.java
 *The source file uses the MIT License.
 */

package piper74.legacy.vanillafix.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.crash.CrashReport;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.fabricmc.loader.api.FabricLoader;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Runemoro
 */
public class CrashUtils {
	
	private static final Logger LOGGER = LogManager.getLogger();
	private static boolean isClient;
	
	static {
        try {
            isClient = MinecraftClient.getInstance() != null;
        } catch (NoClassDefFoundError e) {
            isClient = false;
        }
    }
	
    /**
     * @author Runemoro
     * @param report
     */
    public static void outputReport(CrashReport report) {
        try {
            if (report.getFile() == null) {
                String reportName = "crash-";
                reportName += new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss").format(new Date());
                reportName += isClient && MinecraftClient.getInstance().isOnThread() ? "-client" : "-server";
                reportName += ".txt";

                File reportsDir = isClient ? new File(FabricLoader.getInstance().getGameDirectory(), "crash-reports") : new File("crash-reports");
                File reportFile = new File(reportsDir, reportName);

                report.writeToFile(reportFile);
            }
        } catch (Throwable e) {
            LOGGER.fatal("Failed saving report", e);
        }

        LOGGER.fatal("Minecraft ran into a problem! " + (report.getFile() != null ? "Report saved to: " + report.getFile() : "Crash report could not be saved.")
                + "\n" + report.asString());
    }

// This code is from
// https://github.com/vfyjxf/BetterCrashes/blob/da95d1f801d83b11479511b01d9d83bc822dfa2b/src/main/java/vfyjxf/bettercrashes/utils/CrashUtils.java

    /**
     * @author vfyjxf
     * @param crashReport
     * @throws IOException
     */
    public static void openCrashReport(CrashReport crashReport) throws IOException {

        if(!Desktop.isDesktopSupported()){
            LOGGER.error("Desktop is not supported");
            return;
        }
        File report = crashReport.getFile();
        if(report.exists()){
            Desktop.getDesktop().open(report);
        }
    }

}
