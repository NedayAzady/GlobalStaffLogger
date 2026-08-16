package me.nedayazady.globalstafflogger.command;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import me.nedayazady.globalstafflogger.manager.SpyManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class SpyCommand implements SimpleCommand {

    private final SpyManager spyManager;

    public SpyCommand(SpyManager spyManager) {
        this.spyManager = spyManager;
    }

    @Override
    public void execute(Invocation invocation) {
        if (!(invocation.source() instanceof Player)) {
            invocation.source().sendMessage(Component.text("Only players can use this command.", NamedTextColor.RED));
            return;
        }

        Player player = (Player) invocation.source();
        String[] args = invocation.arguments();

        if (args.length == 0) {
            player.sendMessage(Component.text("Usage: /spy <chat|cmd|sw>", NamedTextColor.RED));
            return;
        }

        String type = args[0].toLowerCase();

        switch (type) {
            case "chat":
                if (player.hasPermission("nedayazady.spy.chat")) {
                    spyManager.toggleChatSpy(player.getUniqueId());
                    boolean enabled = spyManager.isChatSpyEnabled(player.getUniqueId());
                    player.sendMessage(Component.text("Chat spy is now " + (enabled ? "enabled" : "disabled") + ".", enabled ? NamedTextColor.GREEN : NamedTextColor.RED));
                } else {
                    player.sendMessage(Component.text("You do not have permission to use chat spy.", NamedTextColor.RED));
                }
                break;
            case "cmd":
                if (player.hasPermission("nedayazady.spy.cmd")) {
                    spyManager.toggleCmdSpy(player.getUniqueId());
                    boolean enabled = spyManager.isCmdSpyEnabled(player.getUniqueId());
                    player.sendMessage(Component.text("Command spy is now " + (enabled ? "enabled" : "disabled") + ".", enabled ? NamedTextColor.GREEN : NamedTextColor.RED));
                } else {
                    player.sendMessage(Component.text("You do not have permission to use command spy.", NamedTextColor.RED));
                }
                break;
            case "sw":
                if (player.hasPermission("nedayazady.spy.sw")) {
                    spyManager.toggleSwitchSpy(player.getUniqueId());
                    boolean enabled = spyManager.isSwitchSpyEnabled(player.getUniqueId());
                    player.sendMessage(Component.text("Switch spy is now " + (enabled ? "enabled" : "disabled") + ".", enabled ? NamedTextColor.GREEN : NamedTextColor.RED));
                } else {
                    player.sendMessage(Component.text("You do not have permission to use switch spy.", NamedTextColor.RED));
                }
                break;
            default:
                player.sendMessage(Component.text("Usage: /spy <chat|cmd|sw>", NamedTextColor.RED));
                break;
        }
    }
    
    @Override
    public boolean hasPermission(Invocation invocation) {
        return invocation.source().hasPermission("nedayazady.spy.chat") || 
               invocation.source().hasPermission("nedayazady.spy.cmd") || 
               invocation.source().hasPermission("nedayazady.spy.sw");
    }
}
