package mrdark57_.testpluginmc.commands;

import mrdark57_.testpluginmc.TestPluginMC;
import mrdark57_.testpluginmc.events.TestPluginInventory;
import mrdark57_.testpluginmc.utils.ColorTranslator;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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
                    case "spawn":
                        return createSpawnCommand(player);
                    case "setspawn":
                        return createSetSpawnCommand(player);
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
                + ColorTranslator.translateColors("&f- Use &6/testplugin setspawn &fto set a new spawn point. \n")
                + ColorTranslator.translateColors("&f- Use &6/testplugin spawn &fto go to the spawn. \n")
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

    private Boolean createSpawnCommand(Player player) {

        if (config.contains("Config.Spawn.x")){

            ItemStack[] playerItems = player.getInventory().getContents();
            for (ItemStack item : playerItems) {

                // Es null cuando no hay un item en el slot
                // Va a buscar un diamante que tenga aspecto igneo II
                if (item != null
                        && item.hasItemMeta()
                        && item.getItemMeta().hasEnchant(Enchantment.FIRE_ASPECT)
                        && item.getEnchantmentLevel(Enchantment.FIRE_ASPECT) == 2
                        && item.getType() == Material.DIAMOND) {

                    double coordX = Double.parseDouble(config.getString("Config.Spawn.x"));
                    double coordY = Double.parseDouble(config.getString("Config.Spawn.y"));
                    double coordZ= Double.parseDouble(config.getString("Config.Spawn.z"));

                    float yaw = Float.parseFloat(config.getString("Config.Spawn.yaw"));
                    float pitch = Float.parseFloat(config.getString("Config.Spawn.pitch"));

                    World world = plugin.getServer().getWorld(config.getString("Config.Spawn.world"));

                    Location spawn = new Location(world, coordX, coordY, coordZ, yaw, pitch);
                    player.teleport(spawn);

                    player.sendMessage(pluginName + ColorTranslator.translateColors(" &bWelcome to the spawn!"));
                    return true;

                }

            }

            player.sendMessage(pluginName + ColorTranslator.translateColors(" &cYou don´t have the item to use this command!"));
            return true;

        }else {
            player.sendMessage(pluginName + ColorTranslator.translateColors(" &cThe spawn doesn´t exists!"));
            return true;
        }

    }

    private Boolean createSetSpawnCommand(Player player) {
        Location spawn = player.getLocation();

        double coordX = spawn.getX();
        double coordY = spawn.getY();
        double coordZ = spawn.getZ();
        String world = spawn.getWorld().getName();
        float yaw = spawn.getYaw();
        float pitch = spawn.getPitch();

        config.set("Config.Spawn.x", coordX);
        config.set("Config.Spawn.y", coordY);
        config.set("Config.Spawn.z", coordZ);
        config.set("Config.Spawn.world", world);
        config.set("Config.Spawn.yaw", yaw);
        config.set("Config.Spawn.pitch", pitch);

        plugin.saveConfig();

        player.sendMessage(pluginName + ColorTranslator.translateColors(" &aYour spawn was designed to &e[" + coordX + ", " + coordY + ", " + coordZ + "]"));

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
        TestPluginInventory inventory = new TestPluginInventory(plugin);
        inventory.createInventory(player);
        return true;
    }

}
