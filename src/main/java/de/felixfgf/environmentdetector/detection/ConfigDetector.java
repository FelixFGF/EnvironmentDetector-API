package de.felixfgf.environmentdetector.detection;

import de.felixfgf.environmentdetector.Environmentdetector;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class ConfigDetector {
    private static final Path CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve("environmentdetector.properties");
    private static final Properties properties = new Properties();

    static {
        load();
    }

    public static void load() {
        if (Files.exists(CONFIG_FILE)) {
            try (InputStream is = Files.newInputStream(CONFIG_FILE)) {
                properties.load(is);
            } catch (IOException e) {
                Environmentdetector.LOGGER.warn("Failed to load config file", e);
            }
        } else {
            // Create default config
            properties.setProperty("manual_pack_name", "");
            properties.setProperty("manual_launcher_name", "");
            try {
                Files.createDirectories(CONFIG_FILE.getParent());
                try (OutputStream os = Files.newOutputStream(CONFIG_FILE)) {
                    properties.store(os, "EnvironmentDetector Configuration Fallback");
                }
            } catch (IOException e) {
                Environmentdetector.LOGGER.warn("Failed to create default config", e);
            }
        }
    }

    public static String getManualPackName() {
        return properties.getProperty("manual_pack_name", "").trim();
    }

    public static String getManualLauncherName() {
        return properties.getProperty("manual_launcher_name", "").trim();
    }
}
