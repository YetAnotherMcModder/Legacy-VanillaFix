/*
 *This file is from
 *https://github.com/DimensionalDevelopment/VanillaFix/blob/99cb47cc05b4790e8ef02bbcac932b21dafa107f/src/main/java/org/dimdev/vanillafix/crashes/IPatchedCrashReport.java
 *The source file uses the MIT License.
 */

package piper74.legacy.vanillafix.util;

import net.fabricmc.loader.api.metadata.ModMetadata;

import java.util.Set;

public interface PatchedCrashReport {

    Set<ModMetadata> getSuspectedMods();
	
    interface Element {

        String invokeGetName();

        String invokeGetDetail();
    }
}