package com.xSavior_of_God.BungeeCommandLimiter._velocity.events;

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.proxy.Player;
import com.xSavior_of_God.BungeeCommandLimiter._velocity.MainVelocity;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class PlayerChatEvent {
    @Subscribe(order = PostOrder.FIRST)
    public void onPlayerChatEvent(com.velocitypowered.api.event.player.PlayerChatEvent e) {
        Player player = e.getPlayer();
        if (MainVelocity.instance.limiter.checkPlayer(player.getUsername())) {
            player.disconnect( LegacyComponentSerializer.legacyAmpersand().deserialize(MainVelocity.instance.configuration.getString("kick-message"))  );
            MainVelocity.instance.limiter.removePlayer(player.getUsername());
        }
    }
}
