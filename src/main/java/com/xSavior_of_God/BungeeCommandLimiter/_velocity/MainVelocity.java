package com.xSavior_of_God.BungeeCommandLimiter._velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
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
import com.xSavior_of_God.BungeeCommandLimiter.utils.Limiter;
import net.kyori.adventure.audience.MessageType;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;

@Plugin(
        id = "bungeecommandlimiter",
        name = "BungeeCommandLimiter",
        version = "maven-version-number",
        description = "Prevent your server from crashing due to repeated command or message spam",
        dependencies = {},
        authors = {"xSavior_of_God"}
)
public class MainVelocity {
    public static MainVelocity instance;

    public Limiter limiter;
    public JSONObject configuration;
    private final ProxyServer proxy;
    private final Logger logger;
    private final @DataDirectory Path dataDirectory;

    @Inject
    public MainVelocity(ProxyServer proxy, Logger logger, @DataDirectory Path dataDirectory) {
        instance = this;
        this.proxy = proxy;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
    }

    @Subscribe
    public void onProxyStart(ProxyInitializeEvent ignored) {
        log(Level.INFO, "&6Loading BungeeCommandLimiter (Velocity Edition)...");
        CommandManager commandManager = proxy.getCommandManager();

        reloadConfiguration();
        loadLimiter();
        loadListeners();

        log(Level.INFO, "&eVersion&f "+this.proxy.getPluginManager().getPlugin("bungeecommandlimiter").get().getDescription().getVersion());
        log(Level.INFO, "&eDeveloped by &fxSavior_of_God");
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
        log(Level.INFO, "&6Loading Limiter with max &f" + configuration.getInt("max") + " &6commands per &f" + configuration.getInt("time") + " &6milliseconds");
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
     * Logs a message to the console
     *
     * @param level   The level of the message
     * @param message The message to log
     */
    public static void log(Level level, String message) {
        //TODO different level with colors
        instance.proxy.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(message).asComponent(), MessageType.SYSTEM);
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
                e.printStackTrace();
            }
        }
    }

    public static JSONObject parseJSONFile(String filename) throws JSONException, IOException {
        String content = new String(Files.readAllBytes(Paths.get(filename)));
        return new JSONObject(content);
    }
}
