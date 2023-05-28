package mrdark57_.testpluginmc.events;

import mrdark57_.testpluginmc.TestPluginMC;
import mrdark57_.testpluginmc.utils.ColorTranslator;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.ArrayList;
import java.util.List;

public class Chat implements Listener {

    private TestPluginMC plugin;

    public Chat(TestPluginMC plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void modifyChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();

        String filteredWord = filterBadWords(player, message);
        event.setMessage(filteredWord);

        String format = changeChatFormat(player, filteredWord);
        event.setFormat(format);

    }

    public String filterBadWords(Player player, String message) {
        List<String> badWords = fillBadWords();

        for (String word : badWords) {
            if (message.toLowerCase().contains(word)){

                String censuredWord = "";

                for (int i = 0; i < word.length(); i++) {
                    censuredWord += "*";
                }
                message = message.replace(word, censuredWord);

            }
        }
        player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_HIT, 10, (float) 0.2);
        return message;
    }

    public List<String> fillBadWords() {
        List<String> badWords = new ArrayList<>();

        badWords.add("noob");
        badWords.add("stupid");
        badWords.add("loser");

        return badWords;
    }

    public String changeChatFormat(Player player, String message) {
        return ColorTranslator.translate("&b" + player.getName() + " &3>> &f" + message);
    }

}
