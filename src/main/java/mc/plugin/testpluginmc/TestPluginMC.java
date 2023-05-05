package mc.plugin.testpluginmc;

import mc.plugin.testpluginmc.commands.Kit;
import org.bukkit.plugin.java.JavaPlugin;

public class TestPluginMC extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("onEnable is called!");

        // Registrar comando /kit
        this.getCommand("kit").setExecutor(new Kit());
    }
    @Override
    public void onDisable() {
        getLogger().info("onDisable is called!");
    }

}
