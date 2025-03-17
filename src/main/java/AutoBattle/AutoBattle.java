package AutoBattle;

import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import java.io.IOException;
import java.util.Properties;

public class AutoBattle {

    public static boolean modEnabled;
    public static boolean keepHand;
    public static boolean playCurses;
    private static SpireConfig config;

    public AutoBattle(){}


    public static void initialize(){
        config = makeConfig();
        setProperties();
    }
    private static SpireConfig makeConfig() {
        Properties defaultProperties = new Properties();
        defaultProperties.setProperty("keepHand", Boolean.toString(false));
        defaultProperties.setProperty("modEnabled", Boolean.toString(true));
        defaultProperties.setProperty("playCurses", Boolean.toString((false)));
        try {
            return new SpireConfig("AutoBattle", "autobattle-config", defaultProperties);
        } catch (IOException var2) {
            return null;
        }
    }

    private static String getString(String key) {
        return config.getString(key);
    }

    static void setString(String key, String value) {
        config.setString(key, value);

        try {
            config.save();
        } catch (IOException var3) {
            var3.printStackTrace();
        }

    }

    static Boolean getBoolean(String key) {
        assert config != null;
        return config.getBool(key);
    }

    static void setBoolean(String key, Boolean value) {
        config.setBool(key, value);

        try {
            config.save();
        } catch (IOException var3) {
            var3.printStackTrace();
        }

    }

    private static void setProperties() {
        if (config != null) {
            modEnabled = getBoolean("modEnabled");
            keepHand = getBoolean("keepHand");
            playCurses = getBoolean("playCurses");
        }
    }

}
