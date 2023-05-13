package mrdark57_.testpluginmc.utils;

import org.bukkit.ChatColor;

public class ColorTranslator {

    // Transforma el código de color automáticamente
    public static String translateColors(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

}
