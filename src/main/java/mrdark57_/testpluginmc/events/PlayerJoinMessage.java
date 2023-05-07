package mrdark57_.testpluginmc.events;

import mrdark57_.testpluginmc.TestPluginMC;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinMessage implements Listener {

    // Cuando ocurre el evento PlayerJoinEvent (jugador entra al servidor) envía un mensaje

    private TestPluginMC plugin;

    public PlayerJoinMessage(TestPluginMC plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();
        FileConfiguration config = plugin.getConfig();
        String path = "Config.welcome-msg";

        if (config.getString(path).equals("true")){
            player.sendMessage(ChatColor.AQUA + "Welcome " + event.getPlayer().getName() + " to the server!");
        }
    }

}
