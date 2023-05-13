package mrdark57_.testpluginmc;

import mrdark57_.testpluginmc.commands.Kit;
import mrdark57_.testpluginmc.commands.TestPluginCommand;
import mrdark57_.testpluginmc.events.PlayerJoin;
import mrdark57_.testpluginmc.events.Kills;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;

public class TestPluginMC extends JavaPlugin {

    private String pluginName = ChatColor.YELLOW + "[" + getName() + "]";
    private String pluginVersion ="v" + getDescription().getVersion();
    private File config = new File(getDataFolder(),"config.yml");
    private String configRoute;

    private FileConfiguration messages = null;

    private File messagesFile = null;

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
        Bukkit.getConsoleSender().sendMessage(pluginName + ChatColor.AQUA + " has been enabled" + ChatColor.RED +  " (" + pluginVersion + ")");
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
        getServer().getPluginManager().registerEvents(new PlayerJoin(this), this);
        getServer().getPluginManager().registerEvents(new Kills(this), this);
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

    public String getPluginName() {
        return pluginName;
    }

    public String getPluginVersion() {
        return pluginVersion;
    }
}
