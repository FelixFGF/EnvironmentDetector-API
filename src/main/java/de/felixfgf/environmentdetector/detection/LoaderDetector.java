package de.felixfgf.environmentdetector.detection;

import de.felixfgf.environmentdetector.Environmentdetector;
import net.fabricmc.loader.api.FabricLoader;

public class LoaderDetector {
    public static String getLoaderName() {
        try {
            if (FabricLoader.getInstance().isModLoaded("quilt_loader")) {
                return "Quilt";
            }
            if (FabricLoader.getInstance().isModLoaded("neoforge")) {
                return "NeoForge"; // For environments using connectors
            }
            if (FabricLoader.getInstance().isModLoaded("forge")) {
                return "Forge"; // For environments using connectors
            }
            return "Fabric";
        } catch (Exception e) {
            Environmentdetector.LOGGER.warn("Failed to detect loader name", e);
            return "Unknown Loader";
        }
    }

    public static String getLoaderVersion() {
        try {
            // Try Quilt first
            if (FabricLoader.getInstance().isModLoaded("quilt_loader")) {
                return FabricLoader.getInstance().getModContainer("quilt_loader")
                        .map(container -> container.getMetadata().getVersion().getFriendlyString())
                        .orElse("Unknown Version");
            }
            
            // Default to Fabric
            return FabricLoader.getInstance().getModContainer("fabricloader")
                    .map(container -> container.getMetadata().getVersion().getFriendlyString())
                    .orElse("Unknown Version");
        } catch (Exception e) {
            Environmentdetector.LOGGER.warn("Failed to detect loader version", e);
            return "Unknown Version";
        }
    }
}
