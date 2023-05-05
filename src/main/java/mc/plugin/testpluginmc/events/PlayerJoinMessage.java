package mc.plugin.testpluginmc.events;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinMessage implements Listener {

    // Cuando ocurre el evento PlayerJoinEvent (jugador entra al servidor) envía un mensaje
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Bukkit.broadcastMessage("Welcome " + event.getPlayer().getName() + " to the server!");
    }

}
