package mrdark57_.testpluginmc.events;

import mrdark57_.testpluginmc.TestPluginMC;
import mrdark57_.testpluginmc.utils.ColorTranslator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class TestPluginInventory implements Listener {

    private TestPluginMC plugin;

    public TestPluginInventory(TestPluginMC plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void clickInventory(InventoryClickEvent event){
        String inventoryName = ColorTranslator.translateColors("&2TestPluginMC Inventory");
        String inventoryNameWithoutColors = ChatColor.stripColor(inventoryName);

        if (ChatColor.stripColor(event.getView().getTitle()).equals(inventoryNameWithoutColors)) {

            // Si hace click en su propio inventario o
            // Si se clickea fuera del inventario o
            // Si el item seleccionado es aire, el evento se cancela
            if (event.getCurrentItem() == null || event.getSlotType() == null || event.getCurrentItem().getType().equals(Material.AIR)) {
                event.setCancelled(true);

            }else {
                if (event.getCurrentItem().hasItemMeta()) {
                    event.setCancelled(true);
                    Player player = (Player) event.getWhoClicked();

                    if (event.getSlot() == 20) {
                        // Tp a lugar "misterioso"
                        Location location = new Location(player.getWorld(), 200, 70, -200, 0, 0);
                        player.closeInventory();
                        player.teleport(location);
                        player.sendMessage(plugin.getPluginName() +
                                ColorTranslator.translateColors(" &aYou has been teleported to a mysterious place!"));
                        
                    } else if (event.getSlot() == 22) {
                        // Dar un diamante al jugador si tiene permisos
                        if (player.hasPermission("testplugin.inventory.diamond")){
                            ItemStack diamond = new ItemStack(Material.DIAMOND, 1);
                            player.getInventory().addItem(diamond);
                            player.sendMessage(plugin.getPluginName() +
                                    ColorTranslator.translateColors(" &bEnjoy your diamond!"));
                        }else {
                            player.sendMessage(ColorTranslator.translateColors(plugin.getPluginName() + " &4You don´t have permissions to use this command."));
                        }
                        
                    } else if (event.getSlot() == 24) {
                        Inventory inventory2 = Bukkit.createInventory(
                                null, 9, ColorTranslator.translateColors("&7&lNothing XD"));
                        player.openInventory(inventory2);
                    }else {
                        event.setCancelled(true);
                    }

                }
            }
        }

    }

    public void createInventory(Player player) {
        // El tamaño debe ser un número divisible por 9
        Inventory inventory = Bukkit.createInventory(
                null, 45, ColorTranslator.translateColors("&2TestPluginMC Inventory"));

        // Crear item que hace tp
        createTpItem(inventory);

        // Da un diamante si tienes permisos
        createGiveDiamondItem(inventory);

        // Abre otro inventario
        createOtherInventoryItem(inventory);

        // Paneles de cristal rodeando el inventario (decoración)
        createBorderDecoration(inventory);

        player.openInventory(inventory);

    }

    public void createTpItem(Inventory inventory) {
        ItemStack tpItem = new ItemStack(Material.REDSTONE_BLOCK, 1);
        ItemMeta tpItemMeta = tpItem.getItemMeta();

        tpItemMeta.setDisplayName(ColorTranslator.translateColors("&4&lTeleporter"));

        List<String> tpItemLore = new ArrayList<>();
        tpItemLore.add(ColorTranslator.translateColors("&7Teleports you to a mysterious place."));
        tpItemMeta.setLore(tpItemLore);

        tpItem.setItemMeta(tpItemMeta);

        inventory.setItem(20, tpItem);
    }

    public void createGiveDiamondItem(Inventory inventory) {
        ItemStack diamond = new ItemStack(Material.DIAMOND, 1);
        ItemMeta diamondMeta = diamond.getItemMeta();

        diamondMeta.setDisplayName(ColorTranslator.translateColors("&b&lFREE DIAMONDS!"));

        List<String> diamondLore = new ArrayList<>();
        diamondLore.add(ColorTranslator.translateColors("&7Gives you a diamond if you have permissions."));
        diamondMeta.setLore(diamondLore);

        diamond.setItemMeta(diamondMeta);

        inventory.setItem(22, diamond);
    }

    public void createOtherInventoryItem(Inventory inventory) {
        ItemStack otherInventory = new ItemStack(Material.CHEST, 1);
        ItemMeta otherInventoryMeta = otherInventory.getItemMeta();

        otherInventoryMeta.setDisplayName(ColorTranslator.translateColors("&c&lInventory 2"));

        List<String> otherInventoryLore = new ArrayList<>();
        otherInventoryLore.add(ColorTranslator.translateColors("&7Open other inventory."));
        otherInventoryMeta.setLore(otherInventoryLore);

        otherInventory.setItemMeta(otherInventoryMeta);

        inventory.setItem(24, otherInventory);
    }

    public void createBorderDecoration(Inventory inventory) {
        ItemStack decoration = new ItemStack(Material.CYAN_STAINED_GLASS_PANE, 1);
        ItemMeta decorationMeta = decoration.getItemMeta();

        decorationMeta.setDisplayName(" ");

        decoration.setItemMeta(decorationMeta);

        fillBorderWithItem(inventory, decoration);
    }

    public void fillBorderWithItem(Inventory inventory, ItemStack item) {
        // Autor: ImIllusion
        // Link: https://www.spigotmc.org/threads/easy-method-to-fill-inventory-outline-borders.537167/
        int size = inventory.getSize();

        int rowCount = size / 9;

        for(int index = 0; index < size; index++) {
            int row = index / 9;
            int column = (index % 9) + 1;

            if(row == 0 || row == rowCount-1 || column == 1 || column == 9) {
                inventory.setItem(index, item);
            }

        }
    }

}
