package mrdark57_.testpluginmc.events;

import mrdark57_.testpluginmc.TestPluginMC;
import mrdark57_.testpluginmc.utils.ColorTranslator;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Kills implements Listener {

    private TestPluginMC plugin;

    public Kills(TestPluginMC plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void killEntity(EntityDeathEvent event) {
        Player killer = event.getEntity().getKiller();
        EntityType entity = event.getEntityType();
        if (killer != null && killer.getType().equals(EntityType.PLAYER)){

            if (entity.equals(EntityType.ZOMBIE)){
                killZombies(killer, entity);
            } else if (entity.equals(EntityType.PLAYER)) {
                killPlayer(killer, entity);
            }

        }
    }

    public void killPlayer(Player killer, EntityType entity) {
            //ItemStack item = new ItemStack(id, amount, dataValue) -- Para compatibilidad con versiones antiguas (1.8)
            //ItemStack item = new ItemStack(nombre, amount) -- Para versiones nuevas
            ItemStack gem = new ItemStack(Material.EMERALD,1);
            ItemMeta gemMeta = gem.getItemMeta(); // Para modificar las propiedades del item

            gemMeta.setDisplayName(ColorTranslator.translate("&2&lGem"));

            List<String> gemLore = new ArrayList<String>();
            gemLore.add(ColorTranslator.translate("&4&m                                          "));
            gemLore.add("");
            gemLore.add(ColorTranslator.translate("&7You receive this gem for you big strength."));
            gemLore.add(ColorTranslator.translate("&7You can buy items with this."));
            gemLore.add(ColorTranslator.translate("&c&lPrice: &2$5000"));
            gemLore.add("");
            gemLore.add(ColorTranslator.translate("&4&m                                          "));
            gemMeta.setLore(gemLore);

            gemMeta.addEnchant(Enchantment.DAMAGE_ALL, 10, true);

            gem.setItemMeta(gemMeta);

            if (killer.getInventory().firstEmpty() == -1){
                killer.sendMessage(plugin.getPluginName() + ChatColor.DARK_RED + " Your inventory is full!");
            }else {
                killer.getInventory().addItem(gem);
                killer.sendMessage(plugin.getPluginName() + ChatColor.GREEN + " Great job! You has received a reward!");
            }

    }

    public void killZombies(Player killer, EntityType entity) {
        // killer no puede ser el sol por ejemplo, debe ser una entidad
        // killer debe ser un jugador
        // entity debe ser un zombie


            FileConfiguration config = plugin.getConfig();
            String killerNamePath = "Players." + killer.getUniqueId() + ".name";
            String zombieKillsNumberPath = "Players." + killer.getUniqueId() + ".zombieKills";

            config.set(killerNamePath, killer.getName());

            if (config.contains(zombieKillsNumberPath)){

                int zombiesKilled = Integer.parseInt(config.getString(zombieKillsNumberPath));
                int counter = zombiesKilled + 1;

                config.set(zombieKillsNumberPath, counter);
                plugin.saveConfig();

                killer.sendMessage(plugin.getPluginName() + ChatColor.RED + " You are killed " + counter + " zombies!");
            } else {
                config.set(zombieKillsNumberPath, 1);
                plugin.saveConfig();

                killer.sendMessage(plugin.getPluginName() + ChatColor.RED + " You are killed your first zombie!");
            }

        }

}
