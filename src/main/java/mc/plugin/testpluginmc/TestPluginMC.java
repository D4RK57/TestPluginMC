package mc.plugin.testpluginmc;

import mc.plugin.testpluginmc.commands.Kit;
import mc.plugin.testpluginmc.events.PlayerJoinMessage;
import org.bukkit.plugin.java.JavaPlugin;

public class TestPluginMC extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("onEnable is called!");

        // Registrar comando /kit
        this.getCommand("kit").setExecutor(new Kit());

        // Registrar evento
        getServer().getPluginManager().registerEvents(new PlayerJoinMessage(), this);
    }
    @Override
    public void onDisable() {
        getLogger().info("onDisable is called!");
    }

}
