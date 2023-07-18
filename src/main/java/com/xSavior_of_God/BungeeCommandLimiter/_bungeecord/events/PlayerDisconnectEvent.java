package com.xSavior_of_God.BungeeCommandLimiter._bungeecord.events;

import com.xSavior_of_God.BungeeCommandLimiter._bungeecord.MainBungeecord;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PlayerDisconnectEvent implements Listener {

    @EventHandler
    public void onPlayerDisconnectEvent(net.md_5.bungee.api.event.PlayerDisconnectEvent e) {
        MainBungeecord.instance.limiter.removePlayer(e.getPlayer().getDisplayName());
    }

}
