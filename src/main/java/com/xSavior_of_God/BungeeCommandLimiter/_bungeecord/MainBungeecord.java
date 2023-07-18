package com.xSavior_of_God.BungeeCommandLimiter._bungeecord;

import com.xSavior_of_God.BungeeCommandLimiter._bungeecord.commands.ReloadCommand;
import com.xSavior_of_God.BungeeCommandLimiter._bungeecord.events.ChatEvent;
import com.xSavior_of_God.BungeeCommandLimiter._bungeecord.events.PlayerDisconnectEvent;
import com.xSavior_of_God.BungeeCommandLimiter.utils.Limiter;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.logging.Level;

public class MainBungeecord extends Plugin {
    public static MainBungeecord instance;
    public Limiter limiter;
    public Configuration configuration;

    @Override
    public void onEnable() {
        instance = this;
        log(Level.INFO, "&6Loading BungeeCommandLimiter...");

        reloadConfiguration();
        loadLimiter();
        loadListeners();

        log(Level.INFO, "&eVersion &f" + this.getDescription().getVersion());
        log(Level.INFO, "&eDeveloped by &fxSavior_of_God");
    }

    public void reloadConfiguration() {
        makeConfig();
        try {
            configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "config.yml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadLimiter() {
        log(Level.INFO, "&6Loading Limiter with max &f" + configuration.getInt("max", 10) + " &6commands per &f" + configuration.getInt("time", 3000) + " &6milliseconds");
        limiter = new Limiter(configuration.getInt("max", 5), configuration.getInt("time", 3000));
    }

    public void unloadLimiter() {
        limiter = null;
    }

    public void loadListeners() {
        getProxy().getPluginManager().registerListener(this, new PlayerDisconnectEvent());
        getProxy().getPluginManager().registerListener(this, new ChatEvent());
        getProxy().getPluginManager().registerCommand(this, new ReloadCommand());
    }

    public void unloadListeners() {
        getProxy().getPluginManager().unregisterListeners(this);
        getProxy().getPluginManager().unregisterCommands(this);
    }

    @Override
    public void onDisable() {
        unloadListeners();
        unloadLimiter();
        instance = null;
    }


    /**
     * Logs a message to the console
     *
     * @param level   The level of the message
     * @param message The message to log
     */
    public static void log(Level level, String message) {
        if (level == Level.INFO) {
            instance.getLogger().info(ChatColor.translateAlternateColorCodes('&', message));
        } else if (level == Level.WARNING) {
            instance.getLogger().warning(ChatColor.translateAlternateColorCodes('&', message));
        } else if (level == Level.SEVERE) {
            instance.getLogger().severe(ChatColor.translateAlternateColorCodes('&', message));
        }
    }

    public void makeConfig() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }

        File file = new File(getDataFolder(), "config.yml");
        if (!file.exists()) {
            try (InputStream in = getResourceAsStream("config.yml")) {
                Files.copy(in, file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
