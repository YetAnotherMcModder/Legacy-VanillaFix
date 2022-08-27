package piper74.legacy.vanillafix.crashes.compatibility;
/*
 * Decompiled with CFR 0.1.1 (FabricMC 57d88659).
 */

// CWindow class
// Reimplementation of 1.8's Window class

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.MathHelper;

@Environment(value=EnvType.CLIENT)
public class CWindow {
    private final double scaledWidth;
    private final double scaledHeight;
    private int width;
    private int height;
    private int scaleFactor;

    public CWindow(MinecraftClient minecraftClient, int i, int j) {
        this.width = i;
        this.height = j;
        this.scaleFactor = 1;
        boolean bl = minecraftClient.hasReducedDebugInfo();
        int n = minecraftClient.options.guiScale;
        if (n == 0) {
            n = 1000;
        }
        while (this.scaleFactor < n && this.width / (this.scaleFactor + 1) >= 320 && this.height / (this.scaleFactor + 1) >= 240) {
            ++this.scaleFactor;
        }
        if (bl && this.scaleFactor % 2 != 0 && this.scaleFactor != 1) {
            --this.scaleFactor;
        }
        this.scaledWidth = (double)this.width / (double)this.scaleFactor;
        this.scaledHeight = (double)this.height / (double)this.scaleFactor;
        this.width = MathHelper.ceil(this.scaledWidth);
        this.height = MathHelper.ceil(this.scaledHeight);
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public double getScaledWidth() {
        return this.scaledWidth;
    }

    public double getScaledHeight() {
        return this.scaledHeight;
    }

    public int getScaleFactor() {
        return this.scaleFactor;
    }
}


