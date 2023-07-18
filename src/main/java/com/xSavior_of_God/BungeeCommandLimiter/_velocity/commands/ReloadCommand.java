package com.xSavior_of_God.BungeeCommandLimiter._velocity.commands;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.xSavior_of_God.BungeeCommandLimiter._velocity.MainVelocity;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

public class ReloadCommand implements SimpleCommand {
    List<String> commands = Arrays.asList("bungeecommandlimiter", "bcl");

    @Override
    public void execute(Invocation sender) {
        MainVelocity.log(Level.INFO, "&6Reloading BungeeCommandLimiter...");

        if(sender.source() instanceof Player)
            sender.source().sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize("&6Reloading BungeeCommandLimiter..."));

        MainVelocity.instance.unloadListeners();
        MainVelocity.instance.unloadLimiter();

        MainVelocity.instance.reloadConfiguration();

        MainVelocity.instance.loadLimiter();
        MainVelocity.instance.loadListeners();

        if(sender.source() instanceof Player)
            sender.source().sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize("&aReloaded BungeeCommandLimiter!"));
        MainVelocity.log(Level.INFO, "&aReloaded BungeeCommandLimiter!");
    }

    @Override
    public List<String> suggest(Invocation invocation) {
        return commands;
    }

    @Override
    public CompletableFuture<List<String>> suggestAsync(Invocation invocation) {
        return CompletableFuture.completedFuture(commands);
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return invocation.source().hasPermission("bungeecommandlimiter.reload");
    }
}
