package piper74.legacy.vanillafix.stacktrace.mixin;

import piper74.legacy.vanillafix.util.PatchedCrashReport;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

// This file is unused, getinvokename, and getinvokedetail is never used

// It's used for exposing the package private CrashReport.Element
/*
@SuppressWarnings("unused")
@Mixin(targets = "net.minecraft.util.crash.CrashReportSection$Element")
public abstract class CrashReportSectionElementMixin implements PatchedCrashReport.Element {

    @Shadow
    public abstract String getName();

    @Shadow
    public abstract String getDetail();

    @Override
    public String invokeGetName() {
        return getName();
    }

    @Override
    public String invokeGetDetail() {
        return getDetail();
    }
}
*/