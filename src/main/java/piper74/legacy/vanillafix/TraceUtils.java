/*
 *This file is modified based on
 *https://github.com/comp500/mixintrace/blob/3b7c412a9c691c9c994f70d7f9da62e0419bedce/src/main/java/link/infra/mixintrace/TraceUtils.java
 *The source file uses the MIT License.
 */

package piper74.legacy.vanillafix;

import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.mixin.transformer.ClassInfo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TraceUtils {
	public static void printTrace(StackTraceElement[] stackTrace, StringBuilder crashReportBuilder) {
		if (stackTrace != null && stackTrace.length > 0) {
			crashReportBuilder.append("\nMixins in Stacktrace:");

			try {
				List<String> classNames = new ArrayList<>();
				for (StackTraceElement el : stackTrace) {
					if (!classNames.contains(el.getClassName())) {
						classNames.add(el.getClassName());
					}
				}

				boolean found = false;
				for (String className : classNames) {
					ClassInfo classInfo = ClassInfo.fromCache(className);
					if (classInfo != null) {
						// Workaround for bug in Mixin, where it adds to the wrong thing :(
						Method getMixins = ClassInfo.class.getDeclaredMethod("getMixins");
						getMixins.setAccessible(true);
						@SuppressWarnings("unchecked")
						Set<IMixinInfo> mixinInfoSet = (Set<IMixinInfo>) getMixins.invoke(classInfo);
						if (mixinInfoSet.size() > 0) {
							crashReportBuilder.append("\n\t");
							crashReportBuilder.append(className);
							crashReportBuilder.append(":");
							for (IMixinInfo info : mixinInfoSet) {
								crashReportBuilder.append("\n\t\t");
								crashReportBuilder.append(info.getClassName());
								crashReportBuilder.append(" (");
								crashReportBuilder.append(info.getConfig().getName());
								crashReportBuilder.append(")");
							}
							found = true;
						}
					}
				}

				if (!found) {
					crashReportBuilder.append(" None found");
				}
			} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
				crashReportBuilder.append(" Failed to find Mixin metadata: ").append(e);
			}
		}
	}
}
