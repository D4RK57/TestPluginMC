package mrdark57_.testpluginmc.events;

import mrdark57_.testpluginmc.TestPluginMC;
import mrdark57_.testpluginmc.commands.TestPluginCommand;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class ZombieKills implements Listener {

    private TestPluginMC plugin;

    public ZombieKills(TestPluginMC plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void killZombies(EntityDeathEvent event) {

        Player killer = event.getEntity().getKiller();
        EntityType entity = event.getEntityType();

        // killer no puede ser el sol por ejemplo, debe ser una entidad
        // killer debe ser un jugador
        // entity debe ser un zombie
        if (killer != null && killer.getType().equals(EntityType.PLAYER) && entity.equals(EntityType.ZOMBIE)) {

            FileConfiguration config = plugin.getConfig();
            String killerNamePath = "Players." + killer.getUniqueId() + ".name";
            String zombieKillsNumberPath = "Players." + killer.getUniqueId() + ".zombieKills";

            config.set(killerNamePath, killer.getName());

            if (config.contains(zombieKillsNumberPath)){
                int zombiesKilled = Integer.parseInt(config.getString(zombieKillsNumberPath));
                int counter = zombiesKilled + 1;

                config.set(zombieKillsNumberPath, counter);
                plugin.saveConfig();

                killer.sendMessage(plugin.getPluginName() + ChatColor.RED + " You are killed " + counter + " zombies!");
            } else {
                config.set(zombieKillsNumberPath, 1);
                plugin.saveConfig();

                killer.sendMessage(plugin.getPluginName() + ChatColor.RED + " You are killed your first zombie!");
            }

        }

    }

}
