/*
 *This file is modified based on
 *https://github.com/DimensionalDevelopment/VanillaFix/blob/99cb47cc05b4790e8ef02bbcac932b21dafa107f/src/main/java/org/dimdev/vanillafix/crashes/GuiCrashScreen.java
 *The source file uses the MIT License.
 */

package piper74.legacy.vanillafix.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.OptionButtonWidget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.util.crash.CrashReport;

import java.io.IOException;


@Environment(EnvType.CLIENT)
public class GuiCrashScreen extends GuiProblemScreen {

    private int CrashReportNameWidth = 0;
    private int CrashReportNameWidth2 = 0;
    private int CrashReportName_Height = 0;

    public GuiCrashScreen(CrashReport report) {
        super(report);
    }

    @Override
    public void init() {
        super.init();

        if (report != null) {
            CrashReportNameWidth = this.textRenderer.getStringWidth(report.getFile().getName());
            CrashReportNameWidth2 = (this.width / 2) - (CrashReportNameWidth / 2);
        }

        OptionButtonWidget mainMenuButton = new OptionButtonWidget(0, width / 2 - 155, height / 4 + 120 + 12, 150, 20, I18n.translate("legacy.vanillafix.gui.toTitle"));
        this.buttons.add(mainMenuButton);
    }

    @Override
    protected void buttonClicked(ButtonWidget button) {
        super.buttonClicked(button);
        if (button.id == 0) {
            this.client.openScreen(new TitleScreen());
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int button) {
        super.mouseClicked(mouseX, mouseY, button);

        //if (report.getFile() != null) {
            if (mouseX > this.CrashReportNameWidth2 && mouseX < this.CrashReportNameWidth2 + this.CrashReportNameWidth && mouseY > this.CrashReportName_Height && mouseY < this.CrashReportName_Height + 11) {
                try {
                    CrashUtils.openCrashReport(report);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        //}
    }

    @Override
    public void render(int mouseX, int mouseY, float tickDelta) { // TODO: localize number of lines
        this.renderBackground();
        this.drawCenteredString(this.textRenderer, I18n.translate("legacy.vanillafix.crashscreen.title"), width / 2, height / 4 - 40, 0xFFFFFF);

        int textColor = 0xD0D0D0;
        int x = width / 2 - 155;
        int y = height / 4;

        this.drawWithShadow(this.textRenderer, I18n.translate("legacy.vanillafix.crashscreen.summary"), x, y, textColor);
        this.drawWithShadow(this.textRenderer, I18n.translate("legacy.vanillafix.crashscreen.paragraph1.line1"), x, y += 18, textColor);

        this.drawCenteredString(this.textRenderer, getModListString(), width / 2, y += 11, 0xE0E000);

        this.drawWithShadow(this.textRenderer, I18n.translate("legacy.vanillafix.crashscreen.paragraph2.line1"), x, y += 11, textColor);

        this.drawCenteredString(this.textRenderer, report != null && report.getFile() != null ? "\u00A7n" + report.getFile().getName() : I18n.translate("legacy.vanillafix.crashscreen.reportSaveFailed"), width / 2, y += 11, 0x00FF00);
        CrashReportName_Height = y;

        this.drawWithShadow(this.textRenderer, I18n.translate("legacy.vanillafix.crashscreen.paragraph3.line1"), x, y += 12, textColor);
        this.drawWithShadow(this.textRenderer, I18n.translate("legacy.vanillafix.crashscreen.paragraph3.line2"), x, y += 9, textColor);
        this.drawWithShadow(this.textRenderer, I18n.translate("legacy.vanillafix.crashscreen.paragraph3.line3"), x, y += 9, textColor);
        this.drawWithShadow(this.textRenderer, I18n.translate("legacy.vanillafix.crashscreen.paragraph3.line4"), x, y += 9, textColor);
        super.render(mouseX, mouseY, tickDelta);
    }
}
