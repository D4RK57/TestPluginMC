package mrdark57_.testpluginmc.commands;

import mrdark57_.testpluginmc.TestPluginMC;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class TestPluginCommand implements CommandExecutor {

    private TestPluginMC plugin;

    public TestPluginCommand(TestPluginMC plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        String pluginName = plugin.getPluginName();
        String pluginVersion = plugin.getPluginVersion();
        FileConfiguration config = plugin.getConfig();

        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length > 0){

                if (args[0].equalsIgnoreCase("help")){
                    player.sendMessage(pluginName
                            + " Commands: \n"
                            + "Use /testplugin version to see the plugin version. \n"
                            + "Use /testplugin reload to reload the plugin. \n"
                            + "Use /testplugin kills to know how many zombies are you killed.");
                    return true;

                }else if (args[0].equalsIgnoreCase("version")){
                    player.sendMessage(pluginName + " The current version is " + pluginVersion);
                    return true;

                }else if (args[0].equalsIgnoreCase("reload")){
                    plugin.reloadConfig();
                    player.sendMessage(pluginName + " The plugin has been reloaded successfully!");
                    return true;

                }else if (args[0].equalsIgnoreCase("kills")){

                    String zombieKillsNumberPath = "Players." + player.getUniqueId() + ".zombieKills";

                    if (!config.contains("Players")){
                        // El jugador aún no ha matado zombies
                        player.sendMessage(pluginName + ChatColor.DARK_RED + " You aren´t killed zombies yet.");
                        return true;
                    }else if (config.contains(zombieKillsNumberPath)){
                        int zombiesKilled = Integer.parseInt(config.getString(zombieKillsNumberPath));
                        player.sendMessage(pluginName + ChatColor.RED + " KILLS:");
                        player.sendMessage(ChatColor.GREEN + "Zombie: " + ChatColor.YELLOW +zombiesKilled);
                        return true;
                    }else {
                        // El jugador aún no ha matado zombies
                        player.sendMessage(pluginName + ChatColor.DARK_RED + " You aren´t killed zombies yet.");
                        return true;
                    }

                }else{
                    player.sendMessage(pluginName + ChatColor.RED + " That command does not exist.");
                    return true;
                }

            }else{
                player.sendMessage(pluginName + ChatColor.RED + " Use /testplugin help to see the plugin commands.");
                return true;
            }

        }else {
            Bukkit.getConsoleSender().sendMessage(pluginName + ChatColor.RED + " You cannot execute this command from the console.");
            return false;
        }
    }
}
