package mrdark57_.testpluginmc.commands;

import mrdark57_.testpluginmc.TestPluginMC;
import mrdark57_.testpluginmc.events.Inventory;
import mrdark57_.testpluginmc.utils.ColorTranslator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TestPluginCommand implements CommandExecutor {

    private TestPluginMC plugin;

    private String pluginName;
    private String pluginVersion;
    private FileConfiguration config;


    public TestPluginCommand(TestPluginMC plugin) {
        this.plugin = plugin;
        this.pluginName = plugin.getPluginName();
        this.pluginVersion = plugin.getPluginVersion();
        this.config = plugin.getConfig();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length > 0){

                switch (args[0]){
                    case "help":
                        return createHelpCommand(player);
                    case "version":
                        return createVersionCommand(player);
                    case "reload":
                        return createReloadCommand(player);
                    case "kills":
                        return createKillsCommand(player);
                    case "report":
                        return createReportCommand(player, args);
                    case "inventory":
                        return createInventoryCommand(player);
                    default:
                        player.sendMessage(pluginName + ChatColor.RED + " That command does not exist.");
                        return true;
                }

                /* (args[0].equalsIgnoreCase("help")){
                    return createHelpCommand(player);

                }else if (args[0].equalsIgnoreCase("version")){
                    return createVersionCommand(player);

                }else if (args[0].equalsIgnoreCase("reload")){
                    return createReloadCommand(player);

                }else if (args[0].equalsIgnoreCase("kills")){
                    return createKillsCommand(player);

                }else if (args[0].equalsIgnoreCase("report")){
                    return createReportCommand(player, args);

                }else if (args[0].equalsIgnoreCase("inventory")){
                    return createInventoryCommand(player);

                }else{
                    player.sendMessage(pluginName + ChatColor.RED + " That command does not exist.");
                    return true;
                }*/

            }else{
                player.sendMessage(pluginName + ChatColor.RED + " Use /testplugin help to see the plugin commands.");
                return true;
            }

        }else {
            Bukkit.getConsoleSender().sendMessage(pluginName + ChatColor.RED + " You cannot execute this command from the console.");
            return false;
        }
    }

    public Boolean createHelpCommand(Player player) {
        player.sendMessage(pluginName
                + " Commands: \n"
                + ColorTranslator.translateColors("&f- Use &6/testplugin version &fto see the plugin version. \n")
                + ColorTranslator.translateColors("&f- Use &6/testplugin reload &fto reload the plugin. \n")
                + ColorTranslator.translateColors("&f- Use &6/testplugin kills &fto know how many zombies are you killed. \n")
                + ColorTranslator.translateColors("&f- Use &6/testplugin report <player> &fto report a player. \n"));
        return true;
    }

    public Boolean createVersionCommand(Player player) {
        player.sendMessage(pluginName + " The current version is " + pluginVersion);
        return true;
    }

    public Boolean createReloadCommand(Player player) {
        plugin.reloadConfig();
        plugin.reloadMessages();
        player.sendMessage(pluginName + " The plugin has been reloaded successfully!");
        return true;
    }

    public Boolean createKillsCommand(Player player) {
        String zombieKillsNumberPath = "Players." + player.getUniqueId() + ".zombieKills";

        if (!config.contains("Players")){
            // El jugador aún no ha matado zombies
            player.sendMessage(pluginName + ChatColor.DARK_RED + " You are not killed zombies yet.");
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

    }

    public Boolean createReportCommand(Player player, String[] args) {
        //testplugin report [usuario]
        // Ejemplo para añadir una lista de usuarios a la config
        if (args.length == 1){
            player.sendMessage(pluginName + ChatColor.RED + " To report a player use: /testplugin report [user]");
            return true;

        }else {
            String user = args[1];
            String reportedUsersPath = "Config.reported-users";

            if (Bukkit.getPlayer(user) != null){

                if (config.contains(reportedUsersPath)){
                    List<String> reportedUsers = config.getStringList(reportedUsersPath);

                    if (reportedUsers.contains(user)){
                        player.sendMessage(pluginName + ChatColor.RED + " " + user + " is already reported!");
                        return true;
                    }else {
                        reportedUsers.add(user);
                        config.set(reportedUsersPath, reportedUsers);
                        plugin.saveConfig();
                        player.sendMessage(pluginName + ChatColor.GREEN + " " + user + " has been reported successfully!");
                        return true;
                    }

                }else {
                    List<String> reportedUsers = new ArrayList<>();
                    reportedUsers.add(user);
                    config.set(reportedUsersPath, reportedUsers);
                    plugin.saveConfig();
                    player.sendMessage(pluginName + ChatColor.GREEN + " " + user + " has been reported successfully!");
                    return true;
                }

            }else {
                player.sendMessage(pluginName + ChatColor.RED + " This player is not online.");
                return true;
            }
        }
    }

    public Boolean createInventoryCommand(Player player) {
        Inventory inventory = new Inventory();
        inventory.createInventory(player);
        return true;
    }

}
