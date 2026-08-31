package de.felixfgf.environmentdetector.detection;

import de.felixfgf.environmentdetector.Environmentdetector;
import net.fabricmc.loader.api.FabricLoader;

public class RuntimeEnvironmentDetector {
    public static String getMinecraftVersion() {
        try {
            // Using Fabric Loader to get the Minecraft version is more reliable across mappings
            // and avoids SharedConstants version-naming issues (getName/getId/etc)
            return FabricLoader.getInstance().getModContainer("minecraft")
                    .map(m -> m.getMetadata().getVersion().getFriendlyString())
                    .orElse("Unknown Version");
        } catch (Exception e) {
            Environmentdetector.LOGGER.warn("Failed to detect Minecraft version via Fabric Loader", e);
            return "Unknown Version";
        }
    }

    public static String getEnvironment() {
        try {
            return FabricLoader.getInstance().getEnvironmentType().name().toLowerCase();
        } catch (Exception e) {
            Environmentdetector.LOGGER.warn("Failed to detect environment", e);
            return "unknown";
        }
    }

    public static String getOS() {
        try {
            return System.getProperty("os.name", "Unknown OS") + " " + System.getProperty("os.version", "");
        } catch (Exception e) {
            Environmentdetector.LOGGER.warn("Failed to detect OS", e);
            return "Unknown OS";
        }
    }

    public static boolean isDevelopment() {
        try {
            return FabricLoader.getInstance().isDevelopmentEnvironment();
        } catch (Exception e) {
            return false;
        }
    }
}
