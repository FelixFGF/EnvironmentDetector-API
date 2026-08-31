package de.felixfgf.environmentdetector.detection;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.felixfgf.environmentdetector.Environmentdetector;
import net.fabricmc.loader.api.FabricLoader;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class PackDetector {
    public static String detectPackName() {
        try {
            // 0. Manual Override via Config (Highest Priority)
            String manual = ConfigDetector.getManualPackName();
            if (!manual.isEmpty()) return manual;

            Path gameDir = FabricLoader.getInstance().getGameDir();
            Path parentDir = gameDir.getParent();
            Path grandparentDir = parentDir != null ? parentDir.getParent() : null;

            // 1. Modrinth Index
            Path[] modrinthPaths = {
                gameDir.resolve("modrinth.index.json"),
                parentDir != null ? parentDir.resolve("modrinth.index.json") : null,
                grandparentDir != null ? grandparentDir.resolve("modrinth.index.json") : null
            };

            for (Path path : modrinthPaths) {
                if (path != null && Files.exists(path)) {
                    try (Reader reader = Files.newBufferedReader(path)) {
                        JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
                        if (json.has("name")) {
                            return json.get("name").getAsString();
                        }
                    } catch (Exception ignored) {}
                }
            }

            // 2. CurseForge
            String envPack = System.getenv("CURSEFORGE_INSTANCE_NAME");
            if (envPack != null) return envPack;

            // 3. MultiMC / Prism
            Path instanceCfg = parentDir != null ? parentDir.resolve("instance.cfg") : null;
            if (instanceCfg != null && Files.exists(instanceCfg)) {
                try {
                    Properties props = new Properties();
                    props.load(Files.newInputStream(instanceCfg));
                    String name = props.getProperty("name");
                    if (name != null) return name;
                } catch (Exception ignored) {}
            }

            // Fallback: If no pack detected, use directory name as a guess
            if (gameDir.getFileName() != null) {
                String dirName = gameDir.getFileName().toString();
                if (!dirName.equalsIgnoreCase("minecraft") && !dirName.equalsIgnoreCase(".minecraft")) {
                    return dirName;
                }
            }

        } catch (Exception e) {
            Environmentdetector.LOGGER.warn("Failed to detect pack name", e);
        }

        return "Unknown Pack";
    }
}
