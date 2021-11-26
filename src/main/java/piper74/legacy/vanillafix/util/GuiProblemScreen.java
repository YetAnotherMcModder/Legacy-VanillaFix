/*
 *This file is modified based on
 *https://github.com/DimensionalDevelopment/VanillaFix/blob/99cb47cc05b4790e8ef02bbcac932b21dafa107f/src/main/java/org/dimdev/vanillafix/crashes/GuiProblemScreen.java
 *The source file uses the MIT License.
 */

package piper74.legacy.vanillafix.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.util.crash.CrashReport;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Environment(EnvType.CLIENT)
public abstract class GuiProblemScreen extends Screen{

    protected final CrashReport report;
    private String hasteLink = null;
    private String modListString;

    public GuiProblemScreen(CrashReport report) {
        this.report = report;
    }

    @Override
    public void init() {
        this.buttons.clear();
        this.buttons.add(new ButtonWidget(1, width / 2 - 50, height / 4 + 120 + 12, 110, 20, I18n.translate("legacy.vanillafix.gui.openCrashReport")));
        this.buttons.add(new ButtonWidget(2, width / 2 - 50 + 115, height / 4 + 120 + 12, 110, 20, I18n.translate("legacy.vanillafix.gui.uploadReportAndCopyLink")));
    }

    @Override
    protected void buttonClicked(ButtonWidget button) {
        if (button.id == 1){
            try {
                CrashUtils.openCrashReport(report);
            } catch (IOException e) {
				button.message = I18n.translate("legacy.vanillafix.gui.failed");
                button.active = false;
                e.printStackTrace();
            }
        }
		
        if(button.id == 2){
            if(hasteLink == null){
                try {
                    //hasteLink = CrashReportUpload.uploadToHastebin("https://www.toptal.com/developers/hastebin", "mccrash", report.asString());
                    hasteLink = CrashReportUpload.uploadToGithubGists(report.asString());
                } catch (IOException e) {
					button.message = I18n.translate("legacy.vanillafix.gui.failed");
                    button.active = false;
                    e.printStackTrace();
                }
            }
            setClipboard(hasteLink);
        }

    }

    @Override
    protected void keyPressed(char Character, int Code) {}

    private static final String MINECRAFT_ID = "minecraft";
    private static final String LOADER_ID = "fabricloader";

    protected String getModListString() {
		if (modListString == null) {
            Set<ModMetadata> suspectedMods = ((PatchedCrashReport) report).getSuspectedMods();
            if (suspectedMods == null) {
                return modListString = I18n.translate("legacy.vanillafix.crashscreen.identificationErrored");
            }
			
			suspectedMods.removeIf(mod -> mod.getId().equals(MINECRAFT_ID) || mod.getId().equals(LOADER_ID));

            List<String> modNames = new ArrayList<>();
            for (ModMetadata mod : suspectedMods) {
                modNames.add(mod.getName());
            }
            if (modNames.isEmpty()) {
                modListString = I18n.translate("legacy.vanillafix.crashscreen.unknownCause");
            } else {
                modListString = StringUtils.join(modNames, ", ");
            }

		}
        return modListString;
    }
}
