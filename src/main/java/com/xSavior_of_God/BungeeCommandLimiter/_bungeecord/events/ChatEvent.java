package com.xSavior_of_God.BungeeCommandLimiter._bungeecord.events;

import com.xSavior_of_God.BungeeCommandLimiter._bungeecord.MainBungeecord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class ChatEvent implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChatEvent(net.md_5.bungee.api.event.ChatEvent e) {
        if (e.getSender() instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) e.getSender();
            if (MainBungeecord.instance.limiter.checkPlayer(player.getDisplayName())) {
                e.setCancelled(true);
                player.disconnect(ChatColor.translateAlternateColorCodes('&', MainBungeecord.instance.configuration.getString("kick-message", "&cYou are sending too many commands and messages!")));
                MainBungeecord.instance.limiter.removePlayer(player.getDisplayName());
                return;
            }
        }
    }
}
