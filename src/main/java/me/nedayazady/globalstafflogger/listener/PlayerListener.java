package me.nedayazady.globalstafflogger.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.event.command.CommandExecuteEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.ServerConnection;
import me.nedayazady.globalstafflogger.config.ConfigManager;
import me.nedayazady.globalstafflogger.manager.SpyManager;
import me.nedayazady.globalstafflogger.utils.ColorUtils;
import net.kyori.adventure.text.Component;
import org.slf4j.Logger;

import java.util.List;

public class PlayerListener {

    private final SpyManager spyManager;
    private final ProxyServer server;
    private final ConfigManager configManager;
    private final Logger logger;

    public PlayerListener(SpyManager spyManager, ProxyServer server, ConfigManager configManager, Logger logger) {
        this.spyManager = spyManager;
        this.server = server;
        this.configManager = configManager;
        this.logger = logger;
    }

    @Subscribe
    public void onPlayerChat(PlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();
        
        String serverName = player.getCurrentServer().map(ServerConnection::getServerInfo).map(info -> info.getName()).orElse("Unknown");
        
        String logMessage = configManager.getMessage("chat-format")
                .replace("{player}", player.getUsername())
                .replace("{server}", serverName)
                .replace("{message}", message);
                
        String prefix = configManager.getMessage("prefix");
        Component parsedMessage = ColorUtils.parse(prefix + logMessage);

        logger.info(configManager.getConsoleLogPrefix() + " [Chat] " + player.getUsername() + " (" + serverName + ") : " + message);

        for (Player p : server.getAllPlayers()) {
            if (p.hasPermission("nedayazady.spy.chat") && spyManager.isChatSpyEnabled(p.getUniqueId())) {
                p.sendMessage(parsedMessage);
            }
        }
    }

    @Subscribe
    public void onCommandExecute(CommandExecuteEvent event) {
        if (!(event.getCommandSource() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getCommandSource();
        String commandLine = event.getCommand();
        String command = commandLine.split(" ")[0].toLowerCase();
        
        List<String> excluded = configManager.getExcludedCommands();
        if (excluded.contains(command)) {
            return;
        }

        String serverName = player.getCurrentServer().map(ServerConnection::getServerInfo).map(info -> info.getName()).orElse("Unknown");
        
        String logMessage = configManager.getMessage("cmd-format")
                .replace("{player}", player.getUsername())
                .replace("{server}", serverName)
                .replace("{command}", commandLine);

        String prefix = configManager.getMessage("prefix");
        Component parsedMessage = ColorUtils.parse(prefix + logMessage);

        logger.info(configManager.getConsoleLogPrefix() + " [Command] " + player.getUsername() + " (" + serverName + ") : /" + commandLine);

        for (Player p : server.getAllPlayers()) {
            if (p.hasPermission("nedayazady.spy.cmd") && spyManager.isCmdSpyEnabled(p.getUniqueId())) {
                p.sendMessage(parsedMessage);
            }
        }
    }

    @Subscribe
    public void onServerConnected(ServerConnectedEvent event) {
        Player player = event.getPlayer();
        String serverName = event.getServer().getServerInfo().getName();
        
        String previousServerName = event.getPreviousServer().map(serverConnection -> serverConnection.getServerInfo().getName()).orElse("None");
        
        if (previousServerName.equals("None")) {
            return; // Ignore initial connection
        }
        
        String logMessage = configManager.getMessage("switch-format")
                .replace("{player}", player.getUsername())
                .replace("{previous_server}", previousServerName)
                .replace("{new_server}", serverName);

        String prefix = configManager.getMessage("prefix");
        Component parsedMessage = ColorUtils.parse(prefix + logMessage);
        
        logger.info(configManager.getConsoleLogPrefix() + " [Switch] " + player.getUsername() + " switched from " + previousServerName + " to " + serverName);

        for (Player p : server.getAllPlayers()) {
            if (p.hasPermission("nedayazady.spy.sw") && spyManager.isSwitchSpyEnabled(p.getUniqueId())) {
                p.sendMessage(parsedMessage);
            }
        }
    }
}
