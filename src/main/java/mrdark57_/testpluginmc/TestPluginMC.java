package mrdark57_.testpluginmc;

import mrdark57_.testpluginmc.commands.Kit;
import mrdark57_.testpluginmc.events.PlayerJoinMessage;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class TestPluginMC extends JavaPlugin {

    private String pluginName ="[" + getName() + "]";
    private String pluginVersion ="v" + getDescription().getVersion();

    @Override
    public void onEnable() {
        enableMessage();

        commandRegister();

        eventRegister();
    }
    @Override
    public void onDisable() {
        getLogger().info("onDisable is called!");
    }

    // Enviar mensaje cuando se inicia el plugin
    public void enableMessage() {
        getLogger().info("----------------------------------------");
        getLogger().info(pluginName + " has been enabled (" + pluginVersion + ")");
        getLogger().info("----------------------------------------");
    }

    // Registrar comandos
    public void commandRegister() {
        // Registrar comando /kit
        this.getCommand("kit").setExecutor(new Kit());
    }

    // Registrar eventos
    public void eventRegister() {
        // Registrar evento
        getServer().getPluginManager().registerEvents(new PlayerJoinMessage(), this);
    }

}
