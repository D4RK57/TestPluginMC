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
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class TestPluginCommand implements CommandExecutor {

    private TestPluginMC plugin;

    private String pluginName;
    private String pluginVersion;


    public TestPluginCommand(TestPluginMC plugin) {
        this.plugin = plugin;
        this.pluginName = plugin.getPluginName();
        this.pluginVersion = plugin.getPluginVersion();
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
                + ColorTranslator.translate("&f- Use &6/testplugin version &fto see the plugin version. \n")
                + ColorTranslator.translate("&f- Use &6/testplugin reload &fto reload the plugin. \n")
                + ColorTranslator.translate("&f- Use &6/testplugin setspawn &fto set a new spawn point. \n")
                + ColorTranslator.translate("&f- Use &6/testplugin spawn &fto go to the spawn. \n")
                + ColorTranslator.translate("&f- Use &6/testplugin kills &fto know how many zombies are you killed. \n")
                + ColorTranslator.translate("&f- Use &6/testplugin report <player> &fto report a player. \n"));
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
        // Es importante tener una instancia de config en cada método, si no no toma los datos recargados de la config.
        FileConfiguration config = plugin.getConfig();

        if (config.contains("Config.Spawn.x") && config.contains("Config.Spawn.y") && config.contains("Config.Spawn.z")){

            ItemStack spawnItem = createSpawnItem();

            player.getInventory().addItem(spawnItem);

            tpPlayerToSpawn(player, spawnItem);

        }else {
            player.sendMessage(pluginName + ColorTranslator.translate(" &cThe spawn doesn´t exists!"));
            return true;
        }
        return true;
    }

    public ItemStack createSpawnItem(){
        FileConfiguration config = plugin.getConfig();

        String materialName = config.getString("Config.required-item.material").toUpperCase();
        Material material = Material.valueOf(materialName);
        int itemAmount = Integer.parseInt(config.getString("Config.required-item.amount"));

        ItemStack spawnItem = new ItemStack(material, itemAmount);
        ItemMeta spawnItemMeta = spawnItem.getItemMeta();

        String itemNamePath = "Config.required-item.name";
        if (config.contains(itemNamePath)) {
            String itemName = ColorTranslator.translate(config.getString(itemNamePath));
            spawnItemMeta.setDisplayName(itemName);
        }

        String itemLorePath = "Config.required-item.lore";
        if (config.contains(itemLorePath)) {
            List<String> itemLore = config.getStringList(itemLorePath);
            for (int i = 0; i < itemLore.size(); i++) {
                itemLore.set(i, ColorTranslator.translate(itemLore.get(i)));
            }
            spawnItemMeta.setLore(itemLore);
        }

        String itemEnchantsPath = "Config.required-item.enchants";
        if (config.contains(itemEnchantsPath)) {
            List<String> itemEnchants = config.getStringList(itemEnchantsPath);
            for (int i = 0; i < itemEnchants.size(); i++) {
                String[] distinction = itemEnchants.get(i).split(":");
                int level = Integer.parseInt(distinction[1]);
                // El método getByName() está obsoleto, pero el método getByKey() usa nombres distintos a los enums de
                // encantamientos, como por ejemplo, "damage_all" es "sharpness".
                // Una solución es crear un método que transforme los nombres, pero debe existir algo más eficiente
                // que aún no he encontrado.
                spawnItemMeta.addEnchant(Enchantment.getByName(distinction[0]), level, true);
            }
        }
        spawnItem.setItemMeta(spawnItemMeta);

        return spawnItem;
    }

    public Boolean tpPlayerToSpawn(Player player, ItemStack spawnItem){
        FileConfiguration config = plugin.getConfig();
        ItemStack[] playerItems = player.getInventory().getContents();

        for (ItemStack item : playerItems) {
            if (item != null
                    && item.isSimilar(spawnItem)
                    && item.getAmount() == spawnItem.getAmount()) {

                double coordX = Double.parseDouble(config.getString("Config.Spawn.x"));
                double coordY = Double.parseDouble(config.getString("Config.Spawn.y"));
                double coordZ= Double.parseDouble(config.getString("Config.Spawn.z"));

                World world = plugin.getServer().getWorld(config.getString("Config.Spawn.world"));

                if (!config.contains("Config.Spawn.yaw") || !config.contains("Config.Spawn.pitch")) {
                    Location spawn = new Location(world, coordX, coordY, coordZ);
                    player.teleport(spawn);

                    player.sendMessage(pluginName + ColorTranslator.translate(" &bWelcome to the spawn!"));
                    return true;
                }

                float yaw = Float.parseFloat(config.getString("Config.Spawn.yaw"));
                float pitch = Float.parseFloat(config.getString("Config.Spawn.pitch"));

                Location spawn = new Location(world, coordX, coordY, coordZ, yaw, pitch);
                player.teleport(spawn);

                player.sendMessage(pluginName + ColorTranslator.translate(" &bWelcome to the spawn!"));
                return true;
            }
            player.sendMessage(pluginName + ColorTranslator.translate(" &cYou don´t have the item to use this command!"));
            return true;
        }
        return true;
    }

    public Boolean createSetSpawnCommand(Player player) {

        FileConfiguration config = plugin.getConfig();

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

        player.sendMessage(pluginName + ColorTranslator.translate(" &aYour spawn was designed to &e[" + coordX + ", " + coordY + ", " + coordZ + "]"));

        return true;
    }

    public Boolean createKillsCommand(Player player) {

        FileConfiguration config = plugin.getConfig();

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
        FileConfiguration config = plugin.getConfig();
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
