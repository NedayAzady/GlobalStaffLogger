package me.nedayazady.globalstafflogger.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.event.command.CommandExecuteEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.ServerConnection;
import me.nedayazady.globalstafflogger.manager.SpyManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayerListener {

    private final SpyManager spyManager;
    private final ProxyServer server;
    private final Logger logger = LoggerFactory.getLogger(PlayerListener.class);

    public PlayerListener(SpyManager spyManager, ProxyServer server) {
        this.spyManager = spyManager;
        this.server = server;
    }

    @Subscribe
    public void onPlayerChat(PlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();
        
        String serverName = player.getCurrentServer().map(ServerConnection::getServerInfo).map(info -> info.getName()).orElse("Unknown");
        String logMessage = player.getUsername() + " (" + serverName + ") : " + message;
        
        logger.info("[Chat] " + logMessage);

        for (Player p : server.getAllPlayers()) {
            if (p.hasPermission("nedayazady.spy.chat") && spyManager.isChatSpyEnabled(p.getUniqueId())) {
                p.sendMessage(Component.text("[Spy] " + logMessage, NamedTextColor.GRAY));
            }
        }
    }

    @Subscribe
    public void onCommandExecute(CommandExecuteEvent event) {
        if (!(event.getCommandSource() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getCommandSource();
        String command = event.getCommand();
        
        // Exclude the spy command itself from being spied on to avoid spam
        if (command.toLowerCase().startsWith("spy")) {
            return;
        }

        String serverName = player.getCurrentServer().map(ServerConnection::getServerInfo).map(info -> info.getName()).orElse("Unknown");
        String logMessage = player.getUsername() + " (" + serverName + ") : /" + command;
        
        logger.info("[Command] " + logMessage);

        for (Player p : server.getAllPlayers()) {
            if (p.hasPermission("nedayazady.spy.cmd") && spyManager.isCmdSpyEnabled(p.getUniqueId())) {
                p.sendMessage(Component.text("[Spy] " + logMessage, NamedTextColor.YELLOW));
            }
        }
    }

    @Subscribe
    public void onServerConnected(ServerConnectedEvent event) {
        Player player = event.getPlayer();
        String serverName = event.getServer().getServerInfo().getName();
        
        String previousServerName = event.getPreviousServer().map(server -> server.getServerInfo().getName()).orElse("None");
        
        if (previousServerName.equals("None")) {
            return; // Ignore initial connection
        }
        
        String logMessage = player.getUsername() + " switched from " + previousServerName + " to " + serverName;
        
        logger.info("[Switch] " + logMessage);

        for (Player p : server.getAllPlayers()) {
            if (p.hasPermission("nedayazady.spy.sw") && spyManager.isSwitchSpyEnabled(p.getUniqueId())) {
                p.sendMessage(Component.text("[Spy] " + logMessage, NamedTextColor.AQUA));
            }
        }
    }
}
