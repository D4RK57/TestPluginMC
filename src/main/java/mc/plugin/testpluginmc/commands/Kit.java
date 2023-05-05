package mc.plugin.testpluginmc.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Kit implements CommandExecutor {

    // Se llama a este método cuando alguien usa el comando
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

    /*
    CommandSender representa quién envía el comando. Puede ser Player (Jugador), ConsoleCommandSender (Consola), ó BlockCommandSender (Bloque de comandos)
    Command representa el comando que se llama.
    Label representa la primera palabra del comando (sin incluir argumentos) que envía el mensajero
    Args representa los argumentos del comando
     */

        if (sender instanceof Player) {
            Player player = (Player) sender;

            // Crear instancias de items con la clase ItemStack
            ItemStack diamond = new ItemStack(Material.DIAMOND, 10);
            ItemStack gold = new ItemStack(Material.GOLD_INGOT, 20);

            // Añadir los items al inventario del jugador
            player.getInventory().addItem(diamond, gold);
        }

        // Si se usa el comando correctamente devuelve un true
        return true;
    }

}
