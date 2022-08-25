/*
 *This file is from
 *https://github.com/comp500/mixintrace/blob/3b7c412a9c691c9c994f70d7f9da62e0419bedce/src/main/java/link/infra/mixintrace/mixin/MixinCrashReportSection.java
 *The source file uses the MIT License.
 */

package piper74.legacy.vanillafix.stacktrace.mixin;

import net.minecraft.util.crash.CrashReportSection;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = CrashReportSection.class)
public abstract class MixinCrashReportSection {
// TODO: Add vanillafix code here
}
