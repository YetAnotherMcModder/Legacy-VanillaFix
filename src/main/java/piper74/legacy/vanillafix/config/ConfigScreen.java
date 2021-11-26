// This Config Menu code was based on TinyConfig
// github.com/Minenash/TinyConfig/

package piper74.legacy.vanillafix.config;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
//import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.resource.language.I18n;
import org.lwjgl.input.Keyboard;
import piper74.legacy.vanillafix.config.LegacyVanillaFixConfig;
import piper74.legacy.vanillafix.LegacyVanillaFix;
import java.io.IOException;
import piper74.legacy.vanillafix.util.ScreenUtil;
import net.minecraft.client.MinecraftClient;

public class ConfigScreen extends Screen {
    private final Screen parent;
    private final String title = "Legacy VanillaFix Config";
	LegacyVanillaFixConfig defaultConfig = LegacyVanillaFixConfig.DEFAULT;
	LegacyVanillaFixConfig config;

    public ConfigScreen(Screen screen) {
        this.parent = screen;
    }

    public void init() {
        Keyboard.enableRepeatEvents(true);
        this.buttons.add(new ButtonWidget(1, this.width / 2 - 152, this.height - 30, 150, 20, I18n.translate("gui.cancel")));
        this.buttons.add(new ButtonWidget(2, this.width / 2 + 2, this.height - 30, 150, 20, I18n.translate("gui.done")));

	try {
	config = LegacyVanillaFixConfig.load();
	} catch (IOException e) {
	e.printStackTrace();
	}

		int y = 45;
		this.buttons.add(new ButtonWidget(206, this.width-85,y,50,20, ScreenUtil.checkString(config.betterCrashes)) {
			
			@Override
			public void render(MinecraftClient client, int mouseX, int mouseY) {
				visible = true;
				active = true;
				//if (selected != null) {
					message = (ScreenUtil.checkString(config.betterCrashes));
				//}
				super.render(client, mouseX, mouseY);
			}
		});
		y += 30;
		this.buttons.add(new ButtonWidget(205, this.width-85,y,50,20, ScreenUtil.checkString(config.fasterDimensionChange)) {
					@Override
			public void render(MinecraftClient client, int mouseX, int mouseY) {
				visible = true;
				active = true;
					message = (ScreenUtil.checkString(config.fasterDimensionChange));
				super.render(client, mouseX, mouseY);
			}
		});
		y += 30;
		this.buttons.add(new ButtonWidget(203, this.width-85,y,50,20, ScreenUtil.checkString(config.allowGUIsInNetherPortals)) {
			@Override
			public void render(MinecraftClient client, int mouseX, int mouseY) {
				visible = true;
				active = true;
					message = (ScreenUtil.checkString(config.allowGUIsInNetherPortals));
				super.render(client, mouseX, mouseY);
			}
		});
		y += 30;
		this.buttons.add(new ButtonWidget(204, this.width-85,y,50,20, ScreenUtil.checkString(config.disableInitialChunkLoad)) {
			@Override
			public void render(MinecraftClient client, int mouseX, int mouseY) {
				visible = true;
				active = true;
					message = (ScreenUtil.checkString(config.disableInitialChunkLoad));
				super.render(client, mouseX, mouseY);
			}
		});
        //this.addressField = new TextFieldWidget(3, this.textRenderer, this.width / 2 - 100, this.height / 2, 200, 20);
        //this.addressField.setMaxLength(128);
        //this.addressField.setText(ConfigManager.getConfig().address);
        //this.buttons.get(1).active = this.addressField.getText().length() > 0;
    }

    protected void buttonClicked(ButtonWidget button) {
        if (!button.active) return;

        switch (button.id) {
            case 1:
                this.client.openScreen(this.parent);
                break;
            case 2:
                this.client.openScreen(this.parent);
				try {
                LegacyVanillaFixConfig.save( config );
				} catch (IOException e) {
					e.printStackTrace();
				}
                break;
			case 203:
			config.allowGUIsInNetherPortals = !config.allowGUIsInNetherPortals;
			break;
			case 204:
			config.disableInitialChunkLoad = !config.disableInitialChunkLoad;
			break;
			case 205:
			config.fasterDimensionChange = !config.fasterDimensionChange;
			break;
			case 206:
			config.betterCrashes = !config.betterCrashes;
			break;
        }
    }

    public void tick() {
        //this.addressField.tick();
        super.tick();
    }

    protected void keyPressed(char character, int code) {
        //this.addressField.keyPressed(character, code);

        if (code == 28 || code == 156) {
            this.buttonClicked(this.buttons.get(1));
        }

        //this.buttons.get(1).active = this.addressField.getText().length() > 0;
    }

    protected void mouseClicked(int mouseX, int mouseY, int button) {
        super.mouseClicked(mouseX, mouseY, button);
    }

    public void render(int mouseX, int mouseY, float tickDelta) {
        this.renderBackground();
		
		        if (mouseY >= 40 && mouseY <= 39 + 30 || mouseY >= 40 && mouseY <= 39 + 60 || mouseY >= 40 && mouseY <= 39 + 90 || mouseY >= 40 && mouseY <= 39 + 4*30) {
                int low = ((mouseY-10)/30)*30 + 10 + 2;
                fill(0, low, width, low+30-4, 0x33FFFFFF);
				}
		
        this.drawCenteredString(this.textRenderer, this.title, this.width / 2, 8, 16777215);
		this.drawCenteredString(this.textRenderer, I18n.translate("legacy.vanillafix.config.note"), this.width / 2, this.height - 50, 16755200);
        super.render(mouseX, mouseY, tickDelta);
		
		int y = 40;
		drawWithShadow(this.textRenderer, I18n.translate("legacy.vanillafix.config.betterCrashes"), 12, y + 10, 0xFFFFFF);
		
		if (mouseY >= y && mouseY < (y + 30)) {
                        try {
                            renderTooltip(I18n.translate("legacy.vanillafix.config.betterCrashes.tooltip"), mouseX, mouseY);
                        } catch (Exception e) { e.printStackTrace(); }
		}
		
		y += 30;
		drawWithShadow(this.textRenderer, I18n.translate("legacy.vanillafix.config.fasterDimensionChange"), 12, y + 10, 0xFFFFFF);
		
		if (mouseY >= y && mouseY < (y + 30)) {
                        try {
                            renderTooltip(I18n.translate("legacy.vanillafix.config.fasterDimensionChange.tooltip"), mouseX, mouseY);
                            //y += 30;
                        } catch (Exception e) { e.printStackTrace(); }
		}
		
		y += 30;
		drawWithShadow(this.textRenderer, I18n.translate("legacy.vanillafix.config.allowguisinnetherportals"), 12, y + 10, 0xFFFFFF);
		
		if (mouseY >= y && mouseY < (y + 30)) {
                        try {
                            renderTooltip(I18n.translate("legacy.vanillafix.config.allowguisinnetherportals.tooltip"), mouseX, mouseY);
                            //y += 30;
                        } catch (Exception e) { e.printStackTrace(); }
		}
		
		y += 30;
		drawWithShadow(this.textRenderer, I18n.translate("legacy.vanillafix.config.disableinitalchunkload"), 12, y + 10, 0xFFFFFF);
		
		if (mouseY >= y && mouseY < (y + 30)) {
                        try {
                            renderTooltip(I18n.translate("legacy.vanillafix.config.disableinitalchunkload.tooltip"), mouseX, mouseY);
                            //y += 30;
                        } catch (Exception e) { e.printStackTrace(); }
		}
		
    }
}
