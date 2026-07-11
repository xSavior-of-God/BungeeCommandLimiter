package com.xSavior_of_God.BungeeCommandLimiter._velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import com.xSavior_of_God.BungeeCommandLimiter._velocity.commands.ReloadCommand;
import com.xSavior_of_God.BungeeCommandLimiter._velocity.events.CommandExecuteEvent;
import com.xSavior_of_God.BungeeCommandLimiter._velocity.events.DisconnectEvent;
import com.xSavior_of_God.BungeeCommandLimiter._velocity.events.PlayerChatEvent;
import com.xSavior_of_God.BungeeCommandLimiter._velocity.metrics.Metrics;
import com.xSavior_of_God.BungeeCommandLimiter.utils.Limiter;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.event.Level;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@Plugin(id = "bungeecommandlimiter", name = "BungeeCommandLimiter", version = "maven-version-number", description = "Prevent your server from crashing due to repeated command or message spam", dependencies = {}, authors = {"xSavior_of_God"})
public class MainVelocity {
    public static MainVelocity instance;

    public Limiter limiter;
    public JSONObject configuration;
    private final ProxyServer proxy;
    @Inject
    private ComponentLogger componentLogger;
    private final @DataDirectory Path dataDirectory;

    private final Metrics.Factory metricsFactory;

    @Inject
    public MainVelocity(ProxyServer proxy, Logger logger, ComponentLogger componentLogger, @DataDirectory Path dataDirectory, Metrics.Factory metricsFactory) {
        instance = this;
        this.proxy = proxy;
        this.dataDirectory = dataDirectory;
        this.metricsFactory = metricsFactory;
    }

    @Subscribe
    public void onProxyStart(ProxyInitializeEvent ignored) {
        logLegacy(Level.INFO, "&6Loading &fBungeeCommandLimiter &b&lVelocity Edition...");
        metricsFactory.make(this, 19136);

        reloadConfiguration();
        loadLimiter();
        loadListeners();

        logLegacy(Level.INFO, "&aLoading completed successfully!");
        this.proxy.getPluginManager()
                .getPlugin("bungeecommandlimiter")
                .ifPresentOrElse(plugin
                        -> logLegacy(Level.INFO, "&fVersion &e" + plugin.getDescription().getVersion().orElse("Unknown")),
                        () -> logLegacy(Level.INFO, "&cSomething went wrong while looking for plugin version, Plugin ID 'bungeecommandlimiter' not found!")
        );
        logLegacy(Level.INFO, "&f© Developed by &exSavior_of_God");
    }


    public void reloadConfiguration() {
        makeConfig();
        try {
            configuration = parseJSONFile(new File(new File(dataDirectory.toUri()), "config.json").getPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadLimiter() {
        logLegacy(Level.INFO, "&6Loading &fLimiter with &amax &f" + configuration.getInt("max") + " &acommands per &f" + configuration.getInt("time") + " &amilliseconds");
        limiter = new Limiter(configuration.getInt("max"), configuration.getInt("time"));
    }

    public void unloadLimiter() {
        limiter = null;
    }

    public void loadListeners() {
        proxy.getEventManager().register(this, new CommandExecuteEvent());
        proxy.getEventManager().register(this, new DisconnectEvent());
        proxy.getEventManager().register(this, new PlayerChatEvent());
        proxy.getCommandManager().register("bungeecommandlimiter", new ReloadCommand(), "bcl");
    }

    public void unloadListeners() {
        proxy.getEventManager().unregisterListeners(this);
        proxy.getCommandManager().unregister("bungeecommandlimiter");
    }


    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent ignored) {
        unloadListeners();
        unloadLimiter();
        instance = null;
    }


    /**
     * ONLY FOR LEGACY Mojang COLOR such as '&6' or '&l'
     * Logs a message to the console
     *
     * @param level   The level of the message
     * @param message The message to log
     */
    public static void logLegacy(Level level, String message) {
        instance.componentLogger.atLevel(level).log(LegacyComponentSerializer.legacyAmpersand().deserialize(message));
    }

    /**
     * Logs a message to the console
     *
     * @param level   The level of the message
     * @param message The message to log
     */
    public static void log(Level level, String message) {
        instance.componentLogger.atLevel(level).log(MiniMessage.miniMessage().deserialize(message));
    }


    /**
     * Logs a message to the console with an exception
     *
     * @param level   The level of the message
     * @param message The message to log
     */
    public static void log(Level level, String message, Throwable e) {
        instance.componentLogger.atLevel(level).log(MiniMessage.miniMessage().deserialize(message), e);
    }

    public final InputStream getResourceAsStream(String name) {
        return this.getClass().getClassLoader().getResourceAsStream(name);
    }

    public void makeConfig() {
        if (!new File(dataDirectory.toUri()).exists()) {
            new File(dataDirectory.toUri()).mkdir();
        }
        File file = new File(new File(dataDirectory.toUri()), "config.json");

        if (!file.exists()) {
            try (InputStream in = getResourceAsStream("config.json")) {
                Files.copy(in, file.toPath());
            } catch (IOException e) {
                log(Level.ERROR, "Failed to copy config.json", e);
            }
        }
    }

    public static JSONObject parseJSONFile(String filename) throws JSONException, IOException {
        String content = new String(Files.readAllBytes(Paths.get(filename)));
        return new JSONObject(content);
    }
}
