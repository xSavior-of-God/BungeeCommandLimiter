package com.xSavior_of_God.BungeeCommandLimiter._velocity.events;

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.xSavior_of_God.BungeeCommandLimiter._velocity.MainVelocity;

public class DisconnectEvent {
    @Subscribe(order = PostOrder.NORMAL)
    public void onDisconnectEvent(com.velocitypowered.api.event.connection.DisconnectEvent e) {
        MainVelocity.instance.limiter.removePlayer(e.getPlayer().getUsername());
    }
}
