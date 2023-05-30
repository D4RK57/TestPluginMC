package mrdark57_.testpluginmc;

import mrdark57_.testpluginmc.commands.Kit;
import mrdark57_.testpluginmc.commands.TestPluginCommand;
import mrdark57_.testpluginmc.events.Chat;
import mrdark57_.testpluginmc.events.TestPluginInventory;
import mrdark57_.testpluginmc.events.PlayerJoin;
import mrdark57_.testpluginmc.events.Kills;
import mrdark57_.testpluginmc.utils.ColorTranslator;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;

public class TestPluginMC extends JavaPlugin {

    private String pluginName = ChatColor.YELLOW + "[" + getName() + "]";
    private String pluginVersion ="v" + getDescription().getVersion();
    private File config = new File(getDataFolder(),"config.yml");
    private String configRoute;

    private FileConfiguration messages = null;

    private File messagesFile = null;

    private static Economy economy = null;

    @Override
    public void onEnable() {
        enableMessage();
        commandRegister();
        eventRegister();
        configRegister();
        messagesRegister();
    }
    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "----------------------------------------");
        Bukkit.getConsoleSender().sendMessage(pluginName + ChatColor.RED + " has been disabled");
        Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "----------------------------------------" + "\n");
    }

    // Enviar mensaje cuando se inicia el plugin
    public void enableMessage() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "----------------------------------------");
        Bukkit.getConsoleSender().sendMessage(ColorTranslator.translate(pluginName + " &3has been enabled &c(" + pluginVersion + ")"));
        if (setupEconomy()) {
            Bukkit.getConsoleSender().sendMessage(ColorTranslator.translate("&aVault plugin has been found"));
        }else {
            Bukkit.getConsoleSender().sendMessage(ColorTranslator.translate("&4Vault plugin not found"));
        }
        Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "----------------------------------------" + "\n");
    }

    // Registrar comandos
    public void commandRegister() {
        // Registrar comando /kit
        getCommand("kit").setExecutor(new Kit());
        getCommand("testplugin").setExecutor(new TestPluginCommand(this));
    }

    // Registrar eventos
    public void eventRegister() {
        // Registrar evento
        PluginManager pluginManager = getServer().getPluginManager();

        pluginManager.registerEvents(new PlayerJoin(this), this);
        pluginManager.registerEvents(new Kills(this), this);
        pluginManager.registerEvents(new TestPluginInventory(this), this);
        pluginManager.registerEvents(new TestPluginInventory(this), this);
        pluginManager.registerEvents(new Chat(this), this);
    }

    // Registrar la config
    public void configRegister() {

        configRoute = config.getPath();
        if (!config.exists()){
            getConfig().options().copyDefaults(true);
            saveConfig();
        }
    }

    // Obtener mensajes
    public FileConfiguration getMessages() {
        if (messages == null) {
            reloadMessages();
        }
        return messages;
    }

    // Recargar mensajes
    public void reloadMessages() {
        if (messages == null) {
            messagesFile = new File(getDataFolder(), "messages.yml");
        }
        messages = YamlConfiguration.loadConfiguration(messagesFile);
        Reader defConfigStream;
        try {
            defConfigStream = new InputStreamReader(getResource("messages.yml"), "UTF8");
            if (defConfigStream != null) {
                YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
                messages.setDefaults(defConfig);
            }
        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    // Guardar mensajes
    public void saveMessages(){
        try {
            messages.save(messagesFile);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Registrar mensajes
    public void messagesRegister(){
        messagesFile = new File(getDataFolder(), "messages.yml");
        if (!messagesFile.exists()) {
            getMessages().options().copyDefaults(true);
            saveMessages();
        }
    }

    // Registrar economía (Vault)
    public boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }

        RegisteredServiceProvider<Economy> registeredServiceProvider = getServer()
                .getServicesManager()
                .getRegistration(Economy.class);

        if (registeredServiceProvider == null){
            return false;
        }

        economy = registeredServiceProvider.getProvider();

        return economy != null;
    }

    public String getPluginName() {
        return pluginName;
    }

    public String getPluginVersion() {
        return pluginVersion;
    }

    public static Economy getEconomy() {
        return economy;
    }
}
