package piper74.legacy.vanillafix.util;

/**
 * @author piper74
 */
public class ScreenUtil {

    public static String checkString(boolean string) {
    String chkstring = String.valueOf( string );
    if (chkstring.contains("true"))
    chkstring = "§a" + chkstring;
    else if (chkstring.contains("false"))
    chkstring = "§c" + chkstring;

    return chkstring;
    }

}
