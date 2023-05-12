package mrdark57_.testpluginmc;

import mrdark57_.testpluginmc.commands.Kit;
import mrdark57_.testpluginmc.commands.TestPluginCommand;
import mrdark57_.testpluginmc.events.PlayerJoin;
import mrdark57_.testpluginmc.events.ZombieKills;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;

public class TestPluginMC extends JavaPlugin {

    private String pluginName = ChatColor.YELLOW + "[" + getName() + "]";
    private String pluginVersion ="v" + getDescription().getVersion();
    private File config = new File(this.getDataFolder(),"config.yml");
    private String configRoute;

    @Override
    public void onEnable() {
        enableMessage();
        commandRegister();
        eventRegister();
        configRegister();
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
        this.getCommand("kit").setExecutor(new Kit());
        this.getCommand("testplugin").setExecutor(new TestPluginCommand(this));
    }

    // Registrar eventos
    public void eventRegister() {
        // Registrar evento
        getServer().getPluginManager().registerEvents(new PlayerJoin(this), this);
        getServer().getPluginManager().registerEvents(new ZombieKills(this), this);
    }

    // Registrar la config
    public void configRegister() {

        configRoute = config.getPath();
        if (!config.exists()){
            this.getConfig().options().copyDefaults(true);
            saveConfig();
        }
    }

    public String getPluginName() {
        return pluginName;
    }

    public String getPluginVersion() {
        return pluginVersion;
    }
}
