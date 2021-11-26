package piper74.legacy.vanillafix.stacktrace.mixin;

import piper74.legacy.vanillafix.TraceUtils;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashReportSection;
import piper74.legacy.vanillafix.util.PatchedCrashReport;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Set;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import piper74.legacy.vanillafix.stacktrace.ModIdentifier;
import net.fabricmc.loader.api.metadata.ModMetadata;

@Mixin(value = CrashReport.class, priority = 500)
public abstract class MixinCrashReport implements PatchedCrashReport {
	@Shadow private StackTraceElement[] stackTrace;
    @Shadow
    @Final
    private Throwable cause;
	
    @Shadow
    @Final
    private CrashReportSection systemDetailsSection;
	
    @Shadow
    @Final
    private List<CrashReportSection> otherSections;
	
	@Shadow
    private static String generateWittyComment() {
        return null;
    }
	
    @Shadow
    @Final
    private String message;
	
	private Set<ModMetadata> suspectedMods = null;
	
    private static String stacktraceToString(Throwable cause) {
        StringWriter writer = new StringWriter();
        cause.printStackTrace(new PrintWriter(writer));
        return writer.toString();
    }

     /**
     * @reason Adds a list of mixins which have affected a class involved in the crash.
	 * @author comp500
     */
	@Inject(method = "addStackTrace", at = @At(value = "FIELD", target = "Lnet/minecraft/util/crash/CrashReport;otherSections:Ljava/util/List;"))
	private void mixintrace_addTrace(StringBuilder crashReportBuilder, CallbackInfo ci) {
		int trailingNewlineCount = 0;
		// Remove trailing \n
		if (crashReportBuilder.charAt(crashReportBuilder.length() - 1) == '\n') {
			crashReportBuilder.deleteCharAt(crashReportBuilder.length() - 1);
			trailingNewlineCount++;
		}
		if (crashReportBuilder.charAt(crashReportBuilder.length() - 1) == '\n') {
			crashReportBuilder.deleteCharAt(crashReportBuilder.length() - 1);
			trailingNewlineCount++;
		}
		TraceUtils.printTrace(stackTrace, crashReportBuilder);
		
		while(trailingNewlineCount > 0) {
		crashReportBuilder.append("\n");
		trailingNewlineCount--;
		}
	}
	
	@Override
    public Set<ModMetadata> getSuspectedMods() {
        return suspectedMods;
    }
	
     /**
     * @reason Adds a list of mods which may have caused the crash to the report.
	 * @author Runemoro
     */
    @Inject(method = "fillSystemDetails", at = @At("TAIL"))
    private void afterFillSystemDetails(CallbackInfo ci) {
        systemDetailsSection.add("Suspected Mods", () -> {
            try {
                suspectedMods = ModIdentifier.identifyFromStacktrace(cause);

                String modListString = "Unknown";
                List<String> modNames = new ArrayList<>();
                for (ModMetadata mod : suspectedMods) {
                    modNames.add(mod.getName() + " (" + mod.getId() + ")");
                }

                if (!modNames.isEmpty()) {
                    modListString = StringUtils.join(modNames, ", ");
                }
                return modListString;
            } catch (Throwable e) {
                return ExceptionUtils.getStackTrace(e).replace("\t", "    ");
            }
        });
    }
	
    /**
     * @reason Improve report formatting
	 * @author Runemoro
     */
    @Overwrite
    public String asString() {
        StringBuilder builder = new StringBuilder();

        builder.append("---- Minecraft Crash Report ----\n")
                        .append("// ").append(generateWittyComment())
                        .append("\n\n")
                        .append("Time: ").append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z").format(new Date())).append("\n")
                        .append("Description: ").append(message)
                        .append("\n\n")
                        .append(stacktraceToString(cause)
                                        .replace("\t", "    ")) // Vanilla's getCauseStackTraceOrString doesn't print causes and suppressed exceptions
                        .append("\n\nA detailed walkthrough of the error, its code path and all known details is as follows:\n");

        for (int i = 0; i < 87; i++) {
            builder.append("-");
        }

        builder.append("\n\n");
        addStackTrace(builder);
        return builder.toString().replace("\t", "    ");
    }
	
    /**
     * @reason Improve report formatting
	 * @author Runemoro
     */
    @Overwrite
    public void addStackTrace(StringBuilder builder) {
        for (CrashReportSection section : otherSections) {
            section.addStackTrace(builder);
            builder.append("\n");
        }

        systemDetailsSection.addStackTrace(builder);
    }

	
}
