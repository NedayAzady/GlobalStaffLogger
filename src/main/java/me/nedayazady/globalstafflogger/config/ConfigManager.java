package me.nedayazady.globalstafflogger.config;

import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

public class ConfigManager {

    private final Path dataDirectory;
    private CommentedConfigurationNode configNode;
    private CommentedConfigurationNode messagesNode;

    public ConfigManager(Path dataDirectory) {
        this.dataDirectory = dataDirectory;
    }

    public void loadConfigs() {
        if (!Files.exists(dataDirectory)) {
            try {
                Files.createDirectories(dataDirectory);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        configNode = loadConfig("config.yml");
        messagesNode = loadConfig("messages.yml");
    }

    private CommentedConfigurationNode loadConfig(String fileName) {
        File file = new File(dataDirectory.toFile(), fileName);

        if (!file.exists()) {
            try (InputStream in = getClass().getResourceAsStream("/" + fileName)) {
                if (in != null) {
                    Files.copy(in, file.toPath());
                } else {
                    file.createNewFile();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        YamlConfigurationLoader loader = YamlConfigurationLoader.builder()
                .path(file.toPath())
                .build();

        try {
            return loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Config Methods
    public List<String> getExcludedCommands() {
        try {
            return configNode.node("excluded-commands").getList(String.class, Collections.emptyList());
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public String getConsoleLogPrefix() {
        return configNode.node("console-log-prefix").getString("[GlobalStaffLogger]");
    }

    // Messages Methods
    public String getMessage(String path) {
        return messagesNode.node((Object[]) path.split("\\.")).getString("");
    }
}
