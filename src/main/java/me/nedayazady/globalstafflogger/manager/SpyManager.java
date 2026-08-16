package me.nedayazady.globalstafflogger.manager;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class SpyManager {

    private final Set<UUID> chatSpyEnabled = new HashSet<>();
    private final Set<UUID> cmdSpyEnabled = new HashSet<>();
    private final Set<UUID> switchSpyEnabled = new HashSet<>();

    public boolean isChatSpyEnabled(UUID uuid) {
        return chatSpyEnabled.contains(uuid);
    }

    public void toggleChatSpy(UUID uuid) {
        if (chatSpyEnabled.contains(uuid)) {
            chatSpyEnabled.remove(uuid);
        } else {
            chatSpyEnabled.add(uuid);
        }
    }

    public boolean isCmdSpyEnabled(UUID uuid) {
        return cmdSpyEnabled.contains(uuid);
    }

    public void toggleCmdSpy(UUID uuid) {
        if (cmdSpyEnabled.contains(uuid)) {
            cmdSpyEnabled.remove(uuid);
        } else {
            cmdSpyEnabled.add(uuid);
        }
    }

    public boolean isSwitchSpyEnabled(UUID uuid) {
        return switchSpyEnabled.contains(uuid);
    }

    public void toggleSwitchSpy(UUID uuid) {
        if (switchSpyEnabled.contains(uuid)) {
            switchSpyEnabled.remove(uuid);
        } else {
            switchSpyEnabled.add(uuid);
        }
    }
}
