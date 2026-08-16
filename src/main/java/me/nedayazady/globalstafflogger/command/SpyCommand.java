package me.nedayazady.globalstafflogger.command;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import me.nedayazady.globalstafflogger.config.ConfigManager;
import me.nedayazady.globalstafflogger.manager.SpyManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class SpyCommand implements SimpleCommand {

    private final SpyManager spyManager;
    private final ConfigManager configManager;

    public SpyCommand(SpyManager spyManager, ConfigManager configManager) {
        this.spyManager = spyManager;
        this.configManager = configManager;
    }

    @Override
    public void execute(Invocation invocation) {
        if (!(invocation.source() instanceof Player)) {
            sendMessage(invocation, configManager.getMessage("only-players"));
            return;
        }

        Player player = (Player) invocation.source();
        String[] args = invocation.arguments();

        if (args.length == 0) {
            sendMessage(player, configManager.getMessage("usage"));
            return;
        }

        String type = args[0].toLowerCase();
        boolean enabled = false;

        switch (type) {
            case "chat":
                if (player.hasPermission("nedayazady.spy.chat")) {
                    spyManager.toggleChatSpy(player.getUniqueId());
                    enabled = spyManager.isChatSpyEnabled(player.getUniqueId());
                    sendStatusMessage(player, "Chat", enabled);
                } else {
                    sendMessage(player, configManager.getMessage("no-permission"));
                }
                break;
            case "cmd":
                if (player.hasPermission("nedayazady.spy.cmd")) {
                    spyManager.toggleCmdSpy(player.getUniqueId());
                    enabled = spyManager.isCmdSpyEnabled(player.getUniqueId());
                    sendStatusMessage(player, "Command", enabled);
                } else {
                    sendMessage(player, configManager.getMessage("no-permission"));
                }
                break;
            case "sw":
                if (player.hasPermission("nedayazady.spy.sw")) {
                    spyManager.toggleSwitchSpy(player.getUniqueId());
                    enabled = spyManager.isSwitchSpyEnabled(player.getUniqueId());
                    sendStatusMessage(player, "Switch", enabled);
                } else {
                    sendMessage(player, configManager.getMessage("no-permission"));
                }
                break;
            default:
                sendMessage(player, configManager.getMessage("usage"));
                break;
        }
    }

    private void sendStatusMessage(Player player, String type, boolean enabled) {
        String messageKey = enabled ? "spy-enabled" : "spy-disabled";
        String rawMessage = configManager.getMessage(messageKey).replace("{type}", type);
        sendMessage(player, rawMessage);
    }

    private void sendMessage(Invocation invocation, String message) {
        String prefix = configManager.getMessage("prefix");
        invocation.source().sendMessage(MiniMessage.miniMessage().deserialize(prefix + message));
    }

    private void sendMessage(Player player, String message) {
        String prefix = configManager.getMessage("prefix");
        player.sendMessage(MiniMessage.miniMessage().deserialize(prefix + message));
    }
    
    @Override
    public boolean hasPermission(Invocation invocation) {
        return invocation.source().hasPermission("nedayazady.spy.chat") || 
               invocation.source().hasPermission("nedayazady.spy.cmd") || 
               invocation.source().hasPermission("nedayazady.spy.sw");
    }
}
