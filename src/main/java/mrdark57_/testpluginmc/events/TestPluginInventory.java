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
    public void clickInventory(InventoryClickEvent event) {
        String inventoryName = ColorTranslator.translate("&2TestPluginMC Inventory");
        String inventoryNameWithoutColors = ChatColor.stripColor(inventoryName);

        if (ChatColor.stripColor(event.getView().getTitle()).equals(inventoryNameWithoutColors)) {
            event.setCancelled(true); // Cancelar todas las acciones de clic en el inventario

            if (event.getCurrentItem() != null && event.getCurrentItem().getType() != Material.AIR && event.getCurrentItem().hasItemMeta()) {
                Player player = (Player) event.getWhoClicked();
                int clickedSlot = event.getRawSlot();

                if (clickedSlot == 20 && !event.isShiftClick()) {
                    // Teletransportar a un lugar "misterioso"
                    Location location = new Location(player.getWorld(), 200, 70, -200, 0, 0);
                    player.closeInventory();
                    player.teleport(location);
                    player.sendMessage(plugin.getPluginName() + ColorTranslator.translate(" &aYou have been teleported to a mysterious place!"));

                } else if (clickedSlot == 22) {
                    // Dar un diamante al jugador si tiene permisos
                    if (player.hasPermission("testplugin.inventory.diamond")) {
                        ItemStack diamond = new ItemStack(Material.DIAMOND, 1);
                        player.getInventory().addItem(diamond);
                        player.sendMessage(plugin.getPluginName() + ColorTranslator.translate(" &bEnjoy your diamond!"));
                    } else {
                        player.sendMessage(ColorTranslator.translate(plugin.getPluginName() + " &4You don't have permission to use this command."));
                    }

                } else if (clickedSlot == 24) {
                    // Abrir otro inventario
                    Inventory inventory2 = Bukkit.createInventory(null, 9, ColorTranslator.translate("&7&lNothing XD"));
                    player.openInventory(inventory2);
                }
            }
        }
    }


    public void createInventory(Player player) {
        // El tamaño debe ser un número divisible por 9
        Inventory inventory = Bukkit.createInventory(
                null, 45, ColorTranslator.translate("&2TestPluginMC Inventory"));

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

        tpItemMeta.setDisplayName(ColorTranslator.translate("&4&lTeleporter"));

        List<String> tpItemLore = new ArrayList<>();
        tpItemLore.add(ColorTranslator.translate("&7Teleports you to a mysterious place."));
        tpItemMeta.setLore(tpItemLore);

        tpItem.setItemMeta(tpItemMeta);

        inventory.setItem(20, tpItem);
    }

    public void createGiveDiamondItem(Inventory inventory) {
        ItemStack diamond = new ItemStack(Material.DIAMOND, 1);
        ItemMeta diamondMeta = diamond.getItemMeta();

        diamondMeta.setDisplayName(ColorTranslator.translate("&b&lFREE DIAMONDS!"));

        List<String> diamondLore = new ArrayList<>();
        diamondLore.add(ColorTranslator.translate("&7Gives you a diamond if you have permissions."));
        diamondMeta.setLore(diamondLore);

        diamond.setItemMeta(diamondMeta);

        inventory.setItem(22, diamond);
    }

    public void createOtherInventoryItem(Inventory inventory) {
        ItemStack otherInventory = new ItemStack(Material.CHEST, 1);
        ItemMeta otherInventoryMeta = otherInventory.getItemMeta();

        otherInventoryMeta.setDisplayName(ColorTranslator.translate("&c&lInventory 2"));

        List<String> otherInventoryLore = new ArrayList<>();
        otherInventoryLore.add(ColorTranslator.translate("&7Open other inventory."));
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
