package de.felixfgf.environmentdetector.detection;

import de.felixfgf.environmentdetector.Environmentdetector;
import net.fabricmc.loader.api.FabricLoader;
import java.nio.file.Files;

public class PlatformDetector {
    public static String detectPlatform() {
        try {
            if (FabricLoader.getInstance().isModLoaded("theseus") || Files.exists(FabricLoader.getInstance().getGameDir().resolve("modrinth.index.json"))) {
                return "Modrinth";
            }
            if (System.getenv("CURSEFORGE_INSTANCE_NAME") != null || Files.exists(FabricLoader.getInstance().getGameDir().resolve("minecraftinstance.json"))) {
                return "CurseForge";
            }
        } catch (Exception e) {
            Environmentdetector.LOGGER.warn("Failed to detect platform", e);
        }
        return "Unknown Platform";
    }
}
