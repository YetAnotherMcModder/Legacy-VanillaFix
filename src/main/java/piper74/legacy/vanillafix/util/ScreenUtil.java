package piper74.legacy.vanillafix.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/**
 * @author piper74
 */
@Environment(EnvType.CLIENT)
public class ScreenUtil {

    public static String checkString(boolean inputstring) {
        String stringValue = String.valueOf( inputstring );

        return inputstring ? "§a" + stringValue : "§c" + stringValue;
    }

}
