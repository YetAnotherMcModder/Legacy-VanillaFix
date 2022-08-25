/*
 *This file is modified based on
 *https://github.com/DimensionalDevelopment/VanillaFix/blob/99cb47cc05b4790e8ef02bbcac932b21dafa107f/src/main/java/org/dimdev/vanillafix/crashes/GuiInitErrorScreen.java
 *The source file uses the MIT License.
 */


package piper74.legacy.vanillafix.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.OptionButtonWidget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.util.crash.CrashReport;
import org.spongepowered.asm.mixin.Unique;

import java.io.IOException;

// This screen is currently unused

@Environment(EnvType.CLIENT)
public class GuiInitErrorScreen extends GuiProblemScreen{

    public GuiInitErrorScreen(CrashReport report) {
        super(report);
    }

    private int CrashReportNameWidth = 0;
    private int CrashReportNameWidth2 = 0;
    private int CrashReportName_Height = 0;

    private TextRenderer getFontRenderer() {
        return this.client.textRenderer;
    }

    @Override
    public void init() {
        //mc.setIngameNotInFocus();
        client.focused = false;
        super.init();

        if (report != null) {
            CrashReportNameWidth = this.textRenderer.getStringWidth(report.getFile().getName());
            CrashReportNameWidth2 = (this.width / 2) - (CrashReportNameWidth / 2);
        }

        this.buttons.clear();
        this.buttons.add(new OptionButtonWidget(0, width / 2 - 155, height / 4 + 120 + 12, 154, 20, I18n.translate("menu.quit")));
        this.buttons.add(new OptionButtonWidget(1, width / 2 - 155 + 160, height / 4 + 120 + 12, 150, 20, I18n.translate("legacy.vanillafix.gui.uploadReportAndCopyLink")));

        if(report == null)
        {
            this.buttons.get((int)1).active = false;
        }
    }

    @Override
    protected void buttonClicked(ButtonWidget button) {
        super.buttonClicked(button);

        if(button.id == 0)
        {
            System.exit(-1);
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
        this.drawCenteredString(this.textRenderer, I18n.translate("legacy.vanillafix.initerrorscreen.title"), width / 2, height / 4 - 40, 0xFFFFFF);

        int textColor = 0xD0D0D0;
        int x = width / 2 - 155;
        int y = height / 4;

        this.getFontRenderer().draw(I18n.translate("legacy.vanillafix.initerrorscreen.summary"), x, y, textColor);
        this.getFontRenderer().draw(I18n.translate("legacy.vanillafix.crashscreen.paragraph1.line1"), x, y += 18, textColor);

        this.drawCenteredString(this.textRenderer, getModListString(), width / 2, y += 11, 0xE0E000);

        this.getFontRenderer().drawWithShadow(I18n.translate("legacy.vanillafix.crashscreen.paragraph2.line1"), x, y += 11, textColor);

        this.drawCenteredString(this.textRenderer, report != null && report.getFile() != null ? "\u00A7n" + report.getFile().getName() : I18n.translate("legacy.vanillafix.crashscreen.reportSaveFailed"), width / 2, y += 11, 0x00FF00);
        CrashReportName_Height = y;

        this.getFontRenderer().drawWithShadow(I18n.translate("legacy.vanillafix.initerrorscreen.paragraph3.line1"), x, y += 12, textColor);
        this.getFontRenderer().drawWithShadow(I18n.translate("legacy.vanillafix.initerrorscreen.paragraph3.line2"), x, y += 9, textColor);
        this.getFontRenderer().drawWithShadow(I18n.translate("legacy.vanillafix.initerrorscreen.paragraph3.line3"), x, y += 9, textColor);
        this.getFontRenderer().drawWithShadow(I18n.translate("legacy.vanillafix.initerrorscreen.paragraph3.line4"), x, y += 9, textColor);
        super.render(mouseX, mouseY, tickDelta);
    }
}
