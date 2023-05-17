package mrdark57_.testpluginmc.events;

import mrdark57_.testpluginmc.TestPluginMC;
import mrdark57_.testpluginmc.utils.ColorTranslator;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;

public class PlayerJoin implements Listener {

    // Cuando ocurre el evento PlayerJoinEvent (jugador entra al servidor) envía un mensaje

    private TestPluginMC plugin;

    public PlayerJoin(TestPluginMC plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();
        FileConfiguration config = plugin.getConfig();
        FileConfiguration messages = plugin.getMessages();

        if (config.contains("Config.Spawn.x")){
            double coordX = Double.parseDouble(config.getString("Config.Spawn.x"));
            double coordY = Double.parseDouble(config.getString("Config.Spawn.y"));
            double coordZ= Double.parseDouble(config.getString("Config.Spawn.z"));

            float yaw = Float.parseFloat(config.getString("Config.Spawn.yaw"));
            float pitch = Float.parseFloat(config.getString("Config.Spawn.pitch"));

            World world = plugin.getServer().getWorld(config.getString("Config.Spawn.world"));

            Location spawn = new Location(world, coordX, coordY, coordZ, yaw, pitch);
            player.teleport(spawn);
        }

        String welcomeMsgPath = "Config.welcome-msg";


        if (config.getString(welcomeMsgPath).equals("true")){

            String welcomeMsgTextPath = "Messages.welcome-msg-txt";
            // Ejemplo de como extraer una lista de la config
            List<String> welcomeMessages = messages.getStringList(welcomeMsgTextPath);

            // Se utiliza el método translateAlternateColorCodes para activar los códigos de colores &
            for (String message: welcomeMessages) {
                player.sendMessage(ChatColor.translateAlternateColorCodes(
                        '&', message).replaceAll("%player%", player.getName()));
            }
        }



    }

}
