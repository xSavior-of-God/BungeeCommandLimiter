package com.xSavior_of_God.BungeeCommandLimiter._bungeecord.commands;

import com.xSavior_of_God.BungeeCommandLimiter._bungeecord.MainBungeecord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.logging.Level;

public class ReloadCommand extends Command {

    public ReloadCommand() {
        super("bungeecommandlimiter", "bungeecommandlimiter.reload", "bcl");
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (commandSender instanceof ProxiedPlayer) {
            ProxiedPlayer p = (ProxiedPlayer) commandSender;
            if (!p.hasPermission("bungeecommandlimiter.reload")) {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou do not have permission to execute this command!"));
                return;
            }
        }
        if(commandSender instanceof ProxiedPlayer)
            MainBungeecord.log(Level.INFO, "&6Reloading BungeeCommandLimiter...");
        commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6Reloading BungeeCommandLimiter..."));

        MainBungeecord.instance.unloadListeners();
        MainBungeecord.instance.unloadLimiter();

        MainBungeecord.instance.reloadConfiguration();

        MainBungeecord.instance.loadLimiter();
        MainBungeecord.instance.loadListeners();

        if(commandSender instanceof ProxiedPlayer)
            commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aReloaded BungeeCommandLimiter!"));
        MainBungeecord.log(Level.INFO, "&aReloaded BungeeCommandLimiter!");
    }
}
